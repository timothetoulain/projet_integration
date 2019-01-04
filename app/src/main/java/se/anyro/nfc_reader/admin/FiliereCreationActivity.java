package se.anyro.nfc_reader.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.anyro.nfc_reader.R;
import se.anyro.nfc_reader.database.FiliereCreationQuery;

/**
 * permet d'ajouter une filiere dans la base de donnees
 *
 */
public class FiliereCreationActivity extends Activity {
    private Button mvaliderButton;
    private TextView mtextView;
    private EditText meditText;
    private EditText meditName;
    private EditText meditYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filiere_creation);
        mvaliderButton = findViewById(R.id.validerButton);
        mtextView = findViewById(R.id.textViewFiliere);
        meditText = findViewById(R.id.editTextFiliere);
        meditName = findViewById(R.id.editTextFiliere2);
        meditYear = findViewById(R.id.editTextFiliere3);

        //TODO ?
       /* mvaliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent filiereCreationActivity = new Intent(AdminActivity.this, FiliereCreationActivity.class);
                //startActivity(filiereCreationActivity);
                String mfiliere=meditText.getText().toString();
                new GroupeCreationQuery(this,mtextView).execute(mfiliere);
            }
        });*/
    }
        public void creation(View view){
            String id=meditText.getText().toString();
            String name=meditName.getText().toString();
            String yearOfStudy=meditYear.getText().toString();
            new FiliereCreationQuery(this).execute(id, name, yearOfStudy);
        }
    }
