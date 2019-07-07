package GetApprequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiResponseGet {
    String result;

    HttpURLConnection urlConnection;
    String response;
    URL url1;

    public String getapi(String str) {

        try {
            url1 = new URL(str);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setConnectTimeout(30000);

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){

                InputStream ins = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(ins));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                br.close();
                return result;
            } else{
                 result = "NoConnection";
                return result;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
