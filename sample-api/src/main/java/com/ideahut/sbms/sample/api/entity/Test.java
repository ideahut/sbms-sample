package com.ideahut.sbms.sample.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.github.ideahut.sbms.shared.annotation.Auditable;
import com.github.ideahut.sbms.shared.entity.EntityBase;

@Entity
@Table(name = "t_test")
@Auditable
@SuppressWarnings("serial")
public class Test extends EntityBase<Long> {
	
	private Long id;
	
	private String name;

	@Id
	@GeneratedValue(generator = "hibincr")
	@GenericGenerator(name = "hibincr", strategy = "identity")
	@Column(name = COLUMN_ID)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
