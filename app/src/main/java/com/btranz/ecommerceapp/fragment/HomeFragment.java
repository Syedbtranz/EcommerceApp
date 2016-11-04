package com.btranz.ecommerceapp.fragment;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.BrandGridAdapter;
import com.btranz.ecommerceapp.adapter.HorizontalListAdapter;
import com.btranz.ecommerceapp.adapter.ImageSlideAdapter;
import com.btranz.ecommerceapp.adapter.PopProductGridAdapter;
import com.btranz.ecommerceapp.adapter.ProductGlanceAdapter;
import com.btranz.ecommerceapp.json.GetJSONObject;
import com.btranz.ecommerceapp.json.JsonReader;
import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.CirclePageIndicator;
import com.btranz.ecommerceapp.utils.PageIndicator;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment  {
    View rootView;
    private RecyclerView horizontalList, popHorizontalList;
//    private RecyclerView verticalList;
    private HorizontalListAdapter horizontalAdapter;
    GridView  brandGrid;
    ArrayList<ProductModel> dummyServices;
    ArrayList<ProductModel> services;
    ArrayList<ProductModel> popServices;
    ArrayList<ProductModel> brandServices;
    ProductGlanceAdapter adapter;
    PopProductGridAdapter popAdapter;
    BrandGridAdapter brandAdapter;
    ProgressFragment prgs;
    ProgressBar pb;
    public HomeFragment() {
        // Required empty public constructor
    }
    public static final String ARG_ITEM_ID = "home_fragment";

    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    // UI References
    private ViewPager mViewPager;
    TextView glanceTxt, topBuyTxt;
    PageIndicator mIndicator;

    AlertDialog alertDialog;

    List<Product> products;
    RequestImgTask task;
    AsyncHttpTask taskAsynk;
    PopAsyncHttpTask taskPop;
    BrandAsyncHttpTask taskBrand;
    boolean stopSliding = false;
    String message;

    private Runnable animateViewPager;
    private Handler handler;

    FragmentActivity activity;
    private String[] arraySpinner;
    Button glanceBtn, bestSellerBtn;
    LinearLayout progressLL,glanceProgressLL,popProgressLL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
//        sendRequest();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);


        findViewById(rootView);

        //Page Scroller
        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (products != null && products.size() != 0) {
                            stopSliding = false;
                            runnable(products.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });

      //Glance Action
        glanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) activity).showFabs();
                glanceTxt=(TextView) rootView.findViewById(R.id.glance_txt);
                Intent in=new Intent(activity,SecondActivity.class);
                in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
                in.putExtra("prdtsUrl", Utils.prdtUrl);
                in.putExtra("prdtsTitle", glanceTxt.getText().toString());
                activity.startActivity(in);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
//                Bundle arguments = new Bundle();
//                Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                Product product = (Product) products.get(position);
////                arguments.putParcelable("singleProduct", product);
//
//                // Start a new fragment
//                fragment = new ProductsFragment();
////                fragment.setArguments(arguments);
//
//                FragmentTransaction transaction = activity
//                        .getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_second, fragment,
//                        TagName.FRAGMENT_PRODUCTS);
//                transaction.addToBackStack(TagName.FRAGMENT_PRODUCTS);
//                transaction.commit();
            }
        });
        bestSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBuyTxt=(TextView) rootView.findViewById(R.id.top_buys_txt);
               Intent in=new Intent(activity,SecondActivity.class);
                in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
                in.putExtra("prdtsUrl", Utils.popPrdtUrl);
                in.putExtra("prdtsTitle", topBuyTxt.getText().toString());
                activity.startActivity(in);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
//                Bundle arguments = new Bundle();
//                Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                Product product = (Product) products.get(position);
////                arguments.putParcelable("singleProduct", product);
//
//                // Start a new fragment
//                fragment = new ProductsFragment();
////                fragment.setArguments(arguments);
//
//                FragmentTransaction transaction = activity
//                        .getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_second, fragment,
//                        TagName.FRAGMENT_PRODUCTS);
//                transaction.addToBackStack(TagName.FRAGMENT_PRODUCTS);
//                transaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    private void findViewById(View view) {

        progressLL=(LinearLayout)view.findViewById(R.id.progress_ll);
        glanceProgressLL=(LinearLayout)view.findViewById(R.id.glance_progress_ll);
        popProgressLL=(LinearLayout)view.findViewById(R.id.pop_progress_ll);
        pb=(ProgressBar) view.findViewById(R.id.progressbar);
//        DisplayMetrics metrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        horizontalList = (RecyclerView)view.findViewById(R.id.glance_horizontal_recycler);
        horizontalList.setNestedScrollingEnabled(false);
        horizontalList.setHasFixedSize(true);
        popHorizontalList = (RecyclerView)view.findViewById(R.id.pop_horizontal_recycler);
        popHorizontalList.setNestedScrollingEnabled(false);
        popHorizontalList.setHasFixedSize(true);
        //set horizontal LinearLayout as layout manager to creating horizontal list view
        LinearLayoutManager horizontalManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalManager1 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(horizontalManager);
        popHorizontalList.setLayoutManager(horizontalManager1);
//        horizontalAdapter = new HorizontalListAdapter(HomeFragment.this,services);
//        horizontalList.setAdapter(horizontalAdapter);
//        grid=(Gallery) view.findViewById(R.id.grid);
        // set gallery to left side
//        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) grid.getLayoutParams();
//        mlp.setMargins(-(metrics.widthPixels / 2 + (120/2)), mlp.topMargin,
//                mlp.rightMargin, mlp.bottomMargin);
//        grid.setSelection(View.TEXT_ALIGNMENT_VIEW_START);
//        popGrid=(Gallery) view.findViewById(R.id.pop_grid);
//        ViewGroup.MarginLayoutParams mlp1 = (ViewGroup.MarginLayoutParams) popGrid.getLayoutParams();
//        mlp1.setMargins(-(metrics.widthPixels / 2 + (120/2)), mlp1.topMargin,
//                mlp1.rightMargin, mlp1.bottomMargin);
        brandGrid=(GridView)view.findViewById(R.id.brand_grid);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        glanceBtn=(Button) view.findViewById(R.id.glance_btn);
        bestSellerBtn=(Button) view.findViewById(R.id.bestseller_btn);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
//        imgNameTxt = (TextView) view.findViewById(R.id.img_name);
    }

    private void initList() {
        // We populate the planets
         /*Initialize array if null*/
        if (null == dummyServices) {
            dummyServices = new ArrayList<ProductModel>();
        }
         for(int i=0;i<Utils.dummyBanners.length;i++) {
            ProductModel item = new ProductModel();
            item.setTitle(Utils.dummyBanners[i]);
            dummyServices.add(item);
            Log.e("initList",Utils.dummyBanners[i]);
            System.out.println(dummyServices.toString());
        }

//    planetsList.add(new Planet("Mars", 30));
//    planetsList.add(new Planet("Jupiter", 40));
//    planetsList.add(new Planet("Saturn", 50));
//    planetsList.add(new Planet("Uranus", 60));
//    planetsList.add(new Planet("Neptune", 70));


    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        if (products == null) {
            sendRequest();
        } else {
            progressLL.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            mViewPager.setAdapter(new ImageSlideAdapter(activity, products,
                    HomeFragment.this));

            mIndicator.setViewPager(mViewPager);
//            imgNameTxt.setText(""
//                    + ((Product) products.get(mViewPager.getCurrentItem()))
//                    .getName());
            runnable(products.size());
            //Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
//            grid.setAdapter(new ProductGlanceAdapter(activity, services));
//            popGrid.setAdapter(new PopProductGridAdapter(activity, popServices));
//            brandGrid.setAdapter(new BrandGridAdapter(activity, brandServices));

        }

    }


    @Override
    public void onPause() {
        if (task != null)
            task.cancel(true);
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
        super.onPause();
    }

    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
            //offer banner
            task = new RequestImgTask(activity);
            task.execute(Utils.offerBannerUrl);
            //Product on Glance
            taskAsynk = new AsyncHttpTask();
            taskAsynk.execute(Utils.prdtUrl);
            //Popular Products
            taskPop = new PopAsyncHttpTask();
            taskPop.execute(Utils.popPrdtUrl);
            //Brands
            taskBrand = new BrandAsyncHttpTask();
            taskBrand.execute(Utils.brandUrl);
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


    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (products != null) {
//                    imgNameTxt.setText(""
//                            + ((Product) products.get(mViewPager
//                            .getCurrentItem())).getName());
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class RequestImgTask extends AsyncTask<String, Void, List<Product>> {
        private final WeakReference<Activity> activityWeakRef;
        Throwable error;

        public RequestImgTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Product> doInBackground(String... urls) {
            try {
                JSONArray jsonArray = getJsonObject(urls[0]);
                JSONObject jsonObject=jsonArray.getJSONObject(0);

                if (jsonObject != null) {
                    JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                    int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);

                    if (status==1) {
//                        JSONObject jsonData = jsonObject
//                                .getJSONObject(TagName.TAG_OFFER_BANNER);
//                        if (jsonData != null) {
                            products = JsonReader.getHome(jsonObject);

//                        } else {
//                            message = jsonObject.getString(TagName.TAG_DATA);
//                        }
                    } else {
                        message = jsonObject.getString(TagName.TAG_DATA);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return products;
        }

        /**
         * It returns jsonObject for the specified url.
         *
         * @param url
         * @return JSONObject
         */
        public JSONArray getJsonObject(String url) {
            JSONArray jsonObject = null;
            try {
                jsonObject = GetJSONObject.getJSONObject(url);
//                System.out.println("offer json "+ jsonObject);
            } catch (Exception e) {
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            super.onPostExecute(result);
//            Log.e("offer banners",result.toString());
            System.out.println("offer banners"+result);
            if (activityWeakRef != null && !activityWeakRef.get().isFinishing()) {
                if (error != null && error instanceof IOException) {
                    message = getResources().getString(R.string.time_out);
                    showAlertDialog(message, true);
                } else if (error != null) {
                    message = getResources().getString(R.string.error_occured);
                    showAlertDialog(message, true);
                } else {
                    products = result;
                    if (result != null) {
                        if (products != null && products.size() != 0) {

                            mViewPager.setAdapter(new ImageSlideAdapter(
                                    activity, products, HomeFragment.this));

                            mIndicator.setViewPager(mViewPager);
//                            imgNameTxt.setText(""
//                                    + ((Product) products.get(mViewPager
//                                    .getCurrentItem())).getName());
                            runnable(products.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY);
                        } else {
//                            imgNameTxt.setText("No Products");
                        }
                    } else {
                    }
                }
            }
        }
    }

    //Product on Glance
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
            prgs = new ProgressFragment();
            glanceProgressLL.setVisibility(View.VISIBLE);
//            progressLL.setVisibility(View.VISIBLE);
//            pb.setVisibility(View.VISIBLE);
//            activity.getFragmentManager().beginTransaction().add(R.id.container_body, prgs).commit();
//            activity.setProgressBarIndeterminateVisibility(true);
//            progressBar.setVisibility(View.VISIBLE);
//            layout.setVisibility(View.VISIBLE);

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
                Log.d("hello", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
//            activity.getFragmentManager().beginTransaction().remove(prgs).commit();
//            activity.setProgressBarIndeterminateVisibility(false);
            glanceProgressLL.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);

            /* Download complete. Lets update UI */
            if (result == 1) {
                Log.e("onPostExecute", "Asyntask");
                horizontalAdapter = new HorizontalListAdapter(activity, services,R.layout.gallary_inflate);
                horizontalList.setAdapter(horizontalAdapter);
//                grid1.setAdapter(adapter);
                horizontalAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
            } else {
                Log.e("hello", "Failed to fetch data!");
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
//                    item.setCount(post.optInt("finalPrice"));
//                    Log.e("name", "name"+ post.optDouble("finalPrice"));
                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item.setShare(post1.optString(TagName.KEY_SHARE));
                        item.setTag(post1.optString(TagName.KEY_TAG));
                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item.setRating(post1.optInt(TagName.KEY_RATING));
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

    //Product On Top Buys
    public class PopAsyncHttpTask extends AsyncTask<String, Void, Integer> {
//        ProgressFragment prgs;

        @Override
        protected void onPreExecute() {
//            prgs = new ProgressFragment();
//            activity.getFragmentManager().beginTransaction().add(R.id.container_body, prgs).commit();
//            activity.setProgressBarIndeterminateVisibility(true);
            popProgressLL.setVisibility(View.VISIBLE);
//            layout.setVisibility(View.VISIBLE);

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
                    parsePopResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("hello", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
//            activity.getFragmentManager().beginTransaction().remove(prgs).commit();
//            activity.setProgressBarIndeterminateVisibility(false);
            popProgressLL.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);

            /* Download complete. Lets update UI */
            if (result == 1) {
                Log.e("onPostExecute", "onPostExecute");
                horizontalAdapter = new HorizontalListAdapter(activity, popServices,R.layout.gallary_inflate);
                popHorizontalList.setAdapter(horizontalAdapter);
                horizontalAdapter.notifyDataSetChanged();
//                popAdapter = new PopProductGridAdapter(activity, popServices);
////                grid.setAdapter(adapter);
//                popGrid.setAdapter(popAdapter);
//                popAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
            } else {
                Log.e("hello", "Failed to fetch data!");
            }
        }
    }
    private void parsePopResult(String result) {
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
                    if (null == popServices) {
                        popServices = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        ProductModel item = new ProductModel();
                        item.setId(post.optInt(TagName.KEY_ID));
                        item.setTitle(post.optString(TagName.KEY_NAME));
                        item.setDescription(post.optString(TagName.KEY_DES));
                        item.setCost(post.optDouble(TagName.KEY_PRICE));
                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                    Log.e("name", "name"+ post.optDouble("finalPrice"));
                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item.setShare(post1.optString(TagName.KEY_SHARE));
                        item.setTag(post1.optString(TagName.KEY_TAG));
                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item.setRating(post1.optInt(TagName.KEY_RATING));
                        popServices.add(item);
                    }
                } else {
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class BrandAsyncHttpTask extends AsyncTask<String, Void, Integer> {
//        ProgressFragment prgs;

        @Override
        protected void onPreExecute() {
//            prgs = new ProgressFragment();
//            activity.getFragmentManager().beginTransaction().add(R.id.container_body, prgs).commit();
//            activity.setProgressBarIndeterminateVisibility(true);
//            progressBar.setVisibility(View.VISIBLE);
            progressLL.setVisibility(View.VISIBLE);

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
                    parseBrandResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("hello", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


//            activity.setProgressBarIndeterminateVisibility(false);
//            progressBar.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);

            /* Download complete. Lets update UI */
            if (result == 1) {
                Log.e("onPostExecute", "onPostExecute");
                brandAdapter = new BrandGridAdapter(activity, brandServices);
                brandGrid.setAdapter(brandAdapter);
                brandAdapter.notifyDataSetChanged();
                progressLL.setVisibility(View.GONE);
//                pb.setVisibility(View.GONE);
//                activity.getFragmentManager().beginTransaction().remove(prgs).commit();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
            } else {
                Log.e("hello", "Failed to fetch data!");
            }
        }
    }
    private void parseBrandResult(String result) {
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
                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_BRAND);

            /*Initialize array if null*/
                    if (null == brandServices) {
                        brandServices = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        ProductModel item = new ProductModel();
                        item.setId(post.optInt(TagName.KEY_ID));
//                        item.setTitle(post.optString("name"));
//                        item.setDescription(post.optString("description"));
//                        item.setCost(post.optDouble("price"));
//                        item.setFinalPrice(post.optDouble("finalPrice"));
//                    item.setCount(post.optInt("finalPrice"));
                        Log.e("name", "name"+ post.optInt("id"));
                        item.setThumbnail(post.optString(TagName.KEY_IMG_URL));
                        brandServices.add(item);
                    }
                } else {
                    message = jsonObject.getString(TagName.TAG_BRAND);
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
