package co.aamsis.weather.conditions.discovery.clients;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OpenMeteoClient {
    private static OkHttpClient client;
    public static void instantiate() {
        client = new OkHttpClient.Builder().build();
    }

    public static OpenMeteoResponse fetchForecastForLocation(double latitude, double longitude) {
        String basePath = "https://api.open-meteo.com/v1/forecast";
        String url = basePath + "?" + "latitude=" + latitude + "&longitude=" + longitude;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code: " + response);
            }

            ResponseBody body = response.body();
            String json = body.string();

            Gson gson = new Gson();
            return gson.fromJson(json, OpenMeteoResponse.class);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return new OpenMeteoResponse();
        }
    }
}