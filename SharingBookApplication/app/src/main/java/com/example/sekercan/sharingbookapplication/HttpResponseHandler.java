package com.example.sekercan.sharingbookapplication;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class HttpResponseHandler extends JsonHttpResponseHandler {
    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        System.out.println(statusCode);
    }
}
