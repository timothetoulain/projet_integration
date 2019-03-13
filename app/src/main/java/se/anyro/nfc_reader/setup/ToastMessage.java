package se.anyro.nfc_reader.setup;

import android.content.Context;
import android.widget.Toast;

import se.anyro.nfc_reader.R;

/**
 * regroup all the toast messages used in the app
 */
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
    public static void studentAlreadyRegistered(Context c){
        Toast.makeText(c,R.string.already_registered,Toast.LENGTH_SHORT).show();
    }
    public static void courseCreated(Context c){
        Toast.makeText(c,R.string.course_added,Toast.LENGTH_SHORT).show();
    }
    public static void courseNotCreated(Context c){
        Toast.makeText(c,R.string.course_not_added,Toast.LENGTH_SHORT).show();
    }
}
