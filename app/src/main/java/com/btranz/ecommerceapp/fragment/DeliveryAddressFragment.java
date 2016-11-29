package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ravi on 29/07/15.
 */
public class DeliveryAddressFragment extends Fragment {
    Button backBtn, nextBtn;
    EditText addressET, streetET, cityET, countryET,pincodeET;
    FragmentActivity activity;
    String userId,quoteId, deliveryType, shippingRate, customerStreet, customertCity,customerPincode;
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
        }else if(customerPincode.equals("")){
            Toast.makeText(activity,"Please Enter the Pincode",Toast.LENGTH_SHORT).show();
            pincodeET.requestFocus();
        }else if(!customerStreet.equals("")&&!customertCity.equals("")&&!customerPincode.equals("")){
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
    private void delivaryTypeCheck(){
        deliveryType = sharedpreferences.getString("checkoutCustomerDeliveryType", "");
        if(deliveryType.equalsIgnoreCase("standard delivery")){
            shippingRate="freeshipping_freeshipping";
        }else{
            shippingRate="flatrate_flatrate";
        }
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            String URL = "http://...";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("reference_id", "1");
//            jsonBody.put("service_id", "1");
//            jsonBody.put("client_id", userId);
//            jsonBody.put("service_type", "1");
//            final String mRequestBody = jsonBody.toString();

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
