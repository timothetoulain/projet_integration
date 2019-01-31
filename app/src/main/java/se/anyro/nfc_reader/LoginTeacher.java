package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.LoginTeacherQuery;
import se.anyro.nfc_reader.setup.VariableRepository;

public class LoginTeacher extends Activity {
    private Button mButtonValidate;
    private EditText mEditTextIdentifier;
    private EditText mEditTextPsw;
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);
        mButtonValidate = findViewById(R.id.buttonValidate);
        mEditTextIdentifier = findViewById(R.id.editTextIdentifier);
        mEditTextPsw = findViewById(R.id.editTextPsw);

        mButtonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextIdentifier.getText().length()==0 || mEditTextPsw.getText().length()==0){
                    incompleteFieldMessage();
                } else {
                    String teacherName=login();
                    if(teacherName != null ){
                        if(teacherName.equals("")){
                            loginPasswordIncorrectMessage();
                        }
                        else{
                            System.out.println("after query");
                            VariableRepository.getInstance().setTeacherName(teacherName);
                            VariableRepository.getInstance().setTeacherLogin(mEditTextIdentifier.getText().toString());
                            Intent teacherMenuActivity = new Intent(LoginTeacher.this, TeacherMenuActivity.class);
                            startActivity(teacherMenuActivity);
                        }
                    }
                    else{
                        connectionErrorMessage();
                        System.out.println("ERROR 1");
                    }
                }
            }
        });
    }
    public String login(){
        try {
            this.login=mEditTextIdentifier.getText().toString();
            String password=mEditTextPsw.getText().toString();
            String type="checkAccount";
            String resultLogin=new LoginTeacherQuery(this).execute(type,login,password).get();
            return resultLogin;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loginPasswordIncorrectMessage(){
        Toast.makeText(this,R.string.error_login_password,Toast.LENGTH_SHORT).show();
    }
    private void incompleteFieldMessage(){
        Toast.makeText(this,R.string.error_incomplete,Toast.LENGTH_SHORT).show();
    }
    private void connectionErrorMessage(){
        Toast.makeText(this,R.string.error_connection,Toast.LENGTH_SHORT).show();
    }
}
