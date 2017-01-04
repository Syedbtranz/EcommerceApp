package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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


/**
 * Created by Sajid on 01/12/16.
 */
public class ProfileFragment extends Fragment {
    EditText emailET,pswET,forgetPswEt;
    TextView addressTxt,firstNameTxt,lastNameTxt, emailTxt, mobileTxt, editProfile, dobTxt, editAddress, viewOrders, viewWishlist;
    FragmentActivity activity;
    SpannableString s1;
    Button logout;
    ImageView backBtn;
    String profileResponse, firstName, lastName, dob, email,mobile, street, city, state, country, countrycode, postcode;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public  String userName,userEmail,userId,msg;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

        setHasOptionsMenu(true);
//        s1 = new SpannableString(getString(R.string.title_profile));
//        try {
//
//            s1.setSpan(new TypefaceSpan(activity, "hallo_sans_black.otf"), 0, s1.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }catch (Exception e){
//
//        }
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userID", "");
        userName = sharedpreferences.getString("userName", "");
        userEmail = sharedpreferences.getString("userEmail", "");
//        customerId = sharedpreferences.getString("password", "");
        getProfileInfo();
        getAddress();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewById(rootView);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

        @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_cart).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onResume() {
        super.onResume();
//        getProfileInfo();
//        getAddress();
        setData();

    }

    public  void   findViewById(View view){

        addressTxt=(TextView)view.findViewById(R.id.pr_address);
        firstNameTxt=(TextView)view.findViewById(R.id.pr_first_name);
        lastNameTxt=(TextView)view.findViewById(R.id.pr_last_name);
        emailTxt=(TextView)view.findViewById(R.id.pr_email);
        mobileTxt=(TextView)view.findViewById(R.id.pr_contact);
        dobTxt=(TextView)view.findViewById(R.id.pr_dob);
        editProfile=(TextView)view.findViewById(R.id.txtv_edit);
        editAddress=(TextView)view.findViewById(R.id.edit_address);
        viewOrders=(TextView)view.findViewById(R.id.view_orders);
        viewWishlist=(TextView)view.findViewById(R.id.view_wishlist);
        logout=(Button) view.findViewById(R.id.logout_btn);

//        final Toolbar mToolbar= ((SecondActivity)activity).mToolbar;
        final Toolbar mToolbar= (Toolbar)view.findViewById(R.id.toolbar);
        final  TextView toolTitle=(TextView)mToolbar.findViewById(R.id.toolbar_txt);
        ((AppCompatActivity)activity).setSupportActionBar(mToolbar);
        SpannableString s = new SpannableString(getString(R.string.title_profile));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MrE", "home selected");
                activity.onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });
        //VIEW ALL ORDERS
        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(activity,SecondActivity.class);
                i.putExtra("key", TagName.ORDER_LIST);
                activity.startActivity(i);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }
        });
        //VIEW ALL WISHLIST
        viewWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iwish=new Intent(activity,SecondActivity.class);
                iwish.putExtra("key", TagName.WISH_ID);
                activity.startActivity(iwish);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }
        });
        //LOGOUT
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                activity.finish();
                Toast.makeText(activity, "LogOut success!", Toast.LENGTH_SHORT).show();
            }
        });
        //EDIT PROFILE
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        //EDIT ADDRESS
        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
    }
    public void editProfile(){
        Bundle arguments = new Bundle();
        Fragment fragment = null;
        arguments.putString("firstName",firstName);
        arguments.putString("lastName",lastName);
        arguments.putString("dob",dob);
        arguments.putString("email",email);
        arguments.putString("mobile",mobile);
        arguments.putString("street",street);
        arguments.putString("city",city);
        arguments.putString("countrycode",countrycode);
        arguments.putString("country",country);
        arguments.putString("postcode",postcode);
        // Start a new fragment
        fragment = new UpdateProfileFragment();
        fragment.setArguments(arguments);
        FragmentTransaction transaction = activity
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_credentials, fragment,
                UpdateProfileFragment.FRAGMENT_PROFILE_UPDATE);
        transaction.addToBackStack(UpdateProfileFragment.FRAGMENT_PROFILE_UPDATE);
        transaction.commit();
    }
    public void getProfileInfo(){
        class ProfileInfoAsync extends AsyncTask<String, Void, String> {
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
            protected void onPostExecute(String result){
                String response = result.trim();
                Log.e("s",response);
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
//                    JSONArray jsonArray=new JSONArray(response);
//                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
                            JSONArray jarr=jsonObject.optJSONArray("getaccountinfo");
                            JSONObject job1=jarr.optJSONObject(0);
                            JSONObject jobstat=job1.getJSONObject(TagName.TAG_STATUS);
                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                            String message1 = jobstat.optString(TagName.TAG_MSG);
                            if(status1==1) {
                            JSONObject job=job1.optJSONObject("accountinfo");
                            firstName=job.optString("firstname");
                            lastName=job.optString("lastname");
                            dob=job.optString("dob");
                            mobile=job.optString("telephone");
                            email=job.optString("email");

                        }

                        }else{
                            Toast.makeText(activity, "No Profile Found", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(activity, "Network Error.  Please try Again.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ProfileInfoAsync la = new ProfileInfoAsync();
        la.execute(Utils.instantgetProfileUrl+userId);

    }
    public void getAddress(){
        class AddressAsync extends AsyncTask<String, Void, String> {
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
            protected void onPostExecute(String result){
                String response = result.trim();
                Log.e("s",response);
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
//                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
                            JSONArray jarr = jsonObject.optJSONArray("getaddress");
                            JSONObject job1 = jarr.optJSONObject(0);
                            JSONObject jobstat = job1.getJSONObject(TagName.TAG_STATUS);
                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                            String message1 = jobstat.optString(TagName.TAG_MSG);
                            if (status1 == 1) {
                                JSONArray jarry = job1.getJSONArray("billingaddress");
                                JSONObject job = jarry.optJSONObject(0);
//                            String firstName=job.optString("firstname");
//                            String lastName=job.optString("lastname");
                                street = job.optString("street");
                                city = job.optString("city");
                                countrycode = job.optString("country_id");
                                country = job.optString("country_label");
                                postcode = job.optString("postcode");
//                            String telephone=job.optString("telephone");
                                setData();
//                            addressTxt.setText(street+" "+city+" "+countrycode+" "+postcode+".");
//                                lastNameTxt.setText(lastName);
//                                emailTxt.setText(email);
//                                dobTxt.setText(dob);
//                                mobileNoTxt.setText(mobileNo);
//                                emailTxt.setText(email);
//                                walletAmtTxt.setText(walletAmt);
//                                Toast.makeText(activity,jsonObject.optString(TagName.KEY_MSG),Toast.LENGTH_SHORT).show();
//                                activity.finish();
                            }else{
                                Toast.makeText(activity, "No Address Found, Please try Again.", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(activity, "No Address Found.. Please try Again.", Toast.LENGTH_SHORT).show();
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
                }
            }
        }

        AddressAsync la = new AddressAsync();
        la.execute(Utils.instantGetAddressUrl+userId);
    }
    public void setData(){
        firstNameTxt.setText(firstName);
        lastNameTxt.setText(lastName);
        emailTxt.setText(email);
        mobileTxt.setText(mobile);
        dobTxt.setText(dob);
        addressTxt.setText(street+" "+city+" "+country+" "+postcode+".");
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
