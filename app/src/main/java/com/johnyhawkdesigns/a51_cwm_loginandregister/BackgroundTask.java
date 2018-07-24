package com.johnyhawkdesigns.a51_cwm_loginandregister;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ahsan on 7/24/2018.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    Context context;

    BackgroundTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        sharedPreferences = context.getSharedPreferences("MYPREFS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("flag", "0");
        editor.commit();

        String urlRegistration = "http://localhost/sql-connection/LoginAndRegister-register.php";
        String urlLogin = "http://localhost/sql-connection/LoginAndRegister-login.php";

        String task = params[0]; //Get params at 0 index

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
