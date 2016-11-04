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
    EditText userET,emailET,mobileET,forgetPswEt;
    String name, email, mobile,customerEmail, customerName, userPhone;
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
        customerName = sharedpreferences.getString("customerName", "");
        customerEmail = sharedpreferences.getString("customerEmail", "");
        userPhone = sharedpreferences.getString("checkoutUserContact", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        userET=(EditText)rootView.findViewById(R.id.user_name_et);
        emailET=(EditText)rootView.findViewById(R.id.email_et);
        mobileET=(EditText)rootView.findViewById(R.id.mobile_et);
        userET.setText(customerName);
        emailET.setText(customerEmail);
        mobileET.setText(userPhone);
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
        name=userET.getText().toString();
//        lastName=lastNameEt.getText().toString();
        email =emailET.getText().toString();
        mobile=mobileET.getText().toString();
//        Log.e("subject",subject);
//        Log.e("desc ",description);
//        Log.e("issueType",issueType);
//        Log.e("emailMeStr",emailMeStr);
        if (name.equals("") && email.equals("")
                && mobile.equals("")) {
//            Log.i("k1", "null");
            Toast.makeText(activity,
                    "Please enter the details", Toast.LENGTH_SHORT).show();
            userET.requestFocus();
        } else if (name.equals("")) {
            Toast.makeText(activity,
                    "Please enter the First Name", Toast.LENGTH_SHORT).show();
            userET.requestFocus();
        }  else if (email.equals("")) {
            Toast.makeText(activity,
                    "Please enter the Email", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (!email.matches(Utils.EMAIL_PATTERN)) {
            Toast.makeText(activity,
                    "Please enter the valid Email patern", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (mobile.equals("")) {
            Toast.makeText(activity,
                    "Please enter the Phone Number", Toast.LENGTH_SHORT).show();
            mobileET.requestFocus();
        } else if (mobile.length()!=10) {
            Toast.makeText(activity,
                    "Please enter the 10 digit Phone Number", Toast.LENGTH_SHORT).show();
            mobileET.requestFocus();
        } else if (!name.equals("") && email.matches(Utils.EMAIL_PATTERN)
                && mobile.length()==10) {
            editor.putString("checkoutUserName", name);
            editor.putString("checkoutUserEmail", email);
            editor.putString("checkoutUserContact", mobile);
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
