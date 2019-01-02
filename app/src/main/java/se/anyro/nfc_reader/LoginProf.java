package se.anyro.nfc_reader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import se.anyro.nfc_reader.database.AppelQuery;
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
        mButtonValider = (Button) findViewById(R.id.buttonValider);
        meditTextIdentifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        meditTextMdp = (EditText) findViewById(R.id.editTextMdp);


        mButtonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meditTextIdentifiant.getText().length()==0 || meditTextMdp.getText().length()==0){
                    champIncompletMessage();
                } else {
                    String nomProf=login();
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
            }
        });
    }
    public String login(){
        String resultLogin=null;
        try {
            String identifiant=meditTextIdentifiant.getText().toString();
            String mdp=meditTextMdp.getText().toString();
            resultLogin=new LoginProfQuery(this).execute(identifiant,mdp).get();
            return resultLogin;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void identifiantIncorrectMessage(){
        Toast.makeText(this,"Identifiant et/ou mot de passe incorrect(s)",Toast.LENGTH_SHORT).show();
    }
    private void champIncompletMessage(){
        Toast.makeText(this,"Au moins un champ est incomplet !",Toast.LENGTH_SHORT).show();
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
