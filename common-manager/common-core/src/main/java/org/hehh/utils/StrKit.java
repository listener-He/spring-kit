package org.hehh.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.hehh.cloud.common.regular.Regular;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: HeHui
 * @create: 2020-03-21 01:46
 * @description: 字符串工具
 **/
public class StrKit {

    /**
     * RANDOM 基数
     */
    private final static int RANDOM_BASE = 10;

    private final static char[] STRS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private final static char[] INTS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};


    private final static int[] LI_SEC_POS_VALUE = {1601, 1637, 1833, 2078, 2274,
        2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
        4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};
    private final static String[] LC_FIRST_LETTER = {"a", "b", "c", "d", "e",
        "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
        "t", "w", "x", "y", "z"};


    /**
     * 判定输入的是否是汉字
     *
     * @param c 被校验的字符
     *
     * @return true代表是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     *
     * @return 是否为中文
     *
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile(Regular.CN);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 验证是否为数字字母
     *
     * @param str
     *
     * @return
     */
    public static boolean isIntOrStr(String str) {
        if (StrUtil.isBlank(str) || StrUtil.contains(str, ' ')) {
            return false;
        }
        Pattern p = Pattern.compile(Regular.INT_AND_STR);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 校验一个字符是否是汉字
     *
     * @param c 被校验的字符
     *
     * @return true代表是汉字
     */
    public static boolean isChineseChar(char c) {
        try {
            return String.valueOf(c).getBytes("UTF-8").length > 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 校验某个字符是否是a-z、A-Z、_、0-9
     *
     * @param c 被校验的字符
     *
     * @return true代表符合条件
     */
    public static boolean isWord(char c) {
        Pattern p = Pattern.compile(Regular.WORD);
        Matcher m = p.matcher("" + c);
        return m.matches();
    }


    /**
     * 验证字符串是否符合正则表达式
     *
     * @param str     字符串
     * @param regular 正则表达式
     *
     * @return
     */
    public static boolean isRegular(String str, String regular) {
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 过滤掉中文
     *
     * @param str 待过滤中文的字符串
     *
     * @return 过滤掉中文后字符串
     */
    public static String filterChinese(String str) {
        String result = str;
        boolean flag = isContainChinese(str);
        if (flag) {
            /**包含中文*/
            StringBuffer sb = new StringBuffer();
            /**用于校验是否为中文*/
            boolean flag2 = false;

            char chinese = 0;
            /**去除掉文件名中的中文将字符串转换成char[]*/
            char[] charArray = str.toCharArray();
            /**过滤到中文及中文字符*/
            for (int i = 0; i < charArray.length; i++) {
                chinese = charArray[i];
                flag2 = isChinese(chinese);
                if (!flag2) {
                    /**不是中日韩文字及标点符号*/
                    sb.append(chinese);
                }
            }
            result = sb.toString();
        }
        return result;
    }


    /**
     * 是否url
     *
     * @param url
     *
     * @return
     */
    public static boolean isUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return false;
        }


        return isRegular(url, Regular.URL);
    }


    /**
     * 取得给定汉字串的首字母串,即声母串
     *
     * @param str 给定汉字串
     *
     * @return 声母串
     */
    public static String getAllFirstLetter(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }

        String _str = "";
        for (int i = 0; i < str.length(); i++) {
            _str = _str + getFirstLetter(str.substring(i, i + 1));
        }

        return _str;
    }

    /**
     * 取得给定汉字的首字母,即声母
     *
     * @param chinese 给定的汉字
     *
     * @return 给定汉字的声母
     */
    public static String getFirstLetter(String chinese) {
        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }
        chinese = conversionStr(chinese, "GB2312", "ISO8859-1");

        if (chinese.length() > 1) // 判断是不是汉字
        {
            int li_SectorCode = (int) chinese.charAt(0); // 汉字区码
            int li_PositionCode = (int) chinese.charAt(1); // 汉字位码
            li_SectorCode = li_SectorCode - 160;
            li_PositionCode = li_PositionCode - 160;
            int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
            if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                for (int i = 0; i < 23; i++) {
                    if (li_SecPosCode >= LI_SEC_POS_VALUE[i]
                        && li_SecPosCode < LI_SEC_POS_VALUE[i + 1]) {
                        chinese = LC_FIRST_LETTER[i];
                        break;
                    }
                }
            } else // 非汉字字符,如图形符号或ASCII码
            {
                chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }

        return chinese;
    }

    /**
     * 字符串编码转换
     *
     * @param str           要转换编码的字符串
     * @param charsetName   原来的编码
     * @param toCharsetName 转换后的编码
     *
     * @return 经过编码转换后的字符串
     */
    private static String conversionStr(String str, String charsetName, String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (Exception ex) {
            System.out.println("字符串编码转换异常：" + ex.getMessage());
        }
        return str;
    }


    /**
     * 产生指定长度的数字值随机数
     *
     * @param length 需要产生的长度
     *
     * @return
     */
    public static String getInt(int length) {
        Random random = new Random();
        String randStr = "";
        for (int i = 0; i < length; i++) {
            String randItem = String.valueOf(random.nextInt(RANDOM_BASE));
            randStr += randItem;
        }
        return randStr;
    }

    public static int getIntStartEnd(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
     * 描述：手机验证码生成带字符，包含数字和字符
     *
     * @param len 生成手机验证码长度
     *
     * @return
     */
    public static String getIntAndStr(int len) {
        return random(true, len);
    }

    /**
     * 描述：手机验证码生成带字符不包含数字
     *
     * @param len 生成手机验证码长度
     *
     * @return
     */
    public static String getStr(int len) {
        return random(false, len);
    }


    /**
     * 生成当前时间 紧凑型字符串
     *
     * @return
     */
    public static String dateTimeStr() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + getInt(5);
    }


    /**
     * 生成号码
     *
     * @param serviceCode
     * @param supplement
     *
     * @return
     */
    public static String generateNo(int serviceCode, Integer supplement) {
        return serviceCode + DateUtil.format(new Date(), "yyyyMMddHHmmss") + getInt(4) + (null == supplement ? "" : supplement);
    }

    /**
     * 生成号码
     *
     * @param serviceCode
     *
     * @return
     */
    public static String generateNo(int serviceCode) {
        return generateNo(serviceCode, null);
    }


    /**
     * 生成随机字符串
     *
     * @param isInt
     * @param len
     *
     * @return
     */
    private static String random(boolean isInt, int len) {
        int min = 0;
        int maxnum = isInt ? INTS.length : STRS.length;
        String codeStr = "";
        for (int i = 0; i < len; i++) {
            int num = (int) ((maxnum - min) * Math.random() + min);
            codeStr += isInt ? INTS[num] : STRS[num];
        }
        return codeStr;
    }


    /**
     * 二进位组转十六进制字符串
     *
     * @param buf 二进位组
     *
     * @return 十六进制字符串
     */
    public static String hexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串转二进位组
     *
     * @param hexStr 十六进制字符串
     *
     * @return 二进位组
     */
    public static byte[] hex2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];

        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    /**
     * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     *
     * @return 是否为JSON字符串
     *
     * @since 3.3.0
     */
    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }


    /**
     * 是否为JSONObject字符串，首尾都为大括号判定为JSONObject字符串
     *
     * @param str 字符串
     *
     * @return 是否为JSON字符串
     *
     * @since 3.3.0
     */
    public static boolean isJsonObj(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return StrUtil.isWrap(str.trim(), '{', '}');
    }

    /**
     * 是否为JSONArray字符串，首尾都为中括号判定为JSONArray字符串
     *
     * @param str 字符串
     *
     * @return 是否为JSON字符串
     *
     * @since 3.3.0
     */
    public static boolean isJsonArray(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return StrUtil.isWrap(str.trim(), '[', ']');
    }


}
