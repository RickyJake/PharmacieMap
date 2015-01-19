package com.pharmacie.pharmaciemap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
	private final LatLng LOCATION = new LatLng(49.27645, -122.917587);
	private final LatLng LOC = new LatLng(49.187500, -122.849000);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	listPharmacie = new ArrayList<Pharmacie>();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        remplirListePharmacies();
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        hashMap = new WeakHashMap<LatLng, Pharmacie>();
        
        for (Pharmacie pharmacie : listPharmacie) {
			Marker marker = map.addMarker(new MarkerOptions().position(pharmacie.getLocation())
					.title(pharmacie.getTitle())
					.draggable(true)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			hashMap.put(pharmacie.getLocation(), pharmacie);
		}
        
        //map.addMarker(new MarkerOptions().position(LOCATION).title("Location!"));
        //map.addMarker(new MarkerOptions().position(LOC).title("Loc!"));
        
        //CameraUpdate update = CameraUpdateFactory.newLatLng(LOCATION);
        //map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(listPharmacie.get(0).getLocation(), 14);
        map.animateCamera(update);
        
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				Pharmacie pharmacie = hashMap.get(marker.getPosition());
				TextView tv =(TextView) findViewById(R.id.textView);
				if(pharmacie != null) {
					tv.setText(pharmacie.getTitle());
				}
				else {
					LatLng position = marker.getPosition();
					tv.setText("Latitude: "+ position.latitude +
							" Longitude: "+position.longitude);
				}
				
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pharmacie.getLocation(), 17);
			    map.animateCamera(update);
					
				return true;
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pharmacie, menu);
        return true;
    }

    private void remplirListePharmacies() {
    	try {
    		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "pharmacie.xml");
			FileInputStream fileInput = new FileInputStream(file);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.parse(fileInput);
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
					
					Pharmacie pharmacie = new Pharmacie(Integer.parseInt(id.getFirstChild().getNodeValue()), 
							title.getFirstChild().getNodeValue(), "", localisation );
					
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
   
}
