package shopinpager.wingstud.shopinpagerseller.Api;

import com.google.gson.Gson;

public class JsonDeserializer {
    private static Gson gson = new Gson();

    public static <T> T deserializeJson(String jsonString, Class<T> type) {
        T result = null;

        result = gson.fromJson(jsonString, type);


        return result;
    }
    public static String serializeJson(Object type) {
        String result = null;
        result = gson.toJson(type);
        return result;
    }

}
