package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BookDetail extends Activity {
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);

        final TextView textView = (TextView) findViewById(R.id.headerText);
        textView.setText("Book Detail");

        this.setParameters();

        final TextView bookName = (TextView) findViewById(R.id.book_name);
        final TextView summary = (TextView) findViewById(R.id.summary);
        final TextView authorName = (TextView) findViewById(R.id.author_name);
        final TextView point = (TextView) findViewById(R.id.point);
        final TableLayout owners = (TableLayout) findViewById(R.id.owners);
        final ImageView img = (ImageView) findViewById(R.id.book_image);

        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    bookName.setText(jsonData.getString("name"));
                    summary.setText(jsonData.getString("summary"));
                    authorName.setText(jsonData.getString("author"));
                    point.setText(jsonData.getString("point") + " Point");

                    String imageUrl = jsonData.getString("image");
                    if (imageUrl!=null) {
                        Picasso.with(getBaseContext()).load(imageUrl).into(img);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.bookDetail(handler, bookId);

        HttpResponseHandler ownersHandler = new HttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {

                try {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i< jsonData.getJSONArray("data").length(); i++) {
                        final String firstName = jsonData.getJSONArray("data").getJSONObject(i).getString("first_name");
                        final int userId = jsonData.getJSONArray("data").getJSONObject(i).getInt("user_id");
                        String lastName = jsonData.getJSONArray("data").getJSONObject(i).getString("last_name");
                        String email = jsonData.getJSONArray("data").getJSONObject(i).getString("email");

                        TableRow tableRow = new TableRow(getBaseContext());

                        TextView nameView = new TextView(getBaseContext());
                        nameView.setText(firstName + " " + lastName);
                        nameView.setTextSize(18);
                        nameView.setTextColor(Color.BLACK);
                        nameView.setPadding(30,0,0,0);

                        TextView emailView = new TextView(getBaseContext());
                        emailView.setText(email);
                        emailView.setTextSize(18);
                        emailView.setTextColor(Color.BLACK);
                        emailView.setPadding(200,0,0,0);

                        tableRow.addView(nameView);
                        tableRow.addView(emailView);

                        tableRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent("android.intent.action.UserDetail");
                                Bundle bundle = new Bundle();
                                bundle.putInt("user_id", userId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });

                        owners.addView(tableRow);
                    }

                    if (jsonData.getJSONArray("data").length() == 0) {
                        TableRow tableRow = new TableRow(getBaseContext());
                        TextView notFoundView = new TextView(getBaseContext());
                        notFoundView.setText("This book is not available to users.");
                        notFoundView.setTextColor(Color.BLACK);
                        notFoundView.setTextSize(18);
                        tableRow.addView(notFoundView);
                        owners.addView(tableRow);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        api.bookOwners(ownersHandler, bookId);
    }

    private void setParameters() {
        Bundle bundle = getIntent().getExtras();
        this.bookId = bundle.getInt("book_id");
    }

    public void setOwnerListener(View v) {
        int userId = LoggedInUser.id;
        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    String message = jsonData.getString("message");
                    String userMessage;

                    if (message.equals("already_exist")) {
                        userMessage = "You already owns this book.";
                    } else {
                        userMessage = "This book added to you";
                    }
                    Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.setOwner(handler, this.bookId, userId);
    }

    public void removeOwnerListener(View v) {
        int userId = LoggedInUser.id;

        HttpResponseHandler handler = new HttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    String message = jsonData.getString("message");
                    String userMessage;

                    if (message.equals("not_exist")) {
                        userMessage = "This book not exist.";
                    } else {
                        userMessage = "This book remove from you";
                    }
                    Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.removeOwner(handler, this.bookId, userId);
    }
}


