package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    final static String API_KEY = "trnsl.1.1.20191029T061957Z.aaae52194a85d0a2.5ca65e33cba335dcaba261f6fdc235c27937371d";

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<MyThread> threads = new ArrayList<>();
        Scanner sc = new Scanner( System.in );

        System.out.println( "Получение языков" );
        LangsThread langsThread = new LangsThread( API_KEY );
        langsThread.start();
        langsThread.join();

        System.out.println();
        System.out.println( "Возможные языки:" );

        for( String lang : langsThread.getLangs().getDirs() ){
            if( lang.indexOf( "ru-" ) > -1 ) System.out.println( "\t" + lang );
        }

        System.out.println();

        if( !langsThread.status() ) {
            System.out.println("Возникла проблема с получением возможных языков");
            return;
        }

        while( true ){
            System.out.println( "Введите направление перевода" );
            String langTo = sc.nextLine();

            if( !langsThread.getLangs().getDirs().contains( "ru-" + langTo ) ) System.out.println( "Этот язык не поддерживается" );
            break;
        }

        System.out.println( "Введите путь до папки" );
        String path = sc.nextLine();

        File folder = new File( path );
        File[] files = folder.listFiles();

        for( File file : files ){
            if( file.getName().indexOf( "-ru.txt" ) > -1 ) threads.add( new MyThread( file, "./output" ) );
        }

        for( int i = 0; i < files.length; i++ ){
            threads.get( i ).start();
            threads.get( i ).join();
        }

        System.out.println();

        for( int i = 0; i < threads.size(); i++ ){
            if( threads.get( i ).getStatus() ) System.out.println( "Перевод файла \"" + files[i].getName() + "\" окончен" );
        }
    }
}
