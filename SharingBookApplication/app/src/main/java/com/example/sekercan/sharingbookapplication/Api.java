package com.example.sekercan.sharingbookapplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


public class Api {
    private final String BASE_URL = "http://10.0.2.2:8080/RestfulService/webservice/";

    private final AsyncHttpClient client = new AsyncHttpClient();

    public void signupRequest(HttpResponseHandler handler, RequestParams params) {
        String url = this.BASE_URL + "users/signup/";
        client.get(url, params, handler);
    }

    public void doLogin(HttpResponseHandler handler, String email, String password) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        String url = this.BASE_URL + "users/login/";
        client.get(url, params, handler);
    }

    public void saveUserDetail(HttpResponseHandler handler, int id, String firstName, String lastName,
                               String email, String phone, String password, String newPassword) {
        RequestParams params = new RequestParams();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", email);
        params.put("phone", phone);
        params.put("password", password);
        params.put("new_password", newPassword);

        String url = this.BASE_URL + "users/" + id + "/update";
        client.get(url, params, handler);
    }

    public void userDetail(HttpResponseHandler handler, int userId) {
        String url = this.BASE_URL + "users/" + userId + "/";
        client.get(url, null, handler);
    }

    public void userBooks(HttpResponseHandler handler, int userId) {
        String url = this.BASE_URL + "users/" + userId + "/books";
        client.get(url, null, handler);
    }

    public void removeOwner(HttpResponseHandler handler, int bookId, int userId) {
        String url = this.BASE_URL + "books/" + bookId + "/remove_owner/" + userId;
        client.get(url, null, handler);
    }

    public void setOwner(HttpResponseHandler handler, int bookId, int userId) {
        String url = this.BASE_URL + "books/" + bookId + "/owner/" + userId;
        client.get(url, null, handler);
    }

    public void bookDetail(HttpResponseHandler handler, int bookId) {
        String url = this.BASE_URL + "books/" + bookId;
        client.get(url, null, handler);
    }

    public void bookOwners(HttpResponseHandler handler, int bookId) {
        String url = this.BASE_URL + "books/" + bookId + "/owners/";
        client.get(url, null, handler);
    }

    public void books(HttpResponseHandler handler) {
        String url = this.BASE_URL + "books/";
        client.get(url, null, handler);
    }

    public void addBook(HttpResponseHandler handler, int id, String bookName, String authorName){
        String url = this.BASE_URL + "books/new_request/?name=" + bookName + "&author=" + authorName + "&user_id=" + id;
        client.get(url, null, handler);
    }

    public void forgot(HttpResponseHandler handler, String email){
        String url = this.BASE_URL + "users";
        client.get(url, null, handler);
    }

    public void sendPasswordMail(HttpResponseHandler handler, String email) {
        String url = this.BASE_URL + "users/remember_password/";
        RequestParams params = new RequestParams();
        params.put("email", email);
        client.get(url, params, handler);
    }

    public void searchBook(HttpResponseHandler handler, String query) {
        String url = this.BASE_URL + "books/";
        RequestParams params = new RequestParams();
        params.put("query", query);
        client.get(url, params, handler);
    }
}
