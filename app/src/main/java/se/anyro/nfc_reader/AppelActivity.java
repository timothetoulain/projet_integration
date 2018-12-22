package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppelActivity extends Activity {

    private Button mValiderButton;
    private Button mAnnulerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appel);
        mValiderButton = (Button) findViewById(R.id.validerButton);
        mAnnulerButton = (Button) findViewById(R.id.annulerButton);

        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent appelActivity = new Intent(ProfActivity.this, AppelActivity.class);
                //startActivity(appelActivity);
            }
        });
        mAnnulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent appelActivity = new Intent(ProfActivity.this, AppelActivity.class);
                //startActivity(appelActivity);
            }
        });

    }
}