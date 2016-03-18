package com.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.web.core.Constants;
import com.web.core.ResponseData;

public class BaseController {

	protected final transient Log log = LogFactory.getLog(getClass());
	
	//返回页面存入JSON数据的MAP
	protected ResponseData responseData;
	
	protected Map<String, Object> params;
	
	protected HttpServletRequest request;
	 
	protected HttpServletResponse response;
	
	protected HttpSession session;
	
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		//action为多例，每次访问都创建一个responseData返回对象
		responseData = new ResponseData();
		//根据URI结合shiro判断每个页面的CURD权限
		System.out.println(request.getRequestURI());
		System.out.println(request.getRequestURL());
		/*获取参数*/
		try {
			params = getParamAsMap(request);
		} catch (UnsupportedEncodingException e) {
			log.error("获取参数异常", e);
		}
	}
	
	/**
	 * 基于@ExceptionHandler异常处理
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleException(Exception e, HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		log.error(e);
		mav.setViewName("error");
		responseData.setCode(Constants.HttpResponseStatus.ERROR);
		if("".equals(e.getMessage())){
			responseData.setMsg(Constants.HttpResponseMsg.ERROR);
		}else{
			responseData.setMsg(e.getMessage());
		}
		mav.addObject(responseData);
		return mav;
	}
	
	private static Map<String, Object> getParamAsMap(HttpServletRequest request) 
		throws UnsupportedEncodingException {
		Map<String, Object> m = new HashMap<String, Object>();
		request.setCharacterEncoding("UTF-8");
		//HttpSession session = request.getSession();
		Map<?, ?> map = request.getParameterMap();
		Iterator<?> keyIterator = (Iterator<?>) map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value=((String[]) (map.get(key)))[0];
			m.put(key, value);
		}
		return m;
	}
}
