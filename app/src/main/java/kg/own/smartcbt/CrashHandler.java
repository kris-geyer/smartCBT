package kg.own.smartcbt;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

import kg.own.smartcbt.View.MainActivity;

public class CrashHandler implements Thread.UncaughtExceptionHandler{


        private final Activity currentContext;
        private final static String LINE_SEPARATOR = "\n";

        public CrashHandler(Activity context) {currentContext = context;}

        @Override
        public void uncaughtException(@NotNull Thread t, @NotNull Throwable e) {
            StringWriter stackTrace = new StringWriter();
            String estimatedCause = "";

            e.printStackTrace(new PrintWriter(stackTrace));
            estimatedCause += e.getLocalizedMessage();

            Intent restartActivity = new Intent(currentContext, MainActivity.class);
            String errorReport = "******** ERROR CAUSE **********" +
                    LINE_SEPARATOR +
                    stackTrace.toString() +
                    LINE_SEPARATOR +
                    "********** DEVICE INFORMATION ***********" +
                    LINE_SEPARATOR +
                    "Brand: " +
                    Build.BRAND +
                    LINE_SEPARATOR +
                    "Device: " +
                    Build.DEVICE +
                    LINE_SEPARATOR +
                    "Model: " +
                    Build.MODEL +
                    LINE_SEPARATOR +
                    "ID: " +
                    Build.ID +
                    LINE_SEPARATOR +
                    "Product: " +
                    Build.PRODUCT +
                    LINE_SEPARATOR +
                    "*********** FIRMWARE ***********" +
                    LINE_SEPARATOR +
                    "SDK: " +
                    Build.VERSION.SDK_INT +
                    LINE_SEPARATOR +
                    "Release: " +
                    Build.VERSION.RELEASE +
                    LINE_SEPARATOR +
                    "Incremental: " +
                    Build.VERSION.INCREMENTAL;
            restartActivity
                    .putExtra("crashReport", errorReport)
                    .putExtra("cause", estimatedCause);
            currentContext.startActivity(restartActivity);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
    }
}

