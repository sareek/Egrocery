package com.knight.egrocery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private List<Details> itemList = new ArrayList<Details>();
    List<Details> wishList = new ArrayList<Details>();
    private ListView listView;
    private CustomList adapter;
    int[] intArray;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();



    // url to get all products list
    private static String url_all_products = "http://192.168.173.1/getData.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "items";
    private static final String TAG_PID = "Id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_URL = "Url";
    private static final String TAG_PRICE = "Price";
    // products JSONArray
    JSONArray products = null;

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
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
                if(intArray[position]==1)
                {
                    view.setBackgroundColor(Color.WHITE);
                    intArray[position]=0;
                }
                else {
                    view.setBackgroundColor(Color.parseColor("#FF70ED8C"));
                    intArray[position] = 1;


                }}
        });
        // on seleting single product
        // launching Edit Product Screen


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
            pDialog = new ProgressDialog(MainActivity.this);
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
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse


            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        Details detail = new Details();
                        detail.setName(c.getString(TAG_NAME));
                        detail.setThumbnailUrl(c.getString(TAG_URL));
                        detail.setPrice(c.getInt(TAG_PRICE));
                        itemList.add(detail);


                    }
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
            intArray = new int[listView.getAdapter().getCount()];
            // updating UI from Background Thread

        }

    }

}