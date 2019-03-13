package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.StudentRegistrationQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * A student whose card was not recognised can add himself to the database on this activity
 */
public class StudentRegistrationActivity extends Activity {

    private Button mConfirmButton;
    private EditText studentNumberEditText;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        mConfirmButton = findViewById(R.id.confirmButton);
        studentNumberEditText = findViewById(R.id.studentNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentNumberEditText.getText().length()==0 || nameEditText.getText().length()==0){
                    ToastMessage.incompleteFieldMessage(getApplicationContext());
                } else {
                    String nameRetrieved=query();
                    if(nameRetrieved != null && !nameRetrieved.equals("")){
                        VariableRepository.getInstance().setStudentName(nameRetrieved);
                        VariableRepository.getInstance().incrementOnResumeCounter();
                        Intent tagViewer = new Intent(StudentRegistrationActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                }
            }
        });
    }

    public String query(){
        String type="registration";
        String numberStudent=studentNumberEditText.getText().toString();
        String nameStudent=nameEditText.getText().toString();
        String course= VariableRepository.getInstance().getCourseName();
        String nfc= VariableRepository.getInstance().getNfc();

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

    @Override
    public void onBackPressed() {
        Intent tagViewer = new Intent(StudentRegistrationActivity.this, TagViewer.class);
        startActivity(tagViewer);
    }
}
