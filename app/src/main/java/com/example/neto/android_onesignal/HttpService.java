package com.example.neto.android_onesignal;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HttpService extends AsyncTask<Void, Void, Noticia> {

    private final String cidade;

    public HttpService(String cidade) {
        this.cidade = cidade;
    }
    public String replace(String str) {
        return str.replaceAll(" ", "%20");
    }
    @Override
    protected Noticia doInBackground(Void... voids) {
        StringBuilder results = new StringBuilder();

        try {
            URL url = new URL("https://api.hgbrasil.com/weather?key=a2cdd1d6&city_name="+replace(cidade));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                results.append(scanner.next()+" ");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(results.toString());
        return new Gson().fromJson(obj.getAsJsonObject("results".toString()), Noticia.class);
    }
}