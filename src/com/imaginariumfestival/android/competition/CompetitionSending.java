package com.imaginariumfestival.android.competition;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.imaginariumfestival.android.MainMenuActivity;
import com.imaginariumfestival.android.R;

public class CompetitionSending extends AsyncTask<String, Integer, Void> {

	public final static String COMPETITION_SHARED_PREFERENCES = "competitionsharedpreferences";
	public final static String COMPETITION_SENT = "competitionsent";
	private Context context;

	public CompetitionSending(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Toast.makeText(context,
				context.getResources().getString(R.string.competition_begin),
				Toast.LENGTH_LONG).show();
		Log.i("Competition", "Starting to send competition request");
	}

	@Override
	protected Void doInBackground(String... params) {
		String url = "https://imaginariumfestival.herokuapp.com/api/";
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("device", "Android"));
		urlParameters.add(new BasicNameValuePair("email", params[0]));
		Log.i(MainMenuActivity.class.toString(), urlParameters.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(httpPost);
			Log.i(MainMenuActivity.class.toString(), response.getStatusLine().toString());

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.i(MainMenuActivity.class.toString(), "Competition subscription done to " + url);
			} else {
				Log.e(MainMenuActivity.class.toString(), "Failed to send data to " + url);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Toast.makeText(context,
				context.getResources().getString(R.string.competition_end),
				Toast.LENGTH_SHORT).show();
		Log.i("Competition", "Competition request done");
	}
}
