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

import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * add a student to the database with his nfc number
 *  register a student to a course
 */
    public class StudentRegistrationQuery extends AsyncTask<String, Void, String> {
        private Context context;
        private String my_url;

        public StudentRegistrationQuery(Context context) {
            this.context=context;
        }

        @Override
        protected void onPreExecute(){
            my_url= VariableRepository.getInstance().getUrl();
        }

        @Override
        protected String doInBackground(String... params)  {
            String type=params[0];
            String course=params[1];
            String numberStudent=params[2];
            String nameStudent=params[3];
            String nfc=params[4];

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
                my_data += "&" + URLEncoder.encode("numberStudent", "UTF-8") + "=" +
                        URLEncoder.encode(numberStudent, "UTF-8");
                my_data += "&" + URLEncoder.encode("nameStudent", "UTF-8") + "=" +
                        URLEncoder.encode(nameStudent, "UTF-8");
                my_data += "&" + URLEncoder.encode("nfc", "UTF-8") + "=" +
                        URLEncoder.encode(nfc, "UTF-8");
                bw.write(my_data);
                bw.flush();
                bw.close();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                String sb="";
                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb+=line;
                    break;
                }

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
            //return the name of the student so it can be displayed in TagViewer
        }
    }

