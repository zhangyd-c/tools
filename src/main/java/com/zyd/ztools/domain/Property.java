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

public class Property {

	private int returncode;
	private String key;
	private String original;

	public int getReturncode() {
		return returncode;
	}

	public void setReturncode(int returncode) {
		this.returncode = returncode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}
	
	//original=210 : Domain name is available      表示域名可以注册
	//original=211 : In Use   表示域名已经注册
	//original=212 : Domain name is invalid    表示域名参数传输错误
	public int parseOriginal(){
		if(this.original != null && this.original.length() > 0){
			String code = this.original.substring(0, this.original.indexOf(":")).trim();
			return Integer.parseInt(code);
		}
		return -1;
	}
}