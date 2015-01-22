package com.pharmacie.pharmaciemap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class PharmacieActivity extends Activity {

	private GoogleMap map;
	private ArrayList<Pharmacie> listPharmacie;
	private WeakHashMap<LatLng, Pharmacie> hashMap;
	private int typeView = GoogleMap.MAP_TYPE_HYBRID;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	listPharmacie = new ArrayList<Pharmacie>();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        remplirListePharmacies();
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        hashMap = new WeakHashMap<LatLng, Pharmacie>();
        
        refreshView();
        
        
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pharmacie, menu);
        return true;
    }

    private void remplirListePharmacies() {
    	try {
    		// Si notre fichier pharmacie est un fichier externe qui se trouve sur la carte SD.
    		//File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "pharmacie.xml");
    		InputStream in = getResources().openRawResource(R.raw.pharmacie);
    		//FileInputStream fileInput = new FileInputStream(file);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//Document doc = db.parse(fileInput);
			Document doc = db.parse(in);
			Element element =  doc.getDocumentElement();
			
			listPharmacie.clear();
			
			NodeList node = element.getElementsByTagName("entry");
			if(node != null && node.getLength() > 0) {
				for(int i=0; i< node.getLength(); i++) {
					Element entry = (Element) node.item(i);
					Element title = (Element) entry.getElementsByTagName("title").item(0);
					Element g = (Element) entry.getElementsByTagName("georss:point").item(0);
					Element summary = (Element) entry.getElementsByTagName("summary").item(0);
					Element id = (Element) entry.getElementsByTagName("id").item(0);
					
					String point = g.getFirstChild().getNodeValue();
					String[] location = point.split(" ");
					
					LatLng localisation = new LatLng(Double.parseDouble(location[0]), 
							Double.parseDouble(location[1]));
					
					Pharmacie pharmacie = new Pharmacie(new Lieu(Integer.parseInt(id.getFirstChild().getNodeValue()), 
							title.getFirstChild().getNodeValue(), ""), localisation );
					
					listPharmacie.add(pharmacie);
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void clickNormal(View v) {
    	typeView = GoogleMap.MAP_TYPE_NORMAL;
    	refreshView();
    }
    
    public void clickSatellite(View v) {
    	typeView = GoogleMap.MAP_TYPE_SATELLITE;
    	refreshView();
    }
    
    public void clickTerrain(View v) {
    	typeView = GoogleMap.MAP_TYPE_TERRAIN;
    	refreshView();
    }
    
    public void clickHybrid(View v) {
    	typeView = GoogleMap.MAP_TYPE_HYBRID;
    	refreshView();
    }
    
    private void refreshView() {
    	map.setMapType(typeView);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(listPharmacie.get(0).getLocation(), 14);
        map.animateCamera(update);
        map.clear();
        for (Pharmacie pharmacie : listPharmacie) {
			Marker marker = map.addMarker(new MarkerOptions().position(pharmacie.getLocation())
					.title(pharmacie.getLieu().getTitle())
					.snippet("Pour plus d'information, cliquez ici...")
					.draggable(true)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			hashMap.put(pharmacie.getLocation(), pharmacie);
		}
        
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				Pharmacie pharmacie = hashMap.get(marker.getPosition());
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pharmacie.getLocation(), 17);
			    map.animateCamera(update);
				marker.showInfoWindow();
				return true;
			}
		});
        
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub
				Pharmacie pharmacie = hashMap.get(marker.getPosition());
				Intent intent = new Intent(getBaseContext(), PharmacieInfosActivity.class);
				intent.putExtra("com.pharmacie.pharmaciemap.Lieu", pharmacie.getLieu());
				startActivity(intent);
			}
		});
    }
   
}
