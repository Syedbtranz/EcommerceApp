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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Ravi on 29/07/15.
 */
public class ProductsFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    View div;
    ImageView dis_image, arr_image, htol_image, ltoh_image, pop_image;
    TextView disTxt, arrTxt, htolTxt, ltohTxt, popTxt;
    LinearLayout filterBrand, filterPrice, filterRating, filterDiscount;
    ListView filterList;
    TextView sortSubTxt;
    GridView ProductsGrid;
    ArrayList<ProductModel> services;
    FragmentActivity activity;
    ProductGridAdapter adapter;
    AsyncHttpTask task;
    AlertDialog alertDialog;
    String message;
    LinearLayout sortBtn, filterBtn;
    LinearLayout progressLL;
    ProgressBar pb;
    boolean view_flag=true;
    ImageView viewBtn;
    String prdtsUrl, prdtsTitle;
    int color, sort_flag;
    int rating_flag1,rating_flag2, rating_flag3, rating_flag4, rating_flag5;
    int disc_flag1,disc_flag2, disc_flag3, disc_flag4, disc_flag5, disc_flag6, disc_flag7, disc_flag8, disc_flag9, disc_flag10;
    public static final String PRDTS_FRAG = "pdts_fragment";
    // The data to show
    List<ProductModel> brandList = new ArrayList<ProductModel>();
    FilterBrandAdapter aAdpt;
    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

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
                    Toast.makeText(getActivity(), "Filter", Toast.LENGTH_LONG).show();
                    showFilterDialog();
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
        } else {
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
public void showFilterDialog() {
    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this.getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.filter_dialog, null);
    ImageView close = (ImageView) dialogView.findViewById(R.id.filter_close);
    Button reset = (Button) dialogView.findViewById(R.id.reset_btn);
    Button filterApply = (Button) dialogView.findViewById(R.id.filter_apply);
    NavigationView navigationView = (NavigationView)dialogView.findViewById(R.id.filter_nav_view);
    getRatingCount(dialogView);
    getDiscountCount(dialogView);
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
    navigationView.setNavigationItemSelectedListener(ProductsFragment.this);
    // get seekbar from view
    final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) dialogView.findViewById(R.id.rangeSeekbar1);

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
            tvMin.setText(String.valueOf(value1));
            tvMax.setText(String.valueOf(value2));
        }
    });
    dialogBuilder.setView(dialogView);
    final android.support.v7.app.AlertDialog b = dialogBuilder.create();
    b.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_slide;
    b.show();
    close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b.dismiss();
        }
    });
    reset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b.dismiss();
        }
    });
    filterApply.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b.dismiss();
        }
    });
}
    //Rating Count
    public void getRatingCount(View dialog){
        TextView rating1=(TextView)dialog.findViewById(R.id.filter_rating1_count);
        TextView rating2=(TextView)dialog.findViewById(R.id.filter_rating2_count);
        TextView rating3=(TextView)dialog.findViewById(R.id.filter_rating3_count);
        TextView rating4=(TextView)dialog.findViewById(R.id.filter_rating4_count);
        TextView rating5=(TextView)dialog.findViewById(R.id.filter_rating5_count);

        for(int i=0;i<services.size();i++){
            ProductModel pm=services.get(i);
            if(0>=pm.getRating() || pm.getRating()<=20){
                Log.e("pm.getDiscount()",""+pm.getDiscount());
                rating_flag1+=1;
                rating1.setText(""+rating_flag1);

            }else if(20>pm.getRating() || pm.getRating()<=40){
                rating_flag2+=1;
                rating2.setText(""+rating_flag2);
            }else if(40>pm.getRating()||pm.getRating()<=60){
                rating_flag3+=1;
                rating3.setText(""+rating_flag3);
            }else if(60>pm.getRating()||pm.getRating()<=80){
                rating_flag4+=1;
                rating4.setText(""+rating_flag4);
            }else if(80>pm.getRating()||pm.getRating()<=100){
                rating_flag5+=1;
                rating5.setText(""+rating_flag5);
            }

        }
    }
    //Discount Count
    public void getDiscountCount(View dialog){
        TextView disc1=(TextView)dialog.findViewById(R.id.filter_disc_count_1_10);
        TextView disc2=(TextView)dialog.findViewById(R.id.filter_disc_count_10_20);
        TextView disc3=(TextView)dialog.findViewById(R.id.filter_disc_count_20_30);
        TextView disc4=(TextView)dialog.findViewById(R.id.filter_disc_count_30_40);
        TextView disc5=(TextView)dialog.findViewById(R.id.filter_disc_count_40_50);
        TextView disc6=(TextView)dialog.findViewById(R.id.filter_disc_count_50_60);
        TextView disc7=(TextView)dialog.findViewById(R.id.filter_disc_count_60_70);
        TextView disc8=(TextView)dialog.findViewById(R.id.filter_disc_count_70_80);
        TextView disc9=(TextView)dialog.findViewById(R.id.filter_disc_count_80_90);
        TextView disc10=(TextView)dialog.findViewById(R.id.filter_disc_count_90_100);
        for(int i=0;i<services.size();i++){
            ProductModel pm=services.get(i);
            if(0>=pm.getDiscount() || pm.getDiscount()<=10){
                Log.e("pm.getDiscount()",""+pm.getDiscount());
                disc_flag1+=1;
                disc1.setText(""+disc_flag1);

            }else if(10>pm.getDiscount() || pm.getDiscount()<=20){
                disc_flag2+=1;
                disc2.setText(""+disc_flag2);
            }else if(20>pm.getDiscount()||pm.getDiscount()<=30){
                disc_flag3+=1;
                disc3.setText(""+disc_flag3);
            }else if(30>pm.getDiscount()||pm.getDiscount()<=40){
                disc_flag4+=1;
                disc4.setText(""+disc_flag4);
            }else if(40>pm.getDiscount()||pm.getDiscount()<=50){
                disc_flag5+=1;
                disc5.setText(""+disc_flag5);
            }else if(50>pm.getDiscount()||pm.getDiscount()<=60){
                disc_flag6+=1;
                disc6.setText(""+disc_flag6);
            }else if(60>pm.getDiscount()||pm.getDiscount()<=70){
                disc_flag7+=1;
                disc7.setText(""+disc_flag7);
            }else if(70>pm.getDiscount()||pm.getDiscount()<=80){
                disc_flag8+=1;
                disc8.setText(""+disc_flag8);
            }else if(80>pm.getDiscount()||pm.getDiscount()<=90){
                disc_flag9+=1;
                disc9.setText(""+disc_flag9);
            }else if(90>pm.getDiscount()||pm.getDiscount()<=100){
                disc_flag10+=1;
                disc10.setText(""+disc_flag10);
            }
        }
    }
public void showSortDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.sort_dialog, null);
    dialogBuilder.setView(dialogView);
    dis_image = (ImageView) dialogView.findViewById(R.id.sort_dis_img);
    arr_image = (ImageView) dialogView.findViewById(R.id.sort_arrive_img);
    htol_image = (ImageView) dialogView.findViewById(R.id.sort_htol_img);
    ltoh_image = (ImageView) dialogView.findViewById(R.id.sort_ltoh_img);
    pop_image = (ImageView) dialogView.findViewById(R.id.sort_pop_img);
    disTxt = (TextView) dialogView.findViewById(R.id.sort_dis_txt);
    arrTxt = (TextView) dialogView.findViewById(R.id.sort_arrive_txt);
    htolTxt = (TextView) dialogView.findViewById(R.id.sort_htol_txt);
    ltohTxt = (TextView) dialogView.findViewById(R.id.sort_ltoh_txt);
    popTxt = (TextView) dialogView.findViewById(R.id.sort_pop_txt);
    Button applyBtn = (Button) dialogView.findViewById(R.id.sort_apply_btn);

    final AlertDialog b = dialogBuilder.create();

    b.setOnShowListener(new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                revealShow(dialogView, true, null);
            }

        }
    });
    sortRadio();
    dis_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sort_flag=1;
            sortRadio();
//           v.setBackground();
        }
    });
    arr_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sort_flag=2;
            sortRadio();
        }
    });
    htol_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sort_flag=3;
            sortRadio();

        }
    });
    ltoh_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sort_flag=4;
            sortRadio();
        }
    });
    pop_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sort_flag=5;
            sortRadio();
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
                sortSubTxt.setText(disTxt.getText());
            }else if( sort_flag==2){
                Collections.sort(services, new NewArrivalSorter("new"));
                sortSubTxt.setText(arrTxt.getText());
            }else if( sort_flag==3){
                sortSubTxt.setText(htolTxt.getText());
                Collections.sort(services, new Comparator<ProductModel>() {
                    public int compare(ProductModel a, ProductModel b) {
                        if (a.getFinalPrice() == b.getFinalPrice())
                            return a.getTitle().compareTo(b.getTitle());
                        return a.getFinalPrice() > b.getFinalPrice() ? -1 : a.getFinalPrice() < b.getFinalPrice() ? 1 : 0;
                    }
                });
            }else if( sort_flag==4){
                sortSubTxt.setText(ltohTxt.getText());
                Collections.sort(services, new Comparator<ProductModel>() {
                    public int compare(ProductModel a, ProductModel b) {
                        if (a.getFinalPrice() == b.getFinalPrice())
                            return a.getTitle().compareTo(b.getTitle());
                        return a.getFinalPrice() > b.getFinalPrice() ? 1 : a.getFinalPrice() < b.getFinalPrice() ? -1 : 0;
                    }
                });
            }else if( sort_flag==5){
                sortSubTxt.setText(popTxt.getText());
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
    public void sortRadio(){
        if( sort_flag==1){
            dis_image.setImageResource(R.drawable.checked);
//            dis_image.setColorFilter(color);
            arr_image.setImageResource(R.drawable.empty);
            htol_image.setImageResource(R.drawable.empty);
            ltoh_image.setImageResource(R.drawable.empty);
            pop_image.setImageResource(R.drawable.empty);

            disTxt.setTextColor(color);
            arrTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            htolTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            ltohTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            popTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
        }else if( sort_flag==2){
            dis_image.setImageResource(R.drawable.empty);
            arr_image.setImageResource(R.drawable.checked);
//            arr_image.setColorFilter(color);
            htol_image.setImageResource(R.drawable.empty);
            ltoh_image.setImageResource(R.drawable.empty);
            pop_image.setImageResource(R.drawable.empty);

            disTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            arrTxt.setTextColor(color);
            htolTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            ltohTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            popTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
        }else if( sort_flag==3){
            dis_image.setImageResource(R.drawable.empty);
            arr_image.setImageResource(R.drawable.empty);
            htol_image.setImageResource(R.drawable.checked);
//            htol_image.setColorFilter(color);
            ltoh_image.setImageResource(R.drawable.empty);
            pop_image.setImageResource(R.drawable.empty);

            disTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            arrTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            htolTxt.setTextColor(color);
            ltohTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            popTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
        }else if( sort_flag==4){
            dis_image.setImageResource(R.drawable.empty);
            arr_image.setImageResource(R.drawable.empty);
            htol_image.setImageResource(R.drawable.empty);
            ltoh_image.setImageResource(R.drawable.checked);
//            ltoh_image.setColorFilter(color);
            pop_image.setImageResource(R.drawable.empty);

            disTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            arrTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            htolTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            ltohTxt.setTextColor(color);
            popTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
        }else if( sort_flag==5){
            dis_image.setImageResource(R.drawable.empty);
            arr_image.setImageResource(R.drawable.empty);
            htol_image.setImageResource(R.drawable.empty);
            ltoh_image.setImageResource(R.drawable.empty);
            pop_image.setImageResource(R.drawable.checked);
//            pop_image.setColorFilter(color);

            disTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            arrTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            htolTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            ltohTxt.setTextColor(getResources().getColor(R.color.view_divider_color));
            popTxt.setTextColor(color);
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
            Log.e("sendrequest","sendrequest");
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
                filterBrand.setVisibility(View.GONE);
                filterPrice.setVisibility(View.VISIBLE);
                filterRating.setVisibility(View.GONE);
                filterDiscount.setVisibility(View.GONE);
                break;
            case R.id.filter_rating:
                filterBrand.setVisibility(View.GONE);
                filterPrice.setVisibility(View.GONE);
                filterRating.setVisibility(View.VISIBLE);
                filterDiscount.setVisibility(View.GONE);
                break;
            case R.id.filter_dicount:
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
                    showAlertDialog(message, true);
                }
            } else {
                Log.e("hello", "Failed to fetch data!");
                pb.setVisibility(View.GONE);
//                message = getResources().getString(R.string.no_products);
                showAlertDialog(message, true);
//                Toast.makeText(activity,"No Prodructs Found",Toast.LENGTH_SHORT).show();
            }
        }
    }
   /* private void parseResult(String result) {
        try {
//            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=new JSONObject(result);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
//                message = jsonObject.optString(TagName.TAG_MSG);

//               if (message.equals("success")) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);

                if (status!=0) {
//                JSONObject jsonData = jsonObject
//                        .getJSONObject(TagName.TAG_PRODUCT);
//                    }
                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_PRODUCT);

            *//*Initialize array if null*//*
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
                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item.setShare(post1.optString(TagName.KEY_SHARE));
                        item.setTag(post1.optString(TagName.KEY_TAG));
                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item.setRating(post1.optInt(TagName.KEY_RATING));
                        services.add(item);
                    }
                } else {
                    message = jsonObject.optString(TagName.TAG_MSG);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    private void parseResult(String result) {
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
    }


public void loadData(String url){
    Log.d("NGVL", "WEB");
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
            .url(url)
            .build();

    try {
        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();
        Log.d("NGVL", jsonString);
//        JSONArray jsonArray = new JSONArray(jsonString);

        services = new ArrayList<>();

//        int lenght = jsonArray.length();
//        for (int i = 0; i < lenght; i++) {
//            String city = jsonArray.getString(i);
//            services.add(city);
            JSONArray jArry = new JSONArray(jsonString);
            JSONObject jsonObject=jArry.getJSONObject(0);

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
//        }

    } catch (Exception e) {
        e.printStackTrace();
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
