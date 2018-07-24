package com.johnyhawkdesigns.a51_cwm_loginandregister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ahsan on 7/24/2018.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "BackgroundTask";

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
        Log.d(TAG, "doInBackground: task = " + task);

        if (task.equals("register")){
            String regName = params[1];
            String regEmail = params[2];
            String regPassword = params[3];

            try {
                URL url = new URL(urlRegistration);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //This should match our PHP Post method
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("identifier_name", "UTF-8") + "=" + URLEncoder.encode(regName, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_email", "UTF-8") + "=" + URLEncoder.encode(regEmail, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_password", "UTF-8") + "=" + URLEncoder.encode(regPassword, "UTF-8");

                //Note: identifier_name, identifier_email and identifier_password are same as in our PHP file
                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                editor.putString("flag", "register");
                editor.commit();
                return "Successfully Registered " + regName;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //If task equals Login
        }  else if (task.equals("login")){
            String loginEmail = params[1];
            String loginPassword = params[2];

            try {
                URL url = new URL(urlLogin);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                //Set the email and password to the database
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("identifier_email", "UTF-8") + "=" + URLEncoder.encode(loginEmail, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_loginPassword", "UTF-8") + "=" + URLEncoder.encode(loginPassword, "UTF-8");

                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //get response from the database
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String dataResponse = "";
                String inputLine = "";
                while ((inputLine = bufferedReader.readLine()) != null){
                    dataResponse += inputLine;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(dataResponse);

                editor.putString("flag", "login");
                editor.commit();
                return dataResponse;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //This method will be called when doInBackground completes... and it will return the completion string which will display this toast.
    @Override
    protected void onPostExecute(String s) {
        String flag = sharedPreferences.getString("flag", "0");

        if (flag.equals("register")){
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        }

        else if (flag.equals("login")){

            String test = "false";
            String name = "";
            String email = "";
            String[] serverResponse = s.split("[,]"); //Server response should be formatted according to Php code requirement
            test = serverResponse[0];
            name = serverResponse[1];
            email = serverResponse[2];

            if (test.equals("true")){
                editor.putString("name", name);
                editor.commit();
                editor.putString("email", email);
                editor.commit();

                Intent intent = new Intent(context, Login.class);
                context.startActivity(intent);
            } else {
                display("Login Failed...", "That email and password do not match our records :(");
            }

        } else {
            display("Login Failed....", "Something weird happened :(");
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public void display(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
