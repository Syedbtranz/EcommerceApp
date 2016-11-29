package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.BookNowActivity;
import com.btranz.ecommerceapp.activity.CredientialActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.HorizontalListAdapter;
import com.btranz.ecommerceapp.adapter.ProductGlanceAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Ravi on 29/07/15.
 */
public class ProductItemFragment extends Fragment {
    RatingBar ratingBar, ratingBarBtm;
    EditText pincodeTxt;
    Button simiPrdtBtn, discBtn, tagBtn, pincodeBtn;
    CardView cardView;
    TextView pdtIdTxt, pdtNameTxt,desTxt,priceTxt, finalPriceTxt, ratingTxtBtm, standTxt,expTxt, standSubTitle, expSubTitle;
    ImageView pdtImg, share, like;
    FragmentActivity activity;
    LinearLayout addToCartBtn, buyNowBtn;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    ProductModel product, item1;
    AlertDialog alertDialog;
    AsyncHttpTask taskAsynk;
    ProductDetailsAsyncTask task;
    ArrayList<ProductModel> services, checkOutProductsArray;

    List<String> checkList= new ArrayList<String>();
    List<String> wishList= new ArrayList<String>();
    ProductGlanceAdapter adapter;
    String message;
    private RecyclerView horizontalList;
    private HorizontalListAdapter horizontalAdapter;
    public static final String ARG_ITEM_ID = "pdt_item_fragment";
    DatabaseHandler dbHandler;
    int singlePrdtId;
    boolean flag=true;
    String userId,userEmail, cartBadge;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public ProductItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();

        imageListener = new ImageDisplayListener();
        dbHandler=new DatabaseHandler(activity);
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
//        customerName = sharedpreferences.getString("customerName", "");
        userId = sharedpreferences.getString("userID", "");
        userEmail = sharedpreferences.getString("userEmail", "");
        cartBadge = sharedpreferences.getString("cartBadge", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pdt_item, container, false);
        findViewById(rootView);

        Bundle bundle = this.getArguments();
//        Bundle b = activity.getIntent().getExtras();
//        if (b != null) {
//            product = b.getParcelable("singleProduct");
//            setProductItem(product);
//        }else
        if (bundle != null) {
//            product = bundle.getParcelable("singleProduct");
             singlePrdtId = bundle.getInt("singleProductId", 0);
//            setProductItem(product);
            Log.d("bundle", "product");
        }else {
//            product = activity.getIntent().getParcelableExtra("singleProduct");
            singlePrdtId = activity.getIntent().getIntExtra("singleProductId", 0);
//            setProductItem(product);
            Log.d("intent", "product1");
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        // update the actionbar to show the up carat/affordance

//		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        SpannableString s = new SpannableString(getString(R.string.title_product_details));
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
//                Log.d("MrE", "home selected");
                getActivity().onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });
        if (services == null) {
            sendRequest();
//            Log.e("onResume","test");
////            adapter = new ServicesRecyclerAdapter(activity, services);
//            Log.e("onResume", "onResume");
        } else {
            Log.e("onResume else", "onResume else");
            horizontalList.setAdapter(new HorizontalListAdapter(activity, services,R.layout.gallary_inflate));
            cardView.setVisibility(View.GONE);
            setProductItem(item1);
//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//            recyclerView.scrollToPosition(0);
        }

    }

    private void findViewById(View view) {

        cardView=(CardView)view.findViewById(R.id.progress_ll) ;
        horizontalList = (RecyclerView)view.findViewById(R.id.sim_horizontal_recycler);
        horizontalList.setNestedScrollingEnabled(false);
        horizontalList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(horizontalManager);

        pdtNameTxt = (TextView) view.findViewById(R.id.item_title);
        pdtIdTxt = (TextView) view.findViewById(R.id.product_id_text);
        priceTxt = (TextView) view.findViewById(R.id.product_price);
        finalPriceTxt = (TextView) view.findViewById(R.id.product_final_price);
        desTxt = (TextView) view.findViewById(R.id.product_description);
        ratingTxtBtm = (TextView) view.findViewById(R.id.rating_txt_btm);
        ratingTxtBtm = (TextView) view.findViewById(R.id.rating_txt_btm);
        standTxt = (TextView) view.findViewById(R.id.standard_cost);
        expTxt = (TextView) view.findViewById(R.id.express_cost);
        standSubTitle = (TextView) view.findViewById(R.id.standard_sub_title);
        expSubTitle = (TextView) view.findViewById(R.id.express_sub_title);

        pincodeTxt = (EditText) view.findViewById(R.id.pincode_et);

        pdtImg = (ImageView) view.findViewById(R.id.item_image);
        share = (ImageView) view.findViewById(R.id.prdt_share);
        like = (ImageView) view.findViewById(R.id.prdt_like);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBarBtm = (RatingBar) view.findViewById(R.id.ratingbar_btm);

        simiPrdtBtn=(Button) view.findViewById(R.id.similar_prdts_btn);
        discBtn=(Button) view.findViewById(R.id.disc_tag);
        tagBtn=(Button) view.findViewById(R.id.offer_tag);
        pincodeBtn=(Button) view.findViewById(R.id.pincode_check_btn);

        addToCartBtn=(LinearLayout)view.findViewById(R.id.item_addtocart_btn);
        buyNowBtn=(LinearLayout)view.findViewById(R.id.item_buynow_btn);

        pincodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin=pincodeTxt.getText().toString();
                if(pin.equals("")){
                    Toast.makeText(getActivity(), "Pincode Required", Toast.LENGTH_SHORT).show();
                    pincodeTxt.requestFocus();
                }else if(pin.length()!=6){
                    pincodeTxt.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter The Correct Pincode", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "succcess", Toast.LENGTH_SHORT).show();
                }
            }
        });
       //ADD TO CART ACTION
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCart();

            }
        });
        //BUY NOW ACTION
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Item ordered", Toast.LENGTH_SHORT).show();
//                 /*Initialize array if null*/
//                if (null == checkOutProductsArray) {
//                    checkOutProductsArray = new ArrayList<ProductModel>();
//                }
//                checkOutProductsArray.add(item1);
//                Log.e("checkOutProductsArray",""+checkOutProductsArray);
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("CheckOutProductList", checkOutProductsArray);
//                Output
//                ArrayList<ProductModel> challenge = this.getIntent().getExtras().getParcelableArrayList("CheckOutProductList");
                if(userId.equals("")){
                    bookNow();
                }else {
                    getQuoteId(userId, String.valueOf(singlePrdtId));
                }
//                addtoCart();

//                Intent in=new Intent(getActivity(), BookNowActivity.class);
//                in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
//                activity.startActivity(in);
//                activity.overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
            }
        });

        //image color change when change the theme colors
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
        like.setColorFilter(color);
        //LIKE ACTION
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, " liked ", Toast.LENGTH_SHORT).show();
                if(flag){
//
                    ((ImageView)v).setImageResource(R.drawable.like_icon_filled);
                    addtoWishlist();
                    flag=false;
                }else{

                    ((ImageView)v).setImageResource(R.drawable.like_icon);
                    reomvewishlistItem(singlePrdtId);
                    flag=true;
                }
            }
        });

        //SIMILAR PRODUCTS ACTION
        simiPrdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                ProductModel product = (ProductModel) services.get(position);
//                arguments.putParcelable("singleProduct", product);
                arguments.putString("prdtsUrl", Utils.similarPrdtsUrl+singlePrdtId);
                arguments.putString("prdtsTitle", "similar products for "+item1.getTitle());
                // Start a new fragment
                fragment = new ProductsFragment();
                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_second, fragment,
                        ProductsFragment.PRDTS_FRAG);
                transaction.addToBackStack(ProductsFragment.PRDTS_FRAG);
                transaction.commit();
            }
        });
    }
    public void bookNow(){
        Intent in=new Intent(activity, CredientialActivity.class);
        in.putExtra("credKey", TagName.CRED_LOGIN);
        in.putParcelableArrayListExtra("CheckOutProductList",checkOutProductsArray);
//                in.putExtras(bundle);
        activity.startActivity(in);
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }
    public void addtoCart(){
        if(userId.equals("")){
            checkList=dbHandler.checkProduct(String.valueOf(singlePrdtId));
            if(checkList.size()<1){
                Toast.makeText(getActivity(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                dbHandler.insertCart(String.valueOf(singlePrdtId),item1.getTitle(),String.valueOf(item1.getCost()),String.valueOf(item1.getFinalPrice()),item1.getThumbnail(),"1");

                if(cartBadge.equals("")){
                    ((SecondActivity) getActivity()).writeBadge(1);
                    editor.putString("cartBadge", String.valueOf(1));
                    editor.commit();
//                            writeBadge(0);
                }else{
                    ((SecondActivity) getActivity()).writeBadge(Integer.parseInt(cartBadge)+1);
                    editor.putString("cartBadge", String.valueOf(Integer.parseInt(cartBadge)+1));
                    editor.commit();
                }
            }else{
                Toast.makeText(getActivity(), "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
            }

        }else{
            addtocartData(String.valueOf(singlePrdtId),"1",userId);

        }
    }
    public void addtoWishlist(){
        if(userId.equals("")){
            wishList=dbHandler.checkWishlistProduct(String.valueOf(singlePrdtId));
            if(wishList.size()<1){
                Toast.makeText(getActivity(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                dbHandler.insertWishlist(String.valueOf(singlePrdtId),item1.getTitle(),String.valueOf(item1.getCost()),String.valueOf(item1.getFinalPrice()),item1.getThumbnail(),"1");

//                if(cartBadge.equals("")){
//                    ((SecondActivity) getActivity()).writeBadge(1);
//                    editor.putString("cartBadge", String.valueOf(1));
//                    editor.commit();
////                            writeBadge(0);
//                }else{
//                    ((SecondActivity) getActivity()).writeBadge(Integer.parseInt(cartBadge)+1);
//                    editor.putString("cartBadge", String.valueOf(Integer.parseInt(cartBadge)+1));
//                    editor.commit();
//                }
            }else{
                Toast.makeText(getActivity(), "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
            }

        }else{
            addWishlistItem(String.valueOf(singlePrdtId),userId);

        }
    }
    public void reomvewishlistItem(int prdtId){
        if(userId.equals("")) {
            dbHandler.removeWishlistItem(String.valueOf(prdtId));
        }else {
//            sendRequest();
            deleteWishlistItem(String.valueOf(prdtId),userId);
        }

    }
    public void deleteWishlistItem(String prdtId,String userId){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            String URL = "http://...";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("reference_id", "1");
//            jsonBody.put("service_id", "1");
//            jsonBody.put("client_id", userId);
//            jsonBody.put("service_type", "1");
//            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.itemremovewishlistUrl+userId+"/"+prdtId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if(jsonObject!=null) {
                            String status=jsonObject.optString(TagName.TAG_STATUS);
                            if(status.equalsIgnoreCase("success")){
//                                String status=jsonObject.optString(TagName.KEY_MSG);
                                Toast.makeText(activity,jsonObject.optString(TagName.TAG_MSG),Toast.LENGTH_SHORT).show();
//                                activity.finish();
                            }
//                            JSONObject job=jsonObject.optJSONObject(TagName.TAG_DATA);
//                            editor = sharedpreferences.edit();
//                            editor.putString("userId", job.optString("user_id"));
//                            editor.putString("userName", job.optString("user_name"));
////                            editor.putString("password", jsonObject.optString("password"));
////                            editor.putString("userName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(activity,"Following detais are incorrect",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

             /*   @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }*/

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response);
//                        Log.e(" response.data", response);

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ImageDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);

                }
            }
        }
    }

    private void setProductItem(final ProductModel resultProduct) {
        pdtNameTxt.setText("" + resultProduct.getTitle());
        desTxt.setText(Html.fromHtml( resultProduct.getDescription()));
//        desTxt.setText(resultProduct.getDescription());
        priceTxt.setText(String.valueOf(resultProduct.getCost()));
        priceTxt.setPaintFlags(priceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        Log.e("MrE", ""+resultProduct.getFinalPrice());
        finalPriceTxt.setText("" + resultProduct.getFinalPrice());
        standTxt.setText("( " + resultProduct.getStandCost()+" )");
        expTxt.setText("( +"+getString(R.string.rupee_symbol) +" "+ resultProduct.getExpCost()+" )");
        standSubTitle.setText( resultProduct.getSubTitle());
        expSubTitle.setText( resultProduct.getSubTitle1());

        //Rating
        float rat=(float)resultProduct.getRating()/20;
        ratingTxtBtm.setText("" + rat);
        ratingBar.setRating(rat);
        ratingBarBtm.setRating(rat);
//        pdtIdTxt.setText("Product Id: " + resultProduct.getId());
        imageLoader.displayImage(resultProduct.getThumbnail(), pdtImg, options,
                imageListener);
        //SHARE ACTION
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, modelItem.getTitle()+" shared ", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Market Place App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Checkout Deals "+resultProduct.getTitle()+" click here: "+resultProduct.getShare());
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
//OFFER TAG COLOR CHANGES
        tagBtn.setText(resultProduct.getTag());
        if(resultProduct.getTag().equalsIgnoreCase("best offer")){
            tagBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_blue));
        }else  if(resultProduct.getTag().equalsIgnoreCase("new")){
            tagBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_green));
        }else {
            tagBtn.setVisibility(View.INVISIBLE);
        }

        if(resultProduct.getDiscount()==0){
            discBtn.setVisibility(View.INVISIBLE);
        }else {
            discBtn.setText("Sale "+resultProduct.getDiscount()+"% Off");
            discBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_red));
        }

        //Rating Bar
//        if(resultProduct.getRating()==0){
//            ratingBar.setRating(0);
//
//        }else{
//            ratingBar.setRating((float)resultProduct.getRating()/20);
//        }


    }
    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
            //Product Details
            task = new ProductDetailsAsyncTask();
//            Log.e("singlePrdtId",""+singlePrdtId);
            task.execute(Utils.prdtDetailsUrl+singlePrdtId);
            //Product on Glance
            taskAsynk = new AsyncHttpTask();
            taskAsynk.execute(Utils.similarPrdtsUrl+singlePrdtId);
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
    //Product Details
    public class ProductDetailsAsyncTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {

            cardView.setVisibility(View.VISIBLE);
//            pb.setVisibility(View.VISIBLE);

//            progressBar.setVisibility(View.VISIBLE);
//            layout.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.e("response.toString()", response.toString());
                    productDetailsParseResult(response.toString());
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
//            activity.getFragmentManager().beginTransaction().remove(prgs).commit();
//            activity.setProgressBarIndeterminateVisibility(false);
            cardView.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);

            /* Download complete. Lets update UI */
            if (result == 1) {
                try {
                    setProductItem(item1);
//                adapter = new ProductGlanceAdapter(activity, services);
//                grid.setAdapter(adapter);
////                grid1.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                horizontalAdapter = new HorizontalListAdapter(activity, services);
//                horizontalList.setAdapter(horizontalAdapter);
////                grid1.setAdapter(adapter);
//                horizontalAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
                }catch (Exception e){

                }
            } else {
                Log.e("hello", "Failed to fetch data!");
            }
        }
    }
    private void productDetailsParseResult(String result) {
        try {
//            Log.e("MrE", "1");
            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
//                Log.e("MrE", "2");
                if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
//                    Log.e("MrE", "3");
//                    if (status) {
//                JSONObject jsonData = jsonObject
//                        .getJSONObject(TagName.TAG_PRODUCT);
//                    }
                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_PRODUCT_DETAILS);

 /*Initialize array if null*/
                    if (null == checkOutProductsArray) {
                        checkOutProductsArray = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);
                    Log.e("MrE", "4");
//                        pdtNameTxt.setText(post.optString(TagName.KEY_NAME));
//                        desTxt.setText(Html.fromHtml( post.optString(TagName.KEY_DES)));
//                        priceTxt.setText(String.valueOf(post.optDouble(TagName.KEY_PRICE)));
//                        priceTxt.setPaintFlags(priceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
//                        Log.e("MrE", ""+post.optString(TagName.KEY_NAME));
//                        finalPriceTxt.setText("" + post.optDouble(TagName.KEY_FINAL_PRICE));
////
//////        pdtIdTxt.setText("Product Id: " + resultProduct.getId());
////
//                        imageLoader.displayImage(post.optString(TagName.KEY_THUMB), pdtImg, options,
//                                imageListener);
                        item1 = new ProductModel();
                        item1.setId(post.optInt(TagName.KEY_ID));
                        item1.setTitle(post.optString(TagName.KEY_NAME));
                        item1.setDescription(post.optString(TagName.KEY_DES));
                        item1.setCost(post.optDouble(TagName.KEY_PRICE));
                        item1.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
                        item1.setCount(1);
//                    Log.e("name", "name"+ post.optDouble("finalPrice"));
                        item1.setThumbnail(post.optString(TagName.KEY_THUMB));

                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item1.setShare(post1.optString(TagName.KEY_SHARE));
                        item1.setTag(post1.optString(TagName.KEY_TAG));
                        item1.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item1.setRating(post1.optInt(TagName.KEY_RATING));

                        JSONArray deliveryArry=post.getJSONArray(TagName.TAG_DELIVERY);
                        JSONObject deliveryObjStand=deliveryArry.getJSONObject(0);
                        item1.setStandCost(deliveryObjStand.optString(TagName.KEY_COST));
                        item1.setSubTitle(deliveryObjStand.optString(TagName.KEY_SUB_TITLE));
                        JSONObject deliveryObjExp=deliveryArry.getJSONObject(1);
                        item1.setExpCost(deliveryObjExp.optString(TagName.KEY_COST));
                        item1.setSubTitle1(deliveryObjExp.optString(TagName.KEY_SUB_TITLE));
                        Log.e("KEY_SUB_TITLE", deliveryObjExp.optString(TagName.KEY_SUB_TITLE));
                        checkOutProductsArray.add(item1);

                    }
                } else {
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //Similar Products
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {

//            progressLL.setVisibility(View.VISIBLE);
//            pb.setVisibility(View.VISIBLE);

//            progressBar.setVisibility(View.VISIBLE);
//            layout.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
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

//            activity.setProgressBarIndeterminateVisibility(false);
//            progressBar.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);

            /* Download complete. Lets update UI */
            if (result == 1) {
//                Log.e("onPostExecute", "onPostExecute");
//                adapter = new ProductGlanceAdapter(activity, services);
//                grid.setAdapter(adapter);
////                grid1.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                horizontalAdapter = new HorizontalListAdapter(activity, services,R.layout.gallary_inflate);
                horizontalList.setAdapter(horizontalAdapter);
//                grid1.setAdapter(adapter);
                horizontalAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
            } else {
                Log.e("hello", "Failed to fetch data!");
            }
        }
    }
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

            /*Initialize array if null*/
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
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addtocartData(String productid, String quantity,String userid) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String pid = params[0];
                String quant = params[1];
                String userid = params[2];
                InputStream inputStream = null;
                String result= null;;
                HttpURLConnection urlConnection = null;

                try {
                /* forming th java.net.URL object */
                    URL url = new URL(Utils.addtocartUrl+pid+"/"+quant+"/"+userid);
                    Log.e("URL", Utils.addtocartUrl+pid+"/"+quant+"/"+userid);

                    urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                    urlConnection.setRequestMethod("GET");

                    int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                    if (statusCode ==  200) {

                        BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }
                        Log.e("response.toString()", response.toString());
//                        parseResult(response.toString());
                        result = response.toString(); // Successful
                    }else{
                        result = null; //"Failed to fetch data!";
                    }

                } catch (Exception e) {
                    Log.d("catch", e.getLocalizedMessage());
                }

                return result; //"Failed to fetch data!";

//                String pid = params[0];
//                String quant = params[1];
//                String userid = params[2];
////                Log.e("uname",uname);
////                Log.e("pass",pass);
//                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
////                nameValuePairs.add(new BasicNameValuePair("username", uname));
////                nameValuePairs.add(new BasicNameValuePair("password", pass));
//                String result = null;
//
//                try{
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpPost = new HttpGet(Utils.addtocartUrl+pid+"/"+quant+"/"+userid);
////                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    is = entity.getContent();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line + "\n");
//                    }
//                    result = sb.toString();
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
//                Log.e("s",s);
                loadingDialog.dismiss();
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
//                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
//                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
//                        String message = jobstatus.optString(TagName.TAG_MSG);

//                        if (status==1) {
                            if(cartBadge.equals("")){
                                ((SecondActivity) getActivity()).writeBadge(1);
                                editor.putString("cartBadge", String.valueOf(1));
                                editor.commit();
//                            writeBadge(0);
                            }else{

                                ((SecondActivity) getActivity()).writeBadge(Integer.parseInt(cartBadge)+1);
                                editor.putString("cartBadge", String.valueOf(Integer.parseInt(cartBadge)+1));
                                editor.commit();
                            }
                            Toast.makeText(activity, "Added to Cart!", Toast.LENGTH_LONG).show();
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
//                if(message.equalsIgnoreCase("success")){
//                            JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);
//                            editor = sharedpreferences.edit();
//                            editor.putString("customerID", jobcust.optString("id"));
//                            editor.putString("customerEmail", jobcust.optString("username"));
//                            editor.putString("password", jobcust.optString("password"));
////                          editor.putString("customerName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();
//                            activity.finish();
//                            Intent in=new Intent(getActivity(), BookNowActivity.class);
//                            in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
//                            activity.startActivity(in);
//                            activity.overridePendingTransition(android.R.anim.fade_in,
//                                    android.R.anim.fade_out);
//                        }else {
//                            Toast.makeText(activity, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(productid, quantity, userid);

    }

    private void getQuoteId(String userid,String productid) {

        class QuoteAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String userid = params[0];
                String pid = params[1];
//                String price = params[2];
                InputStream inputStream = null;
                String result= null;;
                HttpURLConnection urlConnection = null;

                try {
                /* forming th java.net.URL object */
                    URL url = new URL(Utils.quoteBuynowUrl+userid+"/"+pid);
                    Log.e("URL", Utils.quoteBuynowUrl+userid+"/"+pid);

                    urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                    urlConnection.setRequestMethod("GET");

                    int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                    if (statusCode ==  200) {

                        BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }
                        Log.e("response.toString()", response.toString());
//                        parseResult(response.toString());
                        result = response.toString(); // Successful
                    }else{
                        result = null; //"Failed to fetch data!";
                    }

                } catch (Exception e) {
                    Log.d("catch", e.getLocalizedMessage());
                }

                return result; //"Failed to fetch data!";

//                String pid = params[0];
//                String quant = params[1];
//                String userid = params[2];
////                Log.e("uname",uname);
////                Log.e("pass",pass);
//                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
////                nameValuePairs.add(new BasicNameValuePair("username", uname));
////                nameValuePairs.add(new BasicNameValuePair("password", pass));
//                String result = null;
//
//                try{
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpPost = new HttpGet(Utils.addtocartUrl+pid+"/"+quant+"/"+userid);
////                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    is = entity.getContent();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line + "\n");
//                    }
//                    result = sb.toString();
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
//                Log.e("s",s);
                loadingDialog.dismiss();
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {

                           String quoteId=jsonObject.optString("quoteid");
                            if(cartBadge.equals("")){
                                ((SecondActivity) getActivity()).writeBadge(1);
                                editor.putString("cartBadge", String.valueOf(1));
                                editor.commit();
//                            writeBadge(0);
                            }else{

                                ((SecondActivity) getActivity()).writeBadge(Integer.parseInt(cartBadge)+1);
                                editor.putString("cartBadge", String.valueOf(Integer.parseInt(cartBadge)+1));
                                editor.commit();
                            }
                            bookNow();
//                            Toast.makeText(activity, "Added "+quoteId, Toast.LENGTH_LONG).show();
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
//                if(message.equalsIgnoreCase("success")){
                            editor.putString("quoteId",quoteId);
                            editor.commit();
//                            JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);
//                            editor = sharedpreferences.edit();
//                            editor.putString("customerID", jobcust.optString("id"));
//                            editor.putString("customerEmail", jobcust.optString("username"));
//                            editor.putString("password", jobcust.optString("password"));
////                          editor.putString("customerName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();
//                            activity.finish();
//                            Intent in=new Intent(getActivity(), BookNowActivity.class);
//                            in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
//                            activity.startActivity(in);
//                            activity.overridePendingTransition(android.R.anim.fade_in,
//                                    android.R.anim.fade_out);
                        }else {
                            Toast.makeText(activity, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        QuoteAsync la = new QuoteAsync();
        la.execute(userid, productid);

    }
    public void addWishlistItem(String prdtId,String userId){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            String URL = "http://...";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("reference_id", "1");
//            jsonBody.put("service_id", "1");
//            jsonBody.put("client_id", userId);
//            jsonBody.put("service_type", "1");
//            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.addwishlistUrl+userId+"/"+prdtId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if(jsonObject!=null) {
                            String status=jsonObject.optString(TagName.TAG_STATUS);
                            if(status.equalsIgnoreCase("success")){
//                                String status=jsonObject.optString(TagName.KEY_MSG);
                                Toast.makeText(activity,jsonObject.optString(TagName.TAG_MSG),Toast.LENGTH_SHORT).show();
//                                activity.finish();
                            }
//                            JSONObject job=jsonObject.optJSONObject(TagName.TAG_DATA);
//                            editor = sharedpreferences.edit();
//                            editor.putString("userId", job.optString("user_id"));
//                            editor.putString("userName", job.optString("user_name"));
////                            editor.putString("password", jsonObject.optString("password"));
////                            editor.putString("userName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(activity,"Following detais are incorrect",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

             /*   @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }*/

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response);
//                        Log.e(" response.data", response);

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* private void getQuoteId(final String username, final String password) {

        class QuoteAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];
                Log.e("uname",uname);
                Log.e("pass",pass);
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(Utils.quoteBuynowUrl+uname+"/"+pass);
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                Log.e("s",s);
//                loadingDialog.dismiss();
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

//                      if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
                        if(message.equalsIgnoreCase("success")){

                            JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);
//                            addCart(jobcust.optString("id"));
                            editor = sharedpreferences.edit();
                            editor.putString("customerID", jobcust.optString("id"));
                            editor.putString("customerEmail", jobcust.optString("username"));
                            editor.putString("password", jobcust.optString("password"));
                            editor.putString("customerName",jobcust.optString("name"));
                            editor.putString("logged", "logged");
                            editor.commit();
                            activity.finish();
//                    Intent in=new Intent(getActivity(), SecondActivity.class);
//                    in.putExtra("key", TagName.CART_ID);
//                    activity.startActivity(in);
//                    activity.overridePendingTransition(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
                        }else {
                            Toast.makeText(activity, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        QuoteAsync qa = new QuoteAsync();
        qa.execute(username, password);

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
