package com.web.service.test;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.web.core.ResponseData;
import com.web.mapper.LoginMapper;

@Service
public class AuthService {

	@Resource
	private LoginMapper loginMapper;
	
	public void queryTService(Map<String, Object> params) throws Exception {
		System.out.println("this is authservice!");
		//loginMapper.queryPage(params);
	}

	public void queryPageService(Map<String, Object> params, ResponseData responseData) {
		params.put("page", 1);
		params.put("rows", 10);
		params.put("data", responseData);
		List<Map<String, Object>> result = loginMapper.queryPage(params);
		result.get(100);
		responseData.getData().put("rows", result);
	}
	
	@Cacheable(value="dbCache", key="#key")
	public ResponseData queryUseCache(String key, Map<String, Object> params,
			ResponseData responseData){
		params.put("page", 1);
		params.put("rows", 10);
		params.put("data", responseData);
		List<Map<String, Object>> result = loginMapper.queryPage(params);
		responseData.getData().put("rows", result);
		return responseData;
	}
	
	@Cacheable(value="dbCache", key="#key")
	public String queryUseString(String key){
		String str = "abc";
		System.out.println(str);
		return str;
	}

}
