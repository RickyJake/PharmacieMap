package com.pharmacie.pharmaciemap;

import com.google.android.gms.maps.model.LatLng;

public class Pharmacie {
	
	private LatLng location;
	
	private String title;
	
	private String summary;
	
	private int id;
	
	public Pharmacie() {
		
	}
	
	public Pharmacie(int id, String title, String summary, LatLng location) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.location = location;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
