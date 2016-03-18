package com.web.core.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * 用户浏览日志记录
 * @author Administrator
 *
 */
@Aspect
public class UserTrackLog {
	
	@AfterReturning
	public void userTrack(){
		
	}
}
