package com.btranz.ecommerceapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.fragment.CartFragment;
import com.btranz.ecommerceapp.fragment.CategoriesFragment;
import com.btranz.ecommerceapp.fragment.OfferDetailFragment;
import com.btranz.ecommerceapp.fragment.ProductItemFragment;
import com.btranz.ecommerceapp.fragment.ProductsFragment;
import com.btranz.ecommerceapp.fragment.ProfileFragment;
import com.btranz.ecommerceapp.fragment.WishlistFragment;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;


public class SecondActivity extends AppCompatActivity {

    ImageView SearchET;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    public Toolbar mToolbar;
    public TextView toolbarTitle;
    public LinearLayout searchBar;

    FragmentActivity activity;
    private RecyclerView recyclerView;

    AlertDialog alertDialog;
    private Runnable animateViewPager;
    private Handler handler;

//    RequestImgTask task;
//    AsyncHttpTask task;
    boolean stopSliding = false;
    String message, title;

    private int mToolbarHeight;
    Fragment fragment = null;
    MenuItem item;
    public TextView cartCountTv;

String customerEmail, cartBadge;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    //SharedPreferences
//public static final String PREFS_NAME = "MyPrefs";
//    SharedPreferences mPrefs;//= getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppThemeRed);
//        setTheme(R.style.AppThemeBlue);
        sharedpreferences= getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        // MUST BE SET BEFORE setContentView
//        Utils.onActivityCreateSetTheme(this,mPrefs.getInt("PREF_SPINNER", 0));
        // AFTER SETTING THEME
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        activity=this;
        initToolbar();
        Log.e("onCreate", "onCreate");
//        mFabButton = (ImageButton) findViewById(R.id.fabButton);
//        initRecyclerView();
//        if (services == null) {
//            sendRequest();
//        } else {
//            recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//            recyclerView.scrollToPosition(0);
//        }
        int key=getIntent().getIntExtra("key", 0);
//        if(key==1) {
            displayView(key);
//        }else {
//            displayView(0);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void displayView(int id){
        switch (id){
            case TagName.FRAGMENT_PRODUCTS:
                fragment = new ProductsFragment();
//                title = "PRODUCTS";
                break;
            case TagName.CART_ID:
                fragment = new CartFragment();
//                title = getString(R.string.title_cart);
                break;
            case TagName.FRAGMENT_PRODUCT_DETAILS:
                fragment = new ProductItemFragment();
//                title = getString(R.string.title_product_details);
                break;
            case TagName.FRAGMENT_OFFER_BANNER:
                fragment = new OfferDetailFragment();
//                title = getString(R.string.title_product_details);
                break;
            case TagName.CATG_ID:
                fragment = new CategoriesFragment();
//                title = getString(R.string.title_product_details);
                break;
            case TagName.WISH_ID:
                fragment = new WishlistFragment();
//                title = getString(R.string.title_product_details);
                break;
            case TagName.CRED_PROFILE:
                fragment = new ProfileFragment();
//                title=getString(R.string.title_profile);
                break;
        }
//        SpannableString s = new SpannableString(title);
//                   s.setSpan(new TypefaceSpan(getApplicationContext(), "hallo_sans_black.otf"), 0, s.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_second, fragment);//.addToBackStack("flag").commit();
            fragmentTransaction.commit();

            // set the toolbar title
//            getSupportActionBar().setTitle(s);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {


            getMenuInflater().inflate(R.menu.menu, menu);
            item = menu.findItem(R.id.action_cart);
//        sharedpreferences= getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            cartBadge = sharedpreferences.getString("cartBadge", "");
            if (cartBadge.equals("")) {
                writeBadge(0);
            } else {
                writeBadge(Integer.parseInt(cartBadge));
            }
        }catch (InflateException e){

        }
//        MenuItemCompat.setActionView(item, R.layout.cart_count);
//        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
//
//        cartCountTv = (TextView) notifCount.findViewById(R.id.cart_count_tv);
//        cartCountTv.setText("12");
////        int id = item.getItemId();
//        item.getActionView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(item);
////                if (v.getId() == R.id.action_cart) {
////                    Intent in=new Intent(getApplicationContext(),SecondActivity.class);
////                    in.putExtra("key", TagName.CART_ID);
////                    startActivity(in);
////                    getParent().overridePendingTransition(android.R.anim.fade_in,
////                            android.R.anim.fade_out);
////
////                }
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }
    public void writeBadge(int count) {
//        item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.cart_count);
        RelativeLayout layout = (RelativeLayout) MenuItemCompat.getActionView(item);
        // A TextView with number.
        TextView tv = (TextView) layout.findViewById(R.id.cart_count_tv);
        if (count == 0) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(String.valueOf(count));
        }
        // An icon, it also must be clicked.
        ImageView cartImage = (ImageView) layout.findViewById(R.id.cart_count_img);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        };
        item.getActionView().setOnClickListener(onClickListener);
        cartImage.setOnClickListener(onClickListener);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
//            displayView(TagName.CART_ID);
//            Toast.makeText(getApplicationContext(),"cart",Toast.LENGTH_SHORT).show();
            Bundle arguments = new Bundle();
            Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

            // Start a new fragment
            fragment = new CartFragment();
//                fragment.setArguments(arguments);

            FragmentTransaction transaction = activity
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_second, fragment,
                    TagName.CART_FRAG);
            transaction.addToBackStack(TagName.CART_FRAG);
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
//    @Override
//    protected void onResume() {
//        if (services == null) {
//            sendRequest();
////            adapter = new ServicesRecyclerAdapter(activity, services);
//            Log.e("onResume", "onResume");
//        } else {
//            Log.e("onResume else", "onResume else");
//            recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//            recyclerView.scrollToPosition(0);
//        }
//        super.onResume();
//    }
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

//    private void sendRequest() {
//        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
////            task = new RequestImgTask(activity);
////            task.execute(url);
//            task = new AsyncHttpTask();
//            task.execute(url);
//            Log.e("sendrequest","sendrequest");
//        } else {
//            message = getResources().getString(R.string.no_internet_connection);
//            showAlertDialog(message, true);
//        }
//    }
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
//    private class RequestImgTask extends AsyncTask<String, Void, List<Product>> {
//        private final WeakReference<Activity> activityWeakRef;
//        Throwable error;
//
//        public RequestImgTask(Activity context) {
//            this.activityWeakRef = new WeakReference<Activity>(context);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<Product> doInBackground(String... urls) {
//            try {
//                JSONObject jsonObject = getJsonObject(urls[0]);
//                if (jsonObject != null) {
//                    boolean status = jsonObject.getBoolean(TagName.TAG_STATUS);
//
//                    if (status) {
//                        JSONObject jsonData = jsonObject
//                                .getJSONObject(TagName.TAG_DATA);
//                        if (jsonData != null) {
//                            services = JsonReader.getHome(jsonData);
//
//                        } else {
//                            message = jsonObject.getString(TagName.TAG_DATA);
//                        }
//                    } else {
//                        message = jsonObject.getString(TagName.TAG_DATA);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return services;
//        }
//
//        /**
//         * It returns jsonObject for the specified url.
//         *
//         * @param url
//         * @return JSONObject
//         */
//        public JSONObject getJsonObject(String url) {
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = GetJSONObject.getJSONObject(url);
//            } catch (Exception e) {
//            }
//            return jsonObject;
//        }
//
//        @Override
//        protected void onPostExecute(List<Product> result) {
//            super.onPostExecute(result);
//
//            if (activityWeakRef != null && !activityWeakRef.get().isFinishing()) {
//                if (error != null && error instanceof IOException) {
//                    message = getResources().getString(R.string.time_out);
//                    showAlertDialog(message, true);
//                } else if (error != null) {
//                    message = getResources().getString(R.string.error_occured);
//                    showAlertDialog(message, true);
//                } else {
//                    services = result;
//                    if (result != null) {
//                        if (services != null && services.size() != 0) {
//
//                            recyclerView.setAdapter(new ServicesRecyclerAdapter(
//                                    activity, services));
//                            recyclerView.scrollToPosition(0);
////                            mIndicator.setViewPager(mViewPager);
////                            imgNameTxt.setText(""
////                                    + ((Product) products.get(mViewPager
////                                    .getCurrentItem())).getName());
////                            runnable(services.size());
////                            handler.postDelayed(animateViewPager,
////                                    ANIM_VIEWPAGER_DELAY);
//                        } else {
////                            imgNameTxt.setText("No Products");
//                        }
//                    } else {
//                    }
//                }
//            }
//        }
//    }

//    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
//
//        @Override
//        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
//        }
//
//        @Override
//        protected Integer doInBackground(String... params) {
//            InputStream inputStream = null;
//            Integer result = 0;
//            HttpURLConnection urlConnection = null;
//
//            try {
//                /* forming th java.net.URL object */
//                URL url = new URL(params[0]);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                /* for Get request */
//                urlConnection.setRequestMethod("GET");
//
//                int statusCode = urlConnection.getResponseCode();
//
//                /* 200 represents HTTP OK */
//                if (statusCode ==  200) {
//
//                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = r.readLine()) != null) {
//                        response.append(line);
//                    }
//                    Log.e("response.toString()", response.toString());
//                    parseResult(response.toString());
//                    result = 1; // Successful
//                }else{
//                    result = 0; //"Failed to fetch data!";
//                }
//
//            } catch (Exception e) {
//                Log.d("hello", e.getLocalizedMessage());
//            }
//
//            return result; //"Failed to fetch data!";
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//
//            setProgressBarIndeterminateVisibility(false);
//
//            /* Download complete. Lets update UI */
//            if (result == 1) {
//                Log.e("onPostExecute", "onPostExecute");
//                adapter = new ServicesRecyclerAdapter(activity, services);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
////                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//            } else {
//                Log.e("hello", "Failed to fetch data!");
//            }
//        }
//    }
//    private void parseResult(String result) {
//        try {
//            JSONObject response = new JSONObject(result);
//
//                    boolean status = response.getBoolean(TagName.TAG_STATUS);
//
////                    if (status) {
//                        JSONObject jsonData = response
//                                .getJSONObject(TagName.TAG_DATA);
////                    }
//            JSONArray posts = jsonData.optJSONArray(TagName.TAG_PRODUCTS);
//
//            /*Initialize array if null*/
//            if (null == services) {
//                services = new ArrayList<ServicesModel>();
//            }
//
//            for (int i = 0; i < posts.length(); i++) {
//                JSONObject post = posts.optJSONObject(i);
//
//                ServicesModel item = new ServicesModel();
//                item.setTitle(post.optString("name"));
//                item.setCost(post.optInt("cost"));
//                Log.e("name","name");
//                item.setThumbnail(post.optString("image_url"));
//                services.add(item);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=(TextView)mToolbar.findViewById(R.id.toolbar_txt);
        searchBar=(LinearLayout)findViewById(R.id.search_bar);
        setSupportActionBar(mToolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(activity, R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
//                Toast.makeText(getApplicationContext(),"hai",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(ServicesActivity.this,MainActivity.class));
//                finish();
                onBackPressed();
            }
        });
//        setTitle("Services");
//        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
//        mToolbarHeight = Utils.getToolbarHeight(this);
        SearchET = (ImageView)findViewById(R.id.search_text);
        SearchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),SearchActivity.class);
//                            in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
//                            in.putExtra("prdtsUrl", Utils.searchUrl+v.getText().toString());
//                            Log.e("prdtsUrl",Utils.searchUrl+v.getText().toString());
                startActivity(in);
                SecondActivity.this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });
//        SearchET = (EditText)findViewById(R.id.search_text);
//        SearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    if(!TextUtils.isEmpty(v.getText().toString())){
//                        //Text entered
//                        Bundle arguments = new Bundle();
//                        Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                ProductModel product = (ProductModel) services.get(position);
////                arguments.putParcelable("singleProduct", product);
//                        arguments.putString("prdtsUrl", Utils.searchUrl+v.getText().toString());
//                        // Start a new fragment
//                        fragment = new ProductsFragment();
//                        fragment.setArguments(arguments);
//
//                        FragmentTransaction transaction = activity
//                                .getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.container_second, fragment,
//                                ProductsFragment.PRDTS_FRAG);
//                        transaction.addToBackStack(ProductsFragment.PRDTS_FRAG);
//                        transaction.commit();
////                        displayView(key);
////                        Intent in=new Intent(getApplicationContext(),SecondActivity.class);
////                        in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
////                        in.putExtra("prdtsUrl", Utils.searchUrl+v.getText().toString());
////                        Log.e("prdtsUrl",Utils.searchUrl+v.getText().toString());
////                        startActivity(in);
////                        SecondActivity.this.overridePendingTransition(android.R.anim.fade_in,
////                                android.R.anim.fade_out);
//                    }
//                    else {
//                        //no string
//                    }
//                }
//                return false;
//            }
//
//        });
    }

    @Override
    public Resources.Theme getTheme() {
        Resources res = getResources();
        Resources.Theme theme = super.getTheme();

        String theme_id = res.getString(R.string.theme);
        switch (theme_id)
        {
            case "Default":

                theme.applyStyle(R.style.AppTheme, true);
                break;

            case "Blue":

                theme.applyStyle(R.style.AppTheme_blue, true);
                break;
            case "Yellow":

                theme.applyStyle(R.style.AppTheme_yellow, true);
                break;

        }
        // you could also use a switch if you have many themes that could apply
        return theme;
    }



}