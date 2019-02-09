package se.anyro.nfc_reader.setup;

import android.content.Context;
import android.widget.Toast;

import se.anyro.nfc_reader.R;

public class ToastMessage {

    public static void incompleteFieldMessage(Context c){
        Toast.makeText(c, R.string.error_incomplete,Toast.LENGTH_SHORT).show();
    }
    public static void unknownStudentMessage(Context c){
        Toast.makeText(c,R.string.error_unknown_student,Toast.LENGTH_SHORT).show();
    }
    public static void loginPasswordIncorrectMessage(Context c){
        Toast.makeText(c,R.string.error_login_password,Toast.LENGTH_SHORT).show();
    }
    public static void connectionErrorMessage(Context c){
        Toast.makeText(c,R.string.error_connection,Toast.LENGTH_SHORT).show();
    }
}
