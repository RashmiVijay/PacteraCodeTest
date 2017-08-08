package com.test.pacteraandroidtest;
public class Facts {

	private String title;
	private String description;
	private String image;

	public Facts() {
		// TODO Auto-generated constructor stub
	}
//getter and setter functions for getting title, description and image from the JSON object 
	public Facts(String name, String description,String image) {
		super();
		this.title = name;
		this.description = description;
		this.image = image;
	}


	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
