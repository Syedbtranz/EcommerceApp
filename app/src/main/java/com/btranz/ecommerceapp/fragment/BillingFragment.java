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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ravi on 29/07/15.
 */
public class BillingFragment extends Fragment {
    Button backBtn, nextBtn, promoApplyBtn;
    LinearLayout cardLayout;
    EditText cardNoET,expiryET, cvvET,nameET, promoET;
    String cardNo, expiry, cvv, name, promo;
    FragmentActivity activity;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    String userId, quoteId;
    int paymentMode;
    public BillingFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_billing, container, false);
        cardLayout=(LinearLayout) rootView.findViewById(R.id.card_layout);
        cardNoET=(EditText) rootView.findViewById(R.id.card_no_et);
        expiryET=(EditText) rootView.findViewById(R.id.expiry_et);
        cvvET=(EditText) rootView.findViewById(R.id.cvv_et);
        nameET=(EditText) rootView.findViewById(R.id.name_et);
        promoET=(EditText) rootView.findViewById(R.id.prome_et);

        backBtn=(Button)rootView.findViewById(R.id.back_btn);
        nextBtn=(Button)rootView.findViewById(R.id.next_btn);
        promoApplyBtn=(Button)rootView.findViewById(R.id.promo_apply_btn);
        promoApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promo=promoET.getText().toString();
                getPromoApply(userId,quoteId,promo);

            }
        });
        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                paymentMode=position;
//                editor.putString("checkoutUserPaymentMode", item);
//                editor.commit();
                if(position==0){
                    cardLayout.setVisibility(View.GONE);
                }else{

                    cardLayout.setVisibility(View.VISIBLE);
                }

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("COD");
        categories.add("Card");

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
                if(paymentMode==0){
                    editor.putString("checkoutCustomerPaymentMode", "cashondelivery");
                    editor.commit();
                    Bundle arguments = new Bundle();
                    Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

                    // Start a new fragment
                    fragment = new CheckOutFragment();
//                fragment.setArguments(arguments);

                    FragmentTransaction transaction = activity
                            .getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_booknow, fragment,
                            TagName.FRAGMENT_USER_CHECKOUT);
                    transaction.addToBackStack(TagName.FRAGMENT_USER_CHECKOUT);
                    transaction.commit();
                }else {
                    sendData();
                }

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    private void getPromoApply(String userid,String quoteid,String promo) {

        class PromoApplyAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String userid = params[0];
                String quoteid = params[1];
                String promo = params[2];
                InputStream inputStream = null;
                String result= null;;
                HttpURLConnection urlConnection = null;

                try {
                /* forming th java.net.URL object */
                    URL url = new URL(Utils.promoApplyUrl+userid+"/"+quoteid+"/"+promo);
                    Log.e("URL", Utils.promoApplyUrl+userid+"/"+quoteid+"/"+promo);

                    urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                    urlConnection.setRequestMethod("GET");

                    int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                    if (statusCode ==  200) {

                        BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }
                        Log.e("response.toString()", response.toString());
//                        parseResult(response.toString());
                        result = response.toString(); // Successful
                    }else{
                        result = null; //"Failed to fetch data!";
                    }

                } catch (Exception e) {
                    Log.d("catch", e.getLocalizedMessage());
                }

                return result; //"Failed to fetch data!";

//                String pid = params[0];
//                String quant = params[1];
//                String userid = params[2];
////                Log.e("uname",uname);
////                Log.e("pass",pass);
//                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
////                nameValuePairs.add(new BasicNameValuePair("username", uname));
////                nameValuePairs.add(new BasicNameValuePair("password", pass));
//                String result = null;
//
//                try{
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpPost = new HttpGet(Utils.addtocartUrl+pid+"/"+quant+"/"+userid);
////                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    is = entity.getContent();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line + "\n");
//                    }
//                    result = sb.toString();
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                Log.e("s",s);
                loadingDialog.dismiss();
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

//                        if (status==1) {

//                            String quoteId=jsonObject.optString("quoteid");
//                            Toast.makeText(activity, "Added "+quoteId, Toast.LENGTH_LONG).show();
////            boolean status = response.getBoolean(TagName.TAG_STATUS);
                if(message.equalsIgnoreCase("success")){
//                            editor.putString("quoteid",quoteId);
//                            editor.commit();
                    Toast.makeText(activity, "promo applied successfully", Toast.LENGTH_LONG).show();
//                            JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);
//                            editor = sharedpreferences.edit();
//                            editor.putString("customerID", jobcust.optString("id"));
//                            editor.putString("customerEmail", jobcust.optString("username"));
//                            editor.putString("password", jobcust.optString("password"));
////                          editor.putString("customerName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();
//                            activity.finish();
//                            Intent in=new Intent(getActivity(), BookNowActivity.class);
//                            in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
//                            activity.startActivity(in);
//                            activity.overridePendingTransition(android.R.anim.fade_in,
//                                    android.R.anim.fade_out);
                        }else {
                            Toast.makeText(activity, "Invalid Promo code", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        PromoApplyAsync la = new PromoApplyAsync();
        la.execute(userid, quoteid, promo);

    }
    public void sendData(){
        cardNo=cardNoET.getText().toString();
        expiry=expiryET.getText().toString();
        cvv =cvvET.getText().toString();
        name=nameET.getText().toString();

//        Log.e("subject",subject);
//        Log.e("desc ",description);
//        Log.e("issueType",issueType);
//        Log.e("emailMeStr",emailMeStr);
        if (cardNo.equals("") &&expiry.equals("") && cvv.equals("")
                && name.equals("")) {
//            Log.i("k1", "null");
            Toast.makeText(activity,
                    "Please enter the details", Toast.LENGTH_SHORT).show();
            cardNoET.requestFocus();
        } else if (cardNo.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD NO", Toast.LENGTH_SHORT).show();
            cardNoET.requestFocus();
        }  else if (expiry.equals("")) {
            Toast.makeText(activity,
                    "Please enter the EXPIRY DATE", Toast.LENGTH_SHORT).show();
            expiryET.requestFocus();
        } else if (cvv.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CVV CODE", Toast.LENGTH_SHORT).show();
            cvvET.requestFocus();
        } else if (name.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD HOLDER NAME", Toast.LENGTH_SHORT).show();
            nameET.requestFocus();
        } else if (!cardNo.equals("")&&!expiry.equals("") &&!cvv.equals("")
                && !name.equals("")) {

            Bundle arguments = new Bundle();
            Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

            // Start a new fragment
            fragment = new CheckOutFragment();
//                fragment.setArguments(arguments);

            FragmentTransaction transaction = activity
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_booknow, fragment,
                    TagName.FRAGMENT_USER_CHECKOUT);
            transaction.addToBackStack(TagName.FRAGMENT_USER_CHECKOUT);
            transaction.commit();
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
