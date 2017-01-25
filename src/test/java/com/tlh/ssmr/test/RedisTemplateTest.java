package com.tlh.ssmr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:beans.xml")
public class RedisTemplateTest {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Test
	public void getString(){
		String a = redisTemplate.opsForValue().get("a");
		System.out.println(a);
	}
	
}
