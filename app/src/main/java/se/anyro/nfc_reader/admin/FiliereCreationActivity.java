package se.anyro.nfc_reader.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.anyro.nfc_reader.R;
import se.anyro.nfc_reader.database.FiliereCreationQuery;
import se.anyro.nfc_reader.database.GroupeCreationQuery;

/**
 * permet d'ajouter une filiere dans la base de donnees
 *
 */
public class FiliereCreationActivity extends Activity {
    private Button mvaliderButton;
    private TextView mtextView;
    private EditText meditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filiere_creation);
        mvaliderButton = (Button) findViewById(R.id.validerButton);
        mtextView = (TextView) findViewById(R.id.textViewFiliere);
        meditText = (EditText) findViewById(R.id.editTextFiliere);

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
            String filiere=meditText.getText().toString();
            new FiliereCreationQuery(this).execute(filiere);
        }
    }
