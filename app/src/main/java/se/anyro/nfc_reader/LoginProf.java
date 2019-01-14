package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.LoginProfQuery;

public class LoginProf extends Activity {
    private Button mButtonValider;
    private EditText meditTextIdentifiant;
    private EditText meditTextMdp;
    private String teacherFile = "teacher.txt";
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prof);
        mButtonValider = findViewById(R.id.buttonValider);
        meditTextIdentifiant = findViewById(R.id.editTextIdentifiant);
        meditTextMdp = findViewById(R.id.editTextMdp);


        mButtonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meditTextIdentifiant.getText().length()==0 || meditTextMdp.getText().length()==0){
                    incompleteFieldMessage();
                } else {
                    String teacherName=login();
                    if(teacherName != null ){
                        if(teacherName.equals("")){
                            loginPasswordIncorrectMessage();
                        }
                        else{
                            //on stocke le nom du prof qui vient de se connecter pour les requetes suivantes
                            System.out.println("apres query");
                            saveData(teacherName);
                            Intent teacherMenuActivity = new Intent(LoginProf.this, TeacherMenuActivity.class);
                            startActivity(teacherMenuActivity);
                            /*Intent profActivity = new Intent(LoginProf.this, ProfActivity.class);
                            startActivity(profActivity);*/
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
            this.login=meditTextIdentifiant.getText().toString();
            String password=meditTextMdp.getText().toString();
            String type="checkAccount";
            String resultLogin=new LoginProfQuery(this).execute(type,login,password).get();
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
    private void saveData(String nomProf) {
        try {
            FileOutputStream out = this.openFileOutput(teacherFile, MODE_PRIVATE);
            out.write(nomProf.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
