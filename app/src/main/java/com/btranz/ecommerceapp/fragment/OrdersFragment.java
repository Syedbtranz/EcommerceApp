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
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.CredientialActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.CartServicesRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.HorizontalListAdapter;
import com.btranz.ecommerceapp.adapter.OrdersRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.ProductGridAdapter;
import com.btranz.ecommerceapp.modal.OrdersModel;
import com.btranz.ecommerceapp.modal.Product;
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
public class OrdersFragment extends Fragment {
    public OrdersModel singleOrder;
    ArrayList<OrdersModel> services;// = new ArrayList<ProductModel>();
    ArrayList<ProductModel> orderList;// = new ArrayList<ProductModel>();
    List<String> cartList= new ArrayList<String>();
    FragmentActivity activity;
    private RecyclerView recyclerView;
    public TextView coutTxt,amtTxt,checkoutTxt;
    private OrdersRecyclerAdapter adapter;
    AlertDialog alertDialog;
    AsyncHttpTask task;
    boolean stopSliding = false;
    String message, userId;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    DatabaseHandler db;
    private Dialog loadingDialog;
    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userID", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        initRecyclerView(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        SpannableString s = new SpannableString(getString(R.string.title_orders));
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
            recyclerView.setAdapter(new OrdersRecyclerAdapter(OrdersFragment.this, services, R.layout.order_inflate));
//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//            recyclerView.scrollToPosition(0);
        }
    }
    private void initRecyclerView(View view) {
//        emptyCart=(CardView) view.findViewById(R.id.empty_cart);
//        checkOutToolbar=(Toolbar)view.findViewById(R.id.checkout_toolbar);
//        coutTxt=(TextView)view.findViewById(R.id.checkout_bar_count);
//        amtTxt=(TextView)view.findViewById(R.id.checkout_bar_amt);
//        checkoutTxt=(TextView)view.findViewById(R.id.checkout_bar);
//        checkoutTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ((ServicesActivity)getActivity()).displayView(1);
////                Intent i=new Intent(activity,ServicesActivity.class);
////                i.putExtra("key", 1);
////                startActivity(i);
////                activity.overridePendingTransition(android.R.anim.fade_in,
////                        android.R.anim.fade_out);
//                Toast.makeText(activity, "Cart CheckOut", Toast.LENGTH_SHORT).show();
////                Fragment fragment=null;
////                fragment = new PlaceOrderFragment();
////                if (fragment != null) {
////                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    fragmentTransaction.replace(R.id.container_services, fragment);
////                    fragmentTransaction.addToBackStack(null);
////                    fragmentTransaction.commit();
////                     set the toolbar title
////                    ((ServicesActivity)getActivity()).getActionBar().setTitle("Place Order");
////                }
//                Intent in=new Intent(activity, CredientialActivity.class);
//                in.putExtra("credKey", TagName.CRED_LOGIN);
//                activity.startActivity(in);
//                activity.overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//            }
//        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        int paddingBottom = Utils.getToolbarHeight(activity);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new OrdersRecyclerAdapter(OrdersFragment.this, services, R.layout.order_inflate);
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
            task.execute(Utils.instantOrdersUrl+userId);
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
                Log.e("services",""+services);
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
                    adapter = new OrdersRecyclerAdapter(OrdersFragment.this, services, R.layout.order_inflate);
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
            JSONObject jsonObject = new JSONObject(result);
//            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                String message = jobstatus.optString(TagName.TAG_MSG);

                if (status==1) {
                    JSONArray jarr=jsonObject.optJSONArray("orderhistory");
                    JSONObject job=jarr.optJSONObject(0);
                    JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                    int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                    String message1 = jobstat.optString(TagName.TAG_MSG);
                    if(status1==1) {
                    JSONArray posts = job.optJSONArray(TagName.TAG_ORDER);

            /*Initialize array if null*/
                    if (null == services) {
                        services = new ArrayList<OrdersModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        OrdersModel item = new OrdersModel();
                        item.setOrderId(post.optString("increment_id"));
                        item.setDate(post.optString("created_at"));
                        item.setStatus(post.optString("status"));
                        item.setGrandTotal(post.optString("grand_total"));
                        item.setQnty(post.optInt("total_qty_ordered"));
                        item.setId(post.optInt("order_item_list"));
                        JSONArray jarray = post.optJSONArray("order_single_itemdetails");
                        JSONObject job1 = jarray.optJSONObject(0);
                        item.setThumbnail(job1.optString(TagName.KEY_THUMB));
                        item.setTitle(job1.optString(TagName.KEY_NAME));
                          /*Initialize array if null*/
//                        if (null == orderList) {
//                            orderList = new ArrayList<ProductModel>();
//                        }
//                        for (int j = 0; j < jarray.length(); j++) {
//                            JSONObject job = jarray.optJSONObject(j);
//                            ProductModel item1 = new ProductModel();
//                            item1.setId(job.optInt("product_id"));
//                            item1.setTitle(job.optString(TagName.KEY_NAME));
////                        item.setDescription(post.optString(TagName.KEY_DES));
//                            item1.setCost(job.optDouble(TagName.KEY_PRICE));
////                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
////                            item.setPayment(post.optString("sku"));
//                            item1.setCount(job.optInt(TagName.KEY_COUNT));
//////                    Log.e("name", "name");
//                            item1.setThumbnail(job.optString(TagName.KEY_THUMB));
//                            orderList.add(item1);
////                            item.addOrderList(item1);
//                            item.setOrderList(orderList);
////                            item.setOd(item1);
//                        }

                        services.add(item);

                    }
                    }else{
                        Toast.makeText(activity, "No Orders", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Net Work Error", Toast.LENGTH_SHORT).show();
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void itemClick(OrdersModel singleOrder1 ){
//         singleOrder= singleOrder1;
//        Log.e("singleOrder",""+singleOrder);
//        Bundle arguments = new Bundle();
//        arguments.putParcelable("singleOrder", singleOrder);
//        Intent in=new Intent(activity,SecondActivity.class);
//        Bundle extras = new Bundle();
//        extras.putInt("key",TagName.ORDER_DETAILS);
//        extras.putParcelable("singleOrder", singleOrder);
////        extras.putString("place", propPlace);
////        extras.putString("station", propStation);
//        in.putExtras(extras);
////        in.putExtra("key",TagName.ORDER_DETAILS);
////        in.putExtra("singleOrder", singleOrder);
////                in.putParcelableArrayListExtra("orderList", orderList);
//       startActivity(in);
//        activity.overridePendingTransition(android.R.anim.fade_in,
//                android.R.anim.fade_out);
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
