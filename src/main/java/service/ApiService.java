package service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

  public static Double getRateByCode(String code) throws IOException {

    StringBuilder url_str = new StringBuilder("http://localhost:8080/rate/get-list?currency=")
    .append(code);

    URL url = new URL(url_str.toString());
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.connect();

    JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
    return  root.getAsDouble();

  }

}
