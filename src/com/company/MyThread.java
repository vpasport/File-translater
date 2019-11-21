package com.company;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MyThread extends Thread {

    final static String API_KEY = "trnsl.1.1.20191029T061957Z.aaae52194a85d0a2.5ca65e33cba335dcaba261f6fdc235c27937371d";
    private String content, output, tarnslateText;
    private File file;
    private boolean status;
    private Translate tran;

    public MyThread( File file, String output ){
        this.file = file;
        this.output = output;
    }

    public void run() {
        try {
            readFile();
            translate();
            writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        this.status = false;
        char[] chars = new char[ (int) file.length() ];
        FileReader reader;
        try {
            reader = new FileReader( file );
            reader.read( chars );

            this.status = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.content = String.copyValueOf( chars );
    }

    public void writeFile(){
        this.status = false;
        String texts = this.tran.getText();

        File file = new File( this.output + "/" + this.file.getName() );

        try {
            FileWriter fileWriter = new FileWriter( file, false );
            fileWriter.write( texts );
            fileWriter.flush();

            this.status = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void translate() throws IOException {
        this.status = false;
        String server_url = "https://translate.yandex.net/api/v1.5/tr.json/translate";
        String data = "lang=ru-eu&text=" + this.content + "&format=plain";
        data += "&key=" + API_KEY;

        URL url = new URL( server_url );
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput( true );
        OutputStream out = urlConnection.getOutputStream();
        out.write( data.getBytes() );

        Gson gson = new Gson();
        this.tran = gson.fromJson( new InputStreamReader( urlConnection.getInputStream() ), Translate.class );
        urlConnection.disconnect();

        this.status = true;
    }

    public boolean getStatus(){
        return this.status;
    }
}
