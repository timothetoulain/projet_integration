package se.anyro.nfc_reader;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

import se.anyro.nfc_reader.database.CardForgottenQuery;
import se.anyro.nfc_reader.database.StudentManualAddingQuery;
import se.anyro.nfc_reader.setup.DialogManager;

public class StudentManualAddingActivity extends AppCompatActivity {
    private Button mConfirmButton;
    private EditText mNameEditText;
    private String classFile = "class.txt";
    private String studentFile = "student.txt";


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
                    incompleteFieldMessage();
                }
                else {
                    String nameRetrieved=queryGetStudents();
                    if(nameRetrieved != null && !nameRetrieved.equals("")){
                        System.out.println("name retrieved: "+nameRetrieved);
                        //TODO display the result on this activity and find a way to get the choice of the user
                        //TODO call method queryAddPresent

                        //we save the student name to display it on the TagViewer activity
                        saveData(nameRetrieved,studentFile);
                        Intent tagViewer = new Intent(StudentManualAddingActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                    else{
                        System.out.println("No student found");
                        unknownStudentMessage();
                        String unknownStudent="unknown student";
                        saveData(unknownStudent,studentFile);
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
   /* public String queryAddPresent(){
        String type="addPresent";
       // String numberStudent=studentNumberEditText.getText().toString();
        //String nameStudent=nameEditText.getText().toString();
        String course=readData(classFile);

        try {
            nameStudent=new CardForgottenQuery(this).execute(type,course,numberStudent,nameStudent).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return nameStudent;
    }*/


    private String readData(String file) {
        try {
            FileInputStream in = this.openFileInput(file);
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s).append("\n");
            }
            br.close();
            in.close();
            return sb.toString();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
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
}
