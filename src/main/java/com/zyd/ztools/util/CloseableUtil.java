package com.zyd.ztools.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtil {
	
	public static void close(Closeable... closeables) {
		if (closeables != null && closeables.length > 0) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					try {
						closeable.close();
						closeable = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void close(AutoCloseable... closeables) {
		if (closeables != null && closeables.length > 0) {
			for (AutoCloseable closeable : closeables) {
				if (closeable != null) {
					try {
						closeable.close();
						closeable = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
	}
}
