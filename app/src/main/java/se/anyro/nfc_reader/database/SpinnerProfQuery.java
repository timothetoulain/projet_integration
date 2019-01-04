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
 * permet de recuperer les intitulés des differentes filieres existantes dans la bd
 * afin de les afficher dynamiquement dans les spinners
 */
public class SpinnerProfQuery extends AsyncTask<String, Void, Void>{
    private Context context;
    private String my_url;
    private TextView mtextViewInfo;
    private List coursList;

    public SpinnerProfQuery(Context context, List coursList) {
        this.context=context;
        this.coursList=coursList;
    }

    @Override
    protected void onPreExecute(){
        my_url="http://192.168.1.44/l3_projet_integration/queries.php";
        //my_url="http://192.168.1.72/projet/queries.php";

    }

    @Override
    protected Void doInBackground(String... params)  {

        try{
            System.out.println("in");
            String type=params[0];
            String enseignant=params[1];
            System.out.println("enseignant= "+enseignant);


            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8")
                    +"&"+URLEncoder.encode("enseignant","UTF-8")+"="+URLEncoder.encode(enseignant,"UTF-8");
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
            System.out.println("sb= "+sb);
            String[] spliter = sb.split(",");
            for(int j=0;j< spliter.length;j++) {
                System.out.println(spliter[j]);
                coursList.add(spliter[j]);
            }
            /*for(int i=0;i<spliter.length;i++){
                if(i%2==0) {
                    String[] spliter2=spliter[i].split("\"");
                    for(int j=0;j<spliter2.length;j++) {
                        if(j==3){
                            System.out.println(spliter2[j]);
                            coursList.add(spliter2[j]);
                        }
                    }
                }
            }*/
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
