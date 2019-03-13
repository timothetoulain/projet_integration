package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.CardForgottenQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * A student who's forgotten his card is redirected on this activity
 * so he can register himself manually
 */
public class CardForgottenActivity extends Activity {
    private Button mConfirmButton;
    private EditText studentNumberEditText;
    private EditText nameEditText;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_forgotten);
        mConfirmButton = findViewById(R.id.confirmButton);
        studentNumberEditText = findViewById(R.id.studentNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);

        TAG = "CardForgottenActivityLog";

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentNumberEditText.getText().length()==0 || nameEditText.getText().length()==0){
                    ToastMessage.incompleteFieldMessage(getApplicationContext());
                } else {
                    String nameRetrieved=query();
                    if(nameRetrieved != null && !nameRetrieved.equals("")){

                        //we save the student name to display it on the TagViewer activity
                        VariableRepository.getInstance().setStudentName(nameRetrieved);
                        VariableRepository.getInstance().incrementOnResumeCounter();

                        Intent tagViewer = new Intent(CardForgottenActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                    else{
                        ToastMessage.unknownStudentMessage(getApplicationContext());
                        VariableRepository.getInstance().setStudentName("");
                        Intent tagViewer = new Intent(CardForgottenActivity.this, TagViewer.class);
                        startActivity(tagViewer);
                    }
                }
            }
        });
    }


    public String query(){
        String type="addPresent";
        String numberStudent=studentNumberEditText.getText().toString();
        String nameStudent=nameEditText.getText().toString();
        String course=VariableRepository.getInstance().getCourseName();
        try {
            nameStudent=new CardForgottenQuery(this).execute(type,course,numberStudent,nameStudent).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return nameStudent;
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
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        Intent tagViewer = new Intent(this, TagViewer.class);
        startActivity(tagViewer);
    }
}
