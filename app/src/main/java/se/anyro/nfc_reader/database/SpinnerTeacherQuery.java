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

import se.anyro.nfc_reader.setup.EncodingManager;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * retrieves the different courses taught by the logged in teacher
 * used for dynamic display in spinner
 */
public class SpinnerTeacherQuery extends AsyncTask<String, Void, Void>{
    private Context context;
    private String my_url;
    private TextView mtextViewInfo;
    private List classList;

    public SpinnerTeacherQuery(Context context, List classList) {
        this.context=context;
        this.classList=classList;
    }

    @Override
    protected void onPreExecute(){
        my_url= VariableRepository.getInstance().getUrl();
    }

    @Override
    protected Void doInBackground(String... params)  {

        try{
            String type=params[0];
            String teacher=params[1];

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
                    InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));

            String line = "";
            String sb="";
            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb+=line;
            }
            sb=EncodingManager.convert(sb);
            //delete [ and ] at the beginning and the end
            sb= sb.substring(1, sb.length()-1);

            String[] spliter = sb.split(",");
            for(int j=0;j< spliter.length;j++) {
                //delete the ""
                spliter[j]= spliter[j].substring(1, spliter[j].length()-1);
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
