package org.ivy.util.tool;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/***
 * 物理地址是48位，别和ipv6搞错了
 */
@Slf4j
public class LocalMAC {
    /**
     * @param args parameters
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static void main(String[] args) throws UnknownHostException, SocketException {

        //得到IP，输出PC-201309011313/122.206.73.83
        InetAddress ia = InetAddress.getLocalHost();
        log.debug("{InetAddress: {}}", ia);
        getLocalMac(ia);
    }

    /**
     * get local mac info
     *
     * @param ia InetAddress
     * @throws SocketException
     */
    private static void getLocalMac(InetAddress ia) throws SocketException {
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        int macLen = mac.length;
        log.debug("{macLength: {}}", macLen);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String item = Integer.toHexString(temp);
            log.debug("{item: {}}:", item);
            sb.append((item.length() == 1 ? "0" : "") + item);
            if (i == mac.length - 1) {
                break;
            }
            sb.append("-");
        }
        String macAddr = sb.toString().toUpperCase();
        log.debug("{macAddress: {}}", macAddr);
    }
}