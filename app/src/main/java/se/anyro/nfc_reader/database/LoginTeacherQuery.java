package se.anyro.nfc_reader.database;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import se.anyro.nfc_reader.setup.EncodingManager;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * allow or denied access to the teacher's activities
 * check login and password in the database
 */
public class LoginTeacherQuery extends AsyncTask<String, Void, String>{
    private Context context;
    private String my_url;

    public LoginTeacherQuery(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute(){
        my_url= VariableRepository.getInstance().getUrl();
    }

    @Override
    protected String doInBackground(String... params)  {

        String type=params[0];
        String login=params[1];
        String password=params[2];

        try{
            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            login=login.toLowerCase();

            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8")
                    +"&"+URLEncoder.encode("login","UTF-8")+"="+URLEncoder.encode(login,"UTF-8")
                    +"&"+URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            bw.write(my_data);
            bw.flush();
            bw.close();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(httpURLConnection.getInputStream()));

            String line = null;
            String sb="";
            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb+=line;
                break;
            }
            sb= EncodingManager.convert(sb);
            outputStream.close();
            InputStream inputStream=httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
            return(sb);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //return information if the access is granted or not
    }
}
