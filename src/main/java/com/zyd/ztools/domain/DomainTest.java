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
package com.zyd.ztools.domain;

import com.zyd.ztools.util.HttpUtil;
import com.zyd.ztools.util.URLBuildUtil;
import com.zyd.ztools.util.XmlUtil;

public class DomainTest {

	public static void main(String[] args) {
		String s = "flyat.cc";
		String content = HttpUtil.get(URLBuildUtil.getDomainUrl(s));
		Property property = XmlUtil.parseXml(content);
		if (property == null) {
			System.out.println("请求异常..." + content);
			return;
		}
		int code = property.parseOriginal();
		if (code == 210) {
			System.out.println("恭喜发财！" + s + "域名可以注册...");
		} else if (code == 211) {
			System.out.println(s + "域名已经注册");
		} else {
			System.out.println("域名参数传输错误");
		}
	}
}
