package com.web.core.interceptors;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;

public class LogException implements ThrowsAdvice {

	public void afterThrowing(Method method, Object[] args, Object target, 
			Exception ex) throws Throwable {
		Logger log = Logger.getLogger(target.getClass());
		StringBuilder sb = new StringBuilder();
		sb.append("########################Exception#########################\n");
		sb.append("From Class:" + target.getClass().getName() + "\n");
		sb.append("Method:" + method.getName() + "\n");
		for(int i=0; i < args.length; i++){
			sb.append("arg[" + i + "]:" + args[i] + "\n");
		}
		sb.append("Exception class:" + ex.getClass().getName() + "\n");
		sb.append("Message:" + ex.getMessage());
		sb.append("##########################################################");
		log.error(sb.toString());
	}
}
