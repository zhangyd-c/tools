/**
 * Copyright [2016-2017] [yadong.zhang]
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zyd.ztools.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http请求工具类
 *
 * @author (作者) zhangyd-c 2015年8月13日 上午9:11:28
 * @version (版本) V1.0
 * @since (该版本支持的JDK版本) ： 1.7
 */
public class HttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.10 Safari/537.36";

	private enum HttpMethod {
		POST, GET, PUT
	}

	public static String get(String requestUrl) {
		return request(HttpMethod.GET.toString(), requestUrl, null);
	}

	public static String post(String requestUrl, Object params) {
		return request(HttpMethod.POST.toString(), requestUrl, params.toString());
	}

	public static String put(String requestUrl, Object params) {
		return request(HttpMethod.PUT.toString(), requestUrl, params.toString());
	}

	/**
	 * @Description GET/PUT/POST default GET
	 * @author zhangyd
	 * @date 2017年4月13日 上午9:49:20
	 * @param method
	 *            GET/PUT/POST
	 * @param urlString
	 *            urlString
	 * @param parameter
	 *            request parameter
	 * @return
	 */
	private static String request(String method, String urlString, String parameter) {
		LOGGER.info("{} Http url: {}", new Date(), urlString);
		HttpURLConnection connection = null;
		try {
			connection = openConnection(urlString);

			connection.setRequestMethod(method);
			// 设定传送的内容类型是json数据
			// (如果不设此项,json数据 ,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Action", "1000");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Connection", "keep-alive");
			// http正文内，因此需要设为true, 默认情况下是false;
			connection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			connection.setDoInput(true);
			connection.setConnectTimeout(3000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(3000);// 设置读取数据超时时间，单位毫秒
			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			if (parameter != null) {
				final OutputStream outputStream = connection.getOutputStream();
				writeOutput(outputStream, parameter);
				close(outputStream);
			}

			LOGGER.info("HttpUtil response: {} - {}", connection.getResponseCode(), connection.getResponseMessage());

			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				return readInput(connection.getInputStream());
			} else {
				return readInput(connection.getErrorStream());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private static HttpURLConnection openConnection(final String urlString) throws Exception {

		final URL url = new URL(urlString);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		return connection;
	}

	private static void writeOutput(final OutputStream outputStream, final String parameter) throws Exception {
		ByteArrayInputStream inputStram = new ByteArrayInputStream(parameter.getBytes("UTF-8"));

		final byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStram.read(buffer)) != -1) {
			outputStream.write(buffer, 0, length);
		}
	}

	private static String readInput(final InputStream is) {

		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = reader.readLine()) != null) {
				content.append(line);
				content.append("\r\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(reader, is);
		}
		return content.toString();
	}

	public static void close(Closeable... closeables) {
		if (closeables != null && closeables.length > 0) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
}
