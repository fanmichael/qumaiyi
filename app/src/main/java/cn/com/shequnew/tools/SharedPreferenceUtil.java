package cn.com.shequnew.tools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.com.shequnew.pages.config.AppContext;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class SharedPreferenceUtil {

    // 使用默认的xml文件文件名保存数据
    private static SharedPreferences mSharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(AppContext.appContext);

    // 一次性插入一个值
    public static <T> boolean insert(String key, T value) {
        return put(key, value);
    }

    // 一次只读一个值
    public static String read(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public static long read(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public static int read(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public static float read(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    private static <T> boolean put(String key, T value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        }

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }

        if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }

        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        return editor.commit();
    }

    // 移除某一键值
    public static boolean remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if (hasKey(key)) {
            editor.remove(key);
            return editor.commit();
        }
        return false;
    }

    // 清空所有数据
    public static boolean clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }

    // 判断是否包含某key
    public static boolean hasKey(String key) {
        return mSharedPreferences.contains(key);
    }
}
