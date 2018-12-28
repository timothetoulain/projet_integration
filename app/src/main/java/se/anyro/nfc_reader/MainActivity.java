package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.anyro.nfc_reader.admin.AdminActivity;

public class MainActivity extends Activity {

    private Button mAdminButton;
    private Button mProfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdminButton = (Button) findViewById(R.id.admin_button);
        mAdminButton.setEnabled(true);

        mProfButton = (Button) findViewById(R.id.prof_button);
        mProfButton.setEnabled(true);

        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminActivity = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(adminActivity);
            }
        });

        mProfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginProf = new Intent(MainActivity.this, LoginProf.class);
                startActivity(loginProf);
            }
        });

    }
}