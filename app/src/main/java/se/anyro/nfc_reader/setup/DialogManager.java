package se.anyro.nfc_reader.setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.R;
import se.anyro.nfc_reader.database.LoginTeacherQuery;

/**
 * Manage the poping dialogs such as the one asking for the password before leaving the TagViewer activity (end of the registration)
 *
 */
public class DialogManager extends DialogFragment {
    // I'm using these attributes as a temporary mean to store these variable.
    private Boolean userLoggingOut;
    private Boolean userChose;
    private Boolean userConfirm;
    private Boolean exitTagViewer;
    private Boolean goToManualAdd;

    private se.anyro.nfc_reader.setup.DialogInterface closeListener;
    private String dialogToDisplay;
    private String teacherLogin;

    public DialogManager() {
        this.userLoggingOut = false;
        this.userChose = false;
        this.userConfirm = false;
        this.exitTagViewer=false;
        this.goToManualAdd=false;
        this.dialogToDisplay = "disconnectDialog";
    }

    // This method describe the general layout of the Dialog pop-up appearing when a show() method is called.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = null;
        if ( this.dialogToDisplay.equals("disconnectDialog") ) {
            // Use the Builder class for convenient dialog construction
            builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.disconnect)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            userChose = true;
                            userLoggingOut = true;
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            userChose = true;
                            userLoggingOut = false;
                        }
                    });
            // Create the AlertDialog object and return it

        } else if ( this.dialogToDisplay.equals("acquittanceConfirmationDialog") ) {
            builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle(R.string.confirm_registration);
            builder.setMessage(R.string.confirm_registration);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                    // Add action buttons
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //query to check if the password is correct
                            EditText passwordEditText = (EditText) getDialog().findViewById(R.id.password);
                            String resultLogin=null;
                            String login=teacherLogin;
                            String password=passwordEditText.getText().toString();
                            String type="checkAccount";
                            Context context=getActivity();
                            try {
                                resultLogin=new LoginTeacherQuery(context).execute(type,login,password).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if((resultLogin==null) || (resultLogin=="")){
                                Toast.makeText(context,R.string.error_password,Toast.LENGTH_SHORT).show();
                            }
                            else {
                                userChose = true;
                                userConfirm = true;
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            userChose = true;
                            dismiss();
                        }
                    });
        }
        return builder.create();
    }

    public void onDismissListener(se.anyro.nfc_reader.setup.DialogInterface closeListener) {
        this.closeListener = closeListener;
    }

    public void dismiss() {
        super.dismiss();
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(closeListener != null) {
            closeListener.handleDialogClose(null);
        }

    }

    public Boolean getUserLoggingOut() {
        return this.userLoggingOut;
    }

    public void setUserLogingOut(Boolean param) {
        this.userLoggingOut = param;
    }

    public Boolean getUserChose() {
        return this.userChose;
    }

    public void setUserChose( Boolean param) {
        this.userChose = param;
    }

    public Boolean getUserConfirm() {
        return this.userConfirm;
    }

    public void setUserConfirm(Boolean param) {
        this.userConfirm = param;
    }

    public void setDialogToDisplay(String param) {
        this.dialogToDisplay = param;
    }
   public Boolean getGoToManualAdd(){
        return this.goToManualAdd;
    }
    public Boolean getExitTagViewer(){
        return this.exitTagViewer;
    }
    public void setGoToManualAdd(Boolean param) {
        this.goToManualAdd = param;
    }
    public void setExitTagViewer(Boolean param) {
        this.exitTagViewer = param;
    }

    public void setTeacherLogin(String param){this.teacherLogin=param;}

}
