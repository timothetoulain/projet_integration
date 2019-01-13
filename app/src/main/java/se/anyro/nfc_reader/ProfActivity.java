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
public class ProfActivity extends Activity implements DialogInterface {

    private Button mValiderButton;
    private Spinner mSpinnerCours;
    private EditText meditTextGroupe;
    private TextView mselectClassTextView;
    private String classFile = "class.txt";
    private String teacherFile = "teacher.txt";
    private String teacher=null;
    private DialogFragment logOutDialog;
    private String TAG="ProfActivityLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);
        //meditTextGroupe= findViewById(R.id.editTextGroupe);
        mValiderButton = findViewById(R.id.validerButton);
        mSpinnerCours = findViewById(R.id.spinnerCours);
        mselectClassTextView=findViewById(R.id.selectClassTextView);

        this.teacher=readData(teacherFile);
        //for some reason, we have to delete the last character
        teacher= teacher.substring(0, teacher.length()-1);

        System.out.println("prof="+teacher);


        List classList=new ArrayList();

        classList.add(getString(R.string.select_class));

        String type="getCourses";

        new SpinnerProfQuery(this,classList).execute(type,teacher);


        ArrayAdapter adapterClass = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classList);

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

    DialogInterface closeListener = new DialogInterface() {

        @Override
        public void handleDialogClose(DialogInterface dialog) {
            //do here whatever you want to do on Dialog dismiss
            if ( ((DialogManager) logOutDialog).getUserChose() == true ) {
                if ( ((DialogManager) logOutDialog).getUserLogingOut() == true ) {
                    Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                    Log.d(TAG, "goingToStartActivity");
                    startActivity(mainIntent);
                    finish();
                } else {
                    Log.d(TAG, "goingToStayOnThisActivity");
                }
            }
        }
    };

    public void handleDialogClose(DialogInterface dialog) {
        //do here whatever you want to do on Dialog dismiss
    }

    // SOURCE => https://stackoverflow.com/questions/3141996/android-how-to-override-the-back-button-so-it-doesnt-finish-my-activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        // If we're pressing on the Back button of the phone
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d(TAG, "onKeyDown Called");
            // Call the onBackPressed method defined just after this onKeyDown event callback method
            onBackPressed();
            // Return true for whatever reason that I don't understand right at the moment as of Thursday 10th of January 5:37 PM.
            return true;
        }

        // Doon't know what it does.
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        this.logOutDialog = new DialogManager();
        logOutDialog.show(getFragmentManager(), "logOutDialog");
        ((DialogManager)logOutDialog).onDismissListener(this.closeListener);
        /*
        if ( ((DialogManager) logOutDialog).getUserChose() == true ) {
            if ( ((DialogManager) logOutDialog).getUserLogingOut() == true ) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                Log.d(TAG, "goingToStartActivity");
                startActivity(mainIntent);
                finish();
            } else {
                Log.d(TAG, "goingToStayOnThisActivity");
            }
        }
        */
        /*
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        */
    }
}