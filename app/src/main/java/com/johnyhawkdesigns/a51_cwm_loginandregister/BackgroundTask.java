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
    //params are multiple values params[0], params[1], params[2], params[3] received from the calling activity when se used backgroundTask.execute()
    protected String doInBackground(String... params) //the three dot stays for vargars. you can access it like a String[].
    {

        sharedPreferences = context.getSharedPreferences("MYPREFS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("flag", "0");
        editor.commit();

        //https://stackoverflow.com/questions/18341652/connect-failed-econnrefused
        //"localhost" should be replaced with "10.0.2.2", For Genymotion use: "10.0.3.2" instead of 10.0.2.2
        String urlRegistration = "http://10.0.3.2/sql-connection/LoginAndRegister-register.php";
        String urlLogin = "http://10.0.3.2/sql-connection/LoginAndRegister-login.php";

        String task = params[0]; //Get params at 0 index
        Log.d(TAG, "doInBackground: task = " + task);

        //For registration method, we want to prepare a "POST" method to post to our server. Inside server, we have php code to process this POST request.
        if (task.equals("register")){
            String regName = params[1];
            String regEmail = params[2];
            String regPassword = params[3];

            try {
                URL url = new URL(urlRegistration);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //This should match our PHP Post method
                httpURLConnection.setDoOutput(true);//set to true because we want url for output
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("identifier_name", "UTF-8") + "=" + URLEncoder.encode(regName, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_email", "UTF-8") + "=" + URLEncoder.encode(regEmail, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_password", "UTF-8") + "=" + URLEncoder.encode(regPassword, "UTF-8");

                //Note: identifier_name, identifier_email and identifier_password are same as in our PHP file

                Log.d(TAG, "doInBackground: Register Task myData = " + myData);

                bufferedWriter.write(myData);
                bufferedWriter.flush();//Flushing output on a buffered stream means transmitting all accumulated characters to the file.
                bufferedWriter.close();

                //Read the input data using InputStream after successfully registration process is completed in OutputStream
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                editor.putString("flag", "register"); //Set flag to "register" for further processing in Post Execute
                editor.commit();
                return "Successfully Registered " + regName;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //If task equals LoggedInActivity
        }  else if (task.equals("login")){
            String loginEmail = params[1];
            String loginPassword = params[2];

            try {
                URL url = new URL(urlLogin);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);//set to true because we want url for output
                httpURLConnection.setDoInput(true);//set to true because we want url for input as well

                //send the email and password to the database
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("identifier_email", "UTF-8") + "=" + URLEncoder.encode(loginEmail, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_loginPassword", "UTF-8") + "=" + URLEncoder.encode(loginPassword, "UTF-8");

                Log.d(TAG, "doInBackground: Log in Task myData = " + myData);

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

            //@Ahsan = After successfully signing up, we need to go back to Main Activity
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
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

                Intent intent = new Intent(context, LoggedInActivity.class);
                context.startActivity(intent);
            } else {
                display("LoggedInActivity Failed...", "That email and password do not match our records :(");
            }

        } else {
            display("LoggedInActivity Failed....", "Something weird happened :(");
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
