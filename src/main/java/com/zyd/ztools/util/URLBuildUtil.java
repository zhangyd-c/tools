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

import java.text.MessageFormat;

import com.zyd.ztools.common.Constants;

/**
 * @Description 构建URL的工具类
 * @author zhangyd
 * @date 2015年12月14日 上午10:17:14
 * @version V1.0
 * @since JDK ： 1.7
 * @modify
 * @Review
 */
public class URLBuildUtil {
	private static final String GET_DOMAIN_URL = "{0}?area_domain={1}";

	/**
	 * @Title getDomainUrl
	 * @Description 获取域名查询接口
	 * @param param
	 * @return
	 * @return String
	 */
	public static String getDomainUrl(String param) {
		return MessageFormat.format(GET_DOMAIN_URL, Constants.DOMAIN_BASE_PATH, param);
	}
}
