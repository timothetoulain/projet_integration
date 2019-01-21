package se.anyro.nfc_reader.setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import se.anyro.nfc_reader.R;

public class DialogManager extends DialogFragment {
    // I'm using these attributes as a temporary mean to store these variable.
    private Boolean userLogingOut;
    private Boolean userChose;
    private Boolean userConfirm;
    private se.anyro.nfc_reader.setup.DialogInterface closeListener;
    private String dialogToDisplay;

    public DialogManager() {
        this.userLogingOut = false;
        this.userChose = false;
        this.userConfirm = false;
        this.dialogToDisplay = "disconnectDialog";
    }

    // This method describe the general layout of the Dialog pop-up appearing when a show() method is called.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = null;
        if ( this.dialogToDisplay.equals("disconnectDialog") ) {
            // Use the Builder class for convenient dialog construction
            builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.logoutDialogTitle)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            userChose = true;
                            userLogingOut = true;
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            userChose = true;
                            userLogingOut = false;
                        }
                    });
            // Create the AlertDialog object and return it

        } else if ( this.dialogToDisplay.equals("acquittanceConfirmationDialog") ) {
            builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle(R.string.confirm_registration);
            builder.setMessage(R.string.confirm_registrationText);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                    // Add action buttons
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                            userChose = true;
                            userConfirm = true;

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

    public Boolean getUserLogingOut() {
        return this.userLogingOut;
    }

    public void setUserLogingOut(Boolean param) {
        this.userLogingOut = param;
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
}
