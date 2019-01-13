package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends Activity {

   // private Button mAdminButton;
    private Button mProfButton;
    private String studentFile="student.txt";
    private String teacherFile = "teacher.txt";
    private String classFile = "class.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // mAdminButton = findViewById(R.id.admin_button);
        //mAdminButton.setEnabled(true);
        deleteTextFile(studentFile);
        deleteTextFile(teacherFile);
        deleteTextFile(classFile);

        mProfButton = findViewById(R.id.prof_button);
        mProfButton.setEnabled(true);

        /*mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminActivity = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(adminActivity);
            }
        });*/

        mProfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginProf = new Intent(MainActivity.this, LoginProf.class);
                startActivity(loginProf);
            }
        });

    }
    private void deleteTextFile(String file) {
        try {

            File fileToDelete = new File(file);
            if(fileToDelete.exists()) {
                fileToDelete.delete();
                System.out.println("file deleted");
            }
           else
               System.out.println("file doesn't exist");

            //Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}