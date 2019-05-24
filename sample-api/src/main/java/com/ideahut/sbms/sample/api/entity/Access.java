package com.ideahut.sbms.sample.api.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

import com.github.ideahut.sbms.shared.entity.EntityTime;

@Entity
@Table(name = "t_access")
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
	@JoinColumn(name = "user", nullable = true)
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
		Long now = currentTimeMillis() + 1;
		return now > expiration;
	}
	
	public static Long currentTimeMillis() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String value = format.format(now);
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		Long result = 0L;
		try {
			result = format.parse(value).getTime();
		} catch (Exception e) {}
		return result;
	}
	
}
