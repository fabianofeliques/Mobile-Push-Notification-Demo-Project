package com.example.neto.android_onesignal;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_second);


        final TextInputEditText titulo = findViewById(R.id.titulo);
        final TextInputEditText mensagem = findViewById(R.id.mensagem);
        Button btnEnviar = findViewById(R.id.enviar);
        
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("app_id", "1bac9772-e844-4c84-a086-0df03211efc0");
                    JSONArray ar = new JSONArray();
                    ar.put("ALL");
                    obj.put("included_segments", ar);
                    JSONObject content = new JSONObject();
                    content.put("en", mensagem.getText().toString());
                    obj.put("contents", content);
                    JSONObject heading = new JSONObject();
                    heading.put("en", titulo.getText().toString());
                    obj.put("headings", heading);

                    post(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public String post(final JSONObject data) {
        String results = new String();
        try {
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Basic OTBlZmZjYjAtOWRhYS00ZWMzLWE2YzAtODA4OTc3NmUyNjU5");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);


            final OutputStream outputStream = connection.getOutputStream();
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write((data.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            connection.connect();
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
            int responseCode=connection.getResponseCode();// conexion OK?
            if(responseCode== HttpsURLConnection.HTTP_OK){
                BufferedReader in= new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer sb= new StringBuffer("");
                String linea="";
                while ((linea=in.readLine())!= null){
                    sb.append(linea);
                    break;

                }
                in.close();
                results= sb.toString();
            }
            else{
                results= new String("Error: "+ responseCode);


            }

        } catch (Exception e) {
            Log.e("Your tag", "Error", e);
        }

        return null;
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
