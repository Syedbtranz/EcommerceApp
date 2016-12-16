package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.adapter.CountryAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Sajid on 01/12/16.
 */
public class UpdateProfileFragment extends Fragment {
    private Calendar cal;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    int year, month, day;
    Button profileSubmitBtn, updateAddressBtn;
    Switch accountSwitch;
    EditText firstNameEd, lastNameEd, emailEd, mobileEd, street1ED,street2ED, cityED, stateED, countryED, countrycodeED, postcodeED;
    String firstName, lastName, dob, email, mobile, street, city, state, country, countrycode, postcode;
    FragmentActivity activity;
    TextView changPwd, dobEd;
    Spinner countrySpinner;
    int countryPosition;
    CountryAdapter countryAdp;
    ArrayList<ProductModel> countryList;
    public static final String FRAGMENT_PROFILE_UPDATE= "fragment_profile_update";
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public  String userName,userEmail,userId,msg;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        Bundle b=this.getArguments();
        if(b!=null){
            firstName=b.getString("firstName");
            lastName=b.getString("lastName");
            dob=b.getString("dob");
            email=b.getString("email");
            mobile=b.getString("mobile");
            street=b.getString("street");
            city=b.getString("city");
            countrycode=b.getString("countrycode");
            country=b.getString("country");
            postcode=b.getString("postcode");
        }
        setHasOptionsMenu(true);
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        userId = sharedpreferences.getString("userID", "");
//        userName = sharedpreferences.getString("userName", "");
//        userEmail = sharedpreferences.getString("userEmail", "");
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_profile, container, false);
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
        if(countryList==null){
            getCountryList();
        }else{
            countryAdp = new CountryAdapter(activity, R.layout.country_list_inflate, countryList);

            // attaching data adapter to spinner
            countrySpinner.setAdapter(countryAdp);
            countryAdp.notifyDataSetChanged();
        }
    }

    public void findViewById(View view){
        final Toolbar mToolbar= (Toolbar)view.findViewById(R.id.toolbar);
        final TextView toolTitle=(TextView)mToolbar.findViewById(R.id.toolbar_txt);
        ((AppCompatActivity)activity).setSupportActionBar(mToolbar);
        SpannableString s = new SpannableString(getString(R.string.title_profile_details));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });

        firstNameEd=(EditText) view.findViewById(R.id.input_f_name);
        lastNameEd=(EditText) view.findViewById(R.id.input_last_name);
        dobEd=(TextView) view.findViewById(R.id.date_edit);
         emailEd=(EditText) view.findViewById(R.id.input_email_id);
        mobileEd=(EditText) view.findViewById(R.id.input_mobile_number);
        street1ED=(EditText) view.findViewById(R.id.input_address_one);
        street2ED=(EditText) view.findViewById(R.id.input_address_two);
        cityED=(EditText) view.findViewById(R.id.input_address_three);
//        countryED=(EditText) view.findViewById(R.id.input_address_country);
        postcodeED=(EditText) view.findViewById(R.id.input_address_postcode);

        changPwd=(TextView)view.findViewById(R.id.txtv_change_pwd);
        profileSubmitBtn=(Button)view.findViewById(R.id.edit_profile_submit_btn);
        updateAddressBtn=(Button)view.findViewById(R.id.update_address_btn);

        accountSwitch=(Switch) view.findViewById(R.id.account_switch);

        firstNameEd.setText(firstName);
        lastNameEd.setText(lastName);
        dobEd.setText(dob);
        emailEd.setText(email);
        mobileEd.setText(mobile);
        street1ED.setText(street);
        street2ED.setText(city);
//        countryED.setText(country);
        postcodeED.setText(postcode);
        // Country Spinner element
        countrySpinner = (Spinner) view.findViewById(R.id.spinner_country);

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
                countrycode=p.getSubTitle();
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

        //DATE SET
        dobEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dd = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    Date date = formatter.parse(dateInString);

                                    //txtdate.setText(formatter.format(date).toString());

//                                    formatter = new SimpleDateFormat("yyyy-MM-dd");

                                    dobEd.setText(formatter.format(date).toString());

                                   /* formatter = new SimpleDateFormat("dd-MM-yyyy");

                                    //  txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                    formatter = new SimpleDateFormat("dd.MMM.yyyy");*/

                                    // txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                } catch (Exception ex) {

                                }


                            }
                        }, year, month, day);
                dd.show();
            }
        });
        //CHANG PSW
        changPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=getActivity().getLayoutInflater();
                View view1=inflater.inflate(R.layout.change_pwd_dialog,null);
                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(getActivity());
                dialogBuilder.setView(view1);
                final ImageView close=(ImageView)view1.findViewById(R.id.close_btn);
                final EditText currentPswET=(EditText)view1.findViewById(R.id.current_psw_et);
                final EditText newPswET=(EditText)view1.findViewById(R.id.new_psw_et);
                final EditText confirmPswET=(EditText)view1.findViewById(R.id.confirm_psw_et);
                Button applyBtn=(Button)view1.findViewById(R.id.btn_apply);
                final AlertDialog alertDialog=dialogBuilder.create();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                applyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentPsw=currentPswET.getText().toString();
                        String newPsw=newPswET.getText().toString();
                        String confirmPsw=confirmPswET.getText().toString();
                        if(currentPsw.equals("")){
                            Toast.makeText(getActivity(), "Enter current Password", Toast.LENGTH_SHORT).show();
                            currentPswET.requestFocus();
                        }else if(newPsw.equals("")){
                            Toast.makeText(getActivity(), "Enter New Password", Toast.LENGTH_SHORT).show();
                            newPswET.requestFocus();
                        }else if(confirmPsw.equals("")){
                            Toast.makeText(getActivity(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                            confirmPswET.requestFocus();
                        }else if(!newPsw.equals(confirmPsw)){
                            Toast.makeText(getActivity(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                            confirmPswET.requestFocus();
                        }else {
                            changePasswordData(currentPsw,newPsw);
                            alertDialog.dismiss();

                        }
                    }
                });
                alertDialog.show();
            }
        });
        //UPDATE PROFILE
        profileSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileDetails();
            }
        });
        //UPDATE ADDRESS
        updateAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddressDeatils();
            }
        });
        accountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
//                    buttonView.setChecked(false);
                    Toast.makeText(activity,
                            "active"+isChecked, Toast.LENGTH_SHORT).show();
                } else {
//                    buttonView.setChecked(f);
                    Toast.makeText(activity,
                            "deactive"+isChecked, Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    private void updateProfileDetails() {
        firstName=firstNameEd.getText().toString();
        lastName=lastNameEd.getText().toString();
        dob=dobEd.getText().toString();
        email=emailEd.getText().toString();
        mobile =mobileEd.getText().toString();
        if (firstName.equals("") &&lastName.equals("") &&dob.equals("") && email.equals("")
                && mobile.equals("")) {
//            Log.i("k1", "null");
            Toast.makeText(activity,
                    "Please enter the details", Toast.LENGTH_SHORT).show();
            firstNameEd.requestFocus();
        }else if (firstName.equals("")) {
            Toast.makeText(activity,
                    "Please enter the firstName", Toast.LENGTH_SHORT).show();
            firstNameEd.requestFocus();
        }  else if (lastName.equals("")) {
            Toast.makeText(activity,
                    "Please enter the lastName", Toast.LENGTH_SHORT).show();
            lastNameEd.requestFocus();
        }  else if (dob.equals("")) {
            Toast.makeText(activity,
                    "Please enter the DOB", Toast.LENGTH_SHORT).show();

            dobEd.requestFocus();
        } else if (email.equals("")) {
            Toast.makeText(activity,
                    "Please enter the email", Toast.LENGTH_SHORT).show();

            emailEd.requestFocus();
        }else if (!email.matches(Utils.EMAIL_PATTERN)) {
            Toast.makeText(activity,
                    "Please enter the email", Toast.LENGTH_SHORT).show();

            emailEd.requestFocus();
        } else if (mobile.equals("")) {
            Toast.makeText(activity,
                    "Please enter themobile", Toast.LENGTH_SHORT).show();
            mobileEd.requestFocus();
        } else if (!firstName.equals("")&&!lastName.equals("")&&!dob.equals("") &&email.matches(Utils.EMAIL_PATTERN)
                && !mobile.equals("")) {
            updateProfileData();

        }
    }
    public void updateAddressDeatils(){
        street=street1ED.getText().toString();
        city=street2ED.getText().toString();
        state=cityED.getText().toString();
        postcode=postcodeED.getText().toString();
//        mobile =mobileEd.getText().toString();
        if(street.equals("")&&city.equals("")&&postcode.equals("")) {
            Toast.makeText(activity,"Please Enter the Details",Toast.LENGTH_SHORT).show();
            street1ED.requestFocus();
        }else if(street.equals("")){
            Toast.makeText(activity,"Please Enter the Street",Toast.LENGTH_SHORT).show();
            street1ED.requestFocus();
        }else if(city.equals("")){
            Toast.makeText(activity,"Please Enter the City",Toast.LENGTH_SHORT).show();
            cityED.requestFocus();
        }else if(countryPosition==0){
            Toast.makeText(activity,"Please Select the Country",Toast.LENGTH_SHORT).show();
//            cityET.requestFocus();
        }else if(postcode.equals("")){
            Toast.makeText(activity,"Please Enter the Pincode",Toast.LENGTH_SHORT).show();
            postcodeED.requestFocus();
        }else if(!street.equals("")&&!city.equals("")&&countryPosition!=0&&!postcode.equals("")){
            updateAddressData();

        }
    }
    public void deActive(){
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view1=inflater.inflate(R.layout.deactivate_dialog,null);
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(view1);
        final ImageView close=(ImageView)view1.findViewById(R.id.close_btn);

        Button applyBtn=(Button)view1.findViewById(R.id.btn_deactivate);
        final AlertDialog alertDialog=dialogBuilder.create();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deActivateAccountData();
            }
        });
        alertDialog.show();
    }
    public void updateProfileData(){
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
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jsonObj=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        String status=jsonObj.optString(TagName.TAG_MSG);
                        if(status.equalsIgnoreCase("success")){
                            Toast.makeText(activity,status,Toast.LENGTH_SHORT).show();

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ProfileInfoAsync la = new ProfileInfoAsync();
        la.execute(Utils.updateProfileUrl+userId+"/"+firstName+"/"+lastName+"/"+dob);

    }
    public void updateAddressData(){
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
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jsonObj=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        String status=jsonObj.optString(TagName.TAG_MSG);
                        if(status.equalsIgnoreCase("success")){
                            Toast.makeText(activity,status,Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(activity,status,Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("Url",Utils.editbillingaddressUrl+userId+"/"+firstName+"/"+lastName+"/"+street+"/"+city+","+state+"/"+countrycode+"/"+postcode+"/"+mobile);
        AddressAsync la = new AddressAsync();
        String restUrl =Utils.editbillingaddressUrl+userId+"/"+firstName+"/"+lastName+"/"+street+"/"+city+"%20"+state+"/"+countrycode+"/"+postcode+"/"+mobile;
                la.execute(restUrl.replace(" ","%20"));

    }
    private void changePasswordData(String oldpsw,String newpsw){
        class changePswAsync extends AsyncTask<String, Void, String> {
            Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String oldpsw = params[0];
                String newpsw = params[1];
                Log.e("uname",oldpsw);
                Log.e("pass",newpsw);
                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet( Utils.instantChangePswUrl+userId+"&oldpassword="+oldpsw+"&newpassword="+newpsw);
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
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
//                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jsonObj=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        String status=jsonObj.optString(TagName.TAG_MSG);
                        if(status.equalsIgnoreCase("success")){

                            JSONArray jsonArray=jsonObject.getJSONArray("changepassword");
                            JSONObject jsonOb=jsonArray.getJSONObject(0);
                            Toast.makeText(activity,jsonOb.optString("message"),Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        changePswAsync la = new changePswAsync();
        la.execute(oldpsw, newpsw);



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
                            countrySpinner.setSelection(getIndex(countrySpinner, country));
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
    public void deActivateAccountData(){
        class DeActivateAsync extends AsyncTask<String, Void, String> {
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
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jsonObj=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        String status=jsonObj.optString(TagName.TAG_MSG);
                        if(status.equalsIgnoreCase("  ")){
                            Toast.makeText(activity,status,Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.commit();
                            activity.finish();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        DeActivateAsync la = new DeActivateAsync();
        la.execute(Utils.deacivateaccountUrl+userId);

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
