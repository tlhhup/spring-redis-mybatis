package com.tlh.ssmr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tlh.ssmr.entity.User;
import com.tlh.ssmr.mapper.UserMapper;

@Service
@Transactional(readOnly=true)
public class UserService {

	@Autowired
	private UserMapper mUserMapper;
	
	@Cacheable(value="userService")
	public List<User> findUserInfo(User user){
		return this.mUserMapper.findUserInfo(user);
	}
	
	@Cacheable(value="userService",key="#id")
	public User findUserInfoById(int id){
		return this.mUserMapper.findUserInfoById(id);
	}
	
	@CacheEvict(value="userService")
	@Transactional(readOnly=false)
	public void saveUserInfo(User user){
		this.mUserMapper.saveUser(user);
	}
	
	@CacheEvict(value="userService")
	@Transactional(readOnly=false)
	public void deleteUserById(int id){
		this.mUserMapper.deleteUserById(id);
	}
	
	@CacheEvict(value="userService")
	@Transactional(readOnly=false)
	public void updateUser(User user){
		this.mUserMapper.updateUser(user);
	}
	
}
