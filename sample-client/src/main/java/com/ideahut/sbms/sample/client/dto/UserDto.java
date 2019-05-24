package com.ideahut.sbms.sample.client.dto;

import com.github.ideahut.sbms.client.dto.base.DtoLongIdTimeVersion;
import com.github.ideahut.sbms.client.dto.embed.AddressDto;
import com.github.ideahut.sbms.client.dto.embed.ContactDto;
import com.github.ideahut.sbms.client.dto.embed.PersonDto;

@SuppressWarnings("serial")
public class UserDto extends DtoLongIdTimeVersion {
	
	private String username;
	
	private Integer status;
	
	private Integer role;
	
	private String photo;
	
	private AddressDto address;
	
	private PersonDto person;
	
	private ContactDto contact;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public AddressDto getAddress() {
		return address;
	}

	public void setAddress(AddressDto address) {
		this.address = address;
	}

	public PersonDto getPerson() {
		return person;
	}

	public void setPerson(PersonDto person) {
		this.person = person;
	}

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}			
	
}
