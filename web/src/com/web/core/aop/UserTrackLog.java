package com.web.core.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * �û������־��¼
 * @author Administrator
 *
 */
@Aspect
public class UserTrackLog {
	
	@AfterReturning
	public void userTrack(){
		
	}
}
