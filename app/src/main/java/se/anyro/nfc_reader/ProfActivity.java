package se.anyro.nfc_reader;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfActivity extends Activity {

    private Button mValiderButton;
    private Spinner mSpinnerAnnee;
    private Spinner mSpinnerGroupe;
    private Spinner mSpinnerFiliere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        mValiderButton = (Button) findViewById(R.id.validerButton);

        mSpinnerAnnee = (Spinner) findViewById(R.id.spinnerAnnee);
        mSpinnerGroupe = (Spinner) findViewById(R.id.spinnerGroupe);
        mSpinnerFiliere = (Spinner) findViewById(R.id.spinnerFiliere);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List anneeList = new ArrayList();
        anneeList.add("Selectionnez l'année d'étude");
        anneeList.add("L1");
        anneeList.add("L2");
        anneeList.add("L3");

        List groupeList=new ArrayList();
        groupeList.add("Selectionnez le groupe");
        groupeList.add("A");
        groupeList.add("B");
        groupeList.add("C");

        List filiereList=new ArrayList();
        filiereList.add("Selectionnez la filière");
        filiereList.add("Informatique");
        filiereList.add("Mathématiques");
        filiereList.add("Physique");

		/*Le Spinner a besoin d'un adapter pour sa presentation alors on lui passe le context(this) et
                un fichier de presentation par défaut( android.R.layout.simple_spinner_item)
		Avec la liste des elements (exemple) */
        ArrayAdapter adapterAnnee = new ArrayAdapter(this, android.R.layout.simple_spinner_item, anneeList);
        ArrayAdapter adapterGroupe = new ArrayAdapter(this, android.R.layout.simple_spinner_item, groupeList);
        ArrayAdapter adapterFiliere = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filiereList);

        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapterAnnee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterGroupe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFiliere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner

        mSpinnerAnnee.setAdapter(adapterAnnee);
        mSpinnerGroupe.setAdapter(adapterGroupe);
        mSpinnerFiliere.setAdapter(adapterFiliere);

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSpinnerAnnee.getSelectedItem().toString().equals("Selectionnez l'année d'étude") ||
                        mSpinnerGroupe.getSelectedItem().toString().equals("Selectionnez le groupe") ||
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