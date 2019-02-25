package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.CreateCourseQuery;
import se.anyro.nfc_reader.database.LoginTeacherQuery;
import se.anyro.nfc_reader.database.SpinnerTeacherQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

public class ManageClassActivity extends Activity {

    private Button mAddButton;
    private EditText mNewClassEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_class);

        mAddButton = findViewById(R.id.addButton);
        mNewClassEditText=findViewById(R.id.newClassEditText);




        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNewClassEditText.getText().length()!=0){
                    String type="createCourse";
                    String teacher=VariableRepository.getInstance().getTeacherLogin();
                    String course=mNewClassEditText.getText().toString();
                    try {
                        String result=new CreateCourseQuery(getApplicationContext()).execute(type,teacher,course).get();
                        //TODO gerer les valeurs de retour
                        if(result.equals("1")){
                            ToastMessage.courseCreated(getApplicationContext());
                        }
                        else{
                            ToastMessage.courseNotCreated(getApplicationContext());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Intent manageActivity = new Intent(ManageClassActivity.this, ManageClassActivity.class);
                    startActivity(manageActivity);
                }
                else{
                    ToastMessage.incompleteFieldMessage(getApplicationContext());
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent menu = new Intent(this, TeacherMenuActivity.class);
        startActivity(menu);
    }
}
