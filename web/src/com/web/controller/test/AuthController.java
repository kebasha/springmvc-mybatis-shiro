package com.web.controller.test;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import com.web.controller.BaseController;
import com.web.core.ResponseData;
import com.web.service.test.AuthService;

@Controller
@RequestMapping("/auth")
@Scope("prototype")
public class AuthController extends BaseController {

	@Resource
	private AuthService authService;
	
	@RequestMapping("/login")
	public String login() throws Exception{
		String resultPageUrl = InternalResourceViewResolver.FORWARD_URL_PREFIX + "/";
		String msg = "";
		String userName = params.get("userName").toString();
		String password = params.get("password").toString();
		session.setAttribute("userName", userName);
		String verifyCode = params.get("verifyCode").toString();
		//获取kaptcha生成存放在session中的验证码
		String kaptchaValue = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		System.out.println(verifyCode + "==" + kaptchaValue);
		
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		token.setRememberMe(true);
		
		//获取当前的subject
		Subject currentUser = SecurityUtils.getSubject();
		try {
			//在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查  
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应  
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			currentUser.login(token);
			//Session shiroSession = currentUser.getSession();
			session.setAttribute("shiro", currentUser);
			if(currentUser.isAuthenticated()){
				System.out.println("用户:" + userName + "认证通过");
				System.out.println("拥有角色:"+currentUser.hasRole("admin"));
				System.out.println("拥有权限:"+currentUser.isPermitted("write"));
				resultPageUrl = "admin";
			}else{
				token.clear();
				resultPageUrl = "login";
			}
		} catch (IncorrectCredentialsException e) {
			msg = "登录密码错误";
			session.setAttribute("message", msg);
		} catch (ExcessiveAttemptsException e) {
			msg = "登录失败次数过多";
			session.setAttribute("message", msg);
		} catch (LockedAccountException e) {
			msg = "帐号已被锁定";
			session.setAttribute("message", msg);
		} catch (DisabledAccountException e) {
			msg = "帐号已被禁用";
			session.setAttribute("message", msg);
		} catch (ExpiredCredentialsException e) {
			msg = "帐号已过期";
			session.setAttribute("message", msg);
		} catch (UnknownAccountException e) {
			msg = "帐号不存在";
			session.setAttribute("message", msg);
		} catch (UnauthorizedException e) {
			msg = "您没有得到相应的授权";
			session.setAttribute("message", msg);
		}
		return resultPageUrl;
	}
	
	@RequestMapping("/ajax")
	public @ResponseBody ResponseData ajax() throws Exception{
		
		authService.queryPageService(params, responseData);
		//responseData = authService.queryUseCache("abcd", params, responseData);
		throw new Exception("出现异常");
		//return responseData;
	}

}


























