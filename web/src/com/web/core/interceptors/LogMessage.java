package com.web.core.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


public class LogMessage implements MethodInterceptor {
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Logger log = Logger.getLogger(invocation.getMethod().getDeclaringClass());
		try{
			Object obj = invocation.proceed();
			return obj;
		} finally {
			log.info(invocation.getMethod().getName());
		}
	}


}
