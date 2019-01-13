package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TeacherMenuActivity extends Activity {

    private Button mcreateButton;
    private Button mvisualizeButton;
    private Button mdisconnectionButton;
    private TextView mmenuTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);
        mcreateButton = findViewById(R.id.createButton);
        mvisualizeButton = findViewById(R.id.visualizeButton);
        mdisconnectionButton = findViewById(R.id.disconnectionButton);
        mmenuTextView = findViewById(R.id.menuTextView);

        mcreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profActivity = new Intent(TeacherMenuActivity.this, ProfActivity.class);
                startActivity(profActivity);
            }
        });

        mvisualizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent loginProf = new Intent(TeacherMenuActivity.this, LoginProf.class);
                //startActivity(loginProf);
            }
        });
        mdisconnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(TeacherMenuActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }
}
