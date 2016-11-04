package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.OrdersRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.WislistRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
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
public class WishlistFragment extends Fragment {
    Button cardDetailsBtn, contShopingBtn;
    ArrayList<ProductModel> services;// = new ArrayList<ProductModel>();
    List<String> cartList= new ArrayList<String>();
    FragmentActivity activity;
    private RecyclerView recyclerView;
    public TextView coutTxt,amtTxt,checkoutTxt;
    private WislistRecyclerAdapter adapter;
    AlertDialog alertDialog;
    AsyncHttpTask task;
    boolean stopSliding = false;
    String message, customerId;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    DatabaseHandler db;
    private Dialog loadingDialog;
    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        customerId = sharedpreferences.getString("customerID", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);
        initRecyclerView(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        SpannableString s = new SpannableString(getString(R.string.title_wislist));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final  TextView toolTitle=((SecondActivity) getActivity()).toolbarTitle;
        //Title
        toolTitle.setText(s);
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

        if (services == null) {
            sendRequest();

//            Log.e("onResume","test");
////            adapter = new ServicesRecyclerAdapter(activity, services);
//            Log.e("onResume", "onResume");
        } else {
            Log.e("onResume else", "onResume else");
            recyclerView.setAdapter(new WislistRecyclerAdapter(activity, services, R.layout.wishlist_inflate));
//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//            recyclerView.scrollToPosition(0);
        }
    }
    private void initRecyclerView(View view) {
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        int paddingBottom = Utils.getToolbarHeight(activity);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new WislistRecyclerAdapter(activity, services, R.layout.wishlist_inflate);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
            task = new AsyncHttpTask();
            task.execute(Utils.getcartUrl+customerId);
//            task.execute(prdtsUrl);
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
//                if(services.size()!=0) {
//                    for (int i = 0; i < services.size(); i++) {
//                        ProductModel item = services.get(i);
//                        int count1 = tempCount + item.getCount();
//                        double amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
//                        Log.e("onPostExecute", " " + item.getFinalPrice());
//                        coutTxt.setText(String.valueOf(count1));
//                        amtTxt.setText(String.valueOf(amt1));
//                        tempCount = count1;
//                        tempAmt = amt1;
//                    }
//                    emptyCart.setVisibility(View.GONE);
                    adapter = new WislistRecyclerAdapter(activity, services, R.layout.wishlist_inflate);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//                }else{
//                    emptyCart.setVisibility(View.VISIBLE);
//                }
            } else {
                Log.e("hello", "Failed to fetch data!");
//                emptyCart.setVisibility(View.VISIBLE);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
