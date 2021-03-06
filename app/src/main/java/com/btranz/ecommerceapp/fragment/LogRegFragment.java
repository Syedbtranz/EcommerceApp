package com.btranz.ecommerceapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.btranz.ecommerceapp.activity.BookNowActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.CartServicesRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by Ravi on 29/07/15.
 */
public class LogRegFragment extends Fragment {
    private Dialog loadingDialog;
    Handler handler;
    EditText emailET,pswET,forgetPswEt;
     String email,password;
    int i;
    Button loginBtn;
    TextView signUpBtn, forgotPswBtn,guestBtn;
    ImageView forgotPswImg;
    FragmentActivity activity;
    List<String> cartList= new ArrayList<String>();
    DatabaseHandler db;
    AsyncHttpTask task;
    ArrayList<ProductModel>  services;
    int tempCount;
    // shared prefernce
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    public LogRegFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        // execute when already logged in (shared values)
        sharedPrefernces();
        //Db handler
        db=new DatabaseHandler(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_log_reg, container, false);
        emailET=(EditText)rootView.findViewById(R.id.email_et);
        pswET=(EditText)rootView.findViewById(R.id.psw_et);
        loginBtn=(Button)rootView.findViewById(R.id.login_btn);
        forgotPswBtn=(TextView)rootView.findViewById(R.id.forgot_psw_btn);
        signUpBtn=(TextView)rootView.findViewById(R.id.signup_btn);
        guestBtn=(TextView)rootView.findViewById(R.id.check_guest_btn);
        forgotPswImg=(ImageView) rootView.findViewById(R.id.forgot_psw_img);
        //image color change when change the theme colors
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
        forgotPswImg.setColorFilter(color);

        //Forgot Psw
        forgotPswBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailET.getText().toString();
            if (email.equals("")) {
                Toast.makeText(activity,
                        "Please Enter the Email/Mobile No", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
            } else if (!email.matches(Utils.EMAIL_PATTERN)&&email.length()!=10){
                Toast.makeText(activity,
                        "Please Enter the valid Email/Mobile No",
                        Toast.LENGTH_SHORT).show();
            }/*else if (email.matches(Utils.EMAIL_PATTERN)||email.length()==10) {
                Toast.makeText(activity,
                        "Success Please Check Your Email!",
                        Toast.LENGTH_SHORT).show();
            }*/
            else
            {
                forgotPassword(email);
            }
        }
        });

        //SignUp Action
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in=new Intent(getActivity(), BookNowActivity.class);
//                in.putExtra("buynowKey", TagName.BUYNOW_USER_ADDRESS);
//                activity.startActivity(in);
//                activity.overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

                // Start a new fragment
                fragment = new SignUpFragment();
//                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_credentials, fragment,
                        TagName.FRAGMENT_SIGNUP);
                transaction.addToBackStack(TagName.FRAGMENT_SIGNUP);
                transaction.commit();
            }
        });

        //GUEST ACTION
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBookNow();

            }
        });

        //LOGIN ACTION
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailET.getText().toString();
                password = pswET.getText().toString();
                sendData();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    //FPRGOT PASSWORD
    private void forgotPassword(final String username) {


        class Async extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];

//                Log.e("uname",uname);

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("tag", "forgotpassword"));
//                nameValuePairs.add(new BasicNameValuePair("email", uname));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(Utils.forgotPwdUrl+uname);
                    //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
                String s = result.trim();
                Log.e("forgot_password_result",s);
                loadingDialog.dismiss();
                try {

                    JSONObject jsonObject=new JSONObject(s);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

//                      if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
                        if(message.equalsIgnoreCase("success")){
JSONArray jsonArray=jsonObject.getJSONArray(TagName.TAG_FORGOT_PWD);
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            String message_fpwd=jsonObject1.getString(TagName.TAG_MSG);
                            if(message_fpwd.equalsIgnoreCase(TagName.TAG_ERROR)){
                                showDialog("You are not registered with us");
                            }else
                            {
                                showDialog(message_fpwd);
                            }
                         /*   JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);
                            addCart(jobcust.optString("id"));
                            editor = sharedpreferences.edit();
                            editor.putString("userID", jobcust.optString("id"));
                            editor.putString("userEmail", jobcust.optString("username"));
                            editor.putString("password", jobcust.optString("password"));
                            editor.putString("userName",jobcust.optString("name"));
                            editor.putString("logged", "logged");
                            editor.commit();
                            activity.finish();*/
//                    Intent in=new Intent(getActivity(), SecondActivity.class);
//                    in.putExtra("key", TagName.CART_ID);
//                    activity.startActivity(in);
//                    activity.overridePendingTransition(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
                        }else {
                            Toast.makeText(activity, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Async la = new Async();
        la.execute(username, password);

    }
public void showDialog(String message){
    AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
    dialog.setCancelable(false);
    dialog.setTitle(message);
    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    dialog.show();

}

    public void sendData(){
        if (email.equals("") && password.equals("")) {

            Toast.makeText(activity,
                    "Please Enter the Details", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (email.equals("")) {
            Toast.makeText(activity,
                    "Please Enter the Email", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (!email.matches(Utils.EMAIL_PATTERN)&&email.length()!=10){
            Toast.makeText(activity,
                    "Please Enter the currect Email/Mobile No",
                    Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(activity,
                    "Please Enter the Password", Toast.LENGTH_SHORT).show();
            pswET.requestFocus();
        } else if (password.length() < 6) {
            Toast.makeText(activity,
                    "Please Enter atleast 6 digits/characters of Password",
                    Toast.LENGTH_SHORT).show();
            pswET.requestFocus();
        } else if (email.matches(Utils.EMAIL_PATTERN)||email.length()==10
                && password.length() > 5) {
            login(email,password);
//            Toast.makeText(activity,
//                    "Success",
//                    Toast.LENGTH_SHORT).show();
        }
    }
    private void login(final String username, final String password) {


        class LoginAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];
                Log.e("uname",uname);
                Log.e("pass",pass);
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet( Utils.instantLoginUrl+uname+"&password="+pass);
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
                String s = result.trim();
                Log.e("s",s);
                loadingDialog.dismiss();
                try {
                    JSONObject response = new JSONObject(s);
//                    JSONObject jsonObject=response.getJSONObject("");

                    if (response != null) {
                        JSONObject jobstatus=response.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

//                      if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
                if(message.equalsIgnoreCase("success")){

                          JSONArray jarry=response.getJSONArray("userlogin");
                    for(int i=0;i<jarry.length();i++){
                        JSONObject jobLogin=jarry.getJSONObject(i);
                        if(jobLogin.optInt("code")!=0) {
//                            JSONObject jobsuccess = jobLogin.getJSONObject("status");
                            Toast.makeText(activity, jobLogin.optString("message"), Toast.LENGTH_LONG).show();
//                        }
//                        if(i==1){
                            JSONObject jobcust = jobLogin.getJSONObject("user_detail");
                            sendRequest(jobcust.optString("id"));
                            addCart(jobcust.optString("id"));
                            editor = sharedpreferences.edit();
                            editor.putString("userID", jobcust.optString("id"));
                            editor.putString("userEmail", jobcust.optString("username"));
//                            editor.putString("password", jobcust.optString("password"));
                            editor.putString("userName", jobcust.optString("name"));
                            editor.putString("logged", "logged");
                            editor.commit();

                        }else{

                            if(jobLogin.optString("message").equalsIgnoreCase("notactive")){
//                                Toast.makeText(activity, "This account is "+jobLogin.optString("message"), Toast.LENGTH_LONG).show();
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                                builder.setTitle("Activation?");
                                builder.setMessage("Your Account is Not Active.Please Activate It.");
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "Clicked some button");
                                    }
                                });
                                builder.setPositiveButton("Activate", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                       activateAccountData(username);
                                    }
                                });
//				builder.setIcon(R.drawable.cross_black);

                                android.support.v7.app.AlertDialog alertDialog = builder.create();
                                alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_right;
                                alertDialog.show();
                            }else{
                                Toast.makeText(activity, jobLogin.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
//                        }
                    }

//                    Intent in=new Intent(getActivity(), SecondActivity.class);
//                    in.putExtra("key", TagName.CART_ID);
//                    activity.startActivity(in);
//                    activity.overridePendingTransition(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
                }else {
                    Toast.makeText(activity, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
    protected void sharedPrefernces() {
        // TODO Auto-generated method stub
        sharedpreferences = activity.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.getString("logged", "").toString()
                .equals("logged")) {
            sendBookNow();
//            i.putExtra("CUSTOMEREMAIL", sharedpreferences
//                    .getString("customerEmail", "").toString());
//            i.putExtra("PASSWORD", sharedpreferences.getString("password", "")
//                    .toString());
//             i.putExtra("CUSTOMERNAME",sharedpreferences.getString("customerName",
//             "").toString());
            // i.putExtra("CHECK", true);
//            startActivity(i);

        }

    }
    public void sendBookNow(){
        ArrayList<ProductModel> prdtSummeryList = activity.getIntent().getParcelableArrayListExtra("CheckOutProductList");
        Log.e("checkOutProductsArray",""+prdtSummeryList);
        Intent in=new Intent(getActivity(), BookNowActivity.class);
        in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
        in.putParcelableArrayListExtra("CheckOutProductList",prdtSummeryList);
        activity.startActivity(in);
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        activity.finish();
    }
    public  void addCart(String customerId){
        cartList=db.getCartList();
          /*Initialize array if null*/
//        if (null == services) {
//            services = new ArrayList<ProductModel>();
//        }
        if(cartList.size()!=0) {
            handler = new Handler();
            for (i = 0; i < cartList.size(); i++) {
                String cartListStr = cartList.get(i);
                String cartListArr[] = cartListStr.split(Pattern.quote("***"));
                ProductModel item = new ProductModel();
                item.setId(Integer.valueOf(cartListArr[0]));
//            item.setTitle(cartListArr[1]);
//            item.setCost(Double.valueOf(cartListArr[2]));
//            item.setFinalPrice(Double.valueOf(cartListArr[3]));
//            item.setThumbnail(cartListArr[4]);
                item.setCount(Integer.valueOf(cartListArr[5]));
            Log.e("cartList.size()"," "+cartList.size());
                addtocartData(cartListArr[0], cartListArr[5], customerId);
                db.removeCartItem(cartListArr[0]);
//                if (i == cartList.size()-1) {
//                activity.finish();
//                    activity.onBackPressed();
//                }
//            services.add(item);
            }

        } else {
            loadingDialog.dismiss();
            activity.finish();

        }
//        activity.finish();
//       db.removeCartList();
    }
    private void addtocartData(String productid, String quantity,String userid) {

        class LoginAsync extends AsyncTask<String, Void, String> {

//            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String pid = params[0];
                String quant = params[1];
                String userid = params[2];
                InputStream inputStream = null;
                String result= null;;
                HttpURLConnection urlConnection = null;

                try {
                /* forming th java.net.URL object */
                    URL url = new URL(Utils.instantaddToCartUrl+quant+"&productid="+pid+"&userid="+userid);
//                    URL url = new URL(Utils.addtocartUrl+pid+"/"+quant+"/"+userid);
//                    Log.e("URL", Utils.addtocartUrl+pid+"/"+quant+"/"+userid);

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

            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                Log.e("s",s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
//                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
//                            JSONArray jarr=jsonObject.optJSONArray(TagName.TAG_PRODUCT);
//                            JSONObject job=jarr.optJSONObject(0);
//                            JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
//                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
//                            String message1 = jobstat.optString(TagName.TAG_MSG);
//                            if(status1==1) {
//                                if(cartBadge.equals("")){
////                                    ((SecondActivity) getActivity()).writeBadge(1);
////                                    editor.putString("cartBadge", String.valueOf(1));
////                                    editor.commit();
//////                            writeBadge(0);
////                                }else{
////
////                                    ((SecondActivity) getActivity()).writeBadge(Integer.parseInt(cartBadge)+1);
////                                    editor.putString("cartBadge", String.valueOf(Integer.parseInt(cartBadge)+1));
////                                    editor.commit();
////                                }
//                                Toast.makeText(activity, "Product Added to Cart!", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(activity, "Product Not Added to Cart", Toast.LENGTH_SHORT).show();
//                            }
                        }else {
//                            Toast.makeText(activity, "Product Not Added! Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(productid, quantity, userid);

    }
    public void activateAccountData(String username){
        class ActivateAsync extends AsyncTask<String, Void, String> {
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
                            JSONArray jarr = jsonObject.optJSONArray("acivateaccount");
                            JSONObject job1 = jarr.optJSONObject(0);
                            JSONObject jobstat = job1.getJSONObject(TagName.TAG_STATUS);
                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                            String message1 = jobstat.optString(TagName.TAG_MSG);
                            if (status1 == 1) {
                                Toast.makeText(activity, "Your Account is Activated Please Login Now", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(activity, "Your Account is Not Activated, Please try Again.", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(activity, "Your Account is Not Activated.. Please try Again.", Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ActivateAsync la = new ActivateAsync();
        la.execute(Utils.instantActivateAccountUrl+username);

    }
    private void sendRequest(String userId) {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
            task = new AsyncHttpTask();
            task.execute(Utils.instantGetCartUrl+userId);
            Log.e("sendrequest","sendrequest");
        } else {
//            message = getResources().getString(R.string.no_internet_connection);
//            showAlertDialog(message, true);
        }
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
//        Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
            loadingDialog = ProgressDialog.show(activity, "", "Loading...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

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
                    parseResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("catch", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            loadingDialog.dismiss();
            /* Download complete. Lets update UI */
            SecondActivity sc=new SecondActivity();
            if (result == 1) {
                Log.e("onPostExecute", "onPostExecute");
                if(services.size()!=0) {
                    for (int i = 0; i < services.size(); i++) {
                        ProductModel item = services.get(i);
                        int count1 = tempCount + item.getCount();
//                        double amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
                        Log.e("onPostExecute", " " + count1);
                        //cart badge
//                        ((SecondActivity) getActivity()).writeBadge(services.size());
//                        editor.putString("cartBadge", String.valueOf(services.size()));

//                        sc.writeBadge(count1);
                        editor.putString("cartBadge", String.valueOf(count1));
                        editor.commit();
//                        coutTxt.setText(String.valueOf(count1));
//                        amtTxt.setText(String.valueOf(amt1));
                        tempCount = count1;
//                        tempAmt = amt1;
                    }
                    activity.finish();
//                    emptyCart.setVisibility(View.GONE);
//                    adapter = new CartServicesRecyclerAdapter(CartFragment.this, services);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
                }else{
//                    emptyCart.setVisibility(View.VISIBLE);
                }
            } else {
                Log.e("hello", "Failed to fetch data!");
//                ((SecondActivity) getActivity()).writeBadge(0);
                editor.putString("cartBadge", String.valueOf(0));
                editor.commit();
                activity.finish();
//                emptyCart.setVisibility(View.VISIBLE);
            }
        }
    }
    private void parseResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
//            JSONArray response = new JSONArray(result);
//            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);

                if (status==1) {
                    JSONArray jarr=jsonObject.optJSONArray("getcartproduct");
                    JSONObject job=jarr.optJSONObject(0);
                    JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                    int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                    String message1 = jobstat.optString(TagName.TAG_MSG);
                    if(status1==1) {

                        JSONArray posts = job.optJSONArray(TagName.TAG_PRODUCT);
                        String quoteId = jsonObject.optString("quoteid");
                        Log.e("quoteId", quoteId);
                        editor.putString("quoteId", quoteId);
                        editor.commit();

            /*Initialize array if null*/
                        if (null == services) {
                            services = new ArrayList<ProductModel>();
                        }

                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject post = posts.optJSONObject(i);

                            ProductModel item = new ProductModel();
                            item.setId(post.optInt(TagName.KEY_ID));
                            item.setTitle(post.optString(TagName.KEY_NAME));
                            item.setDescription(post.optString(TagName.KEY_DES));
                            item.setCost(post.optDouble(TagName.KEY_PRICE));
                            item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
                            item.setCount(post.optInt(TagName.KEY_COUNT));
//                    Log.e("name", "name");
                            item.setThumbnail(post.optString(TagName.KEY_THUMB));
                            JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                            item.setShare(post1.optString(TagName.KEY_SHARE));
                            item.setTag(post1.optString(TagName.KEY_TAG));
                            item.setDiscount(post1.optInt(TagName.KEY_DISC));
                            item.setRating(post1.optInt(TagName.KEY_RATING));
                            services.add(item);
                        }
                    }else{
                        Toast.makeText(activity, "No Products", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Net Work Error", Toast.LENGTH_SHORT).show();
//                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
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
