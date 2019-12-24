package org.ivy.xutil;

import java.io.UnsupportedEncodingException;

/**
 * <p>MessyCodeCorrector</p>
 *
 * @author miao.xl
 * @date 2014年6月16日 - 下午5:00:19
 */
public class MessyCodeCorrector {
    /*
     * 这是一个可以从乱码文本中得到正确的原始文本的程序，其基于的原理在于错误的编码往往导致位补充，
     * 因此正确的文本使用的字节数应该是最少的（之一）。
     *
     * 如果你在测试此程序时，无法得到正确的文本，可能的原因如下：
     * 1.此程序仅能从一次错误编码文本中得到原始文本，无法从多次错误编码中恢复文本。
     * 2.有时错误的编码导致一些字符变为不可见字符，若没有把所有的乱码文本拷贝过来，
     * 		从而导致位缺失。这种情况下无法恢复文本。
     * 3.原始文本是一个比较大的字符集，错误的编码使用小的字符集，
     * 		那些在小字符集之外的字符信息丢失，无法从中解析正确的文本。
     * 4.恭喜你中奖了，有一些字符使用任何一种编码没有什么不同或者错误的编码没有导致位补充，
     * 		那么我也无能为力了。（这种情况确实很少见）
     *
     * 注：程序中的乱码文本是将百度首页（utf-8）调整为gbk（显然会乱码）得到的
     */

    public static final String[] CHARSET = new String[]{"ISO8859-1", "GBK", "UTF-8"};
    private int strMaxLen;

    public MessyCodeCorrector() {
        super();
        init();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String xx = "我是一只草泥马";
        String rr = new String(xx.getBytes(), "ISO-8859-1");
        System.out.println("'" + rr + "'");

        String original = "寰蒋鐧惧害鍏辨帹Windows XP鑱斿悎闃叉姢瑙ｅ喅鏂规";
//		String original = "ææ¯ä¸åªèæ³¥é©¬";
        MessyCodeCorrector m = new MessyCodeCorrector();
        System.out.println(m.amend(original));
        System.out.println(m.amend(rr));
    }

    private void init() {
        this.strMaxLen = Integer.MAX_VALUE;
    }

    /**
     * <p>修正</p>
     *
     * @param original
     * @return
     * @author miao.xl
     */
    public String amend(String original) {
        String result = null;
//		String originalCharSet = "";
//		String realCharSet = "";
        int strLen = this.strMaxLen;
        try {
            for (int i = 0; i < CHARSET.length; i++) {
                for (int j = 0; j < CHARSET.length; j++) {
                    String temp = new String(original.getBytes(CHARSET[i]), CHARSET[j]);
                    if (temp.length() <= strLen) {
                        result = temp;
                        strLen = temp.length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
