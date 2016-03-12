package things.shiny.represent.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shinmyung0 on 3/11/16.
 */
public class ApiRequestTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    public JSONObject doInBackground(String... params) {
        // First argument is url of request
        String urlStr = params[0];
        String response;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                reader.close();
                response = result.toString();
                return new JSONObject(response);
            } finally {
                connection.disconnect();
            }

        } catch(Exception e) {
            Log.d("ApiRequest::", e.getMessage());
            return null;
        }
    }
}
