package se.anyro.nfc_reader.database;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * permet d'ajouter une filiere dans la base de donnees
 */
public class FiliereCreationQuery extends AsyncTask<String, Void, Void>{
    private Context context;
    private String my_url;

    public FiliereCreationQuery(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute(){
        my_url="http://192.168.1.44/l3_projet_integration/queries.php";
    }

    @Override
    protected Void doInBackground(String... params)  {

        String filiere=params[0];

        try{
            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("filiere","UTF-8")+"="+URLEncoder.encode(filiere,"UTF-8");
            bw.write(my_data);
            bw.flush();
            bw.close();
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
