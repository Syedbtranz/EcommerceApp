package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;


/**
 * Created by Sajid on 01/12/16.
 */
public class UpdateProfileFragment extends Fragment {
    Button cardDetailsSubmitBtn;
    EditText cardTypeEd, cardNoEd, cardNameEd, cardExpiryEd, cardCvvEd;
    String cardType,cardNo, cardExpiry, cardCvv, cardName;
    FragmentActivity activity;
    TextView changPwd;
    public static final String CARD_DETAILS_FRAG = "card_details_fragment";

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        findViewById(rootView);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_cart).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void findViewById(View view){
        final Toolbar mToolbar= (Toolbar)view.findViewById(R.id.toolbar);
        final TextView toolTitle=(TextView)mToolbar.findViewById(R.id.toolbar_txt);
        ((AppCompatActivity)activity).setSupportActionBar(mToolbar);
        SpannableString s = new SpannableString(getString(R.string.title_card_details));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });

        cardTypeEd=(EditText) view.findViewById(R.id.card_type);
        cardNoEd=(EditText) view.findViewById(R.id.card_no);
        cardNameEd=(EditText) view.findViewById(R.id.card_name);
        cardExpiryEd=(EditText) view.findViewById(R.id.card_expiry);
        cardCvvEd=(EditText) view.findViewById(R.id.card_cvv_no);
        changPwd=(TextView)view.findViewById(R.id.txtv_change_pwd);
        cardDetailsSubmitBtn=(Button)view.findViewById(R.id.card_details_submit_btn);

        changPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
LayoutInflater inflater=getActivity().getLayoutInflater();
                View view1=inflater.inflate(R.layout.change_pwd_otp_alert,null);
                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(getActivity());
                dialogBuilder.setView(view1);
                final EditText otpEdit=(EditText)view1.findViewById(R.id.input_otp);
                Button applyBtn=(Button)view1.findViewById(R.id.btn_apply);
                final AlertDialog alertDialog=dialogBuilder.create();
                applyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String otp=otpEdit.getText().toString();
                        if(otp.equals("")){
                            Toast.makeText(getActivity(), "Enter OTP.", Toast.LENGTH_SHORT).show();
                        }else if(otp.equals("1234")){
                            alertDialog.cancel();
                            Bundle arguments = new Bundle();
                            Fragment fragment = null;

                            // Start a new fragment
                            fragment = new ChangePasswordFragment();
                            fragment.setArguments(arguments);
                            FragmentTransaction transaction = activity
                                    .getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.container_booknow, fragment,
                                    TagName.FRAGMENT_PROFILE_UPDATE);
                            transaction.addToBackStack(TagName.FRAGMENT_PROFILE_UPDATE);
                            transaction.commit();
                        }else{
                            Toast.makeText(getActivity(), "Enter valid OTP.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.show();
            }
        });
        //card details Action
        cardDetailsSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCardDetails();
            }
        });


    }

    private void sendCardDetails() {
        cardType=cardTypeEd.getText().toString();
        cardNo=cardNoEd.getText().toString();
        cardName=cardNameEd.getText().toString();
        cardExpiry=cardExpiryEd.getText().toString();
        cardCvv =cardCvvEd.getText().toString();
//        Log.e("subject",subject);
//        Log.e("desc ",description);
//        Log.e("issueType",issueType);
//        Log.e("emailMeStr",emailMeStr);
        if (cardType.equals("") &&cardNo.equals("") &&cardName.equals("") && cardExpiry.equals("")
                && cardCvv.equals("")) {
//            Log.i("k1", "null");
            Toast.makeText(activity,
                    "Please enter the details", Toast.LENGTH_SHORT).show();
            cardTypeEd.requestFocus();
        }else if (cardType.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD Type", Toast.LENGTH_SHORT).show();
            cardTypeEd.requestFocus();
        }  else if (cardNo.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD NO", Toast.LENGTH_SHORT).show();
            cardNoEd.requestFocus();
        }  else if (cardName.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CARD HOLDER NAME", Toast.LENGTH_SHORT).show();

            cardNameEd.requestFocus();
        } else if (cardExpiry.equals("")) {
            Toast.makeText(activity,
                    "Please enter the EXPIRY DATE", Toast.LENGTH_SHORT).show();

            cardExpiryEd.requestFocus();
        } else if (cardCvv.equals("")) {
            Toast.makeText(activity,
                    "Please enter the CVV CODE", Toast.LENGTH_SHORT).show();
            cardCvvEd.requestFocus();
        } else if (!cardType.equals("")&&!cardNo.equals("")&&!cardName.equals("") &&!cardExpiry.equals("")
                && !cardCvv.equals("")) {
                activity.finish();
//            Bundle arguments = new Bundle();
//            Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                Product product = (Product) products.get(position);
////                arguments.putParcelable("singleProduct", product);
//
//            // Start a new fragment
//            fragment = new CheckOutFragment();
////                fragment.setArguments(arguments);
//
//            FragmentTransaction transaction = activity
//                    .getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.container_booknow, fragment,
//                    TagName.FRAGMENT_USER_CHECKOUT);
//            transaction.addToBackStack(TagName.FRAGMENT_USER_CHECKOUT);
//            transaction.commit();
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
