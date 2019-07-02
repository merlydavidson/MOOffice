package com.microoffice.app.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.io.PrintWriter;
import java.io.StringWriter;

import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by m2comsys on 11/7/17.
 */

public class MyExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {


    public MyExceptionHandler() {

    }

    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);

        String s = stackTrace.toString();

        LogWriter.writeLog("Unacaught exp",stackTrace.toString());

    }
}
