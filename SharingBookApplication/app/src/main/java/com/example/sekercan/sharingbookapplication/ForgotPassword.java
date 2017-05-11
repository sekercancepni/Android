package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPassword extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
    }

    public void rememberPasswordListener(View v) {

        final EditText email = (EditText)findViewById(R.id.email);
        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    String userMessage = jsonData.getString("message");
                    Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.sendPasswordMail(handler, email.getText().toString());
    }
}