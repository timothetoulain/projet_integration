package se.anyro.nfc_reader.database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import se.anyro.nfc_reader.R;

/**
 * permet d'indiquer un etudiant comme present au cours dans la bd
 */
public class AppelQuery extends AsyncTask<String, Void, String>{
    private Context context;
    String my_url;

    public AppelQuery(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute(){
        my_url="http://192.168.43.65/appel.php";
    }

    @Override
    protected String doInBackground(String... params)  {

        String dec=params[0];
        String prof=params[1];
        String cours=params[2];
        System.out.println("dec:"+dec);
        try{
            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("dec","UTF-8")+"="+URLEncoder.encode(dec,"UTF-8");
            my_data += "&" + URLEncoder.encode("prof", "UTF-8") + "=" +
                    URLEncoder.encode(prof, "UTF-8");
            my_data += "&" + URLEncoder.encode("cours", "UTF-8") + "=" +
                    URLEncoder.encode(cours, "UTF-8");
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
            sb= sb.substring(1, sb.length()-1);
            System.out.println(sb.toString());

            outputStream.close();
            InputStream inputStream=httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
            return(sb.toString());
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
        //va renvoyer le nom de l'etudier a l'activité TagViewer
    }
}
