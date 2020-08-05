package org.hehh.utils.http;


import org.hehh.cloud.common.regular.Regular;
import org.hehh.utils.NumberKit;
import org.hehh.utils.StrKit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-03-21 01:51
 * @description: ip工具
 **/
public class IPKit {



    private static final int[] CIDR2MASK = new int[]{0x00000000, 0x80000000,
            0xC0000000, 0xE0000000, 0xF0000000, 0xF8000000, 0xFC000000,
            0xFE000000, 0xFF000000, 0xFF800000, 0xFFC00000, 0xFFE00000,
            0xFFF00000, 0xFFF80000, 0xFFFC0000, 0xFFFE0000, 0xFFFF0000,
            0xFFFF8000, 0xFFFFC000, 0xFFFFE000, 0xFFFFF000, 0xFFFFF800,
            0xFFFFFC00, 0xFFFFFE00, 0xFFFFFF00, 0xFFFFFF80, 0xFFFFFFC0,
            0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, 0xFFFFFFFC, 0xFFFFFFFE,
            0xFFFFFFFF};



    /**
     * 是否cidr表达
     *
     * @param cidr
     * @return
     */
    public static boolean isCidr(String cidr) {
        return StrKit.isRegular(cidr, Regular.cidr);
    }


    /**
     * ipv4转4字节 int
     *
     * @param ip
     * @return
     */
    public static int ipv4ToByte4Int(String ip) {

        String[] ips = ip.split("[.]");

        byte[] ipBytes = new byte[4];

        /**
         * IP地址压缩成4字节,如果要进一步处理的话,就可以转换成一个int了.
         */

        for (int i = 0; i < 4; i++) {

            int m = Integer.parseInt(ips[i]);
            byte b = (byte) m;
            if (m > 127) {
                b = (byte) (127 - m);
            }
            ipBytes[i] = b;
        }
        return NumberKit.byteArrayToInt(ipBytes);
    }


    /**
     *  ipInt 转 ip字符串
     * @param ipInt
     * @return
     */
    public static String intToIp(int ipInt) {
        byte[] bytes = NumberKit.intToByteArray(ipInt);


        /**
         * 把4字节的数组解成IP
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String tmp = String.valueOf(bytes[i]);
            if (bytes[i] < 0) {
                tmp = String.valueOf(127 + Math.abs(bytes[i]));
            }
            sb.append(tmp);
            if (i < 3) {
                sb.append(".");
            }

        }
        return sb.toString();
    }


    /**
     * IPV4 转 CIDR
     *
     * @param startIp
     * @param endIp
     * @return
     */
    public static List<String> ipv4ToCidr(String startIp, String endIp) {
        long start = ipToLong(startIp);
        long end = ipToLong(endIp);

        ArrayList<String> pairs = new ArrayList<>();
        while (end >= start) {
            byte maxsize = 32;
            while (maxsize > 0) {
                long mask = CIDR2MASK[maxsize - 1];
                long maskedBase = start & mask;

                if (maskedBase != start) {
                    break;
                }

                maxsize--;
            }
            double x = Math.log(end - start + 1) / Math.log(2);
            byte maxDiff = (byte) (32 - Math.floor(x));
            if (maxsize < maxDiff) {
                maxsize = maxDiff;
            }
            String ip = longToIP(start);
            pairs.add(ip + "/" + maxsize);
            start += Math.pow(2, (32 - maxsize));
        }
        return pairs;
    }


    /**
     * 字符串ip 转码
     *
     * @param strIP
     * @return
     */
    private static long ipToLong(String strIP) {
        long[] ip = new long[4];
        String[] ipSec = strIP.split("\\.");
        for (int k = 0; k < 4; k++) {
            ip[k] = Long.valueOf(ipSec[k]);
        }

        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }



    /**
     * longIp转码
     *
     * @param longIP
     * @return
     */
    private static String longToIP(long longIP) {
        StringBuffer sb = new StringBuffer("");
        sb.append(longIP >>> 24);
        sb.append(".");
        sb.append((longIP & 0x00FFFFFF) >>> 16);
        sb.append(".");
        sb.append((longIP & 0x0000FFFF) >>> 8);
        sb.append(".");
        sb.append(longIP & 0x000000FF);
        return sb.toString();
    }











}
