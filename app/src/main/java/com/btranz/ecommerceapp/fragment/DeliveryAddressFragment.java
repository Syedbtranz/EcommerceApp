package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.adapter.CountryAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ravi on 29/07/15.
 */
public class DeliveryAddressFragment extends Fragment {
    Button backBtn, nextBtn;
    EditText addressET, streetET, cityET, countryET,pincodeET;
    FragmentActivity activity;
    String userId,quoteId, deliveryType, shippingRate, customerStreet, customertCity,customertCountry, customerPincode;
    int countryPosition;
    Spinner countrySpinner;
    public CountryAdapter countryAdp;
    ArrayList<ProductModel> countryList;//=new ArrayList<>();
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public DeliveryAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        userId = sharedpreferences.getString("userID", "");
        quoteId = sharedpreferences.getString("quoteId", "");
        customerStreet = sharedpreferences.getString("checkoutCustomerStreet", "");
        customertCity = sharedpreferences.getString("checkoutCustomerCity", "");
        customertCountry = sharedpreferences.getString("checkoutCustomerCountry", "");
        customerPincode = sharedpreferences.getString("checkoutCustomerPincode", "");
//        userAddress = sharedpreferences.getString("checkoutUserStreet", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_address, container, false);
        streetET=(EditText) rootView.findViewById(R.id.street_et);
        cityET=(EditText) rootView.findViewById(R.id.city_et);
//        countryET=(EditText) rootView.findViewById(R.id.address_et);
        pincodeET=(EditText) rootView.findViewById(R.id.pincode_et);

        streetET.setText(customerStreet);
        cityET.setText(customertCity);
        pincodeET.setText(customerPincode);

        backBtn=(Button)rootView.findViewById(R.id.back_btn);
        nextBtn=(Button)rootView.findViewById(R.id.next_btn);
//        addressET.setText(userAddress);
        // Country Spinner element
         countrySpinner = (Spinner) rootView.findViewById(R.id.spinner_country);

        // Spinner click listener
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                ProductModel p=countryList.get(position);
                countryPosition=position;
//                editor.putString("checkoutCustomerDeliveryType", item);
//                editor.commit();
                editor.putString("checkoutCustomerCountry",  p.getTitle());
                editor.putString("checkoutCustomerCountryCode",  p.getSubTitle());
                editor.commit();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + p.getSubTitle(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Creating adapter for spinner
//        countryAdp = new CountryAdapter(activity, R.layout.country_list_inflate, countryList);
//        // attaching data adapter to spinner
//        countrySpinner.setAdapter(countryAdp);
//        // Drop down layout style - list view with radio button
//        countryAdp.notifyDataSetChanged();



        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                editor.putString("checkoutCustomerDeliveryType", item);
                editor.commit();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Standard Delivery");
        categories.add("Express Delivery");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //Action  Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        //Action  Next Button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(countryList==null){
            getCountryList();
        }else{
            countryAdp = new CountryAdapter(activity, R.layout.country_list_inflate, countryList);

            // attaching data adapter to spinner
            countrySpinner.setAdapter(countryAdp);
            countryAdp.notifyDataSetChanged();
        }

    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;
        Log.e("spinner",""+spinner.getCount());

        for (int i=0;i<spinner.getCount();i++){
            ProductModel m=countryList.get(i);
            if (m.getTitle().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    public  void sendData(){
     customerStreet=streetET.getText().toString();
     customertCity=cityET.getText().toString();
     customerPincode=pincodeET.getText().toString();
    if(customerStreet.equals("")&&customertCity.equals("")&&customerPincode.equals("")) {
        Toast.makeText(activity,"Please Enter the Details",Toast.LENGTH_SHORT).show();
        streetET.requestFocus();
    }else if(customerStreet.equals("")){
            Toast.makeText(activity,"Please Enter the Street",Toast.LENGTH_SHORT).show();
            streetET.requestFocus();
        }else if(customertCity.equals("")){
            Toast.makeText(activity,"Please Enter the City",Toast.LENGTH_SHORT).show();
            cityET.requestFocus();
        }else if(countryPosition==0){
        Toast.makeText(activity,"Please Select the Country",Toast.LENGTH_SHORT).show();
        cityET.requestFocus();
    }else if(customerPincode.equals("")){
            Toast.makeText(activity,"Please Enter the Pincode",Toast.LENGTH_SHORT).show();
            pincodeET.requestFocus();
        }else if(!customerStreet.equals("")&&!customertCity.equals("")&&countryPosition!=0&&!customerPincode.equals("")){
            editor.putString("checkoutCustomerStreet", customerStreet);
            editor.putString("checkoutCustomerCity", customertCity);
            editor.putString("checkoutCustomerPincode", customerPincode);
            editor.commit();
            delivaryTypeCheck();
            Bundle arguments = new Bundle();
            Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

            // Start a new fragment
            fragment = new BillingFragment();
//                fragment.setArguments(arguments);

            FragmentTransaction transaction = activity
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_booknow, fragment,
                    TagName.FRAGMENT_USER_BILLING);
            transaction.addToBackStack(TagName.FRAGMENT_USER_BILLING);
            transaction.commit();

    }
}
    public void getCountryList(){
        class CountryListAsync extends AsyncTask<String, Void, String> {
            Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
//                String oldpsw = params[0];
//                String newpsw = params[1];
//                Log.e("uname",oldpsw);
//                Log.e("pass",newpsw);
                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;
                HttpURLConnection urlConnection = null;
                try{
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpPost = new HttpGet( Utils.instantChangePswUrl+userId+"&oldpassword="+oldpsw+"&newpassword="+newpsw);
////                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    is = entity.getContent();

                    URL url = new URL(params[0]);

                    urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                    urlConnection.setRequestMethod("GET");

                    int statusCode = urlConnection.getResponseCode();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                String response = result.trim();
                Log.e("s", response);
                loadingDialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject != null) {
                        JSONObject job = jsonObject.optJSONObject(TagName.TAG_STATUS);
                        String status = job.optString(TagName.TAG_MSG);
                        if (status.equalsIgnoreCase("success")) {
                            if (countryList == null) {
                                countryList = new ArrayList<ProductModel>();
                            }
                            JSONArray jarray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject job1 = jarray.optJSONObject(i);
                                ProductModel item = new ProductModel();

                                item.setTitle(job1.optString("label"));
                                item.setSubTitle(job1.optString("value"));
//                                    Log.e("job1.optString(\"label\")", job1.optString("label"));
                                countryList.add(item);

                            }
                            // Creating adapter for spinner
                            countryAdp = new CountryAdapter(activity, R.layout.country_list_inflate, countryList);

                            // Drop down layout style - list view with radio button
//        dataAdapter.(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            countrySpinner.setAdapter(countryAdp);
                            countryAdp.notifyDataSetChanged();
                            countrySpinner.setSelection(getIndex(countrySpinner, customertCountry));
                        }

//

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        CountryListAsync la = new CountryListAsync();
        la.execute(Utils.getCountryListUrl);

    }
    private void delivaryTypeCheck(){
        deliveryType = sharedpreferences.getString("checkoutCustomerDeliveryType", "");
        if(deliveryType.equalsIgnoreCase("standard delivery")){
            shippingRate="freeshipping_freeshipping";
        }else{
            shippingRate="flatrate_flatrate";
        }
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(activity);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.deliveryTypeUrl+userId+"/"+quoteId+"/"+shippingRate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);

                   /* try {
                        JSONObject jsonObject=new JSONObject(response);
                        if(jsonObject!=null) {
                            String status=jsonObject.optString(TagName.TAG_STATUS);
                            if(status.equalsIgnoreCase("success")){
//                                String status=jsonObject.optString(TagName.KEY_MSG);
                                Toast.makeText(activity,jsonObject.optString(TagName.KEY_MSG),Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
//                            JSONObject job=jsonObject.optJSONObject(TagName.TAG_DATA);
//                            editor = sharedpreferences.edit();
//                            editor.putString("userId", job.optString("user_id"));
//                            editor.putString("userName", job.optString("user_name"));
////                            editor.putString("password", jsonObject.optString("password"));
////                            editor.putString("userName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(activity,"Following detais are incorrect",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

             /*   @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }*/

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response);
//                        Log.e(" response.data", response);

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
