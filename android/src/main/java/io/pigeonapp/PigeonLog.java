package io.pigeonapp;

import android.util.Log;

public class PigeonLog {
	private static enum LogLevel {
		INFO(0), DEBUG(1), OFF(-1);

		private final int levelCode;

		LogLevel(int levelCode) {
			this.levelCode = levelCode;
		}

		public int getLevelCode() {
			return this.levelCode;
		}

		public static LogLevel lookupLogLevel(int levelCode) {
			switch (levelCode) {
				case 0:
					return LogLevel.INFO;
				case 1:
					return LogLevel.DEBUG;
				case -1:
					return LogLevel.OFF;
				default:
					return LogLevel.INFO;
			}
		}
	}

	private static LogLevel logLevel = LogLevel.INFO;

	public static void setLogLevel(int logLevel) {
		PigeonLog.logLevel = LogLevel.lookupLogLevel(logLevel);
	}

	public static void d(String tag, String logText) {
		if (logLevel.getLevelCode() > 0) {
			Log.d(tag, logText);
		}
	}

	public static void i(String tag, String logText) {
		if (logLevel.getLevelCode() == 0) {
			Log.i(tag, logText);
		}
	}
}
