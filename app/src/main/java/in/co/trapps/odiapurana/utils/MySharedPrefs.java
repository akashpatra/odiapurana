package in.co.trapps.odiapurana.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

/**
 * Singleton of Shared Preferences. Exposes interfaces for storing and getting different types of data.
 *
 * @author Akash Patra
 */
public class MySharedPrefs {
    public static final String PURANA_PREFS = "purana_prefs";
    public static final String AD_COUNT = "ad_count";
    public static final String AD_COUNT_INITIAL_TIME = "ad_count_initial_time";
    public static final String AD_BLOCKED_LAST_TIME = "ad_blocked_last_time";
    private static final LoggerEnable CLASS_NAME = LoggerEnable.MySharedPrefs;
    private static MySharedPrefs mySharedPrefs;
    private static Gson gson = new Gson();
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();
    private Context context;

    private MySharedPrefs(Context context, String namePreferences, int mode) {
        this.context = context;
        if (CommonUtils.isStringEmpty(namePreferences)) {
            namePreferences = PURANA_PREFS;
        }
        sharedPreferences = context.getSharedPreferences(namePreferences, mode);
        editor = sharedPreferences.edit();
    }

    public static MySharedPrefs init(Context context, String namePreferences, int mode) {
        if (null == mySharedPrefs) {
            mySharedPrefs = new MySharedPrefs(context, namePreferences, mode);
        }
        return mySharedPrefs;
    }

    public static MySharedPrefs with() {
        if (null == mySharedPrefs) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> Ignoring MySharedPrefs.with() invoked before MySharedPrefs.init() called.");
        }
        return mySharedPrefs;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }
        if (CommonUtils.isStringEmpty(key)) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putString(key, gson.toJson(object));
    }

    private void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {
        String objValue = sharedPreferences.getString(key, null);
        if (objValue == null) {
            return null;
        } else {
            try {
                return gson.fromJson(objValue, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key" + key + " is instance of other class");
            }
        }
    }

    public void putString(String key, String value) {
        if (CommonUtils.isStringEmpty(key)) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        String value = sharedPreferences.getString(key, null);
        if (CommonUtils.isStringEmpty(value)) {
            return null;
        } else {
            return value;
        }
    }

    public void putBoolean(String key, boolean value) {
        if (CommonUtils.isStringEmpty(key)) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putLong(String key, long value) {
        if (CommonUtils.isStringEmpty(key)) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0L);
    }

    public void putFloat(String key, float value) {
        if (CommonUtils.isStringEmpty(key)) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0.0f);
    }

    public void putInteger(String key, int value) {
        if (CommonUtils.isStringEmpty(key)) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInteger(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}
