package se.anyro.nfc_reader;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TeacherMenuActivity extends Activity {

    private Button mcreateButton;
    private Button mvisualizeButton;
    private Button mdisconnectionButton;
    private TextView mmenuTextView;
    private DialogFragment logOutDialog;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);
        mcreateButton = findViewById(R.id.createButton);
        mvisualizeButton = findViewById(R.id.visualizeButton);
        mdisconnectionButton = findViewById(R.id.disconnectionButton);
        mmenuTextView = findViewById(R.id.menuTextView);

        TAG = "TeacherMenuActivityLog";

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
                Intent visualizeActivity = new Intent(TeacherMenuActivity.this, VisualizeActivity.class);
                startActivity(visualizeActivity);
            }
        });
        mdisconnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onBackPressed Called");
                logOutDialog = new DialogManager();
                logOutDialog.show(getFragmentManager(), "logOutDialog");
                ((DialogManager)logOutDialog).onDismissListener(closeListener);
            }
            /*
            Intent mainActivity = new Intent(TeacherMenuActivity.this, MainActivity.class);
            startActivity(mainActivity);
            */
        });
    }

    DialogInterface closeListener = new DialogInterface() {

        @Override
        public void handleDialogClose(DialogInterface dialog) {
            //do here whatever you want to do on Dialog dismiss
            if ( ((DialogManager) logOutDialog).getUserChose() == true ) {
                if ( ((DialogManager) logOutDialog).getUserLogingOut() == true ) {
                    Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                    Log.d(TAG, "goingToStartActivity");
                    startActivity(mainIntent);
                    finish();
                } else {
                    Log.d(TAG, "goingToStayOnThisActivity");
                }
            }
        }
    };

    public void handleDialogClose(DialogInterface dialog) {
        //do here whatever you want to do on Dialog dismiss
    }

    // SOURCE => https://stackoverflow.com/questions/3141996/android-how-to-override-the-back-button-so-it-doesnt-finish-my-activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        // If we're pressing on the Back button of the phone
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d(TAG, "onKeyDown Called");
            // Call the onBackPressed method defined just after this onKeyDown event callback method
            onBackPressed();
            // Return true for whatever reason that I don't understand right at the moment as of Thursday 10th of January 5:37 PM.
            return true;
        }

        // Doon't know what it does.
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        this.logOutDialog = new DialogManager();
        // This functions calls whatever is implemented into the DialogManager onCreateDialog method
        logOutDialog.show(getFragmentManager(), "logOutDialog");
        ((DialogManager)logOutDialog).onDismissListener(this.closeListener);
    }
}
