package com.pharmacie.pharmaciemap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PharmacieInfosActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_pharmacie);
		Lieu pharmacieCourante =(Lieu) getIntent().getExtras().getParcelable
				("com.pharmacie.pharmaciemap.Lieu");
		
		TextView tv_nomProduit = (TextView) findViewById(R.id.nomProduit);
		tv_nomProduit.setText(pharmacieCourante.getProduit().getNom());
		TextView tv_prixProduit = (TextView) findViewById(R.id.prixProduit);
		tv_prixProduit.setText(pharmacieCourante.getProduit().getPrice());
		TextView tv_descriptionProduit = (TextView) findViewById(R.id.descriptionProduit);
		tv_descriptionProduit.setText(pharmacieCourante.getProduit().getDescription());
		
		TextView tv_nomPharma = (TextView) findViewById(R.id.nomPharma);
		tv_nomPharma.setText(pharmacieCourante.getTitle());
		TextView tv_descriptionPharma = (TextView) findViewById(R.id.descriptionPharma);
		tv_descriptionPharma.setText(pharmacieCourante.getSummary());
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
}
