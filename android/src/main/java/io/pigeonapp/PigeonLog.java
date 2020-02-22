package io.pigeonapp;

import android.util.Log;

public class PigeonLog {
    private static final int INFO = 0;
    private static final int DEBUG = 1;
    private static final int OFF = -1;

    private static int logLevel = PigeonLog.INFO;

    public static void setLogLevel(int logLevel){
        PigeonLog.logLevel = logLevel;
    }

    public static void d(String tag, String logText){
        if(logLevel > 0){
            Log.d(tag, logText);
        }
    }

    public static void i(String tag, String logText){
      if(logLevel == 0){
          Log.i(tag, logText);
      }
  }
}
