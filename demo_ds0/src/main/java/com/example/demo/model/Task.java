package com.example.demo.model;


import java.time.LocalDateTime;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;


	
	@Column(nullable=false,unique=true)
	private String name;

	@Column(nullable=false)
	private String description;

	@Column(nullable=false)
	private boolean completed;

	@ManyToOne
	private Project project;
	
	@Column(updatable=false,nullable=false)
	private LocalDateTime creationTimeStamp;

	@Column(nullable=false)
	private LocalDateTime lastUpdateTimeStamp;

	//costuttore e getter setter
	public Task() {}
	public Task(String name, String description,Project p) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.description=description;
		this.project=p;
	
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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
	
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
		//metodo persist dei tempi
		@PrePersist
		protected void onPersist() {
			this.creationTimeStamp=LocalDateTime.now();
			this.lastUpdateTimeStamp=LocalDateTime.now();	
		}

		//metodo persist dei tempi
		@PreUpdate
		protected void onUpdate() {
			this.lastUpdateTimeStamp=LocalDateTime.now();	
		}

		//equale e hashcode
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
			Task other = (Task) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}



}
