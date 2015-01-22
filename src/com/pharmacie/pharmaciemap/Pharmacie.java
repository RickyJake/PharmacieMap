package com.pharmacie.pharmaciemap;

import com.google.android.gms.maps.model.LatLng;

public class Pharmacie {
	
	private LatLng location;
	
	private Lieu lieu;
	
	public Pharmacie() {
		
	}
	
	public Pharmacie(Lieu lieu, LatLng location) {
		this.lieu = lieu;
		this.location = location;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public Lieu getLieu() {
		return lieu;
	}

	public void setLieu(Lieu lieu) {
		this.lieu = lieu;
	}

	
	

}
