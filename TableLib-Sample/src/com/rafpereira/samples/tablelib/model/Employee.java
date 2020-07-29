package com.rafpereira.samples.tablelib.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
] */
public class Employee {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Name.
	 */
	private String name;
	
	/**
	 * Birthday Date.
	 */
	private Date birthdayDate;
	
	/**
	 * Count of LinkedIn contacts.
	 */
	private long linkedinContacts;
	
	/**
	 * Salary.
	 */
	private BigDecimal salary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(Date birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	public long getLinkedinFriends() {
		return linkedinContacts;
	}

	public void setLinkedinFriends(long linkedinFriends) {
		this.linkedinContacts = linkedinFriends;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Employee(String name, String birthdayStr, long linkedinFriends, BigDecimal salary) {
		super();
		this.name = name;
		try {
			this.birthdayDate = sdf.parse(birthdayStr);
		} catch (ParseException e) {}
		this.linkedinContacts = linkedinFriends;
		this.salary = salary;
	}
	
}
