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

import java.util.ArrayList;
import java.util.List;

import se.anyro.nfc_reader.database.SpinnerFiliereQuery;
import se.anyro.nfc_reader.database.SpinnerProfQuery;

/**
 * permet de selectionner le cours et le groupe dont on va faire l'appel
 * a t-on vraiment besoin de demander le groupe???
 */
public class ProfActivity extends Activity {

    private Button mValiderButton;
    private Spinner mSpinnerCours;
    private EditText meditTextGroupe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        mValiderButton = (Button) findViewById(R.id.validerButton);
        meditTextGroupe=(EditText)findViewById(R.id.editTextGroupe);
        mSpinnerCours = (Spinner) findViewById(R.id.spinnerCours);


        List coursList=new ArrayList();
        coursList.add("Selectionnez le cours");
        new SpinnerProfQuery(this,coursList).execute();



        ArrayAdapter adapterCours = new ArrayAdapter(this, android.R.layout.simple_spinner_item, coursList);

        adapterCours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCours.setAdapter(adapterCours);

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(/*meditTextGroupe.getText().length()==0 ||*/
                        mSpinnerCours.getSelectedItem().toString().equals("Selectionnez le cours"))
                {
                    champIncompletMessage();
                }
                else{
                    //stocker les valeurs des spinners quelque part pour bdd
                    Intent tagViewer = new Intent(ProfActivity.this, TagViewer.class);
                    startActivity(tagViewer);
                }
            }
        });
    }
    private void champIncompletMessage(){
        Toast.makeText(this,"Veuillez selectionner un cours",Toast.LENGTH_SHORT).show();
    }
}