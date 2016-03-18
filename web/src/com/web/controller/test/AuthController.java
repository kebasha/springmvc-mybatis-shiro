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
		//��ȡkaptcha���ɴ����session�е���֤��
		String kaptchaValue = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		System.out.println(verifyCode + "==" + kaptchaValue);
		
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		token.setRememberMe(true);
		
		//��ȡ��ǰ��subject
		Subject currentUser = SecurityUtils.getSubject();
		try {
			//�ڵ�����login������,SecurityManager���յ�AuthenticationToken,�����䷢�͸������õ�Realmִ�б������֤���  
            //ÿ��Realm�����ڱ�Ҫʱ���ύ��AuthenticationTokens������Ӧ  
            //������һ���ڵ���login(token)����ʱ,�����ߵ�MyRealm.doGetAuthenticationInfo()������,������֤��ʽ����˷���
			currentUser.login(token);
			//Session shiroSession = currentUser.getSession();
			session.setAttribute("shiro", currentUser);
			if(currentUser.isAuthenticated()){
				System.out.println("�û�:" + userName + "��֤ͨ��");
				System.out.println("ӵ�н�ɫ:"+currentUser.hasRole("admin"));
				System.out.println("ӵ��Ȩ��:"+currentUser.isPermitted("write"));
				resultPageUrl = "admin";
			}else{
				token.clear();
				resultPageUrl = "login";
			}
		} catch (IncorrectCredentialsException e) {
			msg = "��¼�������";
			session.setAttribute("message", msg);
		} catch (ExcessiveAttemptsException e) {
			msg = "��¼ʧ�ܴ�������";
			session.setAttribute("message", msg);
		} catch (LockedAccountException e) {
			msg = "�ʺ��ѱ�����";
			session.setAttribute("message", msg);
		} catch (DisabledAccountException e) {
			msg = "�ʺ��ѱ�����";
			session.setAttribute("message", msg);
		} catch (ExpiredCredentialsException e) {
			msg = "�ʺ��ѹ���";
			session.setAttribute("message", msg);
		} catch (UnknownAccountException e) {
			msg = "�ʺŲ�����";
			session.setAttribute("message", msg);
		} catch (UnauthorizedException e) {
			msg = "��û�еõ���Ӧ����Ȩ";
			session.setAttribute("message", msg);
		}
		return resultPageUrl;
	}
	
	@RequestMapping("/ajax")
	public @ResponseBody ResponseData ajax() throws Exception{
		
		authService.queryPageService(params, responseData);
		//responseData = authService.queryUseCache("abcd", params, responseData);
		throw new Exception("�����쳣");
		//return responseData;
	}

}


























