package in.co.trapps.odiapurana.logger;

import android.util.Log;

import in.co.trapps.odiapurana.BuildConfig;

/**
 * Common class to print Logs
 *
 * @author Akash Patra
 */
public class Logger {

    public static void logD(String tag, ILoggerActivator iLoggerActivator, Object... msg) {
        if (BuildConfig.DEBUG && iLoggerActivator.isEnabled()) {
            StringBuilder fullMsg = new StringBuilder();
            fullMsg.append(iLoggerActivator);
            for (Object str : msg) {
                fullMsg.append(str);
            }
            Log.d(tag, fullMsg.toString());
        }
    }

    public static void logI(String tag, ILoggerActivator iLoggerActivator, Object... msg) {
        if (iLoggerActivator.isEnabled()) {
            StringBuilder fullMsg = new StringBuilder();
            for (Object str : msg) {
                fullMsg.append(str);
            }
            Log.i(tag, fullMsg.toString());
        }
    }

    public static void logE(String tag, Throwable throwableException, ILoggerActivator iLoggerActivator, Object... msg) {
        if (iLoggerActivator.isEnabled()) {
            StringBuilder fullMsg = new StringBuilder();
            for (Object str : msg) {
                fullMsg.append(str);
            }
            Log.e(tag, fullMsg.toString());
        }
    }
}