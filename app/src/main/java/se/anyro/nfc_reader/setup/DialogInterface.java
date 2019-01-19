package se.anyro.nfc_reader.setup;

// https://stackoverflow.com/questions/24151891/callback-when-dialogfragment-is-dismissed
public interface DialogInterface {
    public void handleDialogClose(DialogInterface dialog);//or whatever args you want
}
