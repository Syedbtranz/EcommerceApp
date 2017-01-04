package com.btranz.ecommerceapp.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.FilterBrandAdapter;
import com.btranz.ecommerceapp.adapter.ProductGridAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.NewArrivalSorter;
import com.btranz.ecommerceapp.utils.PriceSorter;
import com.btranz.ecommerceapp.utils.RangeSeekBar;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;




import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Mahboob on 12/14/2016.
 */
public class ProductsFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    View div;
    View dialogView;
    RangeSeekBar<Integer> rangeSeekBar;
    TextView disTxt, arrTxt, htolTxt, ltohTxt, popTxt;
    LinearLayout filterBrand, filterPrice, filterRating, filterDiscount;
    ListView filterList;
    ImageView filterClose;
    Button filterApply,filterReset;
    NavigationView navigationView;
    TextView sortSubTxt;
    GridView ProductsGrid;
    ArrayList<ProductModel> services;
    ArrayList<ProductModel> priceFilteredArray=new ArrayList<ProductModel>();
    FragmentActivity activity;
    ProductGridAdapter adapter;
    AsyncHttpTask task;
    AlertDialog alertDialog;
    String message;
    LinearLayout sortBtn, filterBtn;
    LinearLayout progressLL;
    ProgressBar pb;
    android.os.Handler handler;
    boolean view_flag=true;
    ImageView viewBtn;
    String prdtsUrl, prdtsTitle;
    int color, sort_flag;

    RadioGroup WhoRU;
    RadioButton  discRadio, arrRadio, htolRadio, ltohRadio, popRadio;
    int radioBtnId;
    public static final String PRDTS_FRAG = "pdts_fragment";
    // The data to show
    List<ProductModel> brandList = new ArrayList<ProductModel>();
    FilterBrandAdapter aAdpt;
    CheckBox rating1, rating2, rating3, rating4, rating5;
    CheckBox disc1, disc2, disc3, disc4, disc5, disc6, disc7, disc8, disc9, disc10;
    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        handler = new android.os.Handler();
//        initList();
//        sendRequest();
//        Log.e("onCreate","test");
//        Bundle bundle = this.getArguments();
////        Bundle b = activity.getIntent().getExtras();
////        if (b != null) {
////            product = b.getParcelable("singleProduct");
////            setProductItem(product);
////        }else
//        if (bundle != null) {
////            product = bundle.getParcelable("singleProduct");
//            prdtsUrl = bundle.getString("prdtsUrl");
//            prdtsTitle = bundle.getString("prdtsTitle");
////            setProductItem(product);
//            Log.d("bundle", "product");
//        }else {
//            prdtsUrl=activity.getIntent().getStringExtra("prdtsUrl");
//            prdtsTitle=activity.getIntent().getStringExtra("prdtsTitle");
//             services = activity.getIntent().getParcelableArrayListExtra("ProductList");
//            Log.d("intent", "product1");
//        }
//        sendRequest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        View  rootView = inflater.inflate(R.layout.fragment_product, container, false);

            div = (View) rootView.findViewById(R.id.div_a);
            progressLL = (LinearLayout) rootView.findViewById(R.id.progress_ll);
            sortSubTxt = (TextView) rootView.findViewById(R.id.sort_sub_txt);
            pb = (ProgressBar) rootView.findViewById(R.id.progressbar);
            viewBtn = (ImageView) rootView.findViewById(R.id.view_list);
            //image color change when change the theme colors
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getActivity().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
            viewBtn.setColorFilter(color);

            ProductsGrid = (GridView) rootView.findViewById(R.id.products_grid);
            sortBtn = (LinearLayout) rootView.findViewById(R.id.sort_btn);
            filterBtn = (LinearLayout) rootView.findViewById(R.id.filter_btn);
//            adapter = new ProductGridAdapter(activity, services);
//            ProductsGrid.setAdapter(adapter);
        //Sort Btn
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sort", Toast.LENGTH_LONG).show();
                showSortDialog();
            }
        });

        //Filter Btn
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Filter", Toast.LENGTH_SHORT).show();
//                    showFilter();
                try {
//                    handler.postDelayed(new Runnable() {
//                        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
//                        @Override
//                        public void run() {
//                            showFilter();
                            showFilterDialog();
//                        }
//                    }, 1000);
                }catch (Exception e){

                }

            }
        });
        //View Btn
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ViewList", Toast.LENGTH_LONG).show();
                if (view_flag) {
                    ProductsGrid.setBackgroundColor(getResources().getColor(R.color.view_divider_color));
                    ProductsGrid.setNumColumns(1);
                    ProductsGrid.setVerticalSpacing(2);
                    ProductsGrid.setHorizontalSpacing(0);
//                        div.setVisibility(View.VISIBLE);
                    viewBtn.setImageResource(R.drawable.view_grid_btn);
                    view_flag = false;
                } else {
                    ProductsGrid.setBackgroundColor(getResources().getColor(R.color.color_text));
                    ProductsGrid.setNumColumns(2);
                    ProductsGrid.setVerticalSpacing(0);
                    ProductsGrid.setHorizontalSpacing(5);
//                        div.setVisibility(View.GONE);
                    viewBtn.setImageResource(R.drawable.view_icon);
                    view_flag = true;
                }

//                showFilterDialog();
            }
        });
        // Product Click
        ProductsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "Id:" + services.get(position).getTitle(), Toast.LENGTH_LONG).show();
                    Bundle arguments = new Bundle();
                    Fragment fragment = null;
//                Log.d("position adapter", "" + position);
                    ProductModel product = (ProductModel) services.get(position);
//                arguments.putParcelable("singleProduct", product);
                    arguments.putInt("singleProductId", services.get(position).getId());
                    // Start a new fragment
                    fragment = new ProductItemFragment();
                    fragment.setArguments(arguments);

                    FragmentTransaction transaction = activity
                            .getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_second, fragment,
                            ProductItemFragment.ARG_ITEM_ID);
                    transaction.addToBackStack(ProductItemFragment.ARG_ITEM_ID);
                    transaction.commit();
                }
            });


            // Inflate the layout for this fragment
            return rootView;

    }
    @Override
    public void onResume() {
        if (services == null) {
            Bundle bundle = this.getArguments();
//        Bundle b = activity.getIntent().getExtras();
//        if (b != null) {
//            product = b.getParcelable("singleProduct");
//            setProductItem(product);
//        }else
            if (bundle != null) {
//            product = bundle.getParcelable("singleProduct");
                prdtsUrl = bundle.getString("prdtsUrl");
                prdtsTitle = bundle.getString("prdtsTitle");
                sendRequest();
//            setProductItem(product);
                Log.e("bundle", "product");
            }else {
                prdtsUrl=activity.getIntent().getStringExtra("prdtsUrl");
                prdtsTitle=activity.getIntent().getStringExtra("prdtsTitle");
                services = activity.getIntent().getParcelableArrayListExtra("productList");
                Log.e("checkOutProductsArray",""+services);
                try {
                    ProductsGrid.setAdapter(new ProductGridAdapter(activity, services));
                    progressLL.setVisibility(View.GONE);
                }catch (Exception e){

                }


                Log.e("intent", "product1");
            }
//            sendRequest();
            initList();
//            Log.e("onResume","test");
////            adapter = new ServicesRecyclerAdapter(activity, services);
//            Log.e("onResume", "onResume");
        }
        else {
            Log.e("onResume else", "onResume else");
            ProductsGrid.setAdapter(new ProductGridAdapter(activity, services));
//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//            recyclerView.scrollToPosition(0);
        }
        setHasOptionsMenu(true);
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        SpannableString s = new SpannableString(prdtsTitle.toUpperCase());
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        final  TextView toolTitle=((SecondActivity) getActivity()).toolbarTitle;
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






        super.onResume();

    }
//    public void runnable(final int size) {
//        handler = new Handler();
//        animateViewPager = new Runnable() {
//            public void run() {
//                if (!stopSliding) {
//                    if (recyclerView.getChildCount() == size - 1) {
//                        recyclerView.scrollToPosition(0);
//                    } else {
//                        recyclerView.scrollToPosition(
//                                recyclerView.getChildCount() + 1);
//                    }
//                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
//                }
//            }
//        };
//    }
private void initList() {
    // We populate the planets
    String[] brand={"Nike","mochi","Lenovo","Nike","Addidas","Nike","Nokia","Spark","Paragon","Lenovo","Bata","Spark","Nokia"};
    for(int i=0;i<brand.length;i++) {
        ProductModel item = new ProductModel();
        item.setTitle(brand[i]);
        brandList.add(item);
        Log.e("initList",brand[i]);
        System.out.println(brandList.toString());
    }

//    planetsList.add(new Planet("Mars", 30));
//    planetsList.add(new Planet("Jupiter", 40));
//    planetsList.add(new Planet("Saturn", 50));
//    planetsList.add(new Planet("Uranus", 60));
//    planetsList.add(new Planet("Neptune", 70));


}
    public void  showFilter(){
//        Bundle arguments = new Bundle();
//        Fragment fragment = null;
////                Log.d("position adapter", "" + position);
////                Product product = (Product) products.get(position);
////                arguments.putParcelable("singleProduct", product);
//
//        // Start a new fragment
//        fragment = new FilterFragment();
////                fragment.setArguments(arguments);
//
//        FragmentTransaction transaction = activity
//                .getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container_second, fragment,
//                FilterFragment.FILTER_FRAG);
//        transaction.addToBackStack(FilterFragment.FILTER_FRAG);
//        transaction.commit();

//        FragmentTransaction  fm = getFragmentManager().beginTransaction();
        FragmentTransaction  fm = activity
                .getSupportFragmentManager().beginTransaction();;
        FilterFragment dialogFragment = new FilterFragment ();
        dialogFragment.show(fm, "Sample Fragment");
    }
public void showFilterDialog() {
    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity,R.style.AppTheme);
    LayoutInflater inflater = activity.getLayoutInflater();
    dialogView = inflater.inflate(R.layout.filter_dialog, null);
    dialogBuilder.setView(dialogView);
    final android.support.v7.app.AlertDialog b = dialogBuilder.create();
//    b.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_slide;
    b.show();

    filterClose = (ImageView) dialogView.findViewById(R.id.filter_close);
     filterReset = (Button) dialogView.findViewById(R.id.reset_btn);
     filterApply = (Button) dialogView.findViewById(R.id.filter_apply);
    navigationView = (NavigationView)dialogView.findViewById(R.id.filter_nav_view);
//    priceFilteredArray=services;
     filterList = (ListView) dialogView.findViewById(R.id.filter_listView);
    // This is a simple adapter that accepts as parameter
    // Context
    // Data list
    // The row layout that is used during the row creation
    // The keys used to retrieve the data
    // The View id used to show the data. The key number and the view id must match
    //aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, planetsList);
    aAdpt = new FilterBrandAdapter(brandList, activity);
    filterList.setAdapter(aAdpt);

//    // React to user clicks on item
//    filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//        public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
//                                long id) {
//
//
//            // We know the View is a <extView so we can cast it
//            TextView clickedView = (TextView) view;
//
//            Toast.makeText(activity, "Item with id ["+id+"] - Position ["+position+"] - Planet ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();
//
//        }
//    });

    // we register for the contextmneu
    registerForContextMenu(filterList);

    // TextFilter
    filterList.setTextFilterEnabled(true);
    EditText editTxt = (EditText)dialogView.findViewById(R.id.brand_edt);

    editTxt.addTextChangedListener(new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
            if (count < before) {
                // We're deleting char so we need to reset the adapter data
                aAdpt.resetData();
            }

            aAdpt.getFilter().filter(s.toString());

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    });
     filterBrand = (LinearLayout) dialogView.findViewById(R.id.filter_brand_list);
     filterPrice= (LinearLayout) dialogView.findViewById(R.id.filter_price_list);
     filterRating = (LinearLayout) dialogView.findViewById(R.id.filter_rating_list);
    filterDiscount = (LinearLayout) dialogView.findViewById(R.id.filter_discount_list);

    rating1=(CheckBox)dialogView.findViewById(R.id.filter_rating1_count);
    rating2=(CheckBox)dialogView.findViewById(R.id.filter_rating2_count);
    rating3=(CheckBox)dialogView.findViewById(R.id.filter_rating3_count);
    rating4=(CheckBox)dialogView.findViewById(R.id.filter_rating4_count);
    rating5=(CheckBox)dialogView.findViewById(R.id.filter_rating5_count);

    disc1=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_1_10);
    disc2=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_10_20);
    disc3=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_20_30);
    disc4=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_30_40);
    disc5=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_40_50);
    disc6=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_50_60);
    disc7=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_60_70);
    disc8=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_70_80);
    disc9=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_80_90);
    disc10=(CheckBox)dialogView.findViewById(R.id.filter_disc_count_90_100);

    navigationView.setNavigationItemSelectedListener(ProductsFragment.this);
// Setup the new range seek bar
    rangeSeekBar = new RangeSeekBar<Integer>(activity);
    // Set the range
    rangeSeekBar.setRangeValues(0, 2000);
//        rangeSeekBar.setSelectedMinValue(20);
//        rangeSeekBar.setSelectedMaxValue(88);


    // Add to layout
    LinearLayout layout = (LinearLayout)dialogView.findViewById(R.id.seekbar_placeholder);
    layout.addView(rangeSeekBar);
    // get min and max text view
    final TextView tvMin = (TextView) dialogView.findViewById(R.id.textMin1);
    final TextView tvMax = (TextView) dialogView.findViewById(R.id.textMax1);
    tvMin.setText("" + 0);
    tvMax.setText("" + 2000);
    // Sets the display values of the indices

    rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        tvMin.setText("" + minValue);
                        tvMax.setText("" + maxValue);
        }
    });

    filterClose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b.dismiss();
        }
    });
    filterReset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b.dismiss();
        }
    });
    filterApply.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            priceFilteredArray.clear();
            Log.e("services.size()",""+services.size());
            for(int i=0;i<services.size();i++) {
                ProductModel pm = services.get(i);
                if (rangeSeekBar.getSelectedMinValue() <= pm.getFinalPrice() && pm.getFinalPrice() <= rangeSeekBar.getSelectedMaxValue()) {
                    if(disc1.isChecked()){
                        if(0 <= pm.getDiscount()&&pm.getDiscount() <= 10){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc2.isChecked()){
                        if(10 < pm.getDiscount() && pm.getDiscount() <= 20){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc3.isChecked()){
                        if(20 < pm.getDiscount() && pm.getDiscount() <= 30){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc4.isChecked()){
                        if(30 < pm.getDiscount() && pm.getDiscount() <= 40){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc5.isChecked()){
                        if(40 < pm.getDiscount() && pm.getDiscount() <= 50){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc6.isChecked()){
                        if(50 < pm.getDiscount() && pm.getDiscount() <= 60){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc7.isChecked()){
                        if(60 < pm.getDiscount() && pm.getDiscount() <= 70){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc8.isChecked()){
                        if(70 < pm.getDiscount() && pm.getDiscount() <= 80){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc9.isChecked()){
                        if(80 < pm.getDiscount() && pm.getDiscount() <= 90){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(disc10.isChecked()){
                        if(90 < pm.getDiscount() && pm.getDiscount() <= 100){
                            addFilteredPrdts(pm);
                        }
                    }
                    //Rating
                    if(rating1.isChecked()){
                        if(80 < pm.getRating() && pm.getRating() <= 100){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(rating2.isChecked()){
                        if(60 < pm.getRating() && pm.getRating() <= 80){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(rating3.isChecked()){
                        if(40 < pm.getRating() && pm.getRating() <= 60){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(rating4.isChecked()){
                        if(20 < pm.getRating() && pm.getRating() <= 40){
                            addFilteredPrdts(pm);
                        }
                    }
                    if(rating5.isChecked()){

                        if(0 <= pm.getRating() && pm.getRating() <= 20){
                            addFilteredPrdts(pm);
                        }
                    }
                }

            }

            if(priceFilteredArray.size()==0){
                adapter = new ProductGridAdapter(activity, services);
                ProductsGrid.setAdapter(adapter);
                Log.e("services.size()",""+services.size());
            }else{
                adapter = new ProductGridAdapter(activity, priceFilteredArray);
                ProductsGrid.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            b.dismiss();
        }
    });
}
    public void addFilteredPrdts(ProductModel pm){
        if(priceFilteredArray.size()!=0) {
            int flag=0;
            for (int i = 0; i < priceFilteredArray.size(); i++) {
                ProductModel pf = priceFilteredArray.get(i);
                if (pm.getId() == pf.getId()) {
                    flag=1;
                }
            }
            if(flag==1){
                flag=0;
            }else{
                priceFilteredArray.add(pm);
            }
        }else{
            priceFilteredArray.add(pm);
        }
    }
    //Rating Count
    public void getRatingCount(View dialog){
        int rating_flag1=0,rating_flag2=0, rating_flag3=0, rating_flag4=0, rating_flag5=0;

//        rating1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ProductModel pd=new ProductModel();
//                if(isChecked){
//                   pd.setRating(1);
//                }else{
//
//                }
//            }
//        });

        for(int i=0;i<services.size();i++){
            ProductModel pm=services.get(i);
            if (rangeSeekBar.getSelectedMinValue() <= pm.getFinalPrice() && pm.getFinalPrice() <= rangeSeekBar.getSelectedMaxValue()) {
                if (0 <= pm.getRating() && pm.getRating() <= 20) {
                    Log.e("pm.getRating()", "" + pm.getRating());
                    rating_flag1 += 1;
                } else if (20 < pm.getRating() && pm.getRating() <= 40) {
                    rating_flag2 += 1;
                } else if (40 < pm.getRating() && pm.getRating() <= 60) {
                    rating_flag3 += 1;
                } else if (60 < pm.getRating() && pm.getRating() <= 80) {
                    rating_flag4 += 1;
                } else if (80 < pm.getRating() && pm.getRating() <= 100) {
                    rating_flag5 += 1;
                }

            }
            rating1.setText("( " + rating_flag5+" ) ");
            rating2.setText("( " + rating_flag4+" ) ");
            rating3.setText("( " + rating_flag3+" ) ");
            rating4.setText("( " + rating_flag2+" ) ");
            rating5.setText("( " + rating_flag1+" ) ");
        }
    }
    //Discount Count
    public void getDiscountCount(View dialog){
        int disc_flag1=0,disc_flag2=0, disc_flag3=0, disc_flag4=0, disc_flag5=0, disc_flag6=0, disc_flag7=0, disc_flag8=0, disc_flag9=0, disc_flag10=0;

        for(int i=0;i<services.size();i++) {
            ProductModel pm = services.get(i);
            Log.e("getSelectedMinValue()", "" + rangeSeekBar.getSelectedMinValue());
            Log.e("getSelectedMaxValue()", "" + rangeSeekBar.getSelectedMaxValue());
            if (rangeSeekBar.getSelectedMinValue() <= pm.getFinalPrice() && pm.getFinalPrice() <= rangeSeekBar.getSelectedMaxValue()) {
                if (0 <= pm.getDiscount() && pm.getDiscount() <= 10) {
                    Log.e("pm.getDiscount()", "" + pm.getDiscount());
                    disc_flag1 += 1;

                } else if (10 < pm.getDiscount() && pm.getDiscount() <= 20) {
                    disc_flag2 += 1;
                } else if (20 < pm.getDiscount() && pm.getDiscount() <= 30) {
                    disc_flag3 += 1;
                } else if (30 < pm.getDiscount() && pm.getDiscount() <= 40) {
                    disc_flag4 += 1;
                } else if (40 < pm.getDiscount() && pm.getDiscount() <= 50) {
                    disc_flag5 += 1;
                } else if (50 < pm.getDiscount() && pm.getDiscount() <= 60) {
                    disc_flag6 += 1;
                } else if (60 < pm.getDiscount() && pm.getDiscount() <= 70) {
                    disc_flag7 += 1;
                } else if (70 < pm.getDiscount() && pm.getDiscount() <= 80) {
                    disc_flag8 += 1;
                } else if (80 < pm.getDiscount() && pm.getDiscount() <= 90) {
                    disc_flag9 += 1;
                } else if (90 < pm.getDiscount() && pm.getDiscount() <= 100) {
                    disc_flag10 += 1;
                }

            }
            disc1.setText("0 - 10 ( " + disc_flag1+" )");
            disc2.setText("10 - 20 ( " + disc_flag2+" )");
            disc3.setText("20 - 30 ( " + disc_flag3+" )");
            disc4.setText("30 - 40 ( " + disc_flag4+" )");
            disc5.setText("40 - 50 ( " + disc_flag5+" )");
            disc6.setText("50 - 60 ( " + disc_flag6+" )");
            disc7.setText("60 - 70 ( " + disc_flag7+" )");
            disc8.setText("70 - 80 ( " + disc_flag8+" )");
            disc9.setText("80 - 90 ( " + disc_flag9+" )");
            disc10.setText("90 - 100 ( " + disc_flag10+" )");
        }
    }
public void showSortDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.sort_dialog, null);
    dialogBuilder.setView(dialogView);

    Button applyBtn = (Button) dialogView.findViewById(R.id.sort_apply_btn);

    final AlertDialog b = dialogBuilder.create();
    WhoRU=(RadioGroup)dialogView.findViewById(R.id.whoru);
    discRadio = (RadioButton)dialogView.findViewById(R.id.disc_radio);
    arrRadio = (RadioButton)dialogView.findViewById(R.id.arr_radio);
    htolRadio = (RadioButton)dialogView.findViewById(R.id.htol_radio);
    ltohRadio = (RadioButton)dialogView.findViewById(R.id.ltoh_radio);
    popRadio = (RadioButton)dialogView.findViewById(R.id.pop_radio);
    // Session Manager
    //  sessionManager = new SessionManager(getApplicationContext());

//        Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManager.isLoggedIn(), Toast.LENGTH_LONG).show();
    sortRadio(radioBtnId);
    WhoRU.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            selectedId = WhoRU.getCheckedRadioButtonId();
            radioBtnId=checkedId;
//           Toast.makeText(activity,"selectesd iD  :" + checkedId,Toast.LENGTH_SHORT).show();
            sortRadio(checkedId);
//            type=Student.getText().toString();


        }
    });

    b.setOnShowListener(new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                revealShow(dialogView, true, null);
            }

        }
    });

    applyBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                revealShow(dialogView, false, b);
            }else{
                b.dismiss();
            }

            if( sort_flag==1){
                Collections.sort(services, new Comparator<ProductModel>() {
                    public int compare(ProductModel a, ProductModel b) {
                        if (a.getDiscount() == b.getDiscount())
                            return a.getTitle().compareTo(b.getTitle());
                        return a.getDiscount() > b.getDiscount() ? -1 : a.getDiscount() < b.getDiscount() ? 1 : 0;
                    }
                });
                sortSubTxt.setText(discRadio.getText());
            }else if( sort_flag==2){
                Collections.sort(services, new NewArrivalSorter("new"));
                sortSubTxt.setText(arrRadio.getText());
            }else if( sort_flag==3){
                sortSubTxt.setText(htolRadio.getText());
                Collections.sort(services, new Comparator<ProductModel>() {
                    public int compare(ProductModel a, ProductModel b) {
                        if (a.getFinalPrice() == b.getFinalPrice())
                            return a.getTitle().compareTo(b.getTitle());
                        return a.getFinalPrice() > b.getFinalPrice() ? -1 : a.getFinalPrice() < b.getFinalPrice() ? 1 : 0;
                    }
                });
            }else if( sort_flag==4){
                sortSubTxt.setText(ltohRadio.getText());
                Collections.sort(services, new Comparator<ProductModel>() {
                    public int compare(ProductModel a, ProductModel b) {
                        if (a.getFinalPrice() == b.getFinalPrice())
                            return a.getTitle().compareTo(b.getTitle());
                        return a.getFinalPrice() > b.getFinalPrice() ? 1 : a.getFinalPrice() < b.getFinalPrice() ? -1 : 0;
                    }
                });
            }else if( sort_flag==5){
                sortSubTxt.setText(popRadio.getText());
                Collections.sort(services, new Comparator<ProductModel>() {
                    public int compare(ProductModel a, ProductModel b) {
                        if (a.getRating() == b.getRating())
                            return a.getTitle().compareTo(b.getTitle());
                        return a.getRating() > b.getRating() ? -1 : a.getRating() < b.getRating() ? 1 : 0;
                    }
                });
            }
            ProductsGrid.setAdapter(new ProductGridAdapter(activity, services));
            b.dismiss();
        }
    });

    b.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    b.show();

}
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealShow(View rootView, boolean reveal, final AlertDialog dialog) {
        final View view = rootView.findViewById(R.id.reveal_view);
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if(reveal){
            Animator revealAnimator = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                        w / 2, h / 2, 0, maxRadius);
            }
            view.setVisibility(View.VISIBLE);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });

            anim.start();
        }
    }
    public void sortRadio(int checkedId){
        switch (checkedId){
            case R.id.disc_radio:
                sort_flag=1;
                discRadio.setTextColor(color);
                discRadio.setChecked(true);
                arrRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                htolRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                ltohRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                popRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                break;
            case R.id.arr_radio:
                sort_flag=2;
                discRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                arrRadio.setTextColor(color);
                arrRadio.setChecked(true);
                htolRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                ltohRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                popRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                break;
            case R.id.htol_radio:
                sort_flag=3;
                discRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                arrRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                htolRadio.setTextColor(color);
                htolRadio.setChecked(true);
                ltohRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                popRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                break;
            case R.id.ltoh_radio:
                sort_flag=4;
                discRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                arrRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                htolRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                ltohRadio.setTextColor(color);
                ltohRadio.setChecked(true);
                popRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                break;
            case R.id.pop_radio:
                sort_flag=5;
                discRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                arrRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                htolRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                ltohRadio.setTextColor(getResources().getColor(R.color.view_divider_color));
                popRadio.setTextColor(color);
                popRadio.setChecked(true);
                break;
        }

    }
    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
            task = new AsyncHttpTask();
//            task.execute(Utils.productsUrl);
            task.execute(prdtsUrl);
//            task.execute(Utils.instantServerUrl);
            Log.e("sendrequest",prdtsUrl);
        } else {
            message = getResources().getString(R.string.no_internet_connection);
            showAlertDialog(message, true);
        }
    }
    public void showAlertDialog(String message, final boolean finish) {
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            activity.finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(getActivity(), "id"+id, Toast.LENGTH_LONG).show();
        switch (id){
            case R.id.filter_brand:
                filterBrand.setVisibility(View.VISIBLE);
                filterPrice.setVisibility(View.GONE);
                filterRating.setVisibility(View.GONE);
                filterDiscount.setVisibility(View.GONE);
                break;
            case R.id.filter_price:
                // get seekbar from view
               /* final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) dialogView.findViewById(R.id.rangeSeekbar1);

// get min and max text view
                final TextView tvMin = (TextView) dialogView.findViewById(R.id.textMin1);
                final TextView tvMax = (TextView) dialogView.findViewById(R.id.textMax1);

// set listener
                rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue, Number maxValue) {
                        //get entered texts from the edittexts,and convert to integers.
                        int value1 = Integer.parseInt(minValue.toString());
                        int value2 = Integer.parseInt(maxValue.toString());
//            Double value2 = Double.parseDouble(mEditText2.getText().toString());
//            Double value3 = Double.parseDouble(mEditText3.getText().toString());
                        //do the calculation
//            Double calculatedValue = (value2/value1)*value3;
//                        priceFilteredArray.clear();
                        tvMin.setText(String.valueOf(value1));
                        tvMax.setText(String.valueOf(value2));
//                        adapter.filter(value1,value2);
//                        for(int i=0;i<services.size();i++) {
//                            ProductModel pm = services.get(i);
//                            if (value1 >= pm.getFinalPrice() || pm.getFinalPrice() <= value2) {
//
//                                priceFilteredArray.add(pm);
//
//                            }
//                        }
                    }
                });
//                // set final value listener
//                rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
//                    @Override
//                    public void finalValue(Number minValue, Number maxValue) {
//                        Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
//                    }
//                });*/



              /*  // Gets the RangeBar
                RangeBar  rangebar = (RangeBar)dialogView.findViewById(R.id.rangebar1);

                // Setting Index Values -------------------------------

                // Gets the index value TextViews
                // get min and max text view
                final TextView tvMin = (TextView) dialogView.findViewById(R.id.textMin1);
                final TextView tvMax = (TextView) dialogView.findViewById(R.id.textMax1);

                // Sets the display values of the indices
                rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                    @Override
                    public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

//                        tvMin.setText("" + leftThumbIndex);
//                        tvMax.setText("" + rightThumbIndex);
                    }
                });*/


                filterBrand.setVisibility(View.GONE);
                filterPrice.setVisibility(View.VISIBLE);
                filterRating.setVisibility(View.GONE);
                filterDiscount.setVisibility(View.GONE);

                break;
            case R.id.filter_rating:
                getRatingCount(dialogView);
                filterBrand.setVisibility(View.GONE);
                filterPrice.setVisibility(View.GONE);
                filterRating.setVisibility(View.VISIBLE);
                filterDiscount.setVisibility(View.GONE);
                break;
            case R.id.filter_dicount:
                getDiscountCount(dialogView);
                filterBrand.setVisibility(View.GONE);
                filterPrice.setVisibility(View.GONE);
                filterRating.setVisibility(View.GONE);
                filterDiscount.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        return true;
    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(activity, "onItemClick", Toast.LENGTH_LONG).show();
//    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
            try {
                progressLL.setVisibility(View.VISIBLE);
//                pb.setVisibility(View.VISIBLE);
            }catch(Exception e){

            }

        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url(params[0])
//                        .build();

                try {
//                    Response response = client.newCall(request).execute();
//                    String jsonString = response.body().string();
//                    Log.e("NGVL", jsonString);

//                 forming th java.net.URL object
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

//                 for Get request
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();
//                int statusCode = response.code();
//                    Log.e("response.code", ""+response.hashCode());
//                 200 represents HTTP OK
                if (statusCode ==  200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.e("response.toString()", response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("hello", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//             Download complete. Lets update UI
            if (result == 1) {
                Log.e("onPostExecute", "onPostExecute");
                if(services!=null&&services.size()!=0) {
                    adapter = new ProductGridAdapter(activity, services);
                    ProductsGrid.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressLL.setVisibility(View.GONE);
//                pb.setVisibility(View.GONE);

//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
                }else {
                    message = getResources().getString(R.string.no_products);
//                    showAlertDialog(message, true);
                }
            } else {
                Log.e("hello", "Failed to fetch data!");
                pb.setVisibility(View.GONE);
//                message = getResources().getString(R.string.no_products);
//                showAlertDialog(message, true);
//                Toast.makeText(activity,"No Prodructs Found",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(String result) {
        try {
//            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=new JSONObject(result);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
//                message = jsonObject.optString(TagName.TAG_MSG);

//               if (message.equals("success")) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);

                if (status==1) {
                    JSONArray jarr=jsonObject.optJSONArray("getproducts");
                    JSONObject job=jarr.optJSONObject(0);
                    JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                    int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                    String message1 = jobstat.optString(TagName.TAG_MSG);
                    if(status1==1) {
                    JSONArray posts = job.optJSONArray(TagName.TAG_PRODUCT);

//            Initialize array if null
                    if (null == services) {
                        services = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        ProductModel item = new ProductModel();
                        item.setId(post.optInt(TagName.KEY_ID));
                        item.setTitle(post.optString(TagName.KEY_NAME));
                        item.setDescription(post.optString(TagName.KEY_DES));
                        item.setCost(post.optDouble(TagName.KEY_PRICE));
                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                    Log.e("name", "name"+ post.optDouble("finalPrice"));
                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
                        item.setWishlist(post.optInt(TagName.KEY_WISHLIST));
                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item.setShare(post1.optString(TagName.KEY_SHARE));
                        item.setTag(post1.optString(TagName.KEY_TAG));
                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item.setRating(post1.optInt(TagName.KEY_RATING));
                        services.add(item);
                    }
                    }else{
                        Toast.makeText(activity, "No Products", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
//                    message = jsonObject.optString(TagName.TAG_MSG);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
   /* private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);

                if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);

//                    if (status) {
//                JSONObject jsonData = jsonObject
//                        .getJSONObject(TagName.TAG_PRODUCT);
//                    }
                JSONArray posts = jsonObject.optJSONArray(TagName.TAG_PRODUCT);

//            Initialize array if null
                if (null == services) {
                    services = new ArrayList<ProductModel>();
                }

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);

                    ProductModel item = new ProductModel();
                    item.setId(post.optInt(TagName.KEY_ID));
                    item.setTitle(post.optString(TagName.KEY_NAME));
                    item.setDescription(post.optString(TagName.KEY_DES));
                    item.setCost(post.optDouble(TagName.KEY_PRICE));
                    item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                    Log.e("name", "name");
                    item.setThumbnail(post.optString(TagName.KEY_THUMB));
                    item.setWishlist(post.optInt(TagName.KEY_WISHLIST));
                    JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                    item.setShare(post1.optString(TagName.KEY_SHARE));
                    item.setTag(post1.optString(TagName.KEY_TAG));
                    item.setDiscount(post1.optInt(TagName.KEY_DISC));
                    item.setRating(post1.optInt(TagName.KEY_RATING));
                    services.add(item);
                }
                } else {
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
