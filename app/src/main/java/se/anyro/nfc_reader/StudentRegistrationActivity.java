package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.CardForgottenQuery;
import se.anyro.nfc_reader.database.StudentRegistrationQuery;

public class StudentRegistrationActivity extends Activity {

    private Button mconfirmButton;
    private EditText studentNumberEditText;
    private EditText nameEditText;

    private String classFile = "class.txt";
    private String studentFile = "student.txt";
    private String nfcFile = "nfc.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        mconfirmButton = findViewById(R.id.confirmButton);
        studentNumberEditText = findViewById(R.id.studentNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);

        mconfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentNumberEditText.getText().length()==0 || nameEditText.getText().length()==0){
                    incompleteFieldMessage();
                } else {
                    String nameRetrieved=query();
                    if(nameRetrieved != null && !nameRetrieved.equals("")){
                        System.out.println("name retrieved: "+nameRetrieved);
                        //we save the student name to display it on the TagViewer activity
                        saveData(nameRetrieved,studentFile);
                        Intent tagViewer = new Intent(StudentRegistrationActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                }
            }
        });
    }
    private void incompleteFieldMessage(){
        Toast.makeText(this,R.string.error_incomplete,Toast.LENGTH_SHORT).show();
    }
    public String query(){
        String type="registration";
        String numberStudent=studentNumberEditText.getText().toString();
        String nameStudent=nameEditText.getText().toString();
        String course=readData(classFile);
        String nfc=readData(nfcFile);
        System.out.println(nfc+":");
        try {
            nameStudent=new StudentRegistrationQuery(this).execute(type,course,numberStudent,nameStudent,nfc).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return nameStudent;
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
    @Override
    public void onBackPressed() {
        Intent tagViewer = new Intent(StudentRegistrationActivity.this, TagViewer.class);
        startActivity(tagViewer);
    }
}
