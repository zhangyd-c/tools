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
package com.zyd.ztools.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	public static boolean exec(String param, RegexPatternEnum pattern) {
		return loadMatcher(param, pattern).find();
	}

	public static Matcher loadMatcher(String param, RegexPatternEnum pattern) {
		Pattern p = null;
		if (pattern.getIgnoreCase()) {
			p = Pattern.compile(pattern.getPattern(), Pattern.CASE_INSENSITIVE);
		} else {
			p = Pattern.compile(pattern.getPattern());
		}
		Matcher m = null;
		m = p.matcher(param);
		return m;
	}

	public static void main(String[] args) {
		String param = "12.123";
//		Matcher m = loadMatcher(param, RegexPatternEnum.CHINESE_REGEX);
//		while (m.find()) {
//			String result = m.group(0);
//			System.out.println(result);
//		}
		System.out.println(exec(param, RegexPatternEnum.DECIMALS_REGEX));
	}
}
