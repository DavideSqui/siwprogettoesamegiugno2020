package com.example.demo.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name="Users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	/*@Column(nullable=false)
	private String username;

	@Column(nullable=false)
	private String password;*/

	@Column(nullable=false)
	private String firstName;

	@Column(nullable=false)
	private String lastName;

	@Column(nullable=false,updatable=false)
	private LocalDateTime creationTimeStamp;

	@Column(nullable=false)
	private LocalDateTime lastUpdateTimeStamp;

	//onetomany bidirezionale
	@OneToMany( mappedBy="owner", cascade=CascadeType.REMOVE)
	private List<Project> ownedProjects;

	//many to many bidirezionale
	@ManyToMany(mappedBy="members",fetch=FetchType.EAGER)
	private List<Project> visibleProjects;

	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.PERSIST)
	private Task assignedTask;
	
	//costuttore e getter setter
	public User() {}

	public User(String u,String ps,String fn,String ln) {
		//this.username=u;
		//this.password=ps;
		this.firstName=fn;
		this.lastName=ln;
		this.ownedProjects=new ArrayList<>();
		this.visibleProjects=new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}*/

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public LocalDateTime getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(LocalDateTime lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	public List<Project> getOwnedProjects() {
		return ownedProjects;
	}

	public void setOwnedProjects(List<Project> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	public List<Project> getVisibleProjects() {
		return visibleProjects;
	}

	public void setVisibleProjects(List<Project> visibleProjects) {
		this.visibleProjects = visibleProjects;
	}
	

	public Task getAssignedTask() {
		return assignedTask;
	}

	public void setAssignedTask(Task assignedTask) {
		this.assignedTask = assignedTask;
	}

	//metodo persist dei tempi
	@PrePersist
	protected void onPersist() {
		this.creationTimeStamp=LocalDateTime.now();
		this.lastUpdateTimeStamp=LocalDateTime.now();	
	}

	//metodo persist dei tempi di aggiornamento
	@PreUpdate
	protected void onUpdate() {
		this.lastUpdateTimeStamp=LocalDateTime.now();	
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}


}
