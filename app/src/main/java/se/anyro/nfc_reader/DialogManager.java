package se.anyro.nfc_reader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogManager extends DialogFragment {
    /* VARIABLES TEMPORAIRES, je pense que ça peut être intéressant de créer un variable repository, je sais pas ...
     */
    private Boolean userLogingOut = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.logoutDialogTitle)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        userLogingOut = true;
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        userLogingOut = false;
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public Boolean getUserLogingOut() {
        return this.userLogingOut;
    }
}
