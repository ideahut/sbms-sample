package com.ideahut.sbms.sample.client.dto;

import java.math.BigDecimal;

import com.github.ideahut.sbms.client.dto.base.DtoBase;

@SuppressWarnings("serial")
public class TestDto extends DtoBase {
	
	private Long id;
	
	private String name;
	
	private BigDecimal salary; // buat test hessian BigDecimal

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestDto [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", salary=");
		builder.append(salary);
		builder.append("]");
		return builder.toString();
	}	
	
}
