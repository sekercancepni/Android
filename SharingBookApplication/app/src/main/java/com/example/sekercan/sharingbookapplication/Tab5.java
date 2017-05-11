package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Tab5 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab5);

        final TextView textView = (TextView) findViewById(R.id.headerText);
        textView.setText("Profile Settings");

        final EditText firstNameText = (EditText) findViewById(R.id.first_name);
        final EditText lastNameText = (EditText) findViewById(R.id.last_name);
        final EditText emailText = (EditText) findViewById(R.id.email);
        final EditText phoneText = (EditText) findViewById(R.id.phone);

        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {

                    firstNameText.setText(jsonData.getString("first_name"));
                    lastNameText.setText(jsonData.getString("last_name"));
                    emailText.setText(jsonData.getString("email"));
                    phoneText.setText(jsonData.getString("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.userDetail(handler, LoggedInUser.id);
    }

    public void saveUserDetailListener(View v) {
        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    Boolean isSuccess = jsonData.getString("success").toString().equals("1");
                    String userMessage = isSuccess ? "Your profile has been updated." : jsonData.getString("message").toString();
                    Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        final EditText firstNameText = (EditText) findViewById(R.id.first_name);
        final EditText lastNameText = (EditText) findViewById(R.id.last_name);
        final EditText emailText = (EditText) findViewById(R.id.email);
        final EditText phoneText = (EditText) findViewById(R.id.phone);
        final EditText oldPassword = (EditText) findViewById(R.id.old_password);
        final EditText newPassword = (EditText) findViewById(R.id.new_password);

        Api api = new Api();
        api.saveUserDetail(handler, LoggedInUser.id,
                firstNameText.getText().toString(), lastNameText.getText().toString(), emailText.getText().toString(),
                phoneText.getText().toString(), oldPassword.getText().toString(),
                newPassword.getText().toString());
    }

    public void logoutListener(View v) {
        LoggedInUser.logout();
        Intent intent = new Intent("android.intent.action.MainActivity");
        startActivity(intent);
    }

    public void redirectToProfile(View v) {
        Intent intent = new Intent("android.intent.action.UserDetail");
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", LoggedInUser.id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
