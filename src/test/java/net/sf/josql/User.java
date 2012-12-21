package net.sf.josql;

import java.util.Date;

public class User {
	private String name;
	private int age;
	private Date birthDate;

	public User(String name, int age, Date birthDate) {
		super();
		this.name = name;
		this.age = age;
		this.birthDate = birthDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

}
