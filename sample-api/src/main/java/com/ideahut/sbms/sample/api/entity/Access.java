package com.ideahut.sbms.sample.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.github.ideahut.sbms.common.util.TimeUtil;
import com.github.ideahut.sbms.shared.annotation.Auditable;
import com.github.ideahut.sbms.shared.entity.EntityTime;

@Entity
@Table(name = "t_access")
@Auditable
@SuppressWarnings("serial")
public class Access extends EntityTime<String> {
	
	private String key;
	
	private User user;
	
	private String validation;
	
	private Long expiration;

	@Id
	@GeneratedValue(generator = "hbm_uuid")
	@GenericGenerator(name = "hbm_uuid", strategy = "uuid")
	@Column(name = "KEY_", length = 64)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Lob
	@Column(name = "validation")
	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	@Column(name = "expiration")
	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}
	
	public boolean hasExpired() {
		if (null == expiration) return true;
		Long now = TimeUtil.getGMTCurrentTimeMillis();
		return now > expiration;
	}
	
}
