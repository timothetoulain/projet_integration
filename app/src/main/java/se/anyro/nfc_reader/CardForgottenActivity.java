package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.CardForgottenQuery;

public class CardForgottenActivity extends Activity {
    private Button mconfirmButton;
    private EditText studentNumberEditText;
    private EditText nameEditText;
    private String course;
    private String numberStudent;
    private String nameStudent;

    private String classFile = "class.txt";
    private String studentFile = "student.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_forgotten);
        mconfirmButton = findViewById(R.id.confirmButton);
        studentNumberEditText = findViewById(R.id.studentNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);

        mconfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentNumberEditText.getText().length()==0 || nameEditText.getText().length()==0){
                    incompleteFieldMessage();
                } else {
                    String nameRetrieved=appel();
                    if(nameRetrieved != null && !nameRetrieved.equals("")){
                        System.out.println("name retrieved: "+nameRetrieved);

                        //we save the student name to display it on the TagViewer activity
                        saveData(nameRetrieved,studentFile);
                        //deleteTextFile(studentFile);
                        Intent tagViewer = new Intent(CardForgottenActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                    else{
                        System.out.println("unknown student");
                        unknownStudentMessage();
                        String unknownStudent="unknown student";
                        saveData(unknownStudent,studentFile);
                        Intent tagViewer = new Intent(CardForgottenActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                }
            }
        });
    }
    private void incompleteFieldMessage(){
        Toast.makeText(this,R.string.error_incomplete,Toast.LENGTH_SHORT).show();
    }
    private void unknownStudentMessage(){
        Toast.makeText(this,R.string.error_unknown_student,Toast.LENGTH_SHORT).show();
    }

    public String appel(){
        String type="addPresent";
        this.numberStudent=studentNumberEditText.getText().toString();
        this.nameStudent=nameEditText.getText().toString();
        this.course=readData(classFile);

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
            // this.mTextView.setText(sb.toString());
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
            //Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteTextFile(String file) {
        System.out.println(new File(file).getAbsoluteFile().delete());
        System.out.println("delete");

    }
}
