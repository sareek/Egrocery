package com.knight.egrocery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samir on 8/20/2016.
 */
public class Search extends Activity {
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
    String url_all_products = "http://192.168.173.1/get_product_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "product";
    private static final String TAG_PID = "Id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_URL = "Url";
    private static final String TAG_PRICE = "Price";
    // products JSONArray
    JSONArray products;
    EditText search;
    String text = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("Search");
        search =(EditText)findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomList(this, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
                if (intArray[position] == 1) {
                    view.setBackgroundColor(Color.WHITE);
                    intArray[position] = 0;
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF70ED8C"));
                    intArray[position] = 1;


                }
            }
        });

    }


    public void invokeSearch(View arg0) {

        text = search.getText().toString();


        new LoadAllProducts().execute();

    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters

            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_NAME, text));


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


                    JSONObject d = products.getJSONObject(0);
                    Details detail = new Details();

                    // Storing each json item in variable


                    String id = d.getString(TAG_PID);

                    detail.setName(d.getString(TAG_NAME));
                    detail.setThumbnailUrl(d.getString(TAG_URL));
                    detail.setPrice(d.getInt(TAG_PRICE));

                    itemList.add(detail);

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
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            adapter.notifyDataSetChanged();
            intArray = new int[adapter.getCount()];
            // updating UI from Background Thread

        }

    }
}
