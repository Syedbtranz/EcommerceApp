package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
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


/**
 * Created by Ravi on 29/07/15.
 */
public class BillingFragment extends Fragment {
    Button backBtn, nextBtn;
    EditText cardNoET,expiryET, cvvET,nameET, promoET;
    String cardNo, expiry, cvv, name, promo;
    FragmentActivity activity;
    public BillingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_billing, container, false);
        cardNoET=(EditText) rootView.findViewById(R.id.card_no_et);
        expiryET=(EditText) rootView.findViewById(R.id.expiry_et);
        cvvET=(EditText) rootView.findViewById(R.id.cvv_et);
        nameET=(EditText) rootView.findViewById(R.id.name_et);
        promoET=(EditText) rootView.findViewById(R.id.prome_et);

        backBtn=(Button)rootView.findViewById(R.id.back_btn);
        nextBtn=(Button)rootView.findViewById(R.id.next_btn);
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
    public void sendData(){
        cardNo=cardNoET.getText().toString();
        expiry=expiryET.getText().toString();
        cvv =cvvET.getText().toString();
        name=nameET.getText().toString();
        promo=promoET.getText().toString();
//        Log.e("subject",subject);
//        Log.e("desc ",description);
//        Log.e("issueType",issueType);
//        Log.e("emailMeStr",emailMeStr);
        if (cardNo.equals("") &&expiry.equals("") && cvv.equals("")
                && name.equals("")) {
//            Log.i("k1", "null");
            Toast.makeText(activity,
                    "Please enter the details", Toast.LENGTH_SHORT).show();
            cardNoET.requestFocus();
        } else if (cardNo.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD NO", Toast.LENGTH_SHORT).show();
            cardNoET.requestFocus();
        }  else if (expiry.equals("")) {
            Toast.makeText(activity,
                    "Please enter the EXPIRY DATE", Toast.LENGTH_SHORT).show();
            expiryET.requestFocus();
        } else if (cvv.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CVV CODE", Toast.LENGTH_SHORT).show();
            cvvET.requestFocus();
        } else if (name.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD HOLDER NAME", Toast.LENGTH_SHORT).show();
            nameET.requestFocus();
        } else if (!cardNo.equals("")&&!expiry.equals("") &&!cvv.equals("")
                && !name.equals("")) {

            Bundle arguments = new Bundle();
            Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

            // Start a new fragment
            fragment = new CheckOutFragment();
//                fragment.setArguments(arguments);

            FragmentTransaction transaction = activity
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_booknow, fragment,
                    TagName.FRAGMENT_USER_CHECKOUT);
            transaction.addToBackStack(TagName.FRAGMENT_USER_CHECKOUT);
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
