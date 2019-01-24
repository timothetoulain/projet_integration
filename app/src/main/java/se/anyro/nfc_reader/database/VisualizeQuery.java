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
import java.util.ArrayList;

import se.anyro.nfc_reader.setup.EncodingManager;

public class VisualizeQuery extends AsyncTask<String, Void, String> {

        private Context context;
        private String my_url;

        public VisualizeQuery(Context context) {
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
            String dateStart=params[2];
            String dateEnd=params[3];

            System.out.println("type:"+type);
            System.out.println("course:"+course);
            System.out.println("date start:"+dateStart);
            System.out.println("date end:"+dateEnd);

            try{
                URL url=new URL(my_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String my_data= URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
                my_data += "&" + URLEncoder.encode("course", "UTF-8") + "=" +
                        URLEncoder.encode(course, "UTF-8");
                my_data += "&" + URLEncoder.encode("dateStart", "UTF-8") + "=" +
                        URLEncoder.encode(dateStart, "UTF-8");
                my_data += "&" + URLEncoder.encode("dateEnd", "UTF-8") + "=" +
                        URLEncoder.encode(dateEnd, "UTF-8");
                bw.write(my_data);
                bw.flush();
                bw.close();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));

                ArrayList al = new ArrayList();
                String line = null;
                String sb="";
                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb+=line;
                    // break;
                }
                sb=EncodingManager.convert(sb);
                sb=sb.replace("[","");
                sb=sb.replace("{","");
                sb=sb.replace("]","");
                sb=sb.replace("}","");
                sb=sb.replace("\"","");
                sb=sb.replace(",",":");
                String tab[];
                tab=sb.split(":");
                for(int i=0;i<tab.length;i++){
                    al.add(tab[i]);
                }
                for(int i = 0; i < al.size(); i++){
                    if(al.get(i).equals("date_presence")||al.get(i).equals("number_student")||
                            al.get(i).equals("name_course")||al.get(i).equals("name_student"))
                    {
                        al.remove(i);
                    }
                }
                String result="";
                for(int i = 0; i < al.size(); i++){
                    result+=al.get(i)+":";
                }
                result= result.substring(0, result.length()-1);
                System.out.println(result);

                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return(result);

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

        }
    }


