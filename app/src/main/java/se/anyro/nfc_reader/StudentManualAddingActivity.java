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
import se.anyro.nfc_reader.setup.VariableRepository;

public class StudentManualAddingActivity extends Activity {
    private Button mConfirmButton;
    private EditText mNameEditText;
    //private String studentFile = "student.txt";
    private String  resultFile = "result.csv";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manual_adding);
        mConfirmButton = findViewById(R.id.confirmButton);
        mNameEditText=findViewById(R.id.nameEditText);
        if(VariableRepository.getInstance().getStudentName()!=""){
            System.out.println("from var rep:"+VariableRepository.getInstance().getStudentName()+":");
        }
        else{
            System.out.println("error mistake");
        }
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNameEditText.getText().length()==0){
                    incompleteFieldMessage();
                }
                else {
                    String result=queryGetStudents();
                    if(result != null && !result.equals("")){
                        System.out.println("name retrieved: "+result);
                        saveData(result,resultFile);

                        Log.i("StudentManualLog", result);
                        // Toast.makeText(this,R.string.error_incomplete+nameRetrieved,Toast.LENGTH_SHORT).show();

                       // saveData(result,studentFile);
                        Intent resultView = new Intent(StudentManualAddingActivity.this, ResultManualAddingActivity.class);
                        startActivity(resultView);
                    }
                    else{
                        System.out.println("No student found");
                        Log.i("StudentManualLog", "No Student Found.");
                        unknownStudentMessage();
                        VariableRepository.getInstance().setStudentName("");
                        Intent tagViewer = new Intent(StudentManualAddingActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                }
            }
        });
    }

    public String queryGetStudents(){
        String type="getStudents";
        String nameStudent=mNameEditText.getText().toString();
        String studentData=null;
        try {
            studentData=new StudentManualAddingQuery(this).execute(type,nameStudent).get();
            System.out.println("student retrieved "+studentData);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return studentData;
    }

    private void saveData(String nameStudent,String file) {
        try {
            FileOutputStream out = this.openFileOutput(file, MODE_PRIVATE);
            out.write(nameStudent.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void incompleteFieldMessage(){
        Toast.makeText(this,R.string.error_incomplete,Toast.LENGTH_SHORT).show();
    }
    private void unknownStudentMessage(){
        Toast.makeText(this,R.string.error_unknown_student,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Intent tagViewer = new Intent(this, TagViewer.class);
        startActivity(tagViewer);
    }
}
