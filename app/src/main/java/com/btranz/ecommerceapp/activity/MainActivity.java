package com.btranz.ecommerceapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.btranz.ecommerceapp.fragment.CardsFragment;
import com.btranz.ecommerceapp.fragment.CategoriesFragment;
import com.btranz.ecommerceapp.fragment.HelpCenterFragment;
import com.btranz.ecommerceapp.fragment.HomeFragment;
import com.btranz.ecommerceapp.fragment.OrdersFragment;
import com.btranz.ecommerceapp.fragment.ReferEarnFragment;
import com.btranz.ecommerceapp.fragment.WishlistFragment;
import com.btranz.ecommerceapp.utils.AppData;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
//import com.btranz.ecamarceapp.utils.AppData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Boolean exit = false;
//    EditText SearchET;
    ImageView SearchET;
    Handler handler;
    public Toolbar toolbar;
    int key=R.id.nav_home;
    Fragment fragment = null;
    String customerEmail, cartBadge;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public TextView viewTxt, toolbar_title;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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
                            doInitialize();
                            AppData.currentFlag=true;
                        }
                    }).setDuration(5000).start();

                }
            }, 0);
        }else{
            initToolbar();
            doInitialize();
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
        try {
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
        }catch (InflateException e){

        }
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
                    customerEmail = sharedpreferences.getString("customerEmail", "");
                    LinearLayout slideLL = (LinearLayout) findViewById(R.id.slide);
                    TextView emailTxt = (TextView) findViewById(R.id.email_txt);

                    viewTxt = (TextView) findViewById(R.id.view_profile);
                    if (customerEmail.equals("")) {
                        customerEmail = "guest@example.com";
                        viewTxt.setVisibility(View.GONE);
                    } else {
                        viewTxt.setVisibility(View.VISIBLE);
                    }

                    emailTxt.setText(customerEmail);

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
                title = getString(R.string.title_home);
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
                inRe.putExtra("credKey", TagName.REFER_ERN_ID);
                startActivity(inRe);
                this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;
            case R.id.nav_orders:
                fragment = new OrdersFragment();
                title = getString(R.string.title_orders);
                break;
            case R.id.nav_help:
                fragment = new HelpCenterFragment();
                title = getString(R.string.title_help_center);
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
                Intent i=new Intent(getApplicationContext(),SecondActivity.class);
                i.putExtra("key", TagName.WISH_ID);
                startActivity(i);
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

}
