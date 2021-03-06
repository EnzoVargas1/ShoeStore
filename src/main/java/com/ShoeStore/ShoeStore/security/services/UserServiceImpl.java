package com.ShoeStore.ShoeStore.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ShoeStore.ShoeStore.security.models.User;
import com.ShoeStore.ShoeStore.security.repository.UserRepository;



@Service
public class UserServiceImpl implements UserService{
	
	@Autowired UserRepository userRepository;

	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUser(int userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public User getUserByUserName(String username) {
		return userRepository.findByUserName(username).orElse(null);
	}
	
	@Override
	public User updateUser(User user,int userid) {
		User currentUser = userRepository.findById(userid).orElse(null);
		currentUser.setUserName(user.getUserName());
		currentUser.setPassword(user.getPassword());
		currentUser.setActive(user.isActive());
		return userRepository.save(currentUser);
	}
}

