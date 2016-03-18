package com.web.core.exception;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

public class GlobalHandlerMethodException extends ExceptionHandlerExceptionResolver {

	private String defaultErrorView;
	
	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}
	
	protected ModelAndView doResolveHandlerMethodException(
			HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {
	if(handlerMethod == null){
		return null;
	}
	Method method = handlerMethod.getMethod();
	if(method == null){
		return null;
	}
	ModelAndView returnValue =  super.
	doResolveHandlerMethodException(request, response, handlerMethod,exception);
	
	ResponseBody responseBody = AnnotationUtils.
		findAnnotation(method, ResponseBody.class);
	
	if(responseBody != null){
		try {
			ResponseStatus responseStatus = AnnotationUtils.
				findAnnotation(method, ResponseStatus.class);
			if(responseStatus != null){
				HttpStatus httpStatus = responseStatus.value();
				String reason = responseStatus.reason();
				if(!StringUtils.hasText(reason)){
					response.setStatus(httpStatus.value());
				}else{
					response.sendError(httpStatus.value(), reason);
				}
			}
			return handleResponseBody(returnValue, request, response);
		} catch (Exception e) {
			return null;
		}
	}
	if(returnValue == null){
		returnValue = new ModelAndView();
		if(returnValue.getViewName() == null){
			returnValue.setViewName(defaultErrorView);
		}
	}
	return returnValue;
	}
	
	/**
	 * 
	 * @param returnValue
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private ModelAndView handleResponseBody(ModelAndView returnValue,
			HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	ModelMap value = returnValue.getModelMap();  
    HttpInputMessage inputMessage = new ServletServerHttpRequest(request);  
    List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();  
    if (acceptedMediaTypes.isEmpty()) {  
        acceptedMediaTypes = Collections.singletonList(MediaType.ALL);  
    }  
    MediaType.sortByQualityValue(acceptedMediaTypes);  
    HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);  
    Class<?> returnValueType = value.getClass();  
    List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();  
    if (messageConverters != null) {  
        for (MediaType acceptedMediaType : acceptedMediaTypes) {  
            for (HttpMessageConverter messageConverter : messageConverters) {  
                if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {  
                	messageConverter.write(value, acceptedMediaType, outputMessage);  
                    return new ModelAndView();  
                }  
            }  
        }  
    }  
    if (logger.isWarnEnabled()) {  
        logger.warn("Could not find HttpMessageConverter that supports return type [" + 
        		returnValueType + "] and " + acceptedMediaTypes);  
    }  
    return null;  
	}
}

















