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
/**
 * Select the course to create the presence list for
 *
 */
public class ClassSelectionActivity extends Activity {

    private Button mValidateButton;
    private Button mDisconnectButton;
    private Spinner mSpinnerCourse;
    private EditText meditTextGroupe;
    private TextView mselectClassTextView;
    private String classFile;
    private String teacherFile;
    private String teacher;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);
        //meditTextGroupe= findViewById(R.id.editTextGroupe);
        mValidateButton = findViewById(R.id.validateButton);
        mSpinnerCourse = findViewById(R.id.spinnerCourse);
        mselectClassTextView=findViewById(R.id.selectClassTextView);

        classFile = "class.txt";
        teacherFile = "teacher.txt";
        teacher=null;
        TAG="ClassSelectionActivityLog";

        this.teacher=readData(teacherFile);
        //for some reason, we have to delete the last character
        teacher= teacher.substring(0, teacher.length()-1);

        System.out.println("teacher="+teacher);


        List classList=new ArrayList();

        classList.add(getString(R.string.select_class));

        String type="getCourses";

        new SpinnerTeacherQuery(this,classList).execute(type,teacher);

        // This line is invoking a Warning, stipulating that is uses unchecked or unsafe operations
        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classList);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCourse.setAdapter(adapterClass);

        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mSpinnerCourse.getSelectedItem().toString().equals(getString(R.string.select_class))){
                incompletFieldMessage();
            }
            else{
                String course=mSpinnerCourse.getSelectedItem().toString();
                saveData(course);

                Intent tagViewer = new Intent(ClassSelectionActivity.this, TagViewer.class);
                startActivity(tagViewer);
            }
            }
        });
    }
    private void incompletFieldMessage(){
        Toast.makeText(this,R.string.error_select_class,Toast.LENGTH_SHORT).show();
    }
    private void saveData(String course) {
        try {
            FileOutputStream out = this.openFileOutput(classFile, MODE_PRIVATE);
            out.write(course.getBytes());
            out.close();
            //Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
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
            return sb.toString();
            // this.mTextView.setText(sb.toString());
        } catch (Exception e) {
            Toast.makeText(this,R.string.error+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}