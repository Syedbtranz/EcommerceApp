package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.utils.TypefaceSpan;


/**
 * Created by Ravi on 29/07/15.
 */
public class ProfileFragment extends Fragment {
    EditText emailET,pswET,forgetPswEt;
    TextView profileName,nameTxt, emailTxt;
    FragmentActivity activity;
    SpannableString s1;
    Button logout;
    ImageView backBtn;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public static String userName,userEmail,customerId,msg;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        setHasOptionsMenu(true);
//        s1 = new SpannableString(getString(R.string.title_profile));
//        try {
//
//            s1.setSpan(new TypefaceSpan(activity, "hallo_sans_black.otf"), 0, s1.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }catch (Exception e){
//
//        }
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userName = sharedpreferences.getString("userName", "");
        userEmail = sharedpreferences.getString("userEmail", "");
//        customerId = sharedpreferences.getString("password", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
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

    public  void   findViewById(View view){
//        ((SecondActivity)activity).findViewById(R.id.search_bar).setVisibility(View.GONE);
//        searchBar.setVisibility(View.GONE);
//        profileName=(TextView)view.findViewById(R.id.profile_name);
        nameTxt=(TextView)view.findViewById(R.id.pr_name);
        emailTxt=(TextView)view.findViewById(R.id.pr_email);
//        backBtn=(ImageView) view.findViewById(R.id.back_btn);
        logout=(Button) view.findViewById(R.id.logout_btn);
//        profileName.setText(s1);
        nameTxt.setText(userName);
        emailTxt.setText(userEmail);

//        final Toolbar mToolbar= ((SecondActivity)activity).mToolbar;
        final Toolbar mToolbar= (Toolbar)view.findViewById(R.id.toolbar);
        final  TextView toolTitle=(TextView)mToolbar.findViewById(R.id.toolbar_txt);
        ((AppCompatActivity)activity).setSupportActionBar(mToolbar);
        SpannableString s = new SpannableString(getString(R.string.title_profile));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MrE", "home selected");
                activity.onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                activity.onBackPressed();
//
//            }
//        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                activity.finish();
                Toast.makeText(activity, "LogOut success!", Toast.LENGTH_SHORT).show();
            }
        });
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
