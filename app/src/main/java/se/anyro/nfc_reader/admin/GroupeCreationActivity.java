package se.anyro.nfc_reader.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.anyro.nfc_reader.R;
import se.anyro.nfc_reader.database.GroupeCreationQuery;
import se.anyro.nfc_reader.database.SpinnerFiliereQuery;

/**
 * permet de creer un nouveau groupe pour une filiere et année données
 */
public class GroupeCreationActivity extends Activity {
    private Button mvaliderButton;
    private TextView mtextView;
    private TextView mtextViewInfo;
    private EditText meditText;
    private Spinner mSpinnerGroupe;
    private Spinner mSpinnerFiliere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_creation);
        mvaliderButton = findViewById(R.id.validerButton);
        mtextView = findViewById(R.id.textViewFiliere);
        mtextViewInfo = findViewById(R.id.textViewInfo);
        mtextViewInfo.setAlpha(0.0f);

        meditText = findViewById(R.id.editTextGroupe);

        mSpinnerGroupe = findViewById(R.id.spinnerGroupe);
        mSpinnerFiliere = findViewById(R.id.spinnerFiliere);

        List filiereList=new ArrayList();
        filiereList.add("Sélectionnez la filière");
        //la liste sera créee dynamiquement à partir de la base de données
       new SpinnerFiliereQuery(this,filiereList).execute();




        List anneeList = new ArrayList();
        anneeList.add("Sélectionnez l'année d'étude");
        anneeList.add("L1");
        anneeList.add("L2");
        anneeList.add("L3");

        ArrayAdapter adapterAnnee = new ArrayAdapter(this, android.R.layout.simple_spinner_item, anneeList);
        ArrayAdapter adapterFiliere = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filiereList);

        adapterAnnee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFiliere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerGroupe.setAdapter(adapterAnnee);
        mSpinnerFiliere.setAdapter(adapterFiliere);

    }

    //effectuer la requete de creation de groupe
    public void creation(View view){
        String groupe=meditText.getText().toString();
        String annee = mSpinnerGroupe.getSelectedItem().toString();
        String filiere = mSpinnerFiliere.getSelectedItem().toString();
        new GroupeCreationQuery(this).execute(groupe,filiere,annee);
    }
}