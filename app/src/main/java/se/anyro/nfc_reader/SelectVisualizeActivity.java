package se.anyro.nfc_reader;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.SpinnerTeacherQuery;
import se.anyro.nfc_reader.database.VisualizeQuery;
import se.anyro.nfc_reader.setup.ToastMessage;
import se.anyro.nfc_reader.setup.VariableRepository;

/**
 * The teacher can select here the time, date and course he wants to see the presence list for
 */
public class SelectVisualizeActivity extends Activity implements
        View.OnClickListener {

    private Button mResearchButton;
    private Spinner mSpinnerCourse;
    private String teacher;
    private String  resultFile = "result.csv";
    private Button mStartDateButton,mEndDateButton, mStartTimePickerButton, mEndTimePickerButton;
    private TextView mStartDateEditText,mEndDateEditText, mStartTimeEditText, mEndTimeEditText;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_visualize);

        mSpinnerCourse = findViewById(R.id.spinnerCourse);

        mResearchButton = findViewById(R.id.researchButton);
        mStartDateButton=findViewById(R.id.startDateButton);
        mEndDateButton=findViewById(R.id.endDateButton);
        mStartTimePickerButton=findViewById(R.id.startTimeButton);
        mEndTimePickerButton=findViewById(R.id.endTimeButton);

        mStartDateEditText=findViewById(R.id.startDateEditText);
        mEndDateEditText=findViewById(R.id.endDateEditText);
        mStartTimeEditText=findViewById(R.id.startTimeEditText);
        mEndTimeEditText=findViewById(R.id.endTimeEditText);

        mStartDateButton.setOnClickListener(this);
        mEndDateButton.setOnClickListener(this);
        mStartTimePickerButton.setOnClickListener(this);
        mEndTimePickerButton.setOnClickListener(this);
        mResearchButton.setOnClickListener(this);


        teacher=VariableRepository.getInstance().getTeacherName();

        List classList=new ArrayList();
        classList.add(getString(R.string.course));

        String type="getCourses";
        new SpinnerTeacherQuery(this,classList).execute(type,teacher);

        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classList);
        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourse.setAdapter(adapterClass);
    }

    @Override
    public void onClick(View v) {

        if (v == mStartDateButton) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String month=null;
                            String day=null;

                            //month starts at 0 so we have to increment it
                            monthOfYear++;
                            if(monthOfYear<10){
                                //we have to add a 0 before the number
                                month="0"+Integer.toString(monthOfYear);
                            }
                            if(dayOfMonth<10){
                                day="0"+Integer.toString(dayOfMonth);
                            }
                            if(day!=null && month!=null){
                                mStartDateEditText.setText(year +"-"+ (month) + "-" + day);
                            }
                            else if(day!=null && month==null){
                                mStartDateEditText.setText(year +"-"+ (monthOfYear) + "-" + day);
                            }
                            else if(day==null && month!=null){
                                mStartDateEditText.setText(year +"-"+ (month) + "-" + dayOfMonth);
                            }
                            else{
                                mStartDateEditText.setText(year +"-"+ (monthOfYear) + "-" + dayOfMonth);
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == mEndDateButton) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String day=null;
                            String month=null;
                            //month starts at 0 so we have to increment it
                            monthOfYear++;
                            if(monthOfYear<10){
                                //we have to add a 0 before the number
                                month="0"+Integer.toString(monthOfYear);
                            }
                            if(dayOfMonth<10){
                                day="0"+Integer.toString(dayOfMonth);
                            }
                            if(day!=null && month!=null){
                                mEndDateEditText.setText(year +"-"+ (month) + "-" + day);
                            }
                            else if(day!=null && month==null){
                                mEndDateEditText.setText(year +"-"+ (monthOfYear) + "-" + day);
                            }
                            else if(day==null && month!=null){
                                mEndDateEditText.setText(year +"-"+ (month) + "-" + dayOfMonth);
                            }
                            else{
                                mEndDateEditText.setText(year +"-"+ (monthOfYear) + "-" + dayOfMonth);
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == mStartTimePickerButton) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minuteOfDay) {
                            String hour=null;
                            String minute=null;

                            if(hourOfDay<10){
                                hour="0"+Integer.toString(hourOfDay);
                            }
                            if(minuteOfDay<10){
                                minute="0"+Integer.toString(minuteOfDay);
                            }

                            if(minute!=null && hour!=null){
                                mStartTimeEditText.setText(hour + ":" + minute+":00");
                            }
                            else if(minute!=null && hour==null){
                                mStartTimeEditText.setText(hourOfDay + ":" + minute+":00");
                            }
                            else if(minute==null && hour!=null){
                                mStartTimeEditText.setText(hour + ":" + minuteOfDay+":00");
                            }
                            else{
                                mStartTimeEditText.setText(hourOfDay + ":" + minuteOfDay+":00");
                            }

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == mEndTimePickerButton) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minuteOfDay) {

                            String hour=null;
                            String minute=null;

                            if(hourOfDay<10){
                                hour="0"+Integer.toString(hourOfDay);
                            }
                            if(minuteOfDay<10){
                                minute="0"+Integer.toString(minuteOfDay);
                            }

                            if(minute!=null && hour!=null){
                                mEndTimeEditText.setText(hour + ":" + minute+":00");
                            }
                            else if(minute!=null && hour==null){
                                mEndTimeEditText.setText(hourOfDay + ":" + minute+":00");
                            }
                            else if(minute==null && hour!=null){
                                mEndTimeEditText.setText(hour + ":" + minuteOfDay+":00");
                            }
                            else{
                                mEndTimeEditText.setText(hourOfDay + ":" + minuteOfDay+":00");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == mResearchButton) {
            if(mSpinnerCourse.getSelectedItem().toString().equals(getString(R.string.course))||
                    mStartDateEditText.getText().length()==0 || mEndDateEditText.getText().length()==0 ||
                    mStartTimeEditText.getText().length()==0 || mEndTimeEditText.getText().length()==0)
            {
                ToastMessage.incompleteFieldMessage(getApplicationContext());
            }
            else{
                String type="getPresentStudents";
                String course=mSpinnerCourse.getSelectedItem().toString();
                String dateStart=mStartDateEditText.getText().toString()+" "+mStartTimeEditText.getText().toString();
                String dateEnd=mEndDateEditText.getText().toString()+" "+mEndTimeEditText.getText().toString();
                try {
                    String result=new VisualizeQuery(this).execute(type,course,dateStart,dateEnd).get();
                    saveData(result,resultFile);
                    Intent visualize = new Intent(SelectVisualizeActivity.this, VisualizeActivity.class);
                    startActivity(visualize);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //we save the result of the query into a csv file
    private void saveData(String toSave,String file) {
        try {
            FileOutputStream out = this.openFileOutput(file, MODE_PRIVATE);
            out.write(toSave.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
