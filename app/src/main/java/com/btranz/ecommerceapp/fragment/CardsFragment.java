package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.MainActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;


/**
 * Created by Ravi on 29/07/15.
 */
public class CardsFragment extends Fragment {
    Button cardDetailsBtn, contShopingBtn;
    FragmentActivity activity;
    public static final String CARD_FRAG = "card_fragment";

    public CardsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
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
        SpannableString s = new SpannableString(getString(R.string.title_cards));
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

        cardDetailsBtn=(Button)view.findViewById(R.id.card_details_btn);
        contShopingBtn=(Button)view.findViewById(R.id.continue_shoping_btn);

        //card details Action
        cardDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

                // Start a new fragment
                fragment = new CardsDetailsFragment();
//                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_credentials, fragment,
                        CardsDetailsFragment.CARD_DETAILS_FRAG);
                transaction.addToBackStack(CardsDetailsFragment.CARD_DETAILS_FRAG);
                transaction.commit();
            }
        });

        //continue Shoping Action
        contShopingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(activity,SecondActivity.class);
                in.putExtra("key", TagName.CATG_ID);
                startActivity(in);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
//                Bundle arguments = new Bundle();
//                Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                Product product = (Product) products.get(position);
////                arguments.putParcelable("singleProduct", product);
//
//                // Start a new fragment
//                fragment = new CategoriesFragment();
////                fragment.setArguments(arguments);
//
//                FragmentTransaction transaction = activity
//                        .getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_second, fragment,
//                        CategoriesFragment.CATG_FRAG);
//                transaction.addToBackStack(CategoriesFragment.CATG_FRAG);
//                transaction.commit();
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
