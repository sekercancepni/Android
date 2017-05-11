package com.example.sekercan.sharingbookapplication;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TabHost;

public class homePage extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        TabHost tabh = (TabHost)findViewById(android.R.id.tabhost);
        TabHost.TabSpec tab1 = tabh.newTabSpec("tab menü 1. seçenek");
        TabHost.TabSpec tab2 = tabh.newTabSpec("tab menü 2. seçenek");
        TabHost.TabSpec tab3 = tabh.newTabSpec("tab menü 3. seçenek");
        TabHost.TabSpec tab4 = tabh.newTabSpec("tab menü 4. seçenek");
        TabHost.TabSpec tab5 = tabh.newTabSpec("tab menü 5. seçenek");

        tab1.setContent(new Intent(this,Tab1.class));
        Drawable drawable1 = ContextCompat.getDrawable(this,R.drawable.book1112);
        tab1.setIndicator("",drawable1);
        Drawable drawable2 = ContextCompat.getDrawable(this,R.drawable.booktextadd512);
        tab2.setIndicator("",drawable2);
        tab2.setContent(new Intent(this,Tab2.class));
        Drawable drawable3 = ContextCompat.getDrawable(this,R.drawable.usermale12);
        tab3.setIndicator("",drawable3);
        tab3.setContent(new Intent(this,Tab3.class));
        Drawable drawable4 = ContextCompat.getDrawable(this,R.drawable.booktextadd512);
        tab4.setIndicator("",drawable4);
        tab4.setContent(new Intent(this,Tab4.class));
        Drawable drawable5 = ContextCompat.getDrawable(this,R.drawable.usermale12);
        tab5.setIndicator("",drawable5);
        tab5.setContent(new Intent(this,Tab5.class));
        tabh.addTab(tab1); tabh.addTab(tab2); tabh.addTab(tab3);tabh.addTab(tab4);tabh.addTab(tab5);
    }
}
