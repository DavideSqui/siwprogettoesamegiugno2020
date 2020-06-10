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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=false)
	private String name;

	@Column(nullable=false)
	private String description;
	
	@Column(updatable=false,nullable=false)
	private LocalDateTime beginTimeStamp;

	//versione inversa,gi� � eager ma ce lo segno:
	@ManyToOne(fetch=FetchType.EAGER)
	private User owner;

	//questo many to many non ha scritto mapped by
	@ManyToMany
	private List<User> members;

	//one to many unidirezionale con unica join table verso task invece di mappedby
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="project_id")
	public List<Task> tasks;

	//costuttore e getter setter

	public Project() {		}

	public Project(String name, String description) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.description=description;
		this.members=new ArrayList<>();
		this.tasks=new ArrayList<>();
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public LocalDateTime getBeginTimeStamp() {
		return beginTimeStamp;
	}

	public void setBeginTimeStamp(LocalDateTime beginTimeStamp) {
		this.beginTimeStamp = beginTimeStamp;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getVisibleUsers() {
		return members;
	}

	public void setVisibleUsers(List<User> members) {
		this.members = members;
	}

	public List<Task> getTaskList() {
		return tasks;
	}

	public void setTaskList(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	//metdodo aggiuntivo 
	public void addMember(User member) {
		this.members.add(member);
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	@PrePersist
	protected void onPersist() {
		this.beginTimeStamp=LocalDateTime.now();
	}

	
	//metodo hascode e quals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Project other = (Project) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
	
	



}
