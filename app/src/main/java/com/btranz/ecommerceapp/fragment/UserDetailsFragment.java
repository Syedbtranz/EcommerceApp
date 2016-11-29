package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;


/**
 * Created by Ravi on 29/07/15.
 */
public class UserDetailsFragment extends Fragment {
    Button nextBtn;
    FragmentActivity activity;
    EditText nameET,emailET,mobileET,forgetPswEt;
    String userName, userEmail, userMobile,customerEmail, customerName, customerMobile;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public UserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
//        customerId = sharedpreferences.getString("customerID", "");
        userName = sharedpreferences.getString("userName", "");
        userEmail = sharedpreferences.getString("userEmail", "");
        userMobile = sharedpreferences.getString("userContact", "");

        customerName = sharedpreferences.getString("checkoutCustomerName", "");
        customerEmail = sharedpreferences.getString("checkoutCustomerEmail", "");
        customerMobile = sharedpreferences.getString("checkoutCustomerContact", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        nameET=(EditText)rootView.findViewById(R.id.user_name_et);
        emailET=(EditText)rootView.findViewById(R.id.email_et);
        mobileET=(EditText)rootView.findViewById(R.id.mobile_et);

        if(!customerEmail.equals("")) {
            nameET.setText(customerName);
            emailET.setText(customerEmail);
            mobileET.setText(customerMobile);
        }else{
            nameET.setText(userName);
            emailET.setText(userEmail);
            mobileET.setText(userMobile);
        }
        nextBtn=(Button)rootView.findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails();
//                Intent in=new Intent(getActivity(), BookNowActivity.class);
//                in.putExtra("buynowKey", TagName.BUYNOW_USER_ADDRESS);
//                activity.startActivity(in);
//                activity.overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
    public void userDetails(){
        customerName=nameET.getText().toString();
//        lastName=lastNameEt.getText().toString();
        customerEmail =emailET.getText().toString();
        customerMobile=mobileET.getText().toString();
//        Log.e("subject",subject);
//        Log.e("desc ",description);
//        Log.e("issueType",issueType);
//        Log.e("emailMeStr",emailMeStr);
        if (customerName.equals("") && customerEmail.equals("")
                && customerMobile.equals("")) {
//            Log.i("k1", "null");
            Toast.makeText(activity,
                    "Please enter the details", Toast.LENGTH_SHORT).show();
            nameET.requestFocus();
        } else if (customerName.equals("")) {
            Toast.makeText(activity,
                    "Please enter the First Name", Toast.LENGTH_SHORT).show();
            nameET.requestFocus();
        }  else if (customerEmail.equals("")) {
            Toast.makeText(activity,
                    "Please enter the Email", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (!customerEmail.matches(Utils.EMAIL_PATTERN)) {
            Toast.makeText(activity,
                    "Please enter the valid Email patern", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (customerMobile.equals("")) {
            Toast.makeText(activity,
                    "Please enter the Phone Number", Toast.LENGTH_SHORT).show();
            mobileET.requestFocus();
        } else if (customerMobile.length()!=10) {
            Toast.makeText(activity,
                    "Please enter the 10 digit Phone Number", Toast.LENGTH_SHORT).show();
            mobileET.requestFocus();
        } else if (!customerName.equals("") && customerEmail.matches(Utils.EMAIL_PATTERN)
                && customerMobile.length()==10) {
            editor.putString("checkoutCustomerName", customerName);
            editor.putString("checkoutCustomerEmail", customerEmail);
            editor.putString("checkoutCustomerContact", customerMobile);
            editor.commit();
            Bundle arguments = new Bundle();
            Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

            // Start a new fragment
            fragment = new DeliveryAddressFragment();
//                fragment.setArguments(arguments);

            FragmentTransaction transaction = activity
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_booknow, fragment,
                    TagName.FRAGMENT_USER_ADDRESS);
            transaction.addToBackStack(TagName.FRAGMENT_USER_ADDRESS);
            transaction.commit();

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
