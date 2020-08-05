package org.hehh.utils;

/**
 * @author: HeHui
 * @create: 2020-01-17 11:47
 * @description: 数字工具类
 **/
public class NumberKit {

    private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
    private static final double MAX_VALUE = 9999999999999.99D;


    private static final String[] smallNumbers = new String[]{"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE",
            "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
            "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN",
            "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
    private static final String[] tensNumbers = new String[]{"", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"};
    private static final String[] scaleNumers = new String[]{"", "THOUSAND", "MILLION", "BILLION"};
    private static final String end = "ONLY";






    /**
     * int到byte[]
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }



    /**
     * byte[]转int
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }


    /**
     *  判断 int 类型是否相等
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Integer a,Integer b){
        if(a == null){
            return b == null;
        }
        if(b == null){
            return a == null;
        }
        return a.equals(b);
    }







    /**
     *
     *  数字转中文大写
     * @param v
     * @return
     */
    public static String change(double v) {
        if (v < 0 || v > MAX_VALUE) {
            return "参数非法!";
        }
        long l = Math.round(v * 100);
        if (l == 0) {
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }


    /**
     *  数字转英文大写
     * @param money
     * @return
     */
    public  static String english(double money){
        double dMoney = Double.parseDouble(money+"");
        String[] arrMoney = (money+"").split("\\.");
        //小数点前
        long decimals1 = 0;
        //小数点后
        int decimals2 = 0;
        //纯小数
        if(dMoney < 1){
            decimals1 = 0;
        }else{
            decimals1 = Long.parseLong(arrMoney[0]);
        }
        decimals2 = Integer.parseInt(arrMoney[1]);
        //初始化显示英文为ZERO
        String combined1 = smallNumbers[0];
        String combined2 = smallNumbers[0];

        if(decimals1 != 0){
            long[] digitGroups = new long[]{0,0,0,0};
            ////将金额拆分成4段，每段放3位数，即：XXX,XXX,XXX,XXX。最大仅支持到Billion，
            for(int i=0;i<4;i++){
                digitGroups[i] = decimals1%1000;
                decimals1 = decimals1/1000;
            }

            String[] groupText = new String[]{"","","",""};
            //处理每段的金额转英文，百位+十位+个位
            for(int i=0;i<4;i++){
                long hundreds = digitGroups[i]/100;
                long tensUnits = digitGroups[i]%100;

                //百位
                if(hundreds!=0){
                    groupText[i] = groupText[i] + smallNumbers[Long.valueOf(hundreds).intValue()] + " HUNDRED";
                    if(tensUnits!=0){
                        groupText[i] = groupText[i] + " AND ";
                    }
                }

                //十位和个位
                long tens = tensUnits/10;
                long units = tensUnits%10;
                if(tens>=2){//十位大于等于20
                    groupText[i] = groupText[i] + tensNumbers[Long.valueOf(tens).intValue()];
                    if(units!=0){
                        groupText[i] = groupText[i] + " " + smallNumbers[Long.valueOf(tens).intValue()];
                    }
                }else if(tens!=0){//十位和个位，小于20的情况
                    groupText[i] = groupText[i] + smallNumbers[Long.valueOf(tens).intValue()] ;
                }
            }
            //金额的个十百位赋值到combined
            combined1 = groupText[0];
            //将金额排除个十百位以外，余下的3段英文数字，加上千位分隔符英文单词，Thousand/Million/Billion
            for(int i=1;i<4;i++){
                if (digitGroups[i]!=0){
                    String prefix = groupText[i] + " " + scaleNumers[i];  //A:组合Thousand 和Billion
                    if (combined1.length()!=0){ //如果金额的百位+十位+个位非0,则在后面加上空格
                        prefix = prefix+ " ";
                    }
                    combined1 = prefix + combined1; //再连接 A+B

                }
            }
        }

        if(decimals2!=0){
            //十位和个位
            int tens  = decimals2/10;
            int units = decimals2%10;

            if(decimals2 >=20){
                combined2 = "CENTS " + tensNumbers[tens];
                if(units!=0){
                    combined2 = combined2 + " " + smallNumbers[units];
                }
            }else if(decimals2 > 1){//19到2之间
                combined2 = "CENTS " + smallNumbers[decimals2];
            }else{
                combined2 = "CENT " + smallNumbers[decimals2];
            }
        }

        if(!combined1.equals("ZERO")){
            if(!combined2.equals("ZERO")){
                return combined1 + " " + combined2 + " " + end;
            }else{
                return combined1+ " " + end;
            }
        }else if(!combined2.equals("ZERO")){
            return combined2 + " " + end;
        }else{
            return "ZERO";
        }
    }
}
