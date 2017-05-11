package com.example.sekercan.sharingbookapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Tab1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        final SearchView search = (SearchView) findViewById(R.id.srch);
        final TextView textView = (TextView) findViewById(R.id.headerText);
        textView.setText("Books");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item);
        final ListView bookList = (ListView) findViewById(R.id.book_list);
        bookList.setAdapter(adapter);

        final ArrayList<Integer> bookIds = new ArrayList<Integer>();

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int bookId = bookIds.get(position);
                Intent intent = new Intent("android.intent.action.BookDetail");
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        final HttpResponseHandler handler = new HttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonData) {
                try {
                    ArrayList<String> list = new ArrayList<String>();
                    adapter.clear();
                    for (int i = 0; i< jsonData.getJSONArray("data").length(); i++) {
                        String name = jsonData.getJSONArray("data").getJSONObject(i).getString("name");
                        String author = jsonData.getJSONArray("data").getJSONObject(i).getString("author");
                        int id = jsonData.getJSONArray("data").getJSONObject(i).getInt("id");
                        list.add(name + "\n" + author);
                        bookIds.add(id);
                    }
                    adapter.addAll(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api();
        api.books(handler);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sendSearch(newText);
                return true;
            }

            public void sendSearch(String query) {
                Api api = new Api();
                api.searchBook(handler, query);
            }

        });
    }

    public void searchListener(View v) {
        System.out.println("TEST");
    }
}
