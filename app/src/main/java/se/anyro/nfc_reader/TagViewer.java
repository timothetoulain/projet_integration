package se.anyro.nfc_reader;


import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.PresenceQuery;
import se.anyro.nfc_reader.record.ParsedNdefRecord;
import se.anyro.nfc_reader.setup.DialogManager;
import se.anyro.nfc_reader.setup.NdefMessageParser;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device just discovered.
 */
public class TagViewer extends Activity {

    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private LinearLayout mTagContent;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;

    private AlertDialog mDialog;

    private List<Tag> mTags = new ArrayList<>();

    private String teacher;
    private String course;
    private String student;

    private Button mForgottenCardButton,mFinishButton, mManualAddingButton;

    private TextView registeredStudentsNumber;
    // Attribute used for storing a tag used for debugging purpose. When using Log.i(TAG, "Something"), it will be easier to track these messages in the logcat.
    private String TAG;

    private DialogFragment confirmAcquittanceDialog;

    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_viewer);

        registeredStudentsNumber = findViewById(R.id.registeredStudentsNumber);
        VariableRepository.getInstance().setStudentName("");

        mForgottenCardButton = findViewById(R.id.forgottenCardButton);
        mFinishButton = findViewById(R.id.finishButton);
        mManualAddingButton=findViewById(R.id.manualAddingButton);
        mTagContent = findViewById(R.id.list);

        student=null;

        this.teacher= VariableRepository.getInstance().getTeacherName();
        this.course=VariableRepository.getInstance().getCourseName();

        this.TAG = "ViewerActivityLog";
        Log.i(TAG,"teacher:"+teacher);
        Log.i(TAG,"course:"+course);

        resolveIntent(getIntent());

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });

        mForgottenCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardForgotten = new Intent(TagViewer.this, CardForgottenActivity.class);
                startActivity(cardForgotten);
            }
        });
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherLogin=VariableRepository.getInstance().getTeacherLogin();

                System.out.println("teacher login:"+teacherLogin+":");

                Log.d(TAG, "Finish button pressed");
                confirmAcquittanceDialog = new DialogManager();
                ((DialogManager) confirmAcquittanceDialog).setDialogToDisplay("acquittanceConfirmationDialog");
                ((DialogManager) confirmAcquittanceDialog).setTeacherLogin(teacherLogin);
                ((DialogManager) confirmAcquittanceDialog).setExitTagViewer(true);

                // This functions calls whatever is implemented into the DialogManager onCreateDialog method
                confirmAcquittanceDialog.show(getFragmentManager(), "ConfirmDialog");
                ((DialogManager)confirmAcquittanceDialog).onDismissListener(closeListener);
            }
        });
       mManualAddingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String teacherLogin=VariableRepository.getInstance().getTeacherLogin();
                System.out.println("teacher login:"+teacherLogin+":");

                Log.d(TAG, "Finish button pressed");
                confirmAcquittanceDialog = new DialogManager();
                ((DialogManager) confirmAcquittanceDialog).setDialogToDisplay("acquittanceConfirmationDialog");
                ((DialogManager) confirmAcquittanceDialog).setTeacherLogin(teacherLogin);
                ((DialogManager) confirmAcquittanceDialog).setGoToManualAdd(true);

                // This functions calls whatever is implemented into the DialogManager onCreateDialog method
                confirmAcquittanceDialog.show(getFragmentManager(), "ConfirmDialog");
                ((DialogManager)confirmAcquittanceDialog).onDismissListener(closeListener);
            }
        });
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
        //if(count>0){
        if(VariableRepository.getInstance().getOnResumeCounter()==1){
            Log.i(TAG,"let's read");
            this.student=VariableRepository.getInstance().getStudentName();

            Log.i(TAG,"student: "+student);
            if(!student.equals("") && !student.contains("ERROR")){
                displayIfForgottenCard(student);
                Log.i(TAG,"display student: "+student+" and delete him from file");
                VariableRepository.getInstance().setStudentName("");
            }
            else if(student.equals("")){
                System.out.println("on resume ubnknown");
                ToastMessage.unknownStudentMessage(this);
                Log.i(TAG,"unknown student");
            }
            else if(student.contains("ERROR")){
                System.out.println("on resume already");

                ToastMessage.studentAlreadyRegistered(this);
                Log.i(TAG,"student already registered");
            }
        }
        VariableRepository.getInstance().resetOnResumeCounter();
        //count++;
        Log.i(TAG,"resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
    }

    private void displayIfForgottenCard(String studentName){
        NdefMessage[] msgs;
        byte[] empty = new byte[0];
        byte[] id = null;
        Tag tag = null;
        byte[] payload = studentName.getBytes();

        NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
        NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
        msgs = new NdefMessage[] { msg };
        mTags.add(tag);
        buildTagViews(msgs);
        VariableRepository.getInstance().incrementStudentCounter();
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Log.i(TAG,"id: "+id);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Log.i(TAG,"tag: "+tag);

                byte[] payload = dumpTagData(tag).getBytes();
                String bytes=new String(payload);
                System.out.println("bytes= "+bytes);
                if(bytes.equals("")){
                    System.out.println("forced exit of function");
                    return;
                }
                Log.i(TAG,"payload: "+payload);

                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
                mTags.add(tag);
            }
            Log.i(TAG,"msgs: "+msgs);
            // Setup the views
            buildTagViews(msgs);
            VariableRepository.getInstance().incrementStudentCounter();
            registeredStudentsNumber.setText(VariableRepository.getInstance().getStudentCounter());
        }
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append(toDec(id));

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";
                try {
                    MifareClassic mifareTag;
                    try {
                        mifareTag = MifareClassic.get(tag);
                    } catch (Exception e) {
                        tag = cleanupTag(tag);
                        mifareTag = MifareClassic.get(tag);
                    }
                    switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize()).append(" bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: ").append(e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                case MifareUltralight.TYPE_ULTRALIGHT:
                    type = "Ultralight";
                    break;
                case MifareUltralight.TYPE_ULTRALIGHT_C:
                    type = "Ultralight C";
                    break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        String studentName;
        studentName=presence(sb.toString());
        return studentName;
    }

    private Tag cleanupTag(Tag oTag) {
        if (oTag == null)
            return null;

        String[] sTechList = oTag.getTechList();

        Parcel oParcel = Parcel.obtain();
        oTag.writeToParcel(oParcel, 0);
        oParcel.setDataPosition(0);

        int len = oParcel.readInt();
        byte[] id = null;
        if (len >= 0) {
            id = new byte[len];
            oParcel.readByteArray(id);
        }
        int[] oTechList = new int[oParcel.readInt()];
        oParcel.readIntArray(oTechList);
        Bundle[] oTechExtras = oParcel.createTypedArray(Bundle.CREATOR);
        int serviceHandle = oParcel.readInt();
        int isMock = oParcel.readInt();
        IBinder tagService;
        if (isMock == 0) {
            tagService = oParcel.readStrongBinder();
        } else {
            tagService = null;
        }
        oParcel.recycle();

        int nfca_idx = -1;
        int mc_idx = -1;
        short oSak = 0;
        short nSak = 0;

        for (int idx = 0; idx < sTechList.length; idx++) {
            if (sTechList[idx].equals(NfcA.class.getName())) {
                if (nfca_idx == -1) {
                    nfca_idx = idx;
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        oSak = oTechExtras[idx].getShort("sak");
                        nSak = oSak;
                    }
                } else {
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        nSak = (short) (nSak | oTechExtras[idx].getShort("sak"));
                    }
                }
            } else if (sTechList[idx].equals(MifareClassic.class.getName())) {
                mc_idx = idx;
            }
        }

        boolean modified = false;

        if (oSak != nSak) {
            oTechExtras[nfca_idx].putShort("sak", nSak);
            modified = true;
        }

        if (nfca_idx != -1 && mc_idx != -1 && oTechExtras[mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx];
            modified = true;
        }

        if (!modified) {
            return oTag;
        }

        Parcel nParcel = Parcel.obtain();
        nParcel.writeInt(id.length);
        nParcel.writeByteArray(id);
        nParcel.writeInt(oTechList.length);
        nParcel.writeIntArray(oTechList);
        nParcel.writeTypedArray(oTechExtras, 0);
        nParcel.writeInt(serviceHandle);
        nParcel.writeInt(isMock);
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService);
        }
        nParcel.setDataPosition(0);

        Tag nTag = Tag.CREATOR.createFromParcel(nParcel);

        nParcel.recycle();

        return nTag;
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }

    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            TextView timeView = new TextView(this);
            timeView.setText(TIME_FORMAT.format(now));
            content.addView(timeView, 0);
            ParsedNdefRecord record = records.get(i);
            content.addView(record.getView(this, inflater, content, i), 1 + i);
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mTags.size() == 0) {
            Toast.makeText(this, R.string.nothing_scanned, Toast.LENGTH_LONG).show();
            return true;
        }
        switch (item.getItemId()) {
        case R.id.menu_main_clear:
            clearTags();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void clearTags() {
        mTags.clear();
        for (int i = mTagContent.getChildCount() -1; i >= 0 ; i--) {
            View view = mTagContent.getChildAt(i);
            if (view.getId() != R.id.tag_viewer_text) {
                mTagContent.removeViewAt(i);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }
    public String presence(String sb){
        String studentName=null;
        String type="addPresent";
        try {
            VariableRepository.getInstance().setNfc(sb);
            studentName=new PresenceQuery(this).execute(type,course,sb).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("student before error:"+studentName+":");
         if(studentName==null){
            System.out.println("server unreachable");
            ToastMessage.connectionErrorMessage(getApplicationContext());
            studentName="";
            return studentName;
         }
        //case already register
        else if(studentName.contains("ERROR")){
            System.out.println("already register for this course");
            ToastMessage.studentAlreadyRegistered(getApplicationContext());
            studentName="";
            return studentName;
         }
        //case card not recognized
        else if(studentName.equals("")){
            System.out.println("unknown student");
            ToastMessage.unknownStudentMessage(getApplicationContext());
            Intent studentRegistration = new Intent(TagViewer.this, StudentRegistrationActivity.class);
            startActivity(studentRegistration);
        }
        return studentName;
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
        Toast.makeText(this,R.string.confirm_registration_title,Toast.LENGTH_SHORT).show();
    }

    se.anyro.nfc_reader.setup.DialogInterface closeListener = new se.anyro.nfc_reader.setup.DialogInterface() {

        @Override
        public void handleDialogClose(se.anyro.nfc_reader.setup.DialogInterface dialog) {
            //do here whatever you want to do on Dialog dismiss
            if ( ((DialogManager) confirmAcquittanceDialog).getUserChose() == true ) {
                if ( ((DialogManager) confirmAcquittanceDialog).getUserConfirm() == true &&
                        ((DialogManager) confirmAcquittanceDialog).getExitTagViewer() == true ) {
                    Intent mainIntent = new Intent(getBaseContext(), TeacherMenuActivity.class);
                    Log.d(TAG, "goingToStartActivity");
                    VariableRepository.getInstance().resetStudentCounter();
                    startActivity(mainIntent);
                    finish();
                    System.out.println("exit tag view");

                }
                else if ( ((DialogManager) confirmAcquittanceDialog).getUserConfirm() == true &&
                        ((DialogManager) confirmAcquittanceDialog).getGoToManualAdd() == true) {
                    Intent mainIntent = new Intent(getBaseContext(), StudentManualAddingActivity.class);
                    Log.d(TAG, "goingToStartActivity");
                    startActivity(mainIntent);
                    System.out.println("go to manual adding");
                }
                else {
                    Log.d(TAG, "goingToStayOnThisActivity");
                }
            }
        }
    };
    public void handleDialogClose(DialogInterface dialog) {
        //do here whatever you want to do on Dialog dismiss
    }
}