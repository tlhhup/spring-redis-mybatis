package com.tlh.ssmr.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import com.tlh.ssmr.entity.User;
import com.tlh.ssmr.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:beans.xml")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void save(){
		User user=new User();
		user.setAddress("成都");
		user.setSex('1');
		user.setName("张三1");
		user.setPassword(DigestUtils.md5DigestAsHex("admin".getBytes()));
		
		userService.saveUserInfo(user);
	}
	
	@Test
	public void findAll(){
		List<User> users = userService.findUserInfo(null);
		if(users!=null){
			for(User user:users){
				System.out.println(user.getName());
			}
		}
	}
	
}
