package cn.com.shequnew.inc;

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
    public static final String _REG_PRICE = "^(0|([1-9]{1}\\d{0,8}))(\\.\\d{1,2}){0,1}$";
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

    //    public static String Url = "http://qmy.51edn.com/index.php/app";
    public static String Url = "http://qumai.51edn.com/index.php/app";


}
