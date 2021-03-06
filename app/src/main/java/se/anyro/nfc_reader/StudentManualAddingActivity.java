package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.StudentManualAddingQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * The teacher indicates here the name of the students he wants to indicate as present
 */
public class StudentManualAddingActivity extends Activity {
    private Button mConfirmButton;
    private EditText mNameEditText;
    private String  resultFile = "result.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manual_adding);
        mConfirmButton = findViewById(R.id.confirmButton);
        mNameEditText=findViewById(R.id.nameEditText);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNameEditText.getText().length()==0){
                    ToastMessage.incompleteFieldMessage(getApplicationContext());
                }
                else {
                    String result=queryGetStudents();
                    if(result != null && !result.equals("") && !result.equals("null")){
                        saveData(result,resultFile);

                        Log.i("StudentManualLog", result);
                        VariableRepository.getInstance().setStudentName(result);
                        VariableRepository.getInstance().incrementOnResumeCounter();
                        Intent resultView = new Intent(StudentManualAddingActivity.this, ResultManualAddingActivity.class);
                        startActivity(resultView);
                    }
                    else{
                        Log.i("StudentManualLog", "No Student Found.");
                        ToastMessage.unknownStudentMessage(getApplicationContext());
                        VariableRepository.getInstance().setStudentName("");
                    }
                }
            }
        });
    }
    //query class call
    public String queryGetStudents(){
        String type="getStudents";
        String nameStudent=mNameEditText.getText().toString();
        String studentData=null;
        try {
            studentData=new StudentManualAddingQuery(this).execute(type,nameStudent).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return studentData;
    }

    //Save the result of the query into a csv file
    private void saveData(String nameStudent,String file) {
        try {
            FileOutputStream out = this.openFileOutput(file, MODE_PRIVATE);
            out.write(nameStudent.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent tagViewer = new Intent(this, TagViewer.class);
        startActivity(tagViewer);
    }
}
