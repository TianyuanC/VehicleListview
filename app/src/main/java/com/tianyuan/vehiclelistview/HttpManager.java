package com.tianyuan.vehiclelistview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by churyan on 16-03-06.
 */
public class HttpManager {

    public static String getData(String uri){
        BufferedReader reader = null;

        URL url = null;
        try {
            url = new URL(uri);
            HttpURLConnection cn = (HttpURLConnection)url.openConnection();
            StringBuilder builder = new StringBuilder();
            reader  = new BufferedReader(new InputStreamReader(cn.getInputStream()));

            String line;
            while((line = reader.readLine())!=null){
                builder.append(line + "\n");
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
}
