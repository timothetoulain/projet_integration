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

/**
 * add a student to the presence list
 */
public class PresenceQuery extends AsyncTask<String, Void, String>{
    private Context context;
    private String my_url;

    public PresenceQuery(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute(){
        my_url="http://3.120.246.93/checkpresence/controller/queries.php";
    }

    @Override
    protected String doInBackground(String... params)  {
        String type=params[0];
        String course=params[1];
        String nfc=params[2];
        System.out.println("type:"+type);
        System.out.println("nfc:"+nfc);
        System.out.println("class:"+course);

        try{
            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
            my_data += "&" + URLEncoder.encode("course", "UTF-8") + "=" +
                    URLEncoder.encode(course, "UTF-8");
            my_data += "&" + URLEncoder.encode("nfc", "UTF-8") + "=" +
                    URLEncoder.encode(nfc, "UTF-8");
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
               // break;
            }
            //sb= sb.substring(1, sb.length()-1);
            System.out.println(sb);

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
        //return the name of the student to TagViewer so it can be displayed
    }
}
