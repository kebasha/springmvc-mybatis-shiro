package com.web.core;

import java.util.Map;
import com.google.common.collect.Maps;

public class ResponseData {

	public int code = Constants.HttpResponseStatus.SUCCESS;
	public String msg = Constants.HttpResponseMsg.SUCCESS;
	public Map<String, Object> data = Maps.newHashMap();
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
