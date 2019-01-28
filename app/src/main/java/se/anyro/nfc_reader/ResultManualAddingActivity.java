package se.anyro.nfc_reader;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ResultManualAddingActivity extends Activity {

    private String  resultFile = "result.csv";
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_manual_adding);
        ArrayList al = new ArrayList();
        al = readData(resultFile);
        if (al.isEmpty()) {
            Log.i(TAG, "empty");
        } else {
            for (int i = 0; i < al.size(); i++) {
                //TODO i%2==0 ->num étu  i%2!=0 ->nom étu
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
                int studentNumber=0;
                int studentName=1;
                for(int i=0;i<tab.length;i++){
                    if(i==studentNumber) {
                        al.add(tab[i]);
                        studentNumber += 2;
                    }
                    else if(i==studentName){
                        al.add(tab[i]);
                        studentName+=2;
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