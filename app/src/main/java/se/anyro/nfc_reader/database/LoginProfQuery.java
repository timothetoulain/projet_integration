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
 * permet d'indiquer un etudiant comme present au cours dans la bd
 */
public class LoginProfQuery extends AsyncTask<String, Void, String>{
    private Context context;
    private String my_url;

    public LoginProfQuery(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute(){
        //my_url="http://192.168.1.44/l3_projet_integration/queries.php";
        //my_url="http://192.168.1.72/projet/queries.php";
        my_url="http://3.120.246.93/checkpresence/controller/queries.php";

    }

    @Override
    protected String doInBackground(String... params)  {

        String type=params[0];
        String login=params[1];
        String mdp=params[2];

        try{
            URL url=new URL(my_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

           // System.out.println(httpURLConnection.getResponseCode());

            System.out.println(login);
            System.out.println(mdp);
            System.out.println(type);

            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String my_data=URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8")
                    +"&"+URLEncoder.encode("login","UTF-8")+"="+URLEncoder.encode(login,"UTF-8")
                    +"&"+URLEncoder.encode("mdp", "UTF-8") + "=" + URLEncoder.encode(mdp, "UTF-8");
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
            //for some reason, we receive "0", so we have to delete the symbols ""
            if(sb.equals("\"0\"")){
                sb= sb.substring(1, sb.length()-1);
            }
          // sb= sb.substring(1, sb.length()-1);
           // System.out.println("sb= " +sb);
            System.out.println("SB=" + sb);

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
        //va renvoyer le nom de l'etudier a l'activité TagViewer
    }
}
