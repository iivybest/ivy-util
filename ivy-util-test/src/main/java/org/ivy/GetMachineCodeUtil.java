/**
 *
 */
package org.ivy;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 *  GetMachineCodeUtil
 * @author ivybest imiaodev@163.com
 * @date 2018年8月30日 上午8:56:21
 *  获取机器编码
 */
public class GetMachineCodeUtil {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            long start = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            String serial = sc.next();
            System.out.println(property + ": " + serial);
            System.out.println("time:" + (System.currentTimeMillis() - start));

//			Scanner scanner = new Scanner(System.in);
//			String tt = scanner.next();
//			System.out.println(tt);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Properties p = System.getProperties();
        for (Map.Entry<Object, Object> e : p.entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }
}
