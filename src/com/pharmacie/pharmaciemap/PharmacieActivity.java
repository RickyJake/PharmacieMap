package com.pharmacie.pharmaciemap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

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
        new ReadJSONTask().execute("");
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        hashMap = new WeakHashMap<LatLng, Pharmacie>();
        
        //refreshView();
        
        
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pharmacie, menu);
        /** Get the action view of the menu item whose id is search
         *  Il y'a un problème cependant lorsqu'on ajoute un autre ActionView sur l'
         *  action bar. Ce problème sera à TODO*/
        View v = (View) menu.findItem(R.id.actionView).getActionView();
 
        /** Get the edit text from the action view */
        EditText txtSearch = ( EditText ) v.findViewById(R.id.txt_search);
 
        /** Setting an action listener */
        txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
 
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(getBaseContext(), "Search : " + v.getText(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.actionView) {
//           SearchView searchView = (SearchView)item.getActionView();
//		   SearchManager searchManager =
//		         (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//		   SearchableInfo info =
//		         searchManager.getSearchableInfo(getComponentName());
//		   searchView.setSearchableInfo(info);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    
    private String remplirListePharmacies() {
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	   		
    	InputStream in = getResources().openRawResource(R.raw.produit);
    	BufferedReader reader = new BufferedReader(
    			new InputStreamReader(in));
    	String line;
    	try {
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return stringBuilder.toString();
    }
    
    private class ReadJSONTask extends AsyncTask<String, Void, String> {
    	
    
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return remplirListePharmacies();
		}
		
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				
				for(int i=0; i< jsonArray.length(); i++)
				{
					try {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						int id = jsonObject.getInt("id");
						String titleProduit = jsonObject.getString("nom");
						String priceProduit = jsonObject.getString("prix");
						String descriptionProduit = jsonObject.getString("description");
						String titleLieu = jsonObject.getString("pharmacie");
						String summaryLieu = jsonObject.getString("nomGroupe");
						LatLng localisation = new LatLng(jsonObject.getDouble("latitude"),
								jsonObject.getDouble("longitude"));
						Lieu lieu = new Lieu(id, titleLieu, summaryLieu);
						Produit produit = new Produit(titleProduit, descriptionProduit, priceProduit);
						lieu.setProduit(produit);
						
						listPharmacie.add(new Pharmacie(lieu, localisation));
						
					} catch(Exception e) {
						continue;
					}
					
				}
				
				refreshView();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
    
    private void remplirListePharmaciesOld() {
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
   
}
