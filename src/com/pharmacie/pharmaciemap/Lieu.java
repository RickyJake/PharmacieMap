package com.pharmacie.pharmaciemap;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Lieu implements Parcelable{
	
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String summary;
	
	private int id;
	
	public Lieu() {;};
	
	public Lieu(Parcel in) {
		readFromParcel(in);
	}
	
	public Lieu( int id, String title, String summary) {
		this.id = id;
		this.title = title;
		this.summary = summary;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(summary);
	}
	
	public static final Parcelable.Creator CREATOR =
			new Creator() {
				public Lieu createFromParcel(Parcel in) {
					return new Lieu(in);
				}

				@Override
				public Lieu[] newArray(int size) {
					// TODO Auto-generated method stub
					return new Lieu[size];
				}
		
			};
	
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		title = in.readString();
		summary = in.readString();
	}
	
	
}
