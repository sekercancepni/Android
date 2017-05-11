package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);

        final TextView textView = (TextView) findViewById(R.id.headerText);
        textView.setText("Profile");

        Bundle bundle = getIntent().getExtras();
        int userId = bundle.getInt("user_id");

        final TableLayout booksTable = (TableLayout) findViewById(R.id.books_table);

        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {

                try {
                    TextView nameText = (TextView) findViewById(R.id.name);
                    TextView emailText = (TextView) findViewById(R.id.email);
                    TextView phoneText = (TextView) findViewById(R.id.phone);
                    nameText.setText(jsonData.getString("first_name") + " " + jsonData.getString("last_name"));
                    emailText.setText(jsonData.getString("email"));
                    phoneText.setText(jsonData.getString("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.userDetail(handler, userId);

        // Get book owners
        HttpResponseHandler booksHandler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {

                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i< jsonData.getJSONArray("data").length(); i++) {
                        final String name = jsonData.getJSONArray("data").getJSONObject(i).getString("name");
                        final int bookId = jsonData.getJSONArray("data").getJSONObject(i).getInt("id");
                        String author = jsonData.getJSONArray("data").getJSONObject(i).getString("author");

                        TableRow tableRow = new TableRow(getBaseContext());

                        TextView nameView = new TextView(getBaseContext());
                        nameView.setText(name);
                        nameView.setTextSize(18);
                        nameView.setTextColor(Color.BLACK);

                        TextView authorView = new TextView(getBaseContext());
                        authorView.setText(author);
                        authorView.setTextSize(18);
                        authorView.setTextColor(Color.BLACK);
                        authorView.setPadding(200,0,0,0);

                        tableRow.addView(nameView);
                        tableRow.addView(authorView);

                        tableRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent("android.intent.action.BookDetail");
                                Bundle bundle = new Bundle();
                                bundle.putInt("book_id", bookId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });

                        booksTable.addView(tableRow);
                    }

                    if (jsonData.getJSONArray("data").length() == 0) {
                        TableRow tableRow = new TableRow(getBaseContext());
                        TextView notFoundView = new TextView(getBaseContext());
                        notFoundView.setText("The user does not have any books.");
                        tableRow.addView(notFoundView);
                        booksTable.addView(tableRow);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        api.userBooks(booksHandler, userId);
    }
}
