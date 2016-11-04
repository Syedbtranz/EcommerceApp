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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.utils.TagName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ravi on 29/07/15.
 */
public class DeliveryAddressFragment extends Fragment {
    Button backBtn, nextBtn;
    EditText addressET;
    FragmentActivity activity;
    String userAddress;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public DeliveryAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        userAddress = sharedpreferences.getString("checkoutUserAddress", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_address, container, false);
        addressET=(EditText) rootView.findViewById(R.id.address_et);
        backBtn=(Button)rootView.findViewById(R.id.back_btn);
        nextBtn=(Button)rootView.findViewById(R.id.next_btn);
        addressET.setText(userAddress);
        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                editor.putString("checkoutUserDeliveryType", item);
                editor.commit();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Standard Delivery");
        categories.add("Express Delivery");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //Action  Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        //Action  Next Button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
public  void sendData(){
    String address=addressET.getText().toString();
    if(!address.equals("")){
        editor.putString("checkoutUserAddress", address);
        editor.commit();
        Bundle arguments = new Bundle();
        Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

        // Start a new fragment
        fragment = new BillingFragment();
//                fragment.setArguments(arguments);

        FragmentTransaction transaction = activity
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_booknow, fragment,
                TagName.FRAGMENT_USER_BILLING);
        transaction.addToBackStack(TagName.FRAGMENT_USER_BILLING);
        transaction.commit();
    }else{
        Toast.makeText(activity,"Please Enter the Address",Toast.LENGTH_SHORT).show();
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
