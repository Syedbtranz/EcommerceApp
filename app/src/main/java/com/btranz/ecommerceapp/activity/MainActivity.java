package com.btranz.ecommerceapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
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
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.adapter.WislistRecyclerAdapter;
import com.btranz.ecommerceapp.fragment.CardsFragment;
import com.btranz.ecommerceapp.fragment.CategoriesFragment;
import com.btranz.ecommerceapp.fragment.HelpCenterFragment;
import com.btranz.ecommerceapp.fragment.HomeFragment;
import com.btranz.ecommerceapp.fragment.OrdersFragment;
import com.btranz.ecommerceapp.fragment.ReferEarnFragment;
import com.btranz.ecommerceapp.fragment.WishlistFragment;
import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.AppData;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.message;
//import com.btranz.ecamarceapp.utils.AppData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Boolean exit = false;
//    EditText SearchET;
    ImageView SearchET;
    Handler handler;
    public Toolbar toolbar;
    int key=R.id.nav_home;
    Fragment fragment = null;
    String userEmail, cartBadge;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public TextView viewTxt, toolbar_title;
    MenuItem item;
    public List<ProductModel> products;
    public ArrayList<ProductModel> dummyServices;
    public ArrayList<ProductModel> services;
    public ArrayList<ProductModel> popServices;
    public ArrayList<ProductModel> brandServices;
    public ArrayList<ProductModel> wishlistServices;
    String message, userId;
    HomeAsyncHttpTask taskHome;
    WishlistAsyncTask taskWishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        sharedpreferences = this.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userID", "");
        sendRequest();
        setContentView(R.layout.activity_main);


        if(!AppData.currentFlag) {
            handler = new Handler();
            final View splash = findViewById(R.id.splash);
            TextView splashTxt = (TextView) findViewById(R.id.splash_txt);
            SpannableString s1 = new SpannableString(getString(R.string.app_name));
            try {

                s1.setSpan(new TypefaceSpan(getApplicationContext(), "hallo_sans_black.otf"), 0, s1.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }catch (Exception e){

            }
            splashTxt.setText(s1);

            //Theme
            Resources res = getResources();
            String theme_id = res.getString(R.string.theme);
            switch (theme_id)
            {
                case "Default":

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        //  splash.setBackground(res.getDrawable(R.drawable.splash));
                        //splash.setBackground(res.getDrawable(R.drawable.splash));
                        splash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.splash, null));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            splash.setBackground(getDrawable(R.drawable.splash));
                        }
                    }else {
                        // splash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.splash, null));
                        splash.setBackgroundDrawable(getResources().getDrawable(R.drawable.splash));
                    }

                    break;

                case "Blue":


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        //  splash.setBackground(res.getDrawable(R.drawable.splash));
                        //splash.setBackground(res.getDrawable(R.drawable.splash));
                        splash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.splash_blue, null));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            splash.setBackground(getDrawable(R.drawable.splash_blue));
                        }
                    }else {
                        // splash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.splash, null));
                        splash.setBackgroundDrawable(getResources().getDrawable(R.drawable.splash_blue));
                    }

                    break;
                case "Yellow":


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        //  splash.setBackground(res.getDrawable(R.drawable.splash));
                        //splash.setBackground(res.getDrawable(R.drawable.splash));
                        splash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.splash_yellow, null));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            splash.setBackground(getDrawable(R.drawable.splash_yellow));
                        }
                    }else {
                        // splash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.splash, null));
                        splash.setBackgroundDrawable(getResources().getDrawable(R.drawable.splash_yellow));
                    }

                    break;

            }

//            Drawable mDrawable = getApplicationContext().getResources().getDrawable(R.drawable.splash);
//            mDrawable.setColorFilter(new
//                    PorterDuffColorFilter(0xffff00, PorterDuff.Mode.SRC_IN));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                splash.setBackground(mDrawable);
//            }
            splash.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void run() {


                    splash.animate().alpha(0.5f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            splash.setVisibility(View.GONE);
                            initToolbar();
//                            doInitialize();
                            AppData.currentFlag=true;
                        }
                    }).setDuration(8000).start();

                }
            }, 0);
        }else{
            initToolbar();
//            doInitialize();
        }

//        init();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
//            super.onBackPressed();
            //additional code
            //        MainActivity.this.finish();
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        try {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.menu, menu);
            item = menu.findItem(R.id.action_cart);
            sharedpreferences = getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
//        customerName = sharedpreferences.getString("customerName", "");
            cartBadge = sharedpreferences.getString("cartBadge", "");
            if (cartBadge.equals("")) {
                writeBadge(0);
            } else {
                writeBadge(Integer.parseInt(cartBadge));
            }
//        }catch (InflateException e){
//
//        }
       /* MenuItemCompat.setActionView(item, R.layout.cart_count);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

         final TextView cartCountTv = (TextView) notifCount.findViewById(R.id.cart_count_tv);
        cartCountTv.setText("12");
//        int id = item.getItemId();
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
//                if (v.getId() == R.id.action_cart) {
//                    Intent in=new Intent(getApplicationContext(),SecondActivity.class);
//                    in.putExtra("key", TagName.CART_ID);
//                    startActivity(in);
//                    getParent().overridePendingTransition(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
//
//                }
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }
    private void writeBadge(int count) {
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
            Intent in=new Intent(getApplicationContext(),SecondActivity.class);
            in.putExtra("key", TagName.CART_ID);
            startActivity(in);
            this.overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayView(id);
//        if (id == R.id.nav_home) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//           /* Intent intent = new Intent(this, ScrollingActivity.class);
//            startActivity(intent);*/
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        }
       /* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume() {

        super.onResume();

        initToolbar();
        try {
            handler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void run() {

                    init();
                }
            }, 1000);
        }catch (Exception e){

        }
    }
    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title =(TextView)toolbar.findViewById(R.id.toolbar_txt);
//        toolbar.setLogo(R.mipmap.logo_icon);
//        toolbar.setTitle(;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }
    public void init(){

                try {
                    userEmail = sharedpreferences.getString("userEmail", "");
                    LinearLayout slideLL = (LinearLayout) findViewById(R.id.slide);
                    TextView emailTxt = (TextView) findViewById(R.id.email_txt);

                    viewTxt = (TextView) findViewById(R.id.view_profile);
                    if (userEmail.equals("")) {
                        userEmail = "guest@example.com";
                        viewTxt.setVisibility(View.GONE);
                    } else {
                        viewTxt.setVisibility(View.VISIBLE);
                    }

                    emailTxt.setText(userEmail);

                    viewTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
//                        }
                            Intent in = new Intent(getApplicationContext(), CredientialActivity.class);
                            in.putExtra("credKey", TagName.CRED_PROFILE);
                            startActivity(in);
                            (MainActivity.this).overridePendingTransition(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                        }
                    });

                    SearchET = (ImageView)findViewById(R.id.search_text);
                    SearchET.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in=new Intent(getApplicationContext(),SearchActivity.class);
//                            in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
//                            in.putExtra("prdtsUrl", Utils.searchUrl+v.getText().toString());
//                            Log.e("prdtsUrl",Utils.searchUrl+v.getText().toString());
                            startActivity(in);
                            MainActivity.this.overridePendingTransition(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                        }
                    });
                    /*SearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                if(!TextUtils.isEmpty(v.getText().toString())){
                                    //Text entered
                                    Intent in=new Intent(getApplicationContext(),SecondActivity.class);
                                    in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
                                    in.putExtra("prdtsUrl", Utils.searchUrl+v.getText().toString());
                                    Log.e("prdtsUrl",Utils.searchUrl+v.getText().toString());
                                    startActivity(in);
                                    MainActivity.this.overridePendingTransition(android.R.anim.fade_in,
                                            android.R.anim.fade_out);
                                }
                                else {
                                    //no string
                                }
                            }
                            return false;
                        }

                    });*/


                }catch (Exception e){

                }


//    }else{
//        initToolbar();
//        doInitialize();
//    }
    }
    public  void doInitialize(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        key=getIntent().getIntExtra("key", 0);
//        if(key==TagName.FRAGMENT_PRODUCTS) {
            displayView(key);
//        }else {
//            displayView(R.id.nav_home);
//        }
    }
    public void displayView(int id) {
//        Fragment fragment = null;
//        if(profileLRB!=null) {
//            showFabs();
//        }
        String title = getString(R.string.app_name);
        switch (id) {

            case R.id.nav_home:
                fragment = new HomeFragment();
//                title = getString(R.string.title_home);
                break;
            case R.id.nav_catg:
//                fragment = new CategoriesFragment();
//                title = getString(R.string.title_catg);
                Intent in=new Intent(getApplicationContext(),SecondActivity.class);
                in.putExtra("key", TagName.CATG_ID);
                startActivity(in);
                this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;
            case R.id.nav_ref_ern:
//                fragment = new ReferEarnFragment();
//                title = getString(R.string.title_ref_ern);
                Intent inRe = new Intent(getApplicationContext(), CredientialActivity.class);
                inRe.putExtra("credKey",TagName.REFER_ERN_ID);
                startActivity(inRe);
                this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;
            case R.id.nav_orders:
                Intent i=new Intent(getApplicationContext(),SecondActivity.class);
                i.putExtra("key", TagName.ORDER_LIST);
                startActivity(i);
                this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
//                fragment = new OrdersFragment();
//                title = getString(R.string.title_orders);
                break;
            case R.id.nav_help:
                fragment = new HelpCenterFragment();
//                title = getString(R.string.title_help_center);
                break;
            case R.id.nav_cards:
//                fragment = new CardsFragment();
//                title = getString(R.string.title_cards);
                Intent inCards = new Intent(getApplicationContext(), CredientialActivity.class);
                inCards.putExtra("credKey", TagName.CARDS_ID);
                startActivity(inCards);
                this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;
            case R.id.nav_wishlist:
                Intent iwish=new Intent(getApplicationContext(),SecondActivity.class);
                iwish.putExtra("key", TagName.WISH_ID);
                startActivity(iwish);
                this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
//                fragment = new WishlistFragment();
//                title = getString(R.string.title_wislist);
                break;
            default:
                break;
        }
        SpannableString s = new SpannableString(title);
        try {

            s.setSpan(new TypefaceSpan(getApplicationContext(), "hallo_sans_black.otf"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception e){

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);//.addToBackStack(null).commit();
            fragmentTransaction.commit();



            // set the toolbar title
            toolbar_title.setText(s);
//            getSupportActionBar().setTitle(s);



        }

    }
    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
            //Home
            String user;
            if(userId.equals("")){
                user="0";
            }else{
                user=userId;
            }
            taskHome = new HomeAsyncHttpTask();
            taskHome.execute(Utils.homeUrl+user);
//            //offer banner
//            task = new RequestImgTask(activity);
//            task.execute(Utils.bannerUrl);
//            //Product on Glance
//            taskAsynk = new AsyncHttpTask();
//            taskAsynk.execute(Utils.prdtGlanceUrl);
//            //Popular Products
//            taskPop = new PopAsyncHttpTask();
//            taskPop.execute(Utils.popPrdtUrl);
//            //Brands
//            taskBrand = new BrandAsyncHttpTask();
//            taskBrand.execute(Utils.brandUrl);

        } else {
            message = getResources().getString(R.string.no_internet_connection);
//            showAlertDialog(message, true);
        }
    }
    public void showAlertDialog(String message, final boolean finish) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            finish();
                    }
                });
        alertDialog.show();
    }
     //Products on Home
    public class HomeAsyncHttpTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {
//
//            glanceProgressLL.setVisibility(View.VISIBLE);
//            progressLL.setVisibility(View.VISIBLE);
//            pb.setVisibility(View.VISIBLE);
//
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
//                urlConnection.setReadTimeout(15000);
//                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

//                List<NameValuePair> param = new ArrayList<NameValuePair>();
//                param.add(new BasicNameValuePair("tag", "home"));
//                param.add(new BasicNameValuePair("sellerid", "2"));
//                param.add(new BasicNameValuePair("thirdParam", paramValue3));
//                HashMap<String, String> param = new HashMap<String, String>();
//                param.put("tag", "home");
//                param.put("sellerid", "2");
//
//                OutputStream os = urlConnection.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(param));
//                writer.flush();
//                writer.close();
//                os.close();

//                urlConnection.connect();
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
                    parseHomeResult(response.toString());
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

//            glanceProgressLL.setVisibility(View.GONE);
//            popProgressLL.setVisibility(View.GONE);
//            progressLL.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);

            /* Download complete. Lets update UI */
            if (result == 1) {
                doInitialize();
//                Log.e("onPostExecute", "Asyntask");
//                scroll.fullScroll(ScrollView.FOCUS_UP);
              /*  if(services!=null&&services.size()!=0) {
                    //Glance
                    horizontalAdapter = new HorizontalListAdapter(activity, services, R.layout.gallary_inflate);
                    horizontalList.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();
//                    scroll.fullScroll(ScrollView.FOCUS_UP);
                }
                if(popServices!=null&&popServices.size()!=0) {
                    //POP
                    horizontalAdapter = new HorizontalListAdapter(activity, popServices, R.layout.gallary_inflate);
                    popHorizontalList.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();
//                    scroll.fullScroll(ScrollView.FOCUS_UP);
                }

                if(brandServices!=null&&brandServices.size()!=0) {
                    //Brand
                    brandAdapter = new BrandGridAdapter(activity, brandServices);
                    brandGrid.setAdapter(brandAdapter);
                    brandAdapter.notifyDataSetChanged();
//                    scroll.fullScroll(ScrollView.FOCUS_UP);

                }

                if(products!=null&&products.size()!=0){
                    mViewPager.setAdapter(new ImageSlideAdapter(
                            activity, products, HomeFragment.this));

                    mIndicator.setViewPager(mViewPager);
//                            imgNameTxt.setText(""
//                                    + ((Product) products.get(mViewPager
//                                    .getCurrentItem())).getName());
                    runnable(products.size());
                    handler.postDelayed(animateViewPager,
                            ANIM_VIEWPAGER_DELAY);
                    scroll.fullScroll(ScrollView.FOCUS_UP);
                    scroll.requestFocus();
                }*/

//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
            } else {
                Log.e("hello", "Failed to fetch data!");
                showAlertDialog("Something is went wrong!", true);
            }
        }
    }
    private void parseHomeResult(String result) {
        try {
//            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=new JSONObject(result);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                 message = jobstatus.optString(TagName.TAG_MSG);

//                if (message.equals("success")) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);

                    if (status!=0) {
//                JSONObject jsonData = jsonObject
//                        .getJSONObject(TagName.TAG_PRODUCT);
//                    }
                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_HOME);



                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);
                        if(i==0) {


                            //BRAND
                            JSONArray jsonArrayBrand = post.optJSONArray(TagName.TAG_BRAND);

            /*Initialize array if null*/
                            if (null == brandServices) {
                                brandServices = new ArrayList<ProductModel>();
                            }

                            for (int j = 0; j < jsonArrayBrand.length(); j++) {
                                JSONObject jobjBrand = jsonArrayBrand.optJSONObject(j);

                                ProductModel item = new ProductModel();
                                item.setId(jobjBrand.optInt(TagName.KEY_ID));
//                        item.setTitle(post.optString("name"));
//                        item.setDescription(post.optString("description"));
//                        item.setCost(post.optDouble("price"));
//                        item.setFinalPrice(post.optDouble("finalPrice"));
//                    item.setCount(post.optInt("finalPrice"));
//                                Log.e("name", "name"+ jobjBrand.optInt("id"));
                                item.setThumbnail(jobjBrand.optString(TagName.KEY_IMG_URL));
                                brandServices.add(item);
                            }
                            //POPULAR PRODUCTS
                            JSONArray jsonArrayBestBuys = post.optJSONArray(TagName.TAG_TOP_PRODUCT);

            /*Initialize array if null*/
                            if (null == popServices) {
                                popServices = new ArrayList<ProductModel>();
                            }

                            for (int j = 0; j < jsonArrayBestBuys.length(); j++) {
                                JSONObject jobjPop = jsonArrayBestBuys.optJSONObject(j);

                                ProductModel item = new ProductModel();
                                item.setId(jobjPop.optInt(TagName.KEY_ID));
                                item.setTitle(jobjPop.optString(TagName.KEY_NAME));
                                item.setDescription(jobjPop.optString(TagName.KEY_DES));
                                item.setCost(jobjPop.optDouble(TagName.KEY_PRICE));
                                item.setFinalPrice(jobjPop.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                                Log.e("name", "name"+ jobjPop.optDouble("finalPrice"));
                                item.setThumbnail(jobjPop.optString(TagName.KEY_THUMB));
                                item.setWishlist(jobjPop.optInt(TagName.KEY_WISHLIST));
                                JSONObject post1 = jobjPop.optJSONObject(TagName.TAG_OFFER_ALL);
                                item.setShare(post1.optString(TagName.KEY_SHARE));
                                item.setTag(post1.optString(TagName.KEY_TAG));
                                item.setDiscount(post1.optInt(TagName.KEY_DISC));
                                item.setRating(post1.optInt(TagName.KEY_RATING));
                                popServices.add(item);
                            }
                            //GLANCE PRODUCTS
                            JSONArray jsonArrayGlance = post.optJSONArray(TagName.TAG_PRODUCT);

            /*Initialize array if null*/
                            if (null == services) {
                                services = new ArrayList<ProductModel>();
                            }

                            for (int j = 0; j < jsonArrayGlance.length(); j++) {
                                JSONObject jobjGlance = jsonArrayGlance.optJSONObject(j);

                                ProductModel item = new ProductModel();
                                item.setId(jobjGlance.optInt(TagName.KEY_ID));
                                item.setTitle(jobjGlance.optString(TagName.KEY_NAME));
                                item.setDescription(jobjGlance.optString(TagName.KEY_DES));
                                item.setCost(jobjGlance.optDouble(TagName.KEY_PRICE));
                                item.setFinalPrice(jobjGlance.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                                Log.e("name", "name"+ jobjGlance.optDouble("finalPrice"));
                                item.setThumbnail(jobjGlance.optString(TagName.KEY_THUMB));
                                item.setWishlist(jobjGlance.optInt(TagName.KEY_WISHLIST));
                                JSONObject post1 = jobjGlance.optJSONObject(TagName.TAG_OFFER_ALL);
                                item.setShare(post1.optString(TagName.KEY_SHARE));
                                item.setTag(post1.optString(TagName.KEY_TAG));
                                item.setDiscount(post1.optInt(TagName.KEY_DISC));
                                item.setRating(post1.optInt(TagName.KEY_RATING));
                                services.add(item);
                            }
                            //Banner
                            JSONArray jsonArrayBanner = post.getJSONArray(TagName.TAG_OFFER_BANNER);
                             /*Initialize array if null*/
                            if (null == products) {
//                                products = new ArrayList<Product>();
                                products = new ArrayList<ProductModel>();
                            }
                            Product product;
                            for (int j = 0; j < jsonArrayBanner.length(); j++) {
//                                product = new Product();
                                JSONObject productObj = jsonArrayBanner.getJSONObject(j);
                                ProductModel item = new ProductModel();
                                item.setId(productObj.optInt(TagName.KEY_ID));
////
//                                Log.e("name", "name"+ productObj.optInt("id"));
                                item.setThumbnail(productObj.optString(TagName.KEY_IMG_URL));
//                                product.setId(productObj.getInt(TagName.KEY_ID));
//			product.setName(productObj.getString(TagName.KEY_NAME));
//                                product.setImageUrl(productObj.getString(TagName.KEY_IMG_URL));

                                products.add(item);
                            }
                        }

                    }
                } else {
                    message = jsonObject.getString(TagName.TAG_MSG);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class WishlistAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
//            loadingDialog = ProgressDialog.show(activity, "", "Loading...");
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
//            loadingDialog.dismiss();
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
//                emptyWishlist.setVisibility(View.GONE);
//                adapter = new WislistRecyclerAdapter(WishlistFragment.this, services, R.layout.wishlist_inflate);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//                }else{
//                    emptyCart.setVisibility(View.VISIBLE);
//                }
            } else {
                Log.e("hello", "Failed to fetch data!");
//                emptyWishlist.setVisibility(View.VISIBLE);
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
                    if (null == wishlistServices) {
                        wishlistServices = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        ProductModel item = new ProductModel();
                        item.setId(post.optInt(TagName.KEY_ID));
                        item.setTitle(post.optString(TagName.KEY_NAME));
                        item.setDescription(post.optString(TagName.KEY_DES));
                        item.setCost(post.optDouble(TagName.KEY_PRICE));
                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                        item.setCount(post.optInt(TagName.KEY_COUNT));
//                    Log.e("name", "name");
                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
                        wishlistServices.add(item);
                    }
                } else {
                    message = jobstatus.getString(TagName.TAG_MSG);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Resources.Theme getTheme() {
        Resources res = getResources();
        Resources.Theme theme = super.getTheme();

        String theme_id = res.getString(R.string.theme);
        switch (theme_id)
        {
            case "Default":

                theme.applyStyle(R.style.AppTheme_Default, true);
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
