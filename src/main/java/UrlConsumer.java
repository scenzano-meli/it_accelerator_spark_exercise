import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UrlConsumer {


    public String consumeUrl(String urlText, String params) throws IOException {

        URL url = new URL(urlText + params);

        URLConnection urlConnection = null;

        //need to work on exception
        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;

        if (urlConnection instanceof HttpURLConnection){
            connection = (HttpURLConnection) urlConnection;
            connection.setRequestProperty("Accept", "application/json");

            //need to work on exception
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("URL inválida");
            return "URL inválida";
        }

        BufferedReader in = null;
        //need to work on exception
        try {
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String urlString = "";

        String current = null;

        while((current = in.readLine()) != null){

            urlString += current;
        }

        connection.disconnect();

        return urlString;

    }
}
