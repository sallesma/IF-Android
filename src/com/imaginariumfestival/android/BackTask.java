package com.imaginariumfestival.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.imaginariumfestival.android.data.ArtistDataSource;
import com.imaginariumfestival.android.data.InfosDataSource;
import com.imaginariumfestival.android.data.MySQLiteHelper;

class BackTask extends AsyncTask<Void, Integer, Void> {
	public final static String LAST_UPDATE_FROM_DISTANT_DATABASE = "lastSyncWithDistantDatabase";
	
	private final String INFOS_WEB_SERVICE_URL = "http://titouanrossier.com/ifM/api/api.php?request=infos";
	private final String ARTISTS_WEB_SERVICE_URL = "http://titouanrossier.com/ifM/api/api.php?request=artists";
	private Context context; 

	public BackTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Toast.makeText(context, "Mise à jour des données...", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		Boolean isArtistUpdated = getArtistsFromWebService();
		Boolean isInfoUpdated = getInfosFromWebService();
		
        if( isArtistUpdated || isInfoUpdated) {
            SharedPreferences pref = context.getSharedPreferences(LAST_UPDATE_FROM_DISTANT_DATABASE, 0); // 0 - for private mode
            Editor editor = pref.edit();
            Date currentDate = new Date();
            editor.putLong(LAST_UPDATE_FROM_DISTANT_DATABASE, currentDate.getTime());
            editor.commit();
        }
        return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Void result) {
		Toast.makeText(context, "...la mise à jour est terminée",
				Toast.LENGTH_SHORT).show();
	}
	
	private Boolean getArtistsFromWebService() {
		String artistsFromDistantDatabase = getDataFromDistantDatabase(ARTISTS_WEB_SERVICE_URL);
		
		JSONArray jsonArtists = null;
		try {
			jsonArtists = new JSONArray(artistsFromDistantDatabase);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		Boolean result  = recordArtists(jsonArtists);
		return result;
	}
	
	private Boolean recordArtists(JSONArray jsonArtistsArray) {
		ArtistDataSource datasource = new ArtistDataSource(context);
		datasource.open();
		try {
			for (int i = 0; i < jsonArtistsArray.length(); i++) {
				JSONObject c = jsonArtistsArray.getJSONObject(i);

				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String name = c.getString(MySQLiteHelper.COLUMN_NAME);
				String picture = c.getString(MySQLiteHelper.COLUMN_PICTURE);
				String genre = c.getString(MySQLiteHelper.COLUMN_STYLE);
				String description = c.getString(MySQLiteHelper.COLUMN_DESCRIPTION);
				String jour = c.getString(MySQLiteHelper.COLUMN_DAY);
				String scene = c.getString(MySQLiteHelper.COLUMN_STAGE);
				String debut = c.getString(MySQLiteHelper.COLUMN_BEGIN_HOUR);
				String fin = c.getString(MySQLiteHelper.COLUMN_END_HOUR);
				String website = c.getString(MySQLiteHelper.COLUMN_WEBSITE);
				String facebook = c.getString(MySQLiteHelper.COLUMN_FACEBOOK);
				String twitter = c.getString(MySQLiteHelper.COLUMN_TWITTER);
				String youtube = c.getString(MySQLiteHelper.COLUMN_YOUTUBE);

				datasource.createArtist(id, name, picture, genre,
						description, jour, scene, debut, fin, website,
						facebook, twitter, youtube);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private Boolean getInfosFromWebService() {
		String infosFromDistantDatabase = getDataFromDistantDatabase(INFOS_WEB_SERVICE_URL);
		
		JSONArray jsonInfos = null;
		try {
			jsonInfos = new JSONArray(infosFromDistantDatabase);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		Boolean result  = recordInfos(jsonInfos);
		return result;
	}
	
	private Boolean recordInfos(JSONArray jsonInfosArray) {
		InfosDataSource datasource = new InfosDataSource(context);
		datasource.open();
		try {
			for (int i = 0; i < jsonInfosArray.length(); i++) {
				JSONObject c = jsonInfosArray.getJSONObject(i);
				
				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String name = c.getString(MySQLiteHelper.COLUMN_NAME);
				String picture = c.getString(MySQLiteHelper.COLUMN_PICTURE);
				String isCategory = c.getString(MySQLiteHelper.COLUMN_IS_CATEGORY);
				String content = c.getString(MySQLiteHelper.COLUMN_CONTENT);
				Long parent = Long.valueOf( c.getString(MySQLiteHelper.COLUMN_PARENT) );
				
				datasource.createInfo(id, name, picture, isCategory, content, parent);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private String getDataFromDistantDatabase(String URL) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line + "\n");
				}
			} else {
				Log.e(MainMenuActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
}
