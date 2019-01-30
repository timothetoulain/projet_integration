package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultManualAddingActivity extends Activity {

    private String  resultFile = "result.csv";
    private String TAG;
    private HashMap<Integer,String> checkBoxMap;
    private Boolean radioButtonIsChecked;
    //private CheckBox scopeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_manual_adding);

        LinearLayout verticalCheckBoxLayout = (LinearLayout)findViewById(R.id.verticalCheckBoxLayout);

        radioButtonIsChecked = false;
        TAG = "ResultManualLOG";

        ArrayList al = new ArrayList();
        al = readData(resultFile);

        RadioGroup radioGroupStudent = new RadioGroup(this);

        verticalCheckBoxLayout.addView(radioGroupStudent);
        checkBoxMap = new HashMap<Integer,String>();
        /*
        for (CheckBox item : items){
            if(item.isChecked())
                String text=item.getText().toString();
        }
        */
        if (al.isEmpty()) {
            Log.i(TAG, "empty");
        } else {
            for (int i = 0; i < al.size(); i+=2) {
                //TODO i%2==0 ->num étu  i%2!=0 ->nom étu
                //System.out.println("donnée à l'indice " + i + " = " + al.get(i));
                // Log.i(TAG,"donnée à l'indice " + i + " = " + al.get(i));
                // CheckBox studentCheckBox = new CheckBox(this);
                // studentCheckBox.setOnCheckedChangeListener(this.getContext);
                StringBuffer dataBuffer = new StringBuffer();
                dataBuffer.append(al.get(i+1).toString() + " " + al.get(i).toString());

                RadioButton radioButtonStudent = new RadioButton(this);

                radioButtonStudent.setId(i);

                radioButtonStudent.setText(dataBuffer.toString());

                checkBoxMap.put(i,dataBuffer.toString());

                radioButtonStudent.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onradioButtonClicked(v);
                    }
                });

                radioGroupStudent.addView(radioButtonStudent);

                Log.i(TAG,"Eleve n° " + i + " " + al.get(i+1).toString() + " " + al.get(i).toString() + "\n");
                /*
                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setOnCheckedChangeListener(this);
                        checkBox.setId(i);
                        checkBox.setText(Str_Array[i]);
                        row.addView(checkBox);
                        my_layout.addView(row);
                        */

            }
        }
        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            // Called when the SUBMIT button is clicked
            // Add the choosen student to the acquittance roll list then bring back to the StudentManualAddingActivity
            @Override
            public void onClick(View v) {
                // Toast.makeText(Re.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
                if ( radioButtonIsChecked ) {
                    Intent resultView = new Intent(ResultManualAddingActivity.this, StudentManualAddingActivity.class);
                    startActivity(resultView);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.please_check,Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            // Called when the CANCEL button is clicked
            // Bring back to the StudentManualAddingActivity
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
                Intent resultView = new Intent(ResultManualAddingActivity.this, StudentManualAddingActivity.class);
                startActivity(resultView);
            }
        });

    }

    public void onradioButtonClicked(View view) {
        // Is the view now checked?
        // boolean checked = ((CheckBox) view).isChecked();
        if (((RadioButton) view).isChecked()) {
            radioButtonIsChecked = true;
            int checkBoxId= view.getId();
            Boolean checkBoxIsHere = checkBoxMap.containsKey(checkBoxId);
            if (checkBoxIsHere) {
                String checkBoxText = checkBoxMap.get(checkBoxId);
                Log.i(TAG,checkBoxText);

                // Parsing the text of the checkBox by using "space" as separator
                String[] splited = checkBoxText.split("\\s+");
                // Creating Bundle object
                Bundle b = new Bundle();

                // Storing data into bundle
                // Bundle are used to store Data between activities ok ?
                b.putString("studentFirstName", splited[0]);
                b.putString("studentFamilyName", splited[1]);
                b.putString("studentID", splited[2]);

                // Timothé, to get the data that I stored earlier, you have to do something like that
                // get the Intent that started this Activity so as to get the Bundle used in this activity back
                // => Intent in = getIntent();

                // Declare a Bundle object after initiating the Bundle
                // => Bundle b = in.getExtras();

                // getting data from bundle
                // there are some other methods like getLong, getDouble ...
                // But here, it's getString, obviously :)
                // => String nameString = b.getString("fullname");

                // Creating Intent object to go towards StudentManuelAddingActivity
                // Intent in = new Intent(ResultManualAddingActivity.this, StudentManualAddingActivity.class);
            }
        }
        // Check which checkbox was clicked
        /*
        switch(view.getId()) {
            case R.id.checkbox_meat:
                if (checked) {

                } else {

                }
                break;
            case R.id.checkbox_cheese:
                if (checked) {

                } else {

                }
                break;

        }
        */
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