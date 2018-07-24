package com.johnyhawkdesigns.a51_cwm_loginandregister;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Login extends AppCompatActivity {


    TextView name,email;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_layout);

        email = (TextView) findViewById(R.id.textEmail);
        name = (TextView) findViewById(R.id.textName);

        preferences = this.getSharedPreferences("MYPREFS", MODE_PRIVATE);

        String mName = preferences.getString("name","ERROR getting name");
        String mEmail = preferences.getString("email","ERROR getting email");
        name.setText(mName);
        email.setText(mEmail);


    }
}
