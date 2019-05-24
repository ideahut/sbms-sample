package com.ideahut.sbms.sample.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.ideahut.sbms.sample.api.entity.Test;

public interface TestRepository extends CrudRepository<Test, Long> {
		
}
