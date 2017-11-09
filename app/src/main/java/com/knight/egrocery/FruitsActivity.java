package com.knight.egrocery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class FruitsActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private List<Details> itemList = new ArrayList<Details>();
    List<Details> wishList = new ArrayList<Details>();
    private ListView listView;
    private CustomList adapter;
    int[] intArray;
    int success;
    List<NameValuePair> params;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();



    // url to get all products list
    String url_all_products = "http://192.168.173.1/getData1.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "fruitsandveg";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "Name";
    private static final String TAG_URL = "Url";
    private static final String TAG_PRICE = "Price";
    // products JSONArray
    JSONArray products = null;
    String pid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("List");



        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomList(this, itemList);

        listView.setAdapter(adapter);









        // Loading products in Background Thread
        new LoadAllProducts().execute();

listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rid) {
                // getting values from selected ListItem

                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        ProductActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);


                }
        });
        // on seleting single product
        // launching Edit Product Screen


    }
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }






    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FruitsActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters

             params = new ArrayList<NameValuePair>();




            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse


            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                    // products found
                    // Getting Array of Products
                         products = json.getJSONArray(TAG_PRODUCTS);
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);
                            Details detail = new Details();
                            // Storing each json item in variable



                            detail.setId(c.getInt(TAG_PID));
                            detail.setName(c.getString(TAG_NAME));
                            detail.setThumbnailUrl(c.getString(TAG_URL));
                            detail.setPrice(c.getInt(TAG_PRICE));

                            itemList.add(detail);


                    }
                    // looping through All Products

                } else {
                    // no products found
                    // Launch Add New product Activity

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            adapter.notifyDataSetChanged();
            intArray = new int[adapter.getCount()];
            // updating UI from Background Thread

        }

    }

}