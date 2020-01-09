package org.ivy.util.common;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * GUIDCreator
 *
 * @author miao.xl
 * @date 2013-12-27 上午11:16:38
 */
public class GuidUtil {

    public static final int BEFORE_MD_5 = 1;
    public static final int AFTER_MD_5 = 2;
    public static final int FORMAT_STRING = 3;
    private static Random random;
    private static SecureRandom secureRandom;

    private static String s_id;

    static {
        secureRandom = new SecureRandom();
        long secureInitializer = secureRandom.nextLong();
        random = new Random(secureInitializer);
        try {
            s_id = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String seedingString = "";
    private String rawGUID = "";
    private boolean bSecure = false;

    /**
     * Default constructor
     */
    public GuidUtil() {
    }

    /**
     * Constructor with security option. Setting secure true enables each random
     * number generated to be cryptographically strong. Secure false defaults to
     * the standard Random function seeded with a single cryptographically
     * strong random number.
     */
    public GuidUtil(boolean secure) {
        bSecure = secure;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            GuidUtil myGUID = new GuidUtil();
            System.out.println("SeedingString = " + myGUID.createNewGuid(GuidUtil.BEFORE_MD_5));
            System.out.println("rawGUID = " + myGUID.createNewGuid(GuidUtil.AFTER_MD_5));
            System.out.println("RandomGUID1 = " + myGUID.createNewGuid(GuidUtil.FORMAT_STRING));
            System.out.println("RandomGUID2 = " + new GuidUtil(true).createNewGuid(GuidUtil.FORMAT_STRING));
            System.out.println();
        }
    }

    /*
     * Method to generate the random GUID
     */
    private void getRandomGUID(boolean secure) {
        MessageDigest md5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer();

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }

        try {
            long time = DateTimeUtil.getTimestamp(null);
            long rand = 0;

            if (secure) {
                rand = secureRandom.nextLong();
            } else {
                rand = random.nextLong();
            }

            // This StringBuffer can be a long as you need; the MD5
            // hash will always return 128 bits. You can change
            // the seed to include anything you want here.
            // You could even stream a file through the MD5 making
            // the odds of guessing it at least as great as that
            // of guessing the contents of the file!
            sbValueBeforeMD5.append(s_id);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));

            seedingString = sbValueBeforeMD5.toString();
            md5.update(seedingString.getBytes());

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & 0xFF;
                if (b < 0x10) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(b));
            }

            rawGUID = sb.toString();

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    public String createNewGuid(int nFormatType, boolean secure) {
        getRandomGUID(secure);
        String sGuid = "";
        if (BEFORE_MD_5 == nFormatType) {
            sGuid = this.seedingString;
        } else if (AFTER_MD_5 == nFormatType) {
            sGuid = this.rawGUID;
        } else {
            sGuid = this.toString();
        }
        return sGuid;
    }

    public String createNewGuid(int nFormatType) {
        return this.createNewGuid(nFormatType, this.bSecure);
    }

    /*
     * Convert to the standard format for GUID (Useful for SQL Server
     * UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
     */
    @Override
    public String toString() {
        String raw = rawGUID.toUpperCase();
        StringBuffer sb = new StringBuffer();
        sb.append(raw.substring(0, 8)).append("-");
        sb.append(raw.substring(8, 12)).append("-");
        sb.append(raw.substring(12, 16)).append("-");
        sb.append(raw.substring(16, 20)).append("-");
        sb.append(raw.substring(20));
        return sb.toString();
    }

}
