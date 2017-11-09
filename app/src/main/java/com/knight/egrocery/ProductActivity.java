package com.knight.egrocery;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class ProductActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    NetworkImageView thumbNail;
    List<NameValuePair> params;

    TextView Name;
    TextView Price;
    TextView descrp;
    JSONArray products = null;
    JSONObject json;
   String pid;
    EditText quantity;
    Button addtotcart;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    // url to create new product
    private static String url_product_details = "http://192.168.173.1/get_product_details.php";

    // JSON parser class
    JSONParser jsonParser = new JSONParser();



    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "fruitsandveg";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "Name";
    private static final String TAG_PRICE = "Price";
    private static final String TAG_URL = "Url";
    private static final String TAG_DESCRIPTION = "Description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Edit Text

        addtotcart = (Button) findViewById(R.id.addcart);



        // Getting complete product details in background thread
        new GetProductDetails().execute();

    }

        // save button click event


        // Delete button click event


    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        // getting product details from intent
                        Intent i = getIntent();

                        // getting product id (pid) from intent
                        pid = i.getStringExtra(TAG_PID);
                        Toast.makeText(ProductActivity.this, pid , Toast.LENGTH_LONG).show();

                        params.add(new BasicNameValuePair("pid", pid));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // product with this pid found
                            Name = (TextView) findViewById(R.id.name);
                            Price = (TextView) findViewById(R.id.price);
                            descrp = (TextView) findViewById(R.id.desc);
                            thumbNail = (NetworkImageView)findViewById(R.id.thumbnail);

                            // display product data in EditText

                            if (imageLoader == null)
                                imageLoader = AppController.getInstance().getImageLoader();

                            // display product data in EditText
                            Name.setText(product.getString(TAG_NAME));
                            Price.setText(product.getString(TAG_PRICE));
                            descrp.setText(product.getString(TAG_DESCRIPTION));
                            thumbNail.setImageUrl(product.getString(TAG_URL), imageLoader);


                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

}
