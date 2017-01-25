package com.tlh.ssmr.mapper;

import java.util.List;

import com.tlh.ssmr.entity.User;

public interface UserMapper {

	void saveUser(User user);
	
	void deleteUserById(int id);
	
	void updateUser(User user);
	
	List<User> findUserInfo(User user);
	
	User findUserInfoById(int id);
	
}
