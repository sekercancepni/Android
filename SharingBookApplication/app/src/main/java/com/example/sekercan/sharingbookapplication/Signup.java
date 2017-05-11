package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Signup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        final TextView textView = (TextView) findViewById(R.id.headerText);
        textView.setText("Sign Up");
    }

    public void signupListener(View v) {
        final EditText name = (EditText)findViewById(R.id.name);
        final EditText lastName = (EditText)findViewById(R.id.surname);
        final EditText email = (EditText)findViewById(R.id.emailRegister);
        final EditText password = (EditText)findViewById(R.id.passwordRegister);
        final EditText passwordAgain = (EditText)findViewById(R.id.passwordAgainRegister);
        final EditText phone = (EditText)findViewById(R.id.phone);

        if (password.getText().equals(passwordAgain.getText())) {
            Toast.makeText(getApplicationContext(), "Password fields must be equal.", Toast.LENGTH_LONG).show();
            return;
        }

        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    Boolean isSuccess = jsonData.getString("success").equals("1");
                    String userMessage;
                    if (isSuccess) {
                        userMessage = "Your Signup is ok. Please login.";

                    } else {
                        userMessage = jsonData.getString("message");
                    }

                    Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RequestParams params = new RequestParams();
        params.add("first_name", name.getText().toString());
        params.add("last_name", lastName.getText().toString());
        params.add("email", email.getText().toString());
        params.add("phone", phone.getText().toString());
        params.add("password", password.getText().toString());

        Api api = new Api();
        api.signupRequest(handler, params);
    }

    public void loginBtnListener(View v) {
        startActivity(new Intent("android.intent.action.MainActivity"));
    }
}
