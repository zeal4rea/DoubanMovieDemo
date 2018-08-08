package com.zeal4rea.doubanmoviedemo.util.crash;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class CustomOnCrash {
    private static Application mApplication;
    public static final String EXTRA_STACK_TRACE = "extra_stack_trace";
    private static final String LAST_CRASH_TIMESTAMP = "last_crash_timestamp";
    private static final String CUSTOM_ON_CRASH = "custom_on_crash";
    private static final int MAX_STACK_TRACE_SIZE = 131071;
    private static final int minTimeBetweenCrashes = 3000;
    public static void install(final Application application) {
        try {
            mApplication = application;
            final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    Context context = application.getApplicationContext();
                    if (crashTooSoon(context)) {
                        if (oldHandler != null) {
                            oldHandler.uncaughtException(thread, throwable);
                            return;
                        }
                    }
                    setLastCrashTimestamp(context, new Date().getTime());
                    startErrorActivity(throwable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startErrorActivity(Throwable throwable) {
        throwable.printStackTrace();
        Intent startErrorIntent = new Intent(mApplication, ErrorActivity.class);
        startErrorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTrackStr = sw.toString();
        if (stackTrackStr.length() > MAX_STACK_TRACE_SIZE) {
            String claimer = "[stack trace too large]";
            stackTrackStr = stackTrackStr.substring(0, MAX_STACK_TRACE_SIZE - claimer.length()) + claimer;
        }
        startErrorIntent.putExtra(EXTRA_STACK_TRACE, stackTrackStr);
        mApplication.getApplicationContext().startActivity(startErrorIntent);
    }

    private static long getLastCrashTimestamp(@NonNull Context context) {
        return context.getSharedPreferences(CUSTOM_ON_CRASH, Context.MODE_PRIVATE).getLong(LAST_CRASH_TIMESTAMP, -1);
    }

    private static void setLastCrashTimestamp(@NonNull Context context, long timestamp) {
        context.getSharedPreferences(CUSTOM_ON_CRASH, Context.MODE_PRIVATE).edit().putLong(LAST_CRASH_TIMESTAMP, timestamp).apply();
    }

    private static boolean crashTooSoon(@NonNull Context context) {
        long lastCrashTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = new Date().getTime();
        return currentTimestamp <= lastCrashTimestamp || currentTimestamp - lastCrashTimestamp < minTimeBetweenCrashes;
    }

    public static void restartApplication(@NonNull Activity activity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        activity.finish();
        activity.startActivity(intent);
        killCurrentProcess();
    }

    public static void closeApplication(@NonNull Activity activity) {
        activity.finish();
        killCurrentProcess();
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Activity> getLauncherActivity(@NonNull Context context) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntentForPackage != null && launchIntentForPackage.getComponent() != null) {
            try {
                return (Class<? extends Activity>) Class.forName(launchIntentForPackage.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
