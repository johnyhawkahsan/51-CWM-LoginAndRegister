package com.johnyhawkdesigns.a51_cwm_loginandregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {


    EditText etName, etEmail, etPassword;
    String name, email, password;
    Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        etName = (EditText) findViewById(R.id.etNewName);
        etEmail = (EditText) findViewById(R.id.etNewEmail);
        etPassword = (EditText) findViewById(R.id.etNewPassword);
        btnRegister = (Button) findViewById(R.id.btnNewRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                String task = "register";
                BackgroundTask backgroundTask = new BackgroundTask(Register.this);

                //Execute the register task- Passes the params to backgroundTask in the form param[0], param[1], param[2], param[3]
                backgroundTask.execute(task, name, email, password);
                finish();
            }
        });

    }
}
