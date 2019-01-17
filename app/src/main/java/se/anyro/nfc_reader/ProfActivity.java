package se.anyro.nfc_reader;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import se.anyro.nfc_reader.database.SpinnerProfQuery;
/**
 * permet de selectionner le cours et le groupe dont on va faire l'appel
 *
 */
public class ProfActivity extends Activity {

    private Button mValiderButton;
    private Button mDisconnectButton;
    private Spinner mSpinnerCours;
    private EditText meditTextGroupe;
    private TextView mselectClassTextView;
    private String classFile;
    private String teacherFile;
    private String teacher;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);
        //meditTextGroupe= findViewById(R.id.editTextGroupe);
        mValiderButton = findViewById(R.id.validerButton);
        mSpinnerCours = findViewById(R.id.spinnerCours);
        mselectClassTextView=findViewById(R.id.selectClassTextView);

        classFile = "class.txt";
        teacherFile = "teacher.txt";
        teacher=null;
        TAG="ProfActivityLog";

        this.teacher=readData(teacherFile);
        //for some reason, we have to delete the last character
        teacher= teacher.substring(0, teacher.length()-1);

        System.out.println("prof="+teacher);


        List classList=new ArrayList();

        classList.add(getString(R.string.select_class));

        String type="getCourses";

        new SpinnerProfQuery(this,classList).execute(type,teacher);

        // This line is invoking a Warning, stipulating that is uses unchecked or unsafe operations
        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classList);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCours.setAdapter(adapterClass);

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mSpinnerCours.getSelectedItem().toString().equals(getString(R.string.select_class))){
                incompletFieldMessage();
            }
            else{
                String course=mSpinnerCours.getSelectedItem().toString();
                saveData(course);

                Intent tagViewer = new Intent(ProfActivity.this, TagViewer.class);
                startActivity(tagViewer);
            }
            }
        });
    }
    private void incompletFieldMessage(){
        Toast.makeText(this,R.string.error_select_class,Toast.LENGTH_SHORT).show();
    }
    private void saveData(String cours) {
        try {
            FileOutputStream out = this.openFileOutput(classFile, MODE_PRIVATE);
            out.write(cours.getBytes());
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