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
    private String data = "prof.txt";

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
                    champIncompletMessage();
                } else {
                    String nomProf=login();
                    if(nomProf != null){
                        if(nomProf.equals("0")){
                            identifiantIncorrectMessage();
                        }
                        else{
                            //on stocke le nom du prof qui vient de se connecter pour les requetes suivantes
                            saveData(nomProf);
                            Intent profActivity = new Intent(LoginProf.this, ProfActivity.class);
                            startActivity(profActivity);
                        }
                    }
                    else{
                        //TODO erreur !
                        System.out.println("ERROR 1");
                    }
                }
            }
        });
    }
    public String login(){
        try {
            String identifiant=meditTextIdentifiant.getText().toString();
            String mdp=meditTextMdp.getText().toString();
            String type="checkAccount";
            String resultLogin=new LoginProfQuery(this).execute(type,identifiant,mdp).get();
            return resultLogin;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void identifiantIncorrectMessage(){
        Toast.makeText(this,R.string.error_login_password,Toast.LENGTH_SHORT).show();
    }
    private void champIncompletMessage(){
        Toast.makeText(this,R.string.error_incomplete,Toast.LENGTH_SHORT).show();
    }
    private void saveData(String nomProf) {
        try {
            FileOutputStream out = this.openFileOutput(data, MODE_PRIVATE);
            out.write(nomProf.getBytes());
            out.close();
            //Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}
