package org.hehh.cloud.common.regular;

/**
 * @author : HeHui
 * @date : 2019-03-24 13:33
 * @describe : 正则
 */
public class Regular {

    /**
     * 数字偶然字母
     */
    public static final String INT_AND_STR = "^[A-Za-z0-9]+$";

    public static final String INT_AND_STR_AND = "^[0-9a-zA-Z_]{1,}$";

    /**
     * 中文
     */
    public static final String CN = "[\\u4e00-\\u9fa5]";

    /**
     * a-z、A-Z、_、0-9
     */
    public static final String WORD = "^/w+$";

    public static final String IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    public static final String URL = "^(https?|ftp|file|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";




    /**
     * 助记词正则匹配：首位与末位为字母，中间用“_”连接；eg:a_A_b
     */
    public static final String MARK_REGULAR = "^[a-zA-Z]+(\\_?[a-zA-Z])*$";


    /**
     * 特殊字符正则
     */
    public static final String SPECIAL = "[\\n`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";


    /**
     * ip的cidr正则
     */
    public static final String CIDR = "^(?:(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\/([1-9]|[1-2]\\d|3[0-2])$";


    /**
     * qq号正则，目前10位
     * TODO 如果要改QQ号正则长度，请在此处
     */
    public static final String QQ = "[1-9][0-9]{4,10}";

    /**
     * 匹配中文，英文字母和数字及_:
     */
    public static final String NAME = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";

    public static final String SQL =  "('.+--)|(--)|(\\\\|)|(%7C)";

    public static final String JS =  "<script>";
}
