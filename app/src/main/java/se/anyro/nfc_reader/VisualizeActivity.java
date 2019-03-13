package se.anyro.nfc_reader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * we can consult the list of the present students to a given course,
 * between 2 given dates/times
 */
public class VisualizeActivity extends Activity {
    private LinearLayout layout;
    private String  resultFile = "result.csv";
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);

        TAG = "VisualizeActivityLog";
        layout = findViewById(R.id.linearLayout);

        ArrayList al = new ArrayList();
        al=readData(resultFile);
        if(al.isEmpty()){
            Log.i(TAG,"empty");
            TextView textViewData = new TextView(this);
            textViewData.setText(R.string.error_no_student);
            layout.addView(textViewData);
        }
        else {
            TextView textViewDataCount = new TextView(this);
            textViewDataCount.setText(String.valueOf(al.size()/3)+ " results found :");
            layout.addView(textViewDataCount);
            for (int i = 0; i < al.size(); i+=3) {
                Log.i(TAG,"Index " + i + " = " + al.get(i));
                Log.i(TAG,"Student n° " + i + " : " + al.get(i+2)+ "\n" + "\tpresent on " + al.get(i) + "\n" + "\tdecimal id : " + al.get(i+1) + "\n");

                StringBuffer dataBuffer = new StringBuffer();
                dataBuffer.append("- " + al.get(i+2)+ "\n" + "\tDate : " + al.get(i) + "\n" + "\tID : " + al.get(i+1) + "\n");

                TextView textViewData = new TextView(this);
                textViewData.setText(dataBuffer.toString());
                layout.addView(textViewData);
            }
        }
    }
    //we read the csv file containing the results and we put them into an array list
    private ArrayList readData(String file) {
        try {
            FileInputStream in = this.openFileInput(file);
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            StringBuilder sb= new StringBuilder();
            String s= null;
            ArrayList al = new ArrayList();
            while((s= br.readLine())!= null)  {
                sb.append(s).append("\n");
            }
            if(sb!=null){
                String tab[];
                tab=sb.toString().split(":");
                int course=0;
                int dateAndTime=1;
                for(int i=0;i<tab.length;i++){
                    if(i==dateAndTime){
                        al.add(tab[i]+":"+tab[i+1]+":"+tab[i+2]);
                        i=i+2;
                        dateAndTime+=6;
                    }
                    else if(i==course){
                        course+=6;
                    }// we don't add the course name to the arraylist, it's redundant
                    else {
                        al.add(tab[i]);
                    }
                }
                return al;
            }
            else{
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(this,R.string.error+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
