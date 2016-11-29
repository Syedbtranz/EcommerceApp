package com.btranz.ecommerceapp.json;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetJSONObject {

	public static JSONObject getJSONObject(String url) throws IOException,
			JSONException {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		// Use HttpURLConnection
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			jsonObject = jsonParser.getJSONHttpURLConnection(url);
		} else {
			// use HttpClient
			jsonObject = jsonParser.getJSONHttpClient(url);
		}
		return jsonObject;
	}
}
