package com.example.partyplanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApi {
    public ArrayList<String> autoComplete(String input) {
        ArrayList<String> arrayList = new ArrayList();
        HttpURLConnection connection = null;
        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?" + "input=" + input + "&key=AIzaSyAICrXrm83fG91VxT9s34DjXctZ0xEwUAY");
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = inputStreamReader.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        try {
            JSONObject object = new JSONObject(json.toString());
            JSONArray array = object.getJSONArray("predictions");
            for (int i = 0; i < array.length(); i++) {
                arrayList.add(array.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
