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
		TextView tv_titre = (TextView) findViewById(R.id.tv_titre);
		tv_titre.setText(pharmacieCourante.getTitle());
//		tv_titre.setText(getIntent().getExtras().getString("titre"));
		
		TextView tv_info = (TextView) findViewById(R.id.tv_info);
//		tv_info.setText(getIntent().getExtras().getString("info"));
		tv_info.setText(pharmacieCourante.getSummary());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
}
