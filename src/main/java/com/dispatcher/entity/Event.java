package com.dispatcher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Event")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Action")
	private String action;

	@Column(name = "Validation")
	private String validation;

	public Event() {
		super();
	}

	public Event(Long id, String action, String validation) {
		super();
		this.id = id;
		this.action = action;
		this.validation = validation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	@Override
	public String toString() {
		return "DispatcherDb [id=" + id + ", action=" + action + ", validation=" + validation + "]";
	}

}
