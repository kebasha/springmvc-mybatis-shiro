package com.web.core.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

/**
 * 去除SQL多余的无用的工具类
 * @author Administrator
 *
 */
public class SqlRemoveHelper {

	/*Order by*/
	public static final String ORDER_BY_REGEX = "order\\s*by[\\w|\\W|\\s|\\S]*";
	
	/**/
	
	/**
	 * 去除order by
	 */
	public static String removeOrders(String sql){
		Preconditions.checkNotNull(sql);
		Pattern pattern = Pattern.compile(ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
