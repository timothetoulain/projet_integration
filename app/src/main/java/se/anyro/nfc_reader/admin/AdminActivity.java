package se.anyro.nfc_reader.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.anyro.nfc_reader.MainActivity;
import se.anyro.nfc_reader.ProfActivity;
import se.anyro.nfc_reader.R;

public class AdminActivity extends Activity {

    private Button mfiliereButton;
    private Button mgroupeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mfiliereButton = (Button) findViewById(R.id.filiereButton);
        mgroupeButton = (Button) findViewById(R.id.groupeButton);

        mfiliereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filiereCreationActivity = new Intent(AdminActivity.this, FiliereCreationActivity.class);
                startActivity(filiereCreationActivity);
            }
        });

        mgroupeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupeCreationActivity = new Intent(AdminActivity.this, GroupeCreationActivity.class);
                startActivity(groupeCreationActivity);
            }
        });
    }
}