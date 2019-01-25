package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button mProfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProfButton = findViewById(R.id.prof_button);
        mProfButton.setEnabled(true);

        mProfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginProf = new Intent(MainActivity.this, LoginTeacher.class);
                startActivity(loginProf);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
}