package com.yshstudio.originalproduct.inc;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public final class Ini {
    //是否进入debug模式
    public static final boolean IS_DEBUG = true;
    // 正则-数字匹配公式
    public static final String _REG_DIGITS = "^\\d+$";
    // 正则-整数匹配格式
    public static final String _REG_INT = "^[-+]{0,1}[1-9]\\d*$";
    // 正则-整数匹配格式(包含0)
    public static final String _REG_INTNEW = "^[-+]{0,1}[0-9]\\d*$";
    // 正则-价格匹配格式
    public static final String _REG_PRICE = "^(([1-9]{1}\\d{0,8}))(\\.\\d{1,2}){0,1}$";
    // 正则-价格匹配格式
    public static final String _REG_PRICE_TWO = "^[-+]{0,1}(0|([1-9]{1}\\d{0,8}))(\\.\\d{1,2}){0,1}$";
    // 正则-价格匹配格式
    public static final String _REG_AMOUNT = "^[-+]{0,1}(0|([1-9]{1}\\d{0,8}))(\\.\\d{1,3}){0,1}$";
    // 正则-手机号匹配格式
    public static final String _REG_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[57])|(17[678]))\\d{8}$";
    //正则-密码
    public static final String _REG_PAWSS = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
    // 网络连接-读取流超时时间
    public static final int _HTTP_READ_TIMEOUT = 60000; // 60秒
    // 网络连接-连接超时时间
    public static final int _HTTP_CONNECT_TIMEOUT = 60000; // 60秒
    // 输出标记代码-成功
    public static final int _EXOK = 1;
    // 输出标记代码-失败
    public static final int _EXNO = 0;
    public static final String _ENCODED_API = "UTF-8";
    public static final int PAY_TYPE_WEIXIN = 0x00001;
    public static final int PAY_TYPE_ZFB = 0x00002;
    public static final int SDK_PAY_FLAG = 0x00003;
    public static final int SDK_PAY_FLAG2 = 0x00004;
    public static final int SDK_PAY_FLAG3 = 0x00005;

    public static String Url_ROOT="http://qmy.51edn.com/";
    public static String Url=Url_ROOT + "index.php/app";
    public static String ShareGood_Url=Url_ROOT + "app/Trade/good_detail/id/";
    public static String ShareCommunity_Url=Url_ROOT + "app/Community/node_detail/id/";
    public static String RequestPay_Weixin=Url_ROOT + "pay/index.php";
    public static String RequestPayCallBack_Weixin=Url_ROOT + "pay/wxcheck.php";
    public static String RequestPay_Alipay=Url_ROOT + "pay/Alipay.php";


}
