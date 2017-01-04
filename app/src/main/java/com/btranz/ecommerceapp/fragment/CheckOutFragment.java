package com.btranz.ecommerceapp.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.OrdersRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.ProductGridAdapter;
import com.btranz.ecommerceapp.adapter.SummaryRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class CheckOutFragment extends Fragment {
    AlertDialog dialog;
    Button backBtn;
    LinearLayout discountLL;
    TextView   typeTxt, addressTxt, nameTxt,emailTxt, mobileTxt,typeED, addressED, nameED, emailED, mobileED, summaryView, modeTXT, amtTXT, discountTXT, shippingTXT, totalAmtTXT;
//    Button buyNowBtn;
    LinearLayout buyNowBtn;
    String deliveryType, userId, customerName, customerEmail, customerContact, customerStreet, customerCity, customerCountry,customerCountryCode, customerPincode, quoteId, paymentMode;
    double subtot;
    double subwithdisc;
    double shippingCharge;
    double grandtotal;
    FragmentActivity activity;
    ArrayList<ProductModel> prdtSummeryList;
    int tempCount, count1;
    double  tempAmt, amt1;
    private RecyclerView recyclerView;
    private SummaryRecyclerAdapter adapter;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    AsyncHttpTask task;
    public CheckOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        userId = sharedpreferences.getString("userID", "");
        customerName = sharedpreferences.getString("checkoutCustomerName", "");
        customerEmail = sharedpreferences.getString("checkoutCustomerEmail", "");
        customerContact = sharedpreferences.getString("checkoutCustomerContact", "");
        customerStreet = sharedpreferences.getString("checkoutCustomerStreet", "");
        customerCity = sharedpreferences.getString("checkoutCustomerCity", "");
        customerCountry=sharedpreferences.getString("checkoutCustomerCountry", "");
        customerCountryCode=sharedpreferences.getString("checkoutCustomerCountryCode", "");
        customerPincode = sharedpreferences.getString("checkoutCustomerPincode", "");
        deliveryType = sharedpreferences.getString("checkoutCustomerDeliveryType", "");
        paymentMode = sharedpreferences.getString("checkoutCustomerPaymentMode", "");
        quoteId = sharedpreferences.getString("quoteId", "");
        sendRequest();
               prdtSummeryList = activity.getIntent().getParcelableArrayListExtra("CheckOutProductList");
//        Log.e("checkOutProductsArray",""+prdtSummeryList);
        for (int i = 0; i < prdtSummeryList.size(); i++) {
            ProductModel item = prdtSummeryList.get(i);
            count1 = tempCount + item.getCount();
             amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
            Log.e("onPostExecute", " " + item.getFinalPrice());
            //cart badge
//            ((SecondActivity) getActivity()).writeBadge(services.size());
//            editor.putString("cartBadge", String.valueOf(services.size()));
//            editor.commit();
//            coutTxt.setText(String.valueOf(count1));
//            amtTxt.setText(String.valueOf(amt1));
            tempCount = count1;
            tempAmt = amt1;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_checkout, container, false);
        typeTxt=(TextView) rootView.findViewById(R.id.type_txt);
        addressTxt=(TextView) rootView.findViewById(R.id.address_txt);
        nameTxt=(TextView) rootView.findViewById(R.id.name_txt);
        emailTxt=(TextView) rootView.findViewById(R.id.email_txt);
        mobileTxt=(TextView) rootView.findViewById(R.id.contact_txt);
        typeED=(TextView) rootView.findViewById(R.id.type_ed);
        addressED=(TextView) rootView.findViewById(R.id.address_ed);
        nameED=(TextView) rootView.findViewById(R.id.name_ed);
        emailED=(TextView) rootView.findViewById(R.id.email_ed);
        mobileED=(TextView) rootView.findViewById(R.id.mobile_ed);
        summaryView=(TextView) rootView.findViewById(R.id.summery_vw);
        modeTXT=(TextView) rootView.findViewById(R.id.payment_mode_txt);
        amtTXT=(TextView) rootView.findViewById(R.id.due_amt_txt);
        discountTXT=(TextView) rootView.findViewById(R.id.discount_txt);
        shippingTXT=(TextView) rootView.findViewById(R.id.shipping_amt_txt);
        totalAmtTXT=(TextView) rootView.findViewById(R.id.total_amt_txt);
        discountLL=(LinearLayout) rootView.findViewById(R.id.discount_ll);
        backBtn=(Button)rootView.findViewById(R.id.back_btn);
        buyNowBtn=(LinearLayout) rootView.findViewById(R.id.buynow_btn);

        typeTxt.setText(deliveryType);
        addressTxt.setText(customerStreet+"\n"+customerCity+"\n"+customerCountry+"\n"+customerPincode);
        nameTxt.setText(customerName);
        emailTxt.setText(customerEmail);
        mobileTxt.setText(customerContact);
        modeTXT.setText(paymentMode);
//        amtTXT.setText(String.valueOf(amt1));

        typeED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(1);
//                Toast.makeText(getActivity(),"Type Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        addressED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(2);
                Toast.makeText(getActivity(),"Address Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        nameED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(3);
                Toast.makeText(getActivity(),"Address Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        emailED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(4);
                Toast.makeText(getActivity(),"Address Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        mobileED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(5);
                Toast.makeText(getActivity(),"mobile Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        summaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prdtSummary();
//                Toast.makeText(getActivity(),"summaryView Selected",Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutProducts();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
    public void editDialog(final int id){
        final View dialogView = View.inflate(getActivity(), R.layout.edit_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        final EditText ed = (EditText) dialogView.findViewById(R.id.edit);
        final ImageView close = (ImageView) dialogView.findViewById(R.id.close_edit);
        final Button submit = (Button) dialogView.findViewById(R.id.submit);

        // Spinner element
        Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                editor.putString("checkoutCustomerDeliveryType", item);
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

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if(id==1){
            spinner.setVisibility(View.VISIBLE);
            ed.setVisibility(View.GONE);
        }else if(id==2){
            spinner.setVisibility(View.GONE);
            ed.setVisibility(View.VISIBLE);
            ed.setText(addressTxt.getText().toString());
        }else if(id==3){
            spinner.setVisibility(View.GONE);
            ed.setVisibility(View.VISIBLE);
            ed.setText(nameTxt.getText().toString());
        }else if(id==4){
            spinner.setVisibility(View.GONE);
            ed.setVisibility(View.VISIBLE);
            ed.setText(emailTxt.getText().toString());
        }else if(id==5){
            spinner.setVisibility(View.GONE);
            ed.setVisibility(View.VISIBLE);
            ed.setText(mobileTxt.getText().toString());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateStr=ed.getText().toString();
                if(id==1){
                    deliveryType = sharedpreferences.getString("checkoutCustomerDeliveryType", "");
//                    ed.setInputType(InputType.TYPE_CLASS_TEXT);
                    typeTxt.setText(deliveryType);
                    dialog.dismiss();
                }else if(id==2){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    if(updateStr.equals("")){
                        Toast.makeText(activity,"address required",Toast.LENGTH_SHORT).show();
                    }else{
                        addressTxt.setText(updateStr);
                        dialog.dismiss();
                    }
                }else if(id==3){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//                    nameTxt.setText(ed.getText().toString());
                    if(updateStr.equals("")){
                        Toast.makeText(activity,"Name required",Toast.LENGTH_SHORT).show();
                    }else{
                        nameTxt.setText(updateStr);
                        dialog.dismiss();
                    }
                }else if(id==4){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//                    emailTxt.setText(ed.getText().toString());
                    if(updateStr.equals("")){
                        Toast.makeText(activity,"Email required",Toast.LENGTH_SHORT).show();
                    }else if (!updateStr.matches(Utils.EMAIL_PATTERN)){
                        Toast.makeText(activity,"Correct Email required",Toast.LENGTH_SHORT).show();
                    } else{
                        emailTxt.setText(updateStr);
                        dialog.dismiss();
                    }
                }else if(id==5){
                    ed.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    mobileTxt.setText(ed.getText().toString());
                    if(updateStr.equals("")){
                        Toast.makeText(activity,"Number required",Toast.LENGTH_SHORT).show();
                    } if(updateStr.length()!=10){
                        Toast.makeText(activity,"10 Digit Number required",Toast.LENGTH_SHORT).show();
                    }else{
                        mobileTxt.setText(updateStr);
                        dialog.dismiss();
                    }

                }

            }
        });


//        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //do something with edt.getText().toString();
////                submitForgotForm();
//                if(id==1){
//                    ed.setInputType(InputType.TYPE_CLASS_TEXT);
//                    typeTxt.setText(ed.getText().toString());
//                }else if(id==2){
//                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                    addressTxt.setText(ed.getText().toString());
//                }else if(id==3){
//                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//                    nameTxt.setText(ed.getText().toString());
//                }else if(id==4){
//                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//                    emailTxt.setText(ed.getText().toString());
//                }else if(id==5){
//                    ed.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    mobileTxt.setText(ed.getText().toString());
//
//                }
//                dialog.dismiss();
//            }
//        });
        dialog = builder.create();
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                revealShow(dialogView, true, null);
//            }
//        });
//        TextView summaryQnty=(TextView)dialogView.findViewById(R.id.summary_qnty);
//        TextView summaryAmt=(TextView)dialogView.findViewById(R.id.summary_amt);
//        summaryQnty.setText(String.valueOf(count1));
//        summaryAmt.setText(String.valueOf(amt1));
//        dialogView.findViewById(R.id.close_edit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                revealShow(dialogView, false, dialog);
//            }
//        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_right;
        dialog.show();

    }
    public void prdtSummary(){
        final View dialogView = View.inflate(getActivity(), R.layout.prdts_summary_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        recyclerView = (RecyclerView) dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new SummaryRecyclerAdapter(activity, prdtSummeryList, R.layout.product_summary_inflate);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    revealShow(dialogView, true, null);
                }

            }
        });
        TextView summaryQnty=(TextView)dialogView.findViewById(R.id.summary_qnty);
        TextView summaryAmt=(TextView)dialogView.findViewById(R.id.summary_amt);
        summaryQnty.setText(String.valueOf(count1));
        summaryAmt.setText(String.valueOf(amt1));
        dialogView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    revealShow(dialogView, false, dialog);
                }else{
                    dialog.dismiss();
                }
//                revealShow(dialogView, false, dialog);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealShow(View rootView, boolean reveal, final AlertDialog dialog) {
        final View view = rootView;//.findViewById(R.id.reveal_view);
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if(reveal){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                    w / 2, h / 2, 0, maxRadius);
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
    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
            task = new AsyncHttpTask();
//            task.execute(Utils.productsUrl);
            task.execute(Utils.instantCheckoutPaymentInfoUrl);
//            task.execute(Utils.instantServerUrl);
            Log.e("sendrequest","sendrequest");
        } else {
          String  message = getResources().getString(R.string.no_internet_connection);
            showAlertDialog(message, true);
        }
    }
    public void showAlertDialog(String message, final boolean finish) {
        AlertDialog   alertDialog = new AlertDialog.Builder(activity).create();
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
//    private void checkoutProducts(final String userid, final String customerEmail, final String customerName, final String customerStreet,final String customerCity,final String customerCountry, final String customerPincode) {
public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
    private Dialog loadingDialog;
    @Override
    protected void onPreExecute() {
        loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
//            setProgressBarIndeterminateVisibility(true);
        try {
//            progressLL.setVisibility(View.VISIBLE);
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
            URL url = new URL(params[0]+userId+"&quoteId="+quoteId);

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
        loadingDialog.dismiss();
//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//             Download complete. Lets update UI
        if (result == 1) {
            Log.e("onPostExecute", "onPostExecute");
//            if(services!=null&&services.size()!=0) {
//                adapter = new ProductGridAdapter(activity, services);
//                ProductsGrid.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                progressLL.setVisibility(View.GONE);
////                pb.setVisibility(View.GONE);
            amtTXT.setText(String.valueOf(subtot));
            discountTXT.setText(String.valueOf(subwithdisc));
            shippingTXT.setText(String.valueOf(shippingCharge));
            totalAmtTXT.setText(String.valueOf(grandtotal));
            if(subwithdisc!=0){
               discountLL.setVisibility(View.VISIBLE);
            }else{
                discountLL.setVisibility(View.GONE);
            }
////                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//            }else {
//                message = getResources().getString(R.string.no_products);
//                showAlertDialog(message, true);
//            }
        } else {
            Log.e("hello", "Failed to fetch data!");
//            pb.setVisibility(View.GONE);
////                message = getResources().getString(R.string.no_products);
//            showAlertDialog(message, true);
//                Toast.makeText(activity,"No Prodructs Found",Toast.LENGTH_SHORT).show();
        }
    }
}
    /*private void parseResult(String result) {
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
//                    if (null == services) {
//                        services = new ArrayList<ProductModel>();
//                    }

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
//                        services.add(item);
                    }
                } else {
//                    message = jsonObject.optString(TagName.TAG_MSG);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    private void parseResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
//            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
               String message = jobstatus.optString(TagName.TAG_MSG);
                if (status==1) {
                    JSONArray jarr=jsonObject.optJSONArray("customcheckoutcarttotal");
                    JSONObject job=jarr.optJSONObject(0);
                    JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                    int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                    String message1 = jobstat.optString(TagName.TAG_MSG);
                    if(status1==1) {
                JSONObject jsonData = job
                        .getJSONObject(TagName.TAG_CART);
                        subtot=jsonData.optDouble("subtotal");
                        subwithdisc=jsonData.optDouble("subtotal_with_discount");
                        shippingCharge=jsonData.optDouble("shipping_charge");
                        grandtotal=jsonData.optDouble("grandtotal");
                        Log.e("subtot", ""+subtot);

//                    }
//                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_CART);

//            Initialize array if null
//                    if (null == services) {
//                        services = new ArrayList<ProductModel>();
//                    }

//                    for (int i = 0; i < posts.length(); i++) {
//                        JSONObject post = posts.optJSONObject(i);
//
//                        ProductModel item = new ProductModel();
//                        item.setId(post.optInt(TagName.KEY_ID));
//                        item.setTitle(post.optString(TagName.KEY_NAME));
//                        item.setDescription(post.optString(TagName.KEY_DES));
//                        item.setCost(post.optDouble(TagName.KEY_PRICE));
//                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
////                    item.setCount(post.optInt("finalPrice"));
////                    Log.e("name", "name");
//                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
//                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
//                        item.setShare(post1.optString(TagName.KEY_SHARE));
//                        item.setTag(post1.optString(TagName.KEY_TAG));
//                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
//                        item.setRating(post1.optInt(TagName.KEY_RATING));
////                        services.add(item);
//                    }
                    }else{
                        Toast.makeText(activity, "Pament info Not showed", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(activity, "Network Error.  Please try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void checkoutProducts() {

        class CheckoutAsync extends AsyncTask<String, Void, String> {

            Dialog loadingDialog;
            String shippingRate;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
              loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
//                String uname = params[0];
//                String pass = params[1];
//                Log.e("uname",uname);
//                Log.e("pass",pass);
                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;
                if(deliveryType.equalsIgnoreCase("standard delivery")){
                    shippingRate="freeshipping_freeshipping";
                }else{
                    shippingRate="flatrate_flatrate";
                }

                try{
//                    String restUrl = URLEncoder.encode(Utils.checkoutUrl+userId+"/"+customerEmail+"/"+customerName+"/"+customerStreet+"/"+customerCity+"/"+customerCountry+"/"+customerPincode+"/"+customerContact+"/"+shippingRate+"/"+paymentMode+"/"+quoteId, "UTF-8");
                    String restUrl = Utils.checkoutUrl+userId+"/"+customerEmail+"/"+customerName+"/"+customerStreet+"/"+customerCity+"/"+customerCountry+"/"+customerPincode+"/"+customerContact+"/"+shippingRate+"/"+paymentMode+"/"+quoteId;
//                    String restUrl = Utils.instantCheckoutUrl+userId+"&email="+customerEmail+"&name="+customerName+"&street="+customerStreet+"&city="+customerCity+"&country_id="+customerCountryCode+"&postcode="+customerPincode+"&telephone="+customerContact+"&shipping="+shippingRate+"&payment="+paymentMode+"&quoteId="+quoteId ;
                    Log.e("restUrl",restUrl.replace(" ","%20"));
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(restUrl.replace(" ","%20"));
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
                loadingDialog.dismiss();
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
                            Toast.makeText(activity, "Order Placed", Toast.LENGTH_LONG).show();
                            activity.finish();
//                            JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);

//                            editor = sharedpreferences.edit();
//                            editor.putString("userID", jobcust.optString("id"));
//                            editor.putString("userEmail", jobcust.optString("username"));
//                            editor.putString("password", jobcust.optString("password"));
//                            editor.putString("userName",jobcust.optString("name"));
//                            editor.putString("logged", "logged");
//                            editor.commit();
//                            activity.finish();
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

        CheckoutAsync la = new CheckoutAsync();
        la.execute();

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
