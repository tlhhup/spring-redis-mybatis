package com.tlh.ssmr.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class CustomKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder sb = new StringBuilder();
		sb.append(target.getClass().getName()).append(".");
		sb.append(method.getName());
		if (params != null)
			for (Object param : params) {
				if(param!=null)
					sb.append(param.toString());
			}
		return sb.toString();
	}

}
