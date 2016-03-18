package com.web.core.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * �����û��Ƿ��¼
 * @author Administrator
 *
 */
public class UserTrackInterceprot extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String url = request.getRequestURI();
		System.out.println(url);
		Subject subject = (Subject)request.getSession().getAttribute("shiro");
		System.out.println("subject:"+subject);
		if(subject != null){
			//�����¼�ж��Ƿ��в���Ȩ��
			
		}else{
			request.getRequestDispatcher("/").forward(request, response);
			return false;
		}
		return super.preHandle(request, response, handler);
	}
}
