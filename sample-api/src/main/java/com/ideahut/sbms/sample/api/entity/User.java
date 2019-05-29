package com.ideahut.sbms.sample.api.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.github.ideahut.sbms.shared.annotation.Auditable;
import com.github.ideahut.sbms.shared.entity.EntityLongIdTimeVersion;
import com.github.ideahut.sbms.shared.entity.embed.Address;
import com.github.ideahut.sbms.shared.entity.embed.Contact;
import com.github.ideahut.sbms.shared.entity.embed.Person;

@Entity
@Table(name = "t_user", indexes = {@Index(name = "login_idx", columnList = "username,password")})
@Auditable
@SuppressWarnings("serial")
public class User extends EntityLongIdTimeVersion {
	
	public static final Integer ROLE_ADMIN	= 0;
	public static final Integer ROLE_USER 	= 1;	

	public static final Integer STATUS_REGISTERED	= 0;
	public static final Integer STATUS_ACTIVE		= 1;
	public static final Integer STATUS_INACTIVE		= 2;
	public static final Integer STATUS_SUSPENDED	= 3;
	
	
	private String username;
	
	private String password;
	
	private Integer status;
	
	private Integer role;
	
	private String photo;
	
	private Address address;
	
	private Person person;
	
	private Contact contact;

	
	@Column(name = "username", unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "role", nullable = false)
	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	@Column(name = "photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Embedded
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Embedded
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Embedded
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}	
	
}
