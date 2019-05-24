package com.ideahut.sbms.sample.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.ideahut.sbms.sample.api.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public User findByUsername(String username);
	
}
