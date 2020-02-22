package io.pigeonapp;

import android.util.Log;

public class PigeonLog {
		private static enum LogLevel {
				INFO(1), DEBUG(0), OFF(-1);

				private final int code;

				LogLevel(int code) {
						this.code = code;
				}

				public int getCode() {
				    return this.code;
				}

				public static LogLevel lookup(int code) {
						switch (code) {
								case 1:
										return LogLevel.INFO;
								case 0:
										return LogLevel.DEBUG;
								case -1:
										return LogLevel.OFF;
								default:
										return LogLevel.OFF;
						}
				}
		}

		private static LogLevel logLevel = LogLevel.OFF;

		public static void setLogLevel(int logLevel) {
				PigeonLog.logLevel = LogLevel.lookup(logLevel);
		}

		public static void d(String tag, String logText) {
				if (logLevel.getCode() >= LogLevel.DEBUG.getCode()) {
						Log.d(tag, logText);
				}
		}

		public static void i(String tag, String logText) {
				if (logLevel.getCode() >= LogLevel.INFO.getCode()) {
						Log.i(tag, logText);
				}
		}
}
