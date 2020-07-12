package org.hehh.security;

import com.google.common.base.Strings;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-06-19 15:40
 * @description: ip工具类
 */
public class IPUtil {

    /**
     * ip对应的正则表达式
     */
    public static final Pattern IP_PATTERN = Pattern.compile(
            "(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}");


    /**
     * ip的cidr正则
     */
    public static final Pattern IP_CIDR = Pattern.compile("^(?:(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\/([1-9]|[1-2]\\d|3[0-2])$");

    private static final int[] CIDR2MASK = new int[]{0x00000000, 0x80000000,
            0xC0000000, 0xE0000000, 0xF0000000, 0xF8000000, 0xFC000000,
            0xFE000000, 0xFF000000, 0xFF800000, 0xFFC00000, 0xFFE00000,
            0xFFF00000, 0xFFF80000, 0xFFFC0000, 0xFFFE0000, 0xFFFF0000,
            0xFFFF8000, 0xFFFFC000, 0xFFFFE000, 0xFFFFF000, 0xFFFFF800,
            0xFFFFFC00, 0xFFFFFE00, 0xFFFFFF00, 0xFFFFFF80, 0xFFFFFFC0,
            0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, 0xFFFFFFFC, 0xFFFFFFFE,
            0xFFFFFFFF};


    /**
     * 是否ip
     * @param ip
     * @return
     */
    public static boolean isIp(String ip){
       return  IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 是否cidr
     * @param cidr
     * @return
     */
    public static boolean isCidr(String cidr){
        return  IP_CIDR.matcher(cidr).matches();
    }


    /**
     * 将ipV4地址转换为对应的int值
     * @param ipAddr ip地址(如192.168.12.11)
     * @return 转换为int值
     */
    public static int toInt(String ipAddr) {
        if (Strings.isNullOrEmpty(ipAddr) || !isIp(ipAddr)) {
            throw new InvalidParameterException("IP地址不合法");
        }
        byte[] integers = toIntArray(ipAddr);

        return bytesToInt(integers);
    }

    /**
     * 将int值转换为对应的ip地址
     * @param ipInteger ip地址的int值
     * @return 转换为ip地址(如192.168.12.11)
     */
    public static String toIpAddress(int ipInteger) {
        String[] str = new String[4];
        byte[] bytes = intToBytes(ipInteger);

        for (int i = 0; i < bytes.length; i++) {
            str[i] = Integer.toString(bytes[i] & 0xFF);
        }

        return Arrays.stream(str).collect(Collectors.joining("."));
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
     *  字节转int
     * @param b
     * @return
     */
    public static int bytesToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     *  int 转字节
     * @param a
     * @return
     */
    public static byte[] intToBytes(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
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
     * 将ipV4地址根据点号分割为4部分,每部分用byte表示
     * @param ipAddr ipV4地址如 192.168.11.2
     * @return 返回[-64,-88,11,2] 因为 java的byte只能表示 -128~127,所以大于127的值被转换为负数
     */
    private static byte[] toIntArray(String ipAddr) {
        String[] split = ipAddr.split("\\.");
        byte[] result = new byte[split.length];

        for (int i = 0; i < split.length; i++) {
            result[i] = (byte) (Short.parseShort(split[i]));
        }

        return result;
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
