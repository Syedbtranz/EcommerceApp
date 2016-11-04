package com.btranz.ecommerceapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.fragment.DeliveryAddressFragment;
import com.btranz.ecommerceapp.fragment.UserDetailsFragment;
import com.btranz.ecommerceapp.utils.TagName;

/**
 * Created by all on 9/8/2016.
 */
public class BookNowActivity extends AppCompatActivity {
    ImageView closeBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booknow);
        closeBtn=(ImageView)findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();

            }
        });
        int key=getIntent().getIntExtra("buynowKey", 0);
        Log.e("keu",""+key);
        displayView(key);
    }
    public void displayView(int key){
        Fragment fragment = null;
        String title=getString(R.string.app_name);
        switch(key){
            case TagName.BUYNOW_USER_DETAILS:
                fragment = new UserDetailsFragment();
//                title=getString(R.string.app_name);
                break;
            case TagName.BUYNOW_USER_ADDRESS:
                fragment = new DeliveryAddressFragment();
//                title=getString(R.string.title_register);
                break;
//            case 2:
//                fragment = new ProfileFragment();
//                title=getString(R.string.title_profile);
//                break;
        };


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_booknow, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
//            getSupportActionBar().setTitle(title);
        }
    }
}
