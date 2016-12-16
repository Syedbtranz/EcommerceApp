package com.btranz.ecommerceapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ViewHolder> {

    ImageLoader imageLoader = ImageLoader.getInstance();
    private FragmentActivity activity;
    ArrayList<ProductModel> modelList;
    ArrayList<ProductModel> wishlistServices;
    private DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    int layout;
    List<String> wishList= new ArrayList<String>();
    DatabaseHandler dbHandler;
    String userId,userEmail, cartBadge;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public HorizontalListAdapter(FragmentActivity activity, ArrayList<ProductModel> objects,int layout,ArrayList<ProductModel> wishlistServices) {
        this.activity = activity;
        this.modelList = objects;
        this.layout=layout;
        this.wishlistServices=wishlistServices;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.preloader_product)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        imageListener = new ImageDisplayListener();
        dbHandler=new DatabaseHandler(activity);
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();
//        customerName = sharedpreferences.getString("customerName", "");
        userId = sharedpreferences.getString("userID", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View view = inflater.inflate(R.layout.gallary_inflate, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalListAdapter.ViewHolder holder, final int position) {

        final ProductModel modelItem = modelList.get(position);
        final  int i1=position;
//        if(wishlistServices!=null&&wishlistServices.size()!=0){
//            for(int i=0;i<wishlistServices.size();i++){
//                ProductModel model = wishlistServices.get(i);
//                if(model.getId()==modelItem.getId()){
//                    holder.like.setImageResource(R.drawable.like_icon_filled);
//                }else{
//                    holder.like.setImageResource(R.drawable.like_icon);
//                }
//            }
//        }

        holder.text.setText(modelItem.getTitle());
        holder.price.setText(""+modelItem.getCost());
        holder.price.setPaintFlags(holder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.finalPrice.setText(""+modelItem.getFinalPrice());
        //OFFER TAG COLOR CHANGES
        holder.offerTag.setText(modelItem.getTag());
        if(modelItem.getTag().equalsIgnoreCase("best offer")){
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_blue));
        }else  if(modelItem.getTag().equalsIgnoreCase("new")){
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_green));
        }else  if(modelItem.getDiscount()!=0){
            holder.offerTag.setText("Sale "+modelItem.getDiscount()+"% Off");
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_red));
        }else {
            holder.offerTag.setVisibility(View.INVISIBLE);
        }

        //Rating Bar
        if(modelItem.getRating()==0){
            holder.ratingBar.setRating(0);

        }else{
            holder.ratingBar.setRating((float)modelItem.getRating()/20);
        }
//        holder.ratingBar.setProgress(Integer.valueOf(modelItem.getRating()));
//        Log.e("horizalAdapter","onBind");
//        Log.e("horizalAdapter",modelItem.getTitle());
//        holder.desc.setText(modelList.get(position).getDescription());
        imageLoader.displayImage(((ProductModel)modelItem).getThumbnail(), holder.image, options, imageListener);
        //Share Action
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, modelItem.getTitle()+" shared ", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Market Place App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Checkout Deals "+modelItem.getTitle()+" click here: "+modelItem.getShare());
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

//        //image color change when change the theme colors
//        TypedValue typedValue = new TypedValue();
//        Resources.Theme theme = activity.getTheme();
//        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
//        int color = typedValue.data;
////        int color = Color.parseColor("#2e4567"); //The color u want
//        holder.like.setColorFilter(color);
//        //Like Action
//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(activity, modelItem.getTitle()+" liked ", Toast.LENGTH_SHORT).show();
//                if(flag){
////
//                    ((ImageView)v).setImageResource(R.drawable.like_icon_filled);
//                    flag=false;
//                }else{
//
//                    ((ImageView)v).setImageResource(R.drawable.like_icon);
//                  flag=true;
//                }
//            }
//        });
        if(modelItem.getWishlist()==0){
            holder.likeBtn.setChecked(false);

        }else{
            holder.likeBtn.setChecked(true);
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
        holder.likeBtn.setHighlightColor(color);
        //Like Action
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelItem.setId(modelItem.getId());
                modelItem.setTitle(modelItem.getTitle());
                modelItem.setDescription(modelItem.getDescription());
                modelItem.setCost(modelItem.getCost());
                modelItem.setFinalPrice(modelItem.getFinalPrice());
                modelItem.setThumbnail(modelItem.getThumbnail());
                modelItem.setShare(modelItem.getShare());
                modelItem.setTag(modelItem.getTag());
                modelItem.setDiscount(modelItem.getDiscount());
                modelItem.setRating(modelItem.getRating());
                if (((CheckBox)v).isChecked()) {


//                    output.setText("checked");
                    Toast.makeText(activity, modelItem.getTitle()+" liked ", Toast.LENGTH_SHORT).show();
                    if(userId.equals("")){
                        wishList=dbHandler.checkWishlistProduct(String.valueOf(modelItem.getId()));
                        if(wishList.size()<1){
                            Toast.makeText(activity, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                            dbHandler.insertWishlist(String.valueOf(modelItem.getId()),modelItem.getTitle(),String.valueOf(modelItem.getCost()),String.valueOf(modelItem.getFinalPrice()),modelItem.getThumbnail());
                            modelItem.setWishlist(1);
                            modelList.remove(i1);
                            modelList.add(i1, modelItem);
                        }else{
                            Toast.makeText(activity, "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        addWishlistItem(String.valueOf(modelItem.getId()),userId);
                        modelItem.setWishlist(1);
                        modelList.remove(i1);
                        modelList.add(i1, modelItem);
                    }
//                    addtoWishlist(modelItem.getId());
                } else {
                    reomvewishlistItem(modelItem.getId());
//                    output.setText("unchecked");
//                    modelItem.setId(modelItem.getId());
//                    modelItem.setTitle(modelItem.getTitle());
//                    modelItem.setDescription(modelItem.getDescription());
//                    modelItem.setCost(modelItem.getCost());
//                    modelItem.setThumbnail(modelItem.getThumbnail());
                    modelItem.setWishlist(0);
                    modelList.remove(i1);
                    modelList.add(i1, modelItem);

                    Toast.makeText(activity, modelItem.getTitle()+" dis liked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Open Single Item Action
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(activity,SecondActivity.class);
                in.putExtra("key", TagName.FRAGMENT_PRODUCT_DETAILS);
                in.putExtra("singleProductId", modelItem.getId());
                activity.startActivity(in);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });
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

    @Override
    public int getItemCount() {
        return modelList == null ? 0 : modelList.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        Button offerTag;
        RatingBar ratingBar;
        TextView desc,price, finalPrice;
        ImageView image,share, like;
        CheckBox likeBtn;
        private ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.grid_text);
            offerTag = (Button) view.findViewById(R.id.offer_tag_btn);
//            holder.desc = (TextView) view.findViewById(R.id.num_text);
            price = (TextView) view.findViewById(R.id.price);
            finalPrice = (TextView) view.findViewById(R.id.final_price);
            image = (ImageView) view.findViewById(R.id.grid_image);
            share = (ImageView) view.findViewById(R.id.prdt_share);
//            like = (ImageView) view.findViewById(R.id.prdt_like);
            likeBtn = (CheckBox) view.findViewById(R.id.prdt_like_btn);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(view);
        }
    }
   /* public void addtoWishlist(int singlePrdtId){
        if(userId.equals("")){
            wishList=dbHandler.checkWishlistProduct(String.valueOf(singlePrdtId));
            if(wishList.size()<1){
                Toast.makeText(activity, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                dbHandler.insertWishlist(String.valueOf(singlePrdtId),m.getTitle(),String.valueOf(item1.getCost()),String.valueOf(item1.getFinalPrice()),item1.getThumbnail());

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
    }*/
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
}