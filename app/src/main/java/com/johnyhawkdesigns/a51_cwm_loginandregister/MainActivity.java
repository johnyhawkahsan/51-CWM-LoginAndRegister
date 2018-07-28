package com.johnyhawkdesigns.a51_cwm_loginandregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btnRegister, btnLogin;
    EditText etEmail, etPassword;
    String stringEmail, stringPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        //LoggedInActivity user methods
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringEmail = etEmail.getText().toString();
                stringPassword = etPassword.getText().toString();

                String task = "login";

                //Background task
                BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);

                etEmail.setText("");
                etPassword.setText("");

                //Execute the LoggedInActivity task- Passes the params to backgroundTask param[0], param[1], param[2]
                backgroundTask.execute(task, stringEmail, stringPassword);

            }
        });

        //Register new user methods
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }
}
