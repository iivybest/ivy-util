package org.ivy.xutil.sec;

/**
 * <p>CesarCodeUtil</p>
 *
 * @author miao.xl
 * @date 2014-6-11 下午06:16:04
 */
public class CaesarCodeUtil {
    /*
     *	 凯撒密码
     * 		1、加密向右移位3
     * 		将 plaintext 装换成char[]，对每个 char 进行加密，在转换为 cipertext；
     * 		#注：char 加密后的 int 值如果大于等于 65536，减去一个 65536
     * 		2、解密向左移位3
     * 		将 cipertext 装换成char[]，对每个char进行加密，在转换为 plaintext；
     * 		#注：char 解密后的 int 若小于 0，加上一个  65536
     */
    private final int key = 3;
    private final int charLen = 65536;


    private String encrypt(String data, int step) {
        String cipher = null;
        return cipher;
    }

    private String encryptHex(String data, int step) {
        String cipher = null;
        return cipher;
    }


    /**
     * <p>凯撒加密</p>
     *
     * @param original
     * @return
     */
    private String encrypt(char[] original) {
        String cipher = null;
        char[] cipherChar = new char[original.length];
        for (int i = 0; i < original.length; i++)
            cipherChar[i] = (char) this.ciperCode((short) original[i]);
        cipher = new String(cipherChar);
        return cipher;
    }

    /**
     * <p>凯撒加密</p>
     *
     * @param original
     * @return
     */
    public String encrypt(String original) {
        char[] msg = new char[original.length()];
        original.getChars(0, original.length(), msg, 0);
        return encrypt(msg);
    }

    /**
     * <p>凯撒解密</p>
     *
     * @param original
     * @return
     */
    private String decrypt(char[] original) {
        String plain = null;
        char[] plainChar = new char[original.length];
        for (int i = 0; i < original.length; i++)
            plainChar[i] = (char) this.plainCode((short) original[i]);
        plain = new String(plainChar);
        return plain;
    }

    /**
     * <p>凯撒解密</p>
     *
     * @param original
     * @return
     */
    public String decrypt(String original) {
        char[] msg = new char[original.length()];
        original.getChars(0, original.length(), msg, 0);
        return this.decrypt(msg);
    }

    /**
     * <p>获取加密后字符数值</p>
     *
     * @param charNum
     * @return
     */
    private short ciperCode(short charNum) {
        int temp = charNum + this.key;
        short ciper;
        if (temp >= this.charLen) temp -= this.charLen;
        ciper = (short) temp;
        return ciper;
    }

    /**
     * <p>获取解密后字符数值</p>
     *
     * @param charNum
     * @return
     */
    private short plainCode(short charNum) {
        int temp = charNum - this.key;
        short plain;
        if (temp < 0) temp += this.charLen;
        plain = (short) temp;
        return plain;
    }

//	public static void main(String[] args) {
//		// 凯撒密码边界值考虑
//		int bitCount = 3;
//		char cc = 65534;
//		int cipherInt = cc + bitCount;
//		if(cipherInt >= 65536) cipherInt -= 65536;
//		char cipher = (char)cipherInt;
//		System.out.println(cipherInt);
//		System.out.println(cipher);
//
//		int ppInt = cipher - bitCount;
//		if(ppInt < 0) ppInt += 65536;
//		char pp = (char) ppInt;
//		System.out.println(ppInt);
//		System.out.println("PP : " + pp + " , cc : " + cc);
//	}
}
