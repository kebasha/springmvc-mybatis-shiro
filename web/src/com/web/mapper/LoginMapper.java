package com.web.mapper;

import java.util.List;
import java.util.Map;

public interface LoginMapper {

	List<Map<String, Object>> queryPage(Map<String, Object> params);
	
}
