package com.knight.egrocery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Samir on 6/19/2016.
 */
public class Mainmenu extends Activity {
    //Internet status flag
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("Egrocery");
        tb.setLogo(R.drawable.ic_launcher);
        pref = getSharedPreferences("UserPref", MODE_PRIVATE);
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://192.168.173.1/slide.php");

        Button b1=(Button)findViewById(R.id.button1);
        Button b2=(Button)findViewById(R.id.button2);
        pref = getSharedPreferences("UserPref", MODE_PRIVATE);
        edit = pref.edit();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creating connection detector class instance

                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });

    b2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // creating connection detector class instance

            Intent in = new Intent(getApplicationContext(), FruitsActivity.class);
            startActivity(in);
        }
    });
}
    public void invokeSearch(View arg0) {
        Intent in = new Intent(getApplicationContext(), Search.class);
        startActivity(in);
    }
    public void invokeLogout(View arg0) {

        // Get text from email and passord field
    edit.putBoolean("Lstatus",false);
        edit.commit();
        Intent intent = new Intent(Mainmenu.this,LoginActivity.class);
        startActivity(intent);
        Toast.makeText(Mainmenu.this, "Logged Out", Toast.LENGTH_LONG).show();
        Mainmenu.this.finish();


    }
}
