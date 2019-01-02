package se.anyro.nfc_reader;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
 * a t-on vraiment besoin de demander le groupe???
 */
public class ProfActivity extends Activity {

    private Button mValiderButton;
    private Spinner mSpinnerCours;
    private EditText meditTextGroupe;
    private String coursFile = "cours.txt";
    private String profFile = "prof.txt";
    private String prof=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        mValiderButton = findViewById(R.id.validerButton);
        meditTextGroupe= findViewById(R.id.editTextGroupe);
        mSpinnerCours = findViewById(R.id.spinnerCours);
        this.prof=readData(profFile);
        System.out.println("prof="+prof);


        List coursList=new ArrayList();
        coursList.add("Sélectionnez le cours");

        new SpinnerProfQuery(this,coursList).execute(prof);



        ArrayAdapter adapterCours = new ArrayAdapter(this, android.R.layout.simple_spinner_item, coursList);

        adapterCours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCours.setAdapter(adapterCours);

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(/*meditTextGroupe.getText().length()==0 ||*/
                        mSpinnerCours.getSelectedItem().toString().equals("Sélectionnez le cours"))
                {
                    champIncompletMessage();
                }
                else{
                    String cours=mSpinnerCours.getSelectedItem().toString();
                    saveData(cours);

                    Intent tagViewer = new Intent(ProfActivity.this, TagViewer.class);
                    startActivity(tagViewer);
                }
            }
        });
    }
    private void champIncompletMessage(){
        Toast.makeText(this,"Veuillez sélectionner un cours",Toast.LENGTH_SHORT).show();
    }
    private void saveData(String cours) {
        try {
            FileOutputStream out = this.openFileOutput(coursFile, MODE_PRIVATE);
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
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}