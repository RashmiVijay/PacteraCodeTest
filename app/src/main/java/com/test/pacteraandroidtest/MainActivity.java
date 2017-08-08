package com.test.pacteraandroidtest;
import java.io.File;
import java.io.IOException;

import android.util.Log;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//imports for JSON parsing
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	ArrayList<Facts> factsList;

	FactsAdapter adapter;
    String factsUrl = "https://api.myjson.com/bins/m47pd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		factsList = new ArrayList<Facts>();
		//Executing asynctask for recieving JSON data from the server
		new JSONAsyncTask().execute(factsUrl);

		ListView listview = (ListView)findViewById(R.id.list);
		adapter = new FactsAdapter(getApplicationContext(), R.layout.row, factsList);

		listview.setAdapter(adapter);

		Button b=(Button)findViewById(R.id.refresh);
	        b.setOnClickListener(listener);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), factsList.get(position).getName(), Toast.LENGTH_LONG).show();
			}
		});
	}


	class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting to server");
			dialog.show();
			dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {

				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);

				//Pasring The JSON Array and the JSON object with the key value pairs
					JSONObject jsono = new JSONObject(data);
					JSONArray jarray = jsono.getJSONArray("rows");
				//	Log.e("Response:", jarray.toString());

					for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Facts fact = new Facts();
                        if(object.has("title"))
                            fact.setName(object.getString("title")); // title object of JSON data (title:)
                        if(object.has("description"))
                            fact.setDescription(object.getString("description"));//description object of JSON data(description:)
                        if(object.has("imageHref"))
                            fact.setImage(object.getString("imageHref"));        //Image data of JSON data (imageHref:)

                            factsList.add(fact);
                    }
					return true;
				}


			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		//Failure case
		protected void onPostExecute(Boolean result) {
			dialog.cancel();
			adapter.notifyDataSetChanged();
			if(result == false)
				Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

		}
	}

	public View.OnClickListener listener=new View.OnClickListener(){
	        @Override
	        public void onClick(View arg0) {

                adapter.notifyDataSetChanged();
                ListView listview = (ListView)findViewById(R.id.list);
                adapter = new FactsAdapter(getApplicationContext(), R.layout.row, factsList);
                listview.setAdapter(adapter);



	        }
	    };




}
