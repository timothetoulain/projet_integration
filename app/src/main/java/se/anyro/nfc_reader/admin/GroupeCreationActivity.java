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

public class GroupeCreationActivity extends Activity {
    private Button mvaliderButton;
    private TextView mtextView;
    private EditText meditText;
    private Spinner mSpinnerGroupe;
    private Spinner mSpinnerFiliere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_creation);
        mvaliderButton = (Button) findViewById(R.id.validerButton);
        mtextView = (TextView) findViewById(R.id.textViewFiliere);
        meditText = (EditText) findViewById(R.id.editTextFiliere);
        mSpinnerGroupe = (Spinner) findViewById(R.id.spinnerGroupe);
        mSpinnerFiliere = (Spinner) findViewById(R.id.spinnerFiliere);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List anneeList = new ArrayList();
        anneeList.add("Selectionnez l'année d'étude");
        anneeList.add("L1");
        anneeList.add("L2");
        anneeList.add("L3");


        List filiereList=new ArrayList();
        filiereList.add("Selectionnez la filière");
        filiereList.add("Informatique");
        filiereList.add("Mathématiques");
        filiereList.add("Physique");

		/*Le Spinner a besoin d'un adapter pour sa presentation alors on lui passe le context(this) et
                un fichier de presentation par défaut( android.R.layout.simple_spinner_item)
		Avec la liste des elements (exemple) */
        ArrayAdapter adapterAnnee = new ArrayAdapter(this, android.R.layout.simple_spinner_item, anneeList);
        ArrayAdapter adapterFiliere = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filiereList);

        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapterAnnee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFiliere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner

        mSpinnerGroupe.setAdapter(adapterAnnee);
        mSpinnerFiliere.setAdapter(adapterFiliere);


        mvaliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*******a faire*************/
                // Intent filiereCreationActivity = new Intent(AdminActivity.this, FiliereCreationActivity.class);
                //startActivity(filiereCreationActivity);
            }
        });
    }
}
