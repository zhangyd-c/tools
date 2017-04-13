package com.zyd.ztools.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static final String REGEX_IMG = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

	public static void main(String[] args) {
		regex();
	}

	/**
	 * @Description 匹配文章中的img
	 * @author zhangyd
	 * @date 2017年3月14日 下午2:40:21
	 */
	private static void regex() {
		String html = "12<a href=\"www.flyat.cc\"><img src=\"www.flyat.cc/a.png\"></a>"
				+ "34<img src=\"www.flyat.cc/b.png\">"
				+ "56<img src=\"www.flyat.cc/c.png\">";
		Pattern p = Pattern.compile(REGEX_IMG);
		Matcher m = null;
		m = p.matcher(html);
		while (m.find()) {
			String result = m.group(0);
			System.out.println(result);
			String result1 = m.group(1);
			System.out.println(result1);
		}
	}
}
