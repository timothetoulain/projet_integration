package se.anyro.nfc_reader.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

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
import java.util.List;

/**
 * retrieves the different courses taught by the logged in teacher
 * dynamic display in spinner
 */
public class SpinnerProfQuery extends AsyncTask<String, Void, Void>{
    private Context context;
    private String my_url;
    private TextView mtextViewInfo;
    private List classList;

    public SpinnerProfQuery(Context context, List classList) {
        this.context=context;
        this.classList=classList;
    }

    @Override
    protected void onPreExecute(){
        my_url="http://3.120.246.93/checkpresence/controller/queries.php";
    }

    @Override
    protected Void doInBackground(String... params)  {

        try{
            System.out.println("in");
            String type=params[0];
            String teacher=params[1];
            //for some reason, we have to delete the last character
           // teacher= teacher.substring(0, teacher.length()-1);

            System.out.println("teacher before query= "+teacher);


            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8")
                    +"&"+URLEncoder.encode("teacher","UTF-8")+"="+URLEncoder.encode(teacher,"UTF-8");
            bw.write(my_data);
            bw.flush();
            bw.close();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(httpURLConnection.getInputStream()));

            String line = "";
            String sb="";
            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb+=line;
                //break;
            }
            System.out.println("sb= "+sb);
            //delete [ and ] at the beginning and the end
            sb= sb.substring(1, sb.length()-1);


            String[] spliter = sb.split(",");
            for(int j=0;j< spliter.length;j++) {
                //delete the ""
                spliter[j]= spliter[j].substring(1, spliter[j].length()-1);
                System.out.println(spliter[j]);
                classList.add(spliter[j]);
            }

            outputStream.close();
            InputStream inputStream=httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
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

}
