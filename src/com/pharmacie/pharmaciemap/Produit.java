package com.pharmacie.pharmaciemap;

import android.os.Parcel;
import android.os.Parcelable;

public class Produit implements Parcelable{
	
	private String nom;
	private String description;
	private String price;
	
	public Produit() {;};
	
	public Produit(Parcel in) {
		readFromParcel(in);
	}
	
	
	public Produit(String nom, String description, String price) {
		this.nom = nom;
		this.description = description;
		this.price = price;
	}
	
	

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(nom);
		dest.writeString(description);
		dest.writeString(price);
	}
	
	public static final Parcelable.Creator CREATOR =
			new Creator() {
				public Produit createFromParcel(Parcel in) {
					return new Produit(in);
				}

				@Override
				public Produit[] newArray(int size) {
					// TODO Auto-generated method stub
					return new Produit[size];
				}
		
			};
	
	private void readFromParcel(Parcel in) {
		nom = in.readString();
		description = in.readString();
		price = in.readString();
	}

}
