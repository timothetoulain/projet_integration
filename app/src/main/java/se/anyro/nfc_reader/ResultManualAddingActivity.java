package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.CardForgottenQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

public class ResultManualAddingActivity extends Activity {

    private String  resultFile = "result.csv";
    private String TAG;
    private HashMap<Integer,String> checkBoxMap;
    private Boolean radioButtonIsChecked;
    private String studentName;
    private String studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_manual_adding);

        LinearLayout verticalCheckBoxLayout = findViewById(R.id.verticalCheckBoxLayout);

        radioButtonIsChecked = false;
        TAG = "ResultManualLOG";

        ArrayList al = new ArrayList();
        al = readData(resultFile);

        RadioGroup radioGroupStudent = new RadioGroup(this);

        verticalCheckBoxLayout.addView(radioGroupStudent);
        checkBoxMap = new HashMap<Integer,String>();

        if (al.isEmpty()) {
            Log.i(TAG, "empty");
        } else {
            for (int i = 0; i < al.size(); i+=2) {

                StringBuffer dataBuffer = new StringBuffer();
                dataBuffer.append(al.get(i+1).toString() + " " + al.get(i).toString());

                RadioButton radioButtonStudent = new RadioButton(this);

                radioButtonStudent.setId(i);

                radioButtonStudent.setText(dataBuffer.toString());

                checkBoxMap.put(i,dataBuffer.toString());

                radioButtonStudent.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onRadioButtonClicked(v);
                    }
                });

                radioGroupStudent.addView(radioButtonStudent);

                Log.i(TAG,"Student n° " + i + " " + al.get(i+1).toString() + " " + al.get(i).toString() + "\n");
            }
        }
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            // Called when the SUBMIT button is clicked
            // Add the chosen student to the acquittance roll list then bring back to the StudentManualAddingActivity
            @Override
            public void onClick(View v) {
                if ( radioButtonIsChecked ) {
                    VariableRepository.getInstance().setStudentName(studentName);
                    VariableRepository.getInstance().setStudentId(studentID);
                    String result=queryAddPresent();
                    VariableRepository.getInstance().setStudentName("");
                    if(!result.contains("ERROR")) {
                        VariableRepository.getInstance().setStudentName(result);
                    }
                    else{
                        ToastMessage.studentAlreadyRegistered(getApplicationContext());
                    }
                    Intent resultView = new Intent(ResultManualAddingActivity.this, TagViewer.class);
                    // VariableRepository.getInstance().incrementStudentCounter();
                    startActivity(resultView);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.please_check,Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            // Called when the CANCEL button is clicked
            // Bring back to the StudentManualAddingActivity
            @Override
            public void onClick(View v) {
                Intent resultView = new Intent(ResultManualAddingActivity.this, StudentManualAddingActivity.class);
                startActivity(resultView);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the view now checked?
        if (((RadioButton) view).isChecked()) {
            radioButtonIsChecked = true;
            int checkBoxId= view.getId();
            Boolean checkBoxIsHere = checkBoxMap.containsKey(checkBoxId);
            if (checkBoxIsHere) {
                String checkBoxText = checkBoxMap.get(checkBoxId);
                Log.i(TAG,checkBoxText);

                // Parsing the text of the checkBox by using "space" as separator
                String[] split = checkBoxText.split("\\s+");
                studentID=split[2];
                System.out.println("id:"+studentID);
                studentName=split[0]+" "+split[1];
                System.out.println("studentName:"+studentName);
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
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        Intent manualAdding = new Intent(this, StudentManualAddingActivity.class);
        startActivity(manualAdding);
    }
    public String queryAddPresent(){
        String type="addPresent";
        String course=VariableRepository.getInstance().getCourseName();
        String numberStudent=VariableRepository.getInstance().getStudentId();
        String nameStudent=VariableRepository.getInstance().getStudentName();
        try {
            nameStudent=new CardForgottenQuery(this).execute(type,course,numberStudent,nameStudent).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return nameStudent;
    }
}