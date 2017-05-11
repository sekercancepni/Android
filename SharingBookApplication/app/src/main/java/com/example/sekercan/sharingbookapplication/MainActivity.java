package com.example.sekercan.sharingbookapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (LoggedInUser.id > 0) {
            startActivity(new Intent("android.intent.action.homePage"));
        }

        Button signUp = (Button) findViewById(R.id.sign_up);
        Button logIn = (Button) findViewById(R.id.login_button);
        Button forgotPassword = (Button) findViewById(R.id.forgot_password);


        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            startActivity(new Intent("android.intent.action.Signup"));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent("android.intent.action.ForgotPassword"));
            }
        });

        logIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            final EditText email = (EditText)findViewById(R.id.email);
            final EditText password = (EditText)findViewById(R.id.password);

            HttpResponseHandler handler = new HttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                    try {
                        Boolean isSuccess = jsonData.getString("success").equals("1");
                        String userMessage;
                        if (isSuccess) {
                            LoggedInUser.id = jsonData.getInt("id");
                            LoggedInUser.firstName = jsonData.getString("first_name");
                            LoggedInUser.lastName = jsonData.getString("last_name");

                            startActivity(new Intent("android.intent.action.homePage"));
                        } else {
                            userMessage = "Your email or password is wrong.";
                            Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Api api = new Api();
            // api.doLogin(handler, email.getText().toString(), password.getText().toString());
            api.doLogin(handler, "farukcepnigmail.com", "123456");
            }
        });
    }
}
