package com.btranz.ecommerceapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.CredientialActivity;
import com.btranz.ecommerceapp.activity.MainActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.CartServicesRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Ravi on 29/07/15.
 */
public class CartFragment extends Fragment {
    Button contShopingBtn;
    LinearLayout emptyCart;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private Toolbar checkOutToolbar;
    private ImageButton mFabButton;
    ArrayList<ProductModel>  services;// = new ArrayList<ProductModel>();
    List<String> cartList= new ArrayList<String>();
    FragmentActivity activity;
    private RecyclerView recyclerView;
    public TextView coutTxt,amtTxt,checkoutTxt;
    private CartServicesRecyclerAdapter adapter;
    AlertDialog alertDialog;
    private Runnable animateViewPager;
    private Handler handler;
    int tempCount;
    double  tempAmt;
    AsyncHttpTask task;
    boolean stopSliding = false;
    String message, customerId;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    DatabaseHandler db;
    private Dialog loadingDialog;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        db=new DatabaseHandler(activity);
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        handler = new Handler();
//        customerName = sharedpreferences.getString("customerName", "");
//        customerId = sharedpreferences.getString("customerID", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

//        ((MainActivity)getActivity()).mFabButton.setVisibility(View.GONE);
        initRecyclerView(rootView);
//        getCartList();
        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onResume() {
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        final  TextView toolTitle=((SecondActivity) getActivity()).toolbarTitle;
        SpannableString s = new SpannableString(getString(R.string.title_cart));
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
//        if (services == null) {
//            sendRequest();
//            emptyCart.setVisibility(View.VISIBLE);
            tempCount=0;
            tempAmt=0;
        customerId = sharedpreferences.getString("customerID", "");
            if(customerId.equals("")) {
                getCartList();
            }else {
                sendRequest();
            }
//            adapter = new ServicesRecyclerAdapter(activity, services);
            Log.e("onResume", "onResume");
//        } else {
//            Log.e("onResume else", "onResume else");
//            System.out.println(services);
//            recyclerView.setAdapter(new CartServicesRecyclerAdapter(CartFragment.this, services));
//            recyclerView.scrollToPosition(0);
////            emptyCart.setVisibility(View.GONE);
//        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        services.clear();
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (recyclerView.getChildCount() == size - 1) {
                        recyclerView.scrollToPosition(0);
                    } else {
                        recyclerView.scrollToPosition(
                                recyclerView.getChildCount() + 1);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }
    public void getCartList(){
        cartList=db.getCartList();
          /*Initialize array if null*/
        if (null == services) {
            services = new ArrayList<ProductModel>();
        }
        for(int i=0;i<cartList.size();i++){
            String cartListStr=cartList.get(i);
            String cartListArr[]=cartListStr.split(Pattern.quote("***"));
            ProductModel item = new ProductModel();
            item.setId(Integer.valueOf(cartListArr[0]));
            item.setTitle(cartListArr[1]);
            item.setCost(Double.valueOf(cartListArr[2]));
            item.setFinalPrice(Double.valueOf(cartListArr[3]));
            item.setThumbnail(cartListArr[4]);
            item.setCount(Integer.valueOf(cartListArr[5]));
//            Log.e("name","name");

            services.add(item);


        }
        if(services.size()!=0) {
            for (int i = 0; i < services.size(); i++) {
                ProductModel item = services.get(i);
                int count1 = tempCount + item.getCount();
                double amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
//            Log.e("onPostExecute", " " + item.getCount());


                coutTxt.setText(String.valueOf(count1));
                amtTxt.setText(String.valueOf(amt1));
                tempCount = count1;
                tempAmt = amt1;
            }
            //cart badge
            handler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void run() {

                    ((SecondActivity) getActivity()).writeBadge(services.size());
                    editor.putString("cartBadge", String.valueOf(services.size()));
                    editor.commit();
                }
            }, 1000);
            emptyCart.setVisibility(View.GONE);
            adapter = new CartServicesRecyclerAdapter(CartFragment.this, services);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            emptyCart.setVisibility(View.VISIBLE);
        }
    }

    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
            task = new AsyncHttpTask();
            task.execute(Utils.getcartUrl+customerId);
            Log.e("sendrequest","sendrequest");
        } else {
            message = getResources().getString(R.string.no_internet_connection);
            showAlertDialog(message, true);
        }
    }
    public void showAlertDialog(String message, final boolean finish) {
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            activity.finish();
                    }
                });
        alertDialog.show();
    }
    private void initRecyclerView(View view) {
        emptyCart=(LinearLayout) view.findViewById(R.id.empty_cart);
//        checkOutToolbar=(Toolbar)view.findViewById(R.id.checkout_toolbar);
        coutTxt=(TextView)view.findViewById(R.id.checkout_bar_count);
        amtTxt=(TextView)view.findViewById(R.id.checkout_bar_amt);
        checkoutTxt=(TextView)view.findViewById(R.id.checkout_bar);
        contShopingBtn=(Button)view.findViewById(R.id.continue_shoping_btn);

        //continue Shoping Action
        contShopingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in=new Intent(activity,SecondActivity.class);
//                in.putExtra("key", TagName.CATG_ID);
//                startActivity(in);
//                activity.overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

                // Start a new fragment
                fragment = new CategoriesFragment();
//                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_second, fragment,
                        CategoriesFragment.CATG_FRAG);
                transaction.addToBackStack(CategoriesFragment.CATG_FRAG);
                transaction.commit();
            }
        });
        //CHECKOUT ACTION
        checkoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ServicesActivity)getActivity()).displayView(1);
//                Intent i=new Intent(activity,ServicesActivity.class);
//                i.putExtra("key", 1);
//                startActivity(i);
//                activity.overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
                Toast.makeText(activity, "Cart CheckOut", Toast.LENGTH_SHORT).show();
//                Fragment fragment=null;
//                fragment = new PlaceOrderFragment();
//                if (fragment != null) {
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container_services, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                     set the toolbar title
//                    ((ServicesActivity)getActivity()).getActionBar().setTitle("Place Order");
//                }
                Intent in=new Intent(activity, CredientialActivity.class);
                in.putExtra("credKey", TagName.CRED_LOGIN);
                in.putParcelableArrayListExtra("CheckOutProductList",services);
                activity.startActivity(in);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        int paddingBottom = Utils.getToolbarHeight(activity);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
//        adapter = new ServicesRecyclerAdapter(activity, services);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//

//        recyclerView.addOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
//                hideViews();
//            }
//
//            @Override
//            public void onShow() {
//                showViews();
//            }
//        });
//        hideViews();
//        showViews();
    }
    private void hideViews() {
        checkOutToolbar.setVisibility(View.GONE);
//
// checkOutToolbar.animate().translationY(-checkOutToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//        int paddingBottom = Utils.getToolbarHeight(activity);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) checkOutToolbar.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        checkOutToolbar.animate().translationY(checkOutToolbar.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void showViews() {
        checkOutToolbar.setVisibility(View.VISIBLE);
        int paddingBottom = Utils.getToolbarHeight(activity);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);
//        checkOutToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        checkOutToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }
    public void afterDelete(int PrdtId,double amt, int count){
//        showViews();
//        int count1=count;
        if(customerId.equals("")) {
            db.removeCartItem(String.valueOf(PrdtId));
        }else {
//            sendRequest();
            deletecartData(String.valueOf(PrdtId),customerId);
        }

        int count1=tempCount-count;
        double amt1=tempAmt-(amt * count);

        if(count1!=0) {
            coutTxt.setText(String.valueOf(count1));
            ((SecondActivity) getActivity()).writeBadge(services.size());
            editor.putString("cartBadge", String.valueOf(services.size()));
            editor.commit();
            amtTxt.setText(String.valueOf(amt1));
            tempCount = count1;
            tempAmt = amt1;
        }else {
            emptyCart.setVisibility(View.VISIBLE);
//            ((SecondActivity) getActivity()).cartCountTv.setText(count1);
        }
    }
    public void checkOutAdd(double amt){
//        showViews();

//        int count1=count;
        int count1=tempCount+1;
        double amt1=tempAmt+amt;
        //cart badge
//        ((SecondActivity) getActivity()).cartCountTv.setText(String.valueOf(count1));
        coutTxt.setText(String.valueOf(count1));
        amtTxt.setText(String.valueOf(amt1));
        tempCount=count1;
        tempAmt=amt1;
    }
    public void checkOutRemove(double amt){
//        int count1=count;
//        showViews();
        if(tempCount>0) {
            int count1=tempCount-1;
            double amt1 = tempAmt - amt;
            //cart badge
//            ((SecondActivity) getActivity()).cartCountTv.setText(String.valueOf(count1));
            coutTxt.setText(String.valueOf(count1));
            amtTxt.setText(String.valueOf(amt1));
            tempCount=count1;
            tempAmt = amt1;
        }
    }
    public void updateData(int PrdtId, int quantity){
        if(customerId.equals("")) {
//            db.removeCartItem(String.valueOf(PrdtId));
            db.updateCartItem(String.valueOf(PrdtId),String.valueOf(quantity));
        }else {
//            sendRequest();
            updateCartPrdtData(String.valueOf(PrdtId),String.valueOf(quantity),customerId);
        }
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

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

//            setProgressBarIndeterminateVisibility(false);
            loadingDialog.dismiss();
            /* Download complete. Lets update UI */
            if (result == 1) {
                Log.e("onPostExecute", "onPostExecute");
                if(services.size()!=0) {
                    for (int i = 0; i < services.size(); i++) {
                        ProductModel item = services.get(i);
                        int count1 = tempCount + item.getCount();
                        double amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
                        Log.e("onPostExecute", " " + item.getFinalPrice());
                        //cart badge
                        ((SecondActivity) getActivity()).writeBadge(services.size());
                        editor.putString("cartBadge", String.valueOf(services.size()));
                        editor.commit();
                        coutTxt.setText(String.valueOf(count1));
                        amtTxt.setText(String.valueOf(amt1));
                        tempCount = count1;
                        tempAmt = amt1;
                    }
                    emptyCart.setVisibility(View.GONE);
                    adapter = new CartServicesRecyclerAdapter(CartFragment.this, services);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
                }else{
                    emptyCart.setVisibility(View.VISIBLE);
                }
            } else {
                Log.e("hello", "Failed to fetch data!");
                ((SecondActivity) getActivity()).writeBadge(0);
                editor.putString("cartBadge", String.valueOf(0));
                editor.commit();
                emptyCart.setVisibility(View.VISIBLE);
            }
        }
    }
    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);

                if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);

//                    if (status) {
//                JSONObject jsonData = jsonObject
//                        .getJSONObject(TagName.TAG_PRODUCT);
//                    }
                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_PRODUCT);

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
                services.add(item);
            }
                } else {
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deletecartData(String productid,String userid) {

        class Async extends AsyncTask<String, Void, String> {

//            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please Wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String pid = params[0];
//                String quant = params[1];
                String userid = params[1];
//                Log.e("uname",uname);
//                Log.e("pass",pass);
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(Utils.deletecartUrl+pid+"/"+userid);
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
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
                            Toast.makeText(activity, "Product deleted", Toast.LENGTH_LONG).show();
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
//                if(message.equalsIgnoreCase("success")){
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
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Async la = new Async();
        la.execute(productid, userid);

    }
    private void updateCartPrdtData(String productid,String quantity,String userid) {

        class UpdateAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loadingDialog = ProgressDialog.show(activity, "", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String pid = params[0];
                String quant = params[1];
                String userid = params[2];
//                Log.e("uname",uname);
//                Log.e("pass",pass);
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(Utils.updatecartUrl+pid+"/"+quant+"/"+userid);
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
//                loadingDialog.dismiss();
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
//                            Toast.makeText(activity, "Product deleted", Toast.LENGTH_LONG).show();
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
//                if(message.equalsIgnoreCase("success")){
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
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        UpdateAsync ua = new UpdateAsync();
        ua.execute(productid,quantity, userid);

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
