package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button mTeacherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTeacherButton = findViewById(R.id.teacher_button);
        mTeacherButton.setEnabled(true);

        mTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginTeacher = new Intent(MainActivity.this, LoginTeacher.class);
                startActivity(loginTeacher);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
}