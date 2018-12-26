package se.anyro.nfc_reader;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.anyro.nfc_reader.database.SpinnerQuery;

/**
 * permet de selectionner la filiere, l'annee et le groupe dont on va faire l'appel
 */
public class ProfActivity extends Activity {

    private Button mValiderButton;
    private Spinner mSpinnerAnnee;
   // private Spinner mSpinnerGroupe;
    private Spinner mSpinnerFiliere;
    private EditText meditTextGroupe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        mValiderButton = (Button) findViewById(R.id.validerButton);
        meditTextGroupe=(EditText)findViewById(R.id.editTextGroupe);
        mSpinnerAnnee = (Spinner) findViewById(R.id.spinnerAnnee);
        mSpinnerFiliere = (Spinner) findViewById(R.id.spinnerFiliere);


        List filiereList=new ArrayList();
        filiereList.add("Selectionnez la filière");
        new SpinnerQuery(this,filiereList).execute();

        /*************faire liste liée en fonction de la selection*******************/
        List anneeList = new ArrayList();
        anneeList.add("Selectionnez l'année d'étude");
        anneeList.add("L1");
        anneeList.add("L2");
        anneeList.add("L3");


        ArrayAdapter adapterAnnee = new ArrayAdapter(this, android.R.layout.simple_spinner_item, anneeList);
        ArrayAdapter adapterFiliere = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filiereList);

        adapterAnnee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFiliere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAnnee.setAdapter(adapterAnnee);
        mSpinnerFiliere.setAdapter(adapterFiliere);

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSpinnerAnnee.getSelectedItem().toString().equals("Selectionnez l'année d'étude") ||
                        meditTextGroupe.getText().length()==0 ||
                        mSpinnerFiliere.getSelectedItem().toString().equals("Selectionnez la filière"))
                {
                    //afficher message d'erreur
                }
                else{
                    //stocker les valeurs des spinners quelque part pour bdd
                    Intent tagViewer = new Intent(ProfActivity.this, TagViewer.class);
                    startActivity(tagViewer);
                }
            }
        });
    }
}