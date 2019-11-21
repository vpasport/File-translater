package com.company;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LangsThread extends Thread {

    private String API_KEY;
    private boolean status;
    private Lang langs;

    public LangsThread( String apiKey ){
        this.API_KEY = apiKey;
    }

    public void run(){
        InputStream inputStream;
        HttpURLConnection urlConnection;
        this.status = false;

        String responsData = "ui=ru&key=" + this.API_KEY;

        try{
            URL url = new URL( "https://translate.yandex.net/api/v1.5/tr.json/getLangs" );
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput( true );
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write( responsData.getBytes() );
            inputStream = urlConnection.getInputStream();
        } catch ( IOException e ) {
            e.printStackTrace();
            this.langs = null;
            return;
        }

        Gson gson = new Gson();
        try {
            this.langs = gson.fromJson( new InputStreamReader( inputStream ), Lang.class );
        } catch ( Exception e ){
            e.printStackTrace();
        }

        urlConnection.disconnect();
        this.status = true;
    }

    public boolean status(){
        return this.status;
    }

    public Lang getLangs(){
        return this.langs;
    }
}
