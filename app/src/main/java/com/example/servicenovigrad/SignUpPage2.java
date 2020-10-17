package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
//import java.lang.Object.javax.mail.internet.InternetAddress;
//import org.apache.commons.validator.routines.EmailValidator;


public class SignUpPage2 extends AppCompatActivity {
    private Button createaccountButton;
    private EditText prenom;
    private EditText nom;
    private EditText courriel;
    private EditText utilisateur;
    private EditText motDePasse;
    private EditText confirmationMotDePasse;
    private TextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page2);

        createaccountButton = (Button) findViewById(R.id.createaccount);
        createaccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidSignUp()) {
                    openWelcomePage();
                }else{
                    createError();
                }
            }
        });

        prenom = (EditText) findViewById(R.id.firstname);
        nom = (EditText) findViewById(R.id.lastname);
        courriel = (EditText) findViewById(R.id.email);
        utilisateur = (EditText) findViewById(R.id.username);
        motDePasse = (EditText) findViewById(R.id.password);
        confirmationMotDePasse = (EditText) findViewById(R.id.confirmpassword);
        errors = (TextView) findViewById(R.id.errorMessages);

    }

    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    public boolean checkValidSignUp(){
        boolean prenomValide = !prenom.getText().toString().isEmpty() /*&& only contains alphabet*/;
        boolean nomValide = !nom.getText().toString().isEmpty() /*&& only contains alphabet*/;
        boolean utilisateurValide = !utilisateur.getText().toString().isEmpty() /*&& not already a user*/;
        boolean courrielValide = !courriel.getText().toString().isEmpty() && isValidEmail(courriel.getText().toString());
        boolean passeValide = !motDePasse.getText().toString().isEmpty();
        boolean confirmationValide = confirmationMotDePasse.getText().toString().equals(motDePasse.getText().toString());
        return prenomValide && nomValide && utilisateurValide && courrielValide && passeValide && confirmationValide;
    }

    public void createError(){
        errors.setText("you have entered invalid credentials");
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}