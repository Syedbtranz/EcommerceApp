package com.btranz.ecommerceapp.activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.fragment.CardsFragment;
import com.btranz.ecommerceapp.fragment.LogRegFragment;
import com.btranz.ecommerceapp.fragment.ProfileFragment;
import com.btranz.ecommerceapp.fragment.ReferEarnFragment;
import com.btranz.ecommerceapp.utils.TagName;


public class CredientialActivity extends AppCompatActivity {
    private String[] arraySpinner;
    Spinner spThemes;
    private Toolbar mToolbar;
    int themePosition;
    //shared preference
    SharedPreferences mPrefs;
    public static final String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        // MUST BE SET BEFORE setContentView
//        Utils.onActivityCreateSetTheme(this, mPrefs.getInt("PREF_SPINNER", 0));
        // AFTER SETTING THEME
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credentials);
        int key=getIntent().getIntExtra("credKey", 0);
//        initToolbar();
        displayView(key);

    }
    /*private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
//                Toast.makeText(getApplicationContext(),"hai",Toast.LENGTH_SHORT).show();
                CredientialActivity.this.finish();
//                MainActivity ma=new MainActivity();
//                ma.onResume();
//                CredientialActivity.this.startActivity(new Intent(CredientialActivity.this, MainActivity.class));
                CredientialActivity.this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }
        });

//        setTitle("Settings");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
//        mToolbarHeight = Utils.getToolbarHeight(this);
    }*/
    public void displayView(int key){
        Fragment fragment = null;
//        String title=getString(R.string.app_name);
        switch(key){
            case  TagName.CRED_LOGIN:
                fragment = new LogRegFragment();
//                title=getString(R.string.title_login);
                break;
            case TagName.REFER_ERN_ID:
                fragment = new ReferEarnFragment();
//                title=getString(R.string.title_register);
                break;
            case TagName.CARDS_ID:
                fragment = new CardsFragment();
//                title=getString(R.string.title_register);
                break;
            case TagName.CRED_PROFILE:
                fragment = new ProfileFragment();
//                title=getString(R.string.title_profile);
                break;
        };

//        if(key==0){
//            fragment = new ProfileFragment();
//            Toast.makeText(getApplicationContext(),"hai login", Toast.LENGTH_SHORT).show();
//
//            title=getString(R.string.title_login);
//
//        }else if(key==1){
//            fragment = new RegisterFragment();
//            Toast.makeText(getApplicationContext(),"hai reg" ,Toast.LENGTH_SHORT).show();
//
//            title=getString(R.string.title_register);
//        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_credentials, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
//            getSupportActionBar().setTitle(title);
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
