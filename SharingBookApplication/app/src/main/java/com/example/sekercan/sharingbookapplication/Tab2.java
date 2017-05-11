
package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Tab2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);

        final TextView textView = (TextView) findViewById(R.id.headerText);
        textView.setText("Add Book");
    }

    public void addBookListener(View v) {
        final EditText bookNameText = (EditText) findViewById(R.id.book_name);
        final EditText authorNameText = (EditText) findViewById(R.id.author_name);

        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    String isSuccess = jsonData.getString("message");
                    String userMessage;
                    if(isSuccess.equals("Success")) {
                        userMessage = "Successful book addition";
                    } else {
                        userMessage = "Enter the name of the book and the author.";
                    }
                    Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.addBook(handler, LoggedInUser.id,
                bookNameText.getText().toString(), authorNameText.getText().toString());

    }
}
