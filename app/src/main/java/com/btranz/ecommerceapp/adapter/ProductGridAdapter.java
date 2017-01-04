package com.btranz.ecommerceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.btranz.ecommerceapp.modal.Post;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by all on 9/2/2016.
 */
public class ProductGridAdapter extends BaseAdapter {


    ArrayList<ProductModel> modelList;
    ArrayList<ProductModel> arraylist;
    int Resource;
    private LayoutInflater inflater;

    FragmentActivity context;
    private DisplayImageOptions options;
    List<String> wishList= new ArrayList<String>();
    DatabaseHandler dbHandler;
    String userId,userEmail, cartBadge;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public ProductGridAdapter(FragmentActivity context, ArrayList<ProductModel> objects) {
//        Resource = resource;
        modelList = objects;
        this.context=context;
        arraylist = new ArrayList<ProductModel>();
        arraylist.addAll(modelList);
        inflater = LayoutInflater.from(context);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.preloader_product)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5,5))
                .build();
        dbHandler=new DatabaseHandler(context);
        sharedpreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();
//        customerName = sharedpreferences.getString("customerName", "");
        userId = sharedpreferences.getString("userID", "");
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.grid_inflate, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.grid_text);
//            holder.desc = (TextView) view.findViewById(R.id.num_text);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.finalPrice = (TextView) view.findViewById(R.id.final_price);
            holder.image = (ImageView) view.findViewById(R.id.grid_image);
            holder.share = (ImageView) view.findViewById(R.id.prdt_share);
//            holder.like = (ImageView) view.findViewById(R.id.prdt_like);
            holder.likeBtn = (CheckBox) view.findViewById(R.id.prdt_like_btn);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            holder.offerTag = (Button) view.findViewById(R.id.offer_tag_btn);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(modelList.get(position).getTitle());
        holder.price.setText(""+modelList.get(position).getCost());
        holder.price.setPaintFlags(holder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.finalPrice.setText(""+modelList.get(position).getFinalPrice());
//        holder.desc.setText(modelList.get(position).getDescription());
        holder.offerTag.setText(modelList.get(position).getTag());
        if(modelList.get(position).getDiscount()!=0){
            holder.offerTag.setText("Sale "+modelList.get(position).getDiscount()+"% Off");
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(context, R.color.color_red));
        }else  if(modelList.get(position).getTag().equalsIgnoreCase("best offer")){
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue));
        }else if(modelList.get(position).getTag().equalsIgnoreCase("new")){
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(context, R.color.color_green));
        }else {
            holder.offerTag.setVisibility(View.INVISIBLE);
        }
        //RAtingBar
        if(modelList.get(position).getRating()==0){
            holder.ratingBar.setRating(0);

        }else{
            holder.ratingBar.setRating((float)modelList.get(position).getRating()/20);
        }
        //SHARE ACTION
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, modelItem.getTitle()+" shared ", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Market Place App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Checkout Deals "+modelList.get(position).getTitle()+" click here: "+modelList.get(position).getShare());
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        //image color change when change the theme colors
      /*  TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
        holder.like.setColorFilter(color);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,modelList.get(position).getTitle()+" liked ", Toast.LENGTH_SHORT).show();
                if(flag){
//
                    ((ImageView)v).setImageResource(R.drawable.like_icon_filled);
                    flag=false;
                }else{

                    ((ImageView)v).setImageResource(R.drawable.like_icon);
                    flag=true;
                }
            }
        });*/
        if(modelList.get(position).getWishlist()==0){
            holder.likeBtn.setChecked(false);

        }else{
            holder.likeBtn.setChecked(true);
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
        holder.likeBtn.setHighlightColor(color);
        //Like Action
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelList.get(position).setId(modelList.get(position).getId());
                modelList.get(position).setTitle(modelList.get(position).getTitle());
                modelList.get(position).setDescription(modelList.get(position).getDescription());
                modelList.get(position).setCost(modelList.get(position).getCost());
                modelList.get(position).setFinalPrice(modelList.get(position).getFinalPrice());
                modelList.get(position).setThumbnail(modelList.get(position).getThumbnail());
                modelList.get(position).setShare(modelList.get(position).getShare());
                modelList.get(position).setTag(modelList.get(position).getTag());
                modelList.get(position).setDiscount(modelList.get(position).getDiscount());
                modelList.get(position).setRating(modelList.get(position).getRating());
                if (((CheckBox)v).isChecked()) {


//                    output.setText("checked");
                    Toast.makeText(context, modelList.get(position).getTitle()+" liked ", Toast.LENGTH_SHORT).show();
                    if(userId.equals("")){
                        wishList=dbHandler.checkWishlistProduct(String.valueOf(modelList.get(position).getId()));
                        if(wishList.size()<1){
                            Toast.makeText(context, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                            dbHandler.insertWishlist(String.valueOf(modelList.get(position).getId()),modelList.get(position).getTitle(),String.valueOf(modelList.get(position).getCost()),String.valueOf(modelList.get(position).getFinalPrice()),modelList.get(position).getThumbnail(),modelList.get(position).getTag(),String.valueOf(modelList.get(position).getDiscount()),String.valueOf(modelList.get(position).getRating()));
                            modelList.get(position).setWishlist(1);
                            modelList.remove(position);
                            modelList.add(position, modelList.get(position));
                        }else{
                            Toast.makeText(context, "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        addWishlistItem(String.valueOf(modelList.get(position).getId()),userId);
                        modelList.get(position).setWishlist(1);
                        modelList.remove(position);
                        modelList.add(position, modelList.get(position));
                    }
//                    addtoWishlist(modelItem.getId());
                } else {
                    reomvewishlistItem(modelList.get(position).getId());
//                    output.setText("unchecked");
//                    modelItem.setId(modelItem.getId());
//                    modelItem.setTitle(modelItem.getTitle());
//                    modelItem.setDescription(modelItem.getDescription());
//                    modelItem.setCost(modelItem.getCost());
//                    modelItem.setThumbnail(modelItem.getThumbnail());
                    modelList.get(position).setWishlist(0);
                    modelList.remove(position);
                    modelList.add(position, modelList.get(position));

                    Toast.makeText(context, modelList.get(position).getTitle()+" dis liked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageLoader.getInstance().displayImage(modelList.get(position).getThumbnail(), holder.image, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setProgress(0);
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                holder.progressBar.setProgress(Math.round(100.0f * current / total));
            }
        });

        return view;
    }
    public class ViewHolder {
        TextView text;
        Button offerTag;
        RatingBar ratingBar;
        TextView desc,price, finalPrice;
        ImageView image,share,like;
        CheckBox likeBtn;
        ProgressBar progressBar;
    }
    public void filter(int lowPrice, int highPrice) {

//        charText = charText.toLowerCase(Locale.getDefault());
//        lowPrice = lowPrice.toLowerCase(Locale.getDefault());
//        highPrice = highPrice.toLowerCase(Locale.getDefault());

        modelList.clear();
        if (lowPrice == 0&&highPrice == 0) {
            modelList.addAll(arraylist);

        } else {
            for (ProductModel postDetail : arraylist) {
                if (lowPrice != 0&&highPrice != 0 && Double.valueOf(lowPrice)<=postDetail.getFinalPrice()&&postDetail.getFinalPrice()<=Double.valueOf(highPrice)) {
                    modelList.add(postDetail);
                }

//                else if (charText.length() != 0 && postDetail.getPostSubTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    modelList.add(postDetail);
//                }
            }
        }
        notifyDataSetChanged();
    }
//    public void filter(int lowPrice, int highPrice) {
//
////        charText = charText.toLowerCase(Locale.getDefault());
////        lowPrice = lowPrice.toLowerCase(Locale.getDefault());
////        highPrice = highPrice.toLowerCase(Locale.getDefault());
//
//        modelList.clear();
//        if (lowPrice == 0&&highPrice == 0) {
//            modelList.addAll(arraylist);
//
//        } else {
//            for (ProductModel postDetail : arraylist) {
//                if (lowPrice != 0&&highPrice != 0 && Double.valueOf(lowPrice)<=postDetail.getFinalPrice()&&postDetail.getFinalPrice()<=Double.valueOf(highPrice)) {
//                    modelList.add(postDetail);
//                }
//
////                else if (charText.length() != 0 && postDetail.getPostSubTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
////                    modelList.add(postDetail);
////                }
//            }
//        }
//        notifyDataSetChanged();
//    }
    public void addWishlistItem(String prdtId,String userId){
        class Async extends AsyncTask<String, Void, String> {

//            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
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
                    URL url = new URL(Utils.instantAddtoWishlistUrl+userid+"&productid="+pid);
//                    Log.e("URL", Utils.instantAddWishlistUrl+userid+"&productid="+pid);

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

            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
//                Log.e("s",s);
//                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
//                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {

                            JSONArray jarr=jsonObject.optJSONArray("addwishlist");
                            JSONObject job=jarr.optJSONObject(0);
                            JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                            String message1 = jobstat.optString(TagName.TAG_MSG);
                            if(status1==1) {
//                                Toast.makeText(context, "Wishlist Product Added", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(context, "Wishlist Product Not Added", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "Net work Error", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Async la = new Async();
        la.execute(userId, prdtId);

      /*  try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                Toast.makeText(context,jsonObject.optString(TagName.TAG_MSG),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context,"Following detais are incorrect",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

             *//*   @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }*//*

                *//*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response);
//                        Log.e(" response.data", response);

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*//*
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
        class Async extends AsyncTask<String, Void, String> {

//            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loadingDialog = ProgressDialog.show(activity, "", "Pease Wait...");
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
                    URL url = new URL(Utils.instantDeleteWishlistItemUrl+userid+"&productid="+pid);
//                    Log.e("URL", Utils.quoteBuynowUrl+userid+"/"+pid);

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

            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
//                Log.e("s",s);
//                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
//                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {

                            JSONArray jarr=jsonObject.optJSONArray("itemremovewishlist");
                            JSONObject job=jarr.optJSONObject(0);
                            JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
                            int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                            String message1 = jobstat.optString(TagName.TAG_MSG);
                            if(status1==1) {
//                                Toast.makeText(context, "Wishlist Product deleted", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(context, "Wishlist Product Not deleted", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "Net work Error", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Async la = new Async();
        la.execute(userId, prdtId);


        /*try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                Toast.makeText(context,jsonObject.optString(TagName.TAG_MSG),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context,"Following detais are incorrect",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

             *//*   @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }*//*

                *//*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response);
//                        Log.e(" response.data", response);

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*//*
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}

