package se.anyro.nfc_reader.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.anyro.nfc_reader.R;

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
