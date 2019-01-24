package se.anyro.nfc_reader;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VisualizeActivity extends Activity {
    private LinearLayout layout;
    private String  resultFile = "result.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);

        TextView text=new TextView(this);
        layout = findViewById(R.id.linearLayout);

        text.setText("test1");
        layout.addView(text);


        ArrayList al = new ArrayList();
        al=readData(resultFile);
        if(al.isEmpty()){
            //TODO display textview
            System.out.println("empty");
        }
        else {
            for (int i = 0; i < al.size(); i++) {
                //TODO display info on scrollable view. i=0=>date;i=1=>student_number;i=2=>name; i=3=>date...etc
                System.out.println("donnée à l'indice " + i + " = " + al.get(i));
            }
        }
    }
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

            //return sb.toString();
            // this.mTextView.setText(sb.toString());
        } catch (Exception e) {
            Toast.makeText(this,R.string.error+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
