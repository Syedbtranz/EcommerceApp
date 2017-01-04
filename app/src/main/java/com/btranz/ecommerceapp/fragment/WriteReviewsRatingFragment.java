package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.ReviewRatingRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;

import org.apache.http.client.ClientProtocolException;
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


/**
 * Created by Ravi on 29/07/15.
 */
public class WriteReviewsRatingFragment extends Fragment {
    public static final String WRR_FRAG = "write_review_fragment";
    ProductModel reviewProduct;
    RatingBar priceRating, valueRating, qualityRating;
    EditText nameET, summaryET, reviewET;
    Button submitReviewBtn;
    float price, value, quality;
    String nickname, summary, review;
    ArrayList<ProductModel> reviewArray;
    RecyclerView reviewList;
    FragmentActivity activity;
    ReviewRatingRecyclerAdapter reviewRateAdapter;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public  String userName,userEmail,userId,msg,productId,productName;
    public WriteReviewsRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        setHasOptionsMenu(true);
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        userId = sharedpreferences.getString("userID", "");
        reviewProduct=activity.getIntent().getParcelableExtra("reviewProduct");
        productId=String.valueOf(reviewProduct.getId());
        productName=reviewProduct.getTitle();
        Log.e("productId",productId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.write_review_rating, container, false);
        findViewById(rootView);

//        Bundle bundle = this.getArguments();
//        Bundle b = activity.getIntent().getExtras();
//        if (b != null) {
//            product = b.getParcelable("singleProduct");
//            setProductItem(product);
//        }else
//        if (bundle != null) {
////            product = bundle.getParcelable("singleProduct");
//            reviewArray = bundle.getParcelableArrayList("reviews");
////            prdtsTitle = bundle.getString("prdtsTitle");
//            sendRequest();
////            setProductItem(product);
//            Log.e("bundle", "product");
//
//
//        }
        // Inflate the layout for this fragment
        return rootView;
    }

    private void findViewById(View rootView) {
        final Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        final TextView toolTitle = (TextView) mToolbar.findViewById(R.id.toolbar_txt);
        ((AppCompatActivity) activity).setSupportActionBar(mToolbar);
        SpannableString s = new SpannableString(getString(R.string.title_write_review)+" for "+productName);
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
//        reviewList = (RecyclerView)rootView.findViewById(R.id.review_rate_recycler);
//        reviewList.setLayoutManager(new LinearLayoutManager(activity));
        priceRating=(RatingBar)rootView.findViewById(R.id.rating_price);
        valueRating=(RatingBar)rootView.findViewById(R.id.rating_value);
        qualityRating=(RatingBar)rootView.findViewById(R.id.rating_quality);

        nameET=(EditText) rootView.findViewById(R.id.nickname_et);
        summaryET=(EditText) rootView.findViewById(R.id.summary_et);
        reviewET=(EditText) rootView.findViewById(R.id.review_et);

        submitReviewBtn=(Button) rootView.findViewById(R.id.submit_review_btn);
        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }
    private void sendRequest() {
        price=priceRating.getRating();
        value=valueRating.getRating();
        quality=qualityRating.getRating();
        nickname=nameET.getText().toString();
        summary=summaryET.getText().toString();
        review=reviewET.getText().toString();

        if(price==0){
            Toast.makeText(activity,"Please select the Price Rating",Toast.LENGTH_SHORT).show();
        }else if(value==0){
            Toast.makeText(activity,"Please select the Value Rating",Toast.LENGTH_SHORT).show();
        }else if(quality==0){
            Toast.makeText(activity,"Please select the Quality Rating",Toast.LENGTH_SHORT).show();
        }else if(nickname.equals("")){
            Toast.makeText(activity,"Please enter the nickName",Toast.LENGTH_SHORT).show();
            nameET.requestFocus();
        }else if(summary.equals("")){
            Toast.makeText(activity,"Please enter the summary",Toast.LENGTH_SHORT).show();
            summaryET.requestFocus();
        }else if(review.equals("")){
            Toast.makeText(activity,"Please enter the Review",Toast.LENGTH_SHORT).show();
            reviewET.requestFocus();
        }else{
            sendReviewData();
//            Toast.makeText(activity,"success"+price,Toast.LENGTH_SHORT).show();
        }

    }
    public void sendReviewData(){
        class ReviewAsync extends AsyncTask<String, Void, String> {
            Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
//                String oldpsw = params[0];
//                String newpsw = params[1];
//                Log.e("uname",oldpsw);
//                Log.e("pass",newpsw);
                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;
                HttpURLConnection urlConnection = null;
                try{
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpPost = new HttpGet( Utils.instantChangePswUrl+userId+"&oldpassword="+oldpsw+"&newpassword="+newpsw);
////                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    is = entity.getContent();

                    URL url = new URL(params[0]);

                    urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                    urlConnection.setRequestMethod("GET");

                    int statusCode = urlConnection.getResponseCode();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"), 8);
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
                String response = result.trim();
                Log.e("s",response);
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
//                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
                            JSONArray jarr=jsonObject.optJSONArray(TagName.TAG_PRODUCT);
                            JSONObject job=jarr.optJSONObject(0);
                            JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                            String message1 = jobstat.optString(TagName.TAG_MSG);
                            if(status1==1) {
                            Toast.makeText(activity,"Review Posted Successfully",Toast.LENGTH_SHORT).show();
                            activity.onBackPressed();
                            }else{
                                Toast.makeText(activity, "Review Not Posted, Please try Again.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "Review Not Posted, Please try Again.", Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ReviewAsync la = new ReviewAsync();
        la.execute(Utils.instantPostReviewUrl+productId+"&userid="+userId+"&nicname="+nickname+"&summary="+summary+"&review="+review+"&quality="+quality+"&value="+value+"&price="+price);

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
