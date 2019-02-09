package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import se.anyro.nfc_reader.database.SpinnerTeacherQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * Select the course to create the presence list for
 *
 */
public class ClassSelectionActivity extends Activity {

    private Button mValidateButton;
    private Spinner mSpinnerCourse;
    private String teacher;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);
        mValidateButton = findViewById(R.id.validateButton);
        mSpinnerCourse = findViewById(R.id.spinnerCourse);
        mSpinnerCourse.setVisibility(View.VISIBLE);

        TAG="ClassSelectionActivityLog";
        teacher=VariableRepository.getInstance().getTeacherName();

        System.out.println("teacher="+teacher);

        List classList=new ArrayList();

        classList.add(getString(R.string.course));

        String type="getCourses";
        new SpinnerTeacherQuery(this,classList).execute(type,teacher);

        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classList);
        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCourse.setAdapter(adapterClass);

        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mSpinnerCourse.getSelectedItem().toString().equals(getString(R.string.course))){
                ToastMessage.incompleteFieldMessage(getApplicationContext());
            }
            else{
                String course=mSpinnerCourse.getSelectedItem().toString();
                VariableRepository.getInstance().setCourseName(course);
                Intent tagViewer = new Intent(ClassSelectionActivity.this, TagViewer.class);
                startActivity(tagViewer);
            }
            }
        });
    }
}