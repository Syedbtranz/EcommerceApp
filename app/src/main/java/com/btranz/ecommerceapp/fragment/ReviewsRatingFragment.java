package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.CredientialActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.ReviewRatingRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;

import java.util.ArrayList;


/**
 * Created by Ravi on 29/07/15.
 */
public class ReviewsRatingFragment extends Fragment {
    public static final String RR_FRAG = "reviews_fragment";
    ArrayList<ProductModel> reviewArray;
    RecyclerView reviewList;
    FragmentActivity activity;
    ReviewRatingRecyclerAdapter reviewRateAdapter;
    TextView writeReviewBtn;
    public  String productId,productName;
    ProductModel product;

    public ReviewsRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity=getActivity();
        Bundle bundle = this.getArguments();
//        Bundle b = activity.getIntent().getExtras();
//        if (b != null) {
//            product = b.getParcelable("singleProduct");
//            setProductItem(product);
//        }else
        if (bundle != null) {
            product = bundle.getParcelable("singleProduct");
            reviewArray = bundle.getParcelableArrayList("reviews");
            productName=product.getTitle();
//            prdtsTitle = bundle.getString("prdtsTitle");
//            setProductItem(product);
            Log.e("bundle", "product");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review_rating, container, false);
        findViewById(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void sendRequest() {
        reviewRateAdapter = new ReviewRatingRecyclerAdapter(activity, reviewArray,R.layout.review_rating_inflate);
        reviewList.setAdapter(reviewRateAdapter);
//                grid1.setAdapter(adapter);
        reviewRateAdapter.notifyDataSetChanged();
    }

    private void findViewById(View rootView) {

        reviewList = (RecyclerView)rootView.findViewById(R.id.review_rate_recycler);
        reviewList.setLayoutManager(new LinearLayoutManager(activity));
        writeReviewBtn = (TextView) rootView.findViewById(R.id.write_review_btn);
        //WRITE REVIEWS & RATING ACTION
        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle arguments = new Bundle();
//                Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                ProductModel product = (ProductModel) services.get(position);
////                arguments.putParcelable("singleProduct", product);
////                arguments.putParcelableArrayList("reviews", reviewArray);
////                arguments.putString("prdtsTitle", "similar products for "+item1.getTitle());
//                // Start a new fragment
//                fragment = new WriteReviewsRatingFragment();
//                fragment.setArguments(arguments);
//
//                FragmentTransaction transaction = activity
//                        .getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_second, fragment,
//                        WriteReviewsRatingFragment.WRR_FRAG);
//                transaction.addToBackStack( WriteReviewsRatingFragment.WRR_FRAG);
//                transaction.commit();
                Intent inRe = new Intent(activity, CredientialActivity.class);
                inRe.putExtra("credKey", TagName.WRITE_REVIEW);
                inRe.putExtra("reviewProduct",product);
                activity.startActivity(inRe);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();

        // update the actionbar to show the up carat/affordance

//		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        final LinearLayout searchBar=((SecondActivity)activity).searchBar;
        Log.e("bundle", "product"+searchBar.getId());
        searchBar.setVisibility(View.GONE);
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        SpannableString s = new SpannableString(getString(R.string.title_review_details)+" for "+productName);
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final TextView toolTitle=((SecondActivity) getActivity()).toolbarTitle;
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("MrE", "home selected");
                getActivity().onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });
        sendRequest();
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
