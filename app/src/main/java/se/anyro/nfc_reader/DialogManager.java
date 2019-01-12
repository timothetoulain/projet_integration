package se.anyro.nfc_reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogManager extends DialogFragment {
    /* VARIABLES TEMPORAIRES, je pense que ça peut être intéressant de créer un variable repository, je sais pas ...
     */
    private Boolean userLogingOut;
    private Boolean userChose;
    private se.anyro.nfc_reader.DialogInterface closeListener;

    public DialogManager() {
        userLogingOut = false;
        userChose = false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        return builder.create();
    }

    public void onDismissListener(se.anyro.nfc_reader.DialogInterface closeListener) {
        this.closeListener = closeListener;
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

    public Boolean getUserChose() {
        return this.userChose;
    }
}
