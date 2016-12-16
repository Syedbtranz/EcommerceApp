package com.btranz.ecommerceapp.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.fragment.OrderDetailFragment;
import com.btranz.ecommerceapp.fragment.OrdersFragment;
import com.btranz.ecommerceapp.modal.OrdersModel;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.OrdersRecycleViewRowHolder> {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    OrdersFragment acti;
    int layout;
    private ImageLoadingListener imageListener;
    private List<OrdersModel> feedItemList;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    String userId;
    public OrdersRecyclerAdapter(OrdersFragment context, List<OrdersModel> feedItemList, int layout) {
        this.feedItemList = feedItemList;
        this.acti = context;
        this.layout=layout;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.preloader_product)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
//
        imageListener = new ImageDisplayListener();
        sharedpreferences = acti.getActivity().getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();
//        customerName = sharedpreferences.getString("customerName", "");
        userId = sharedpreferences.getString("userID", "");
    }

    @Override
    public OrdersRecycleViewRowHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
       final View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        return new OrdersRecycleViewRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final OrdersRecycleViewRowHolder feedListRowHolder, int i) {
        final OrdersModel feedItem = feedItemList.get(i);
        final int i1=i;
//        Picasso.with(mContext).load(feedItem.getThumbnail())
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(feedListRowHolder.thumbnail);
        feedListRowHolder.title.setText(feedItem.getTitle());
        feedListRowHolder.cost.setText(String.valueOf(feedItem.getGrandTotal()));
        feedListRowHolder.num.setText(String.valueOf(feedItem.getQnty()));
        feedListRowHolder.orderId.setText(String.valueOf(feedItem.getOrderId()));
        feedListRowHolder.orderDate.setText(feedItem.getDate());
        Resources res=acti.getResources();
        //Cancel
        if(feedItem.getStatus().equalsIgnoreCase("pending")){
            feedListRowHolder.statusImg.setImageResource(R.drawable.pending_statusbar);
//            feedListRowHolder.cancelBtn.setVisibility(View.VISIBLE);
            feedListRowHolder.cancelBtn.setText("Cancel");
//            feedListRowHolder.cancelBtn.setTextColor(res.getColor(R.color.color_red));
        }else if(feedItem.getStatus().equalsIgnoreCase("canceled")){
            feedListRowHolder.statusImg.setImageResource(R.drawable.cancelled_statusbar);
            feedListRowHolder.cancelBtn.setText("Canceled");
            feedListRowHolder.cancelBtn.setEnabled(false);
//            feedListRowHolder.cancelBtn.setVisibility(View.GONE);
        }else if(feedItem.getStatus().equalsIgnoreCase("processing")){
            feedListRowHolder.statusImg.setImageResource(R.drawable.success_statusbar);
            feedListRowHolder.cancelBtn.setText("Processing");
            feedListRowHolder.cancelBtn.setEnabled(false);
//            feedListRowHolder.cancelBtn.setVisibility(View.GONE);
        }else if(feedItem.getStatus().equalsIgnoreCase("complete")){
            feedListRowHolder.statusImg.setImageResource(R.drawable.delivered_statusbar);
//            feedListRowHolder.cancelBtn.setVisibility(View.VISIBLE);
//            feedListRowHolder.cancelBtn.setTextColor(res.getColor(R.color.color_red));
            feedListRowHolder.cancelBtn.setText("Return");
        }
        //Cancel Order
        feedListRowHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(acti.getActivity());
                builder.setTitle("Cancel?");
                builder.setMessage("Do you want to Cancel the Order ?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "Clicked some button");
                    }
                });
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrderData(feedItem.getId());
                        feedListRowHolder.cancelBtn.setEnabled(false);
                        feedListRowHolder.cancelBtn.setText("Canceled");
                    }
                });
//				builder.setIcon(R.drawable.cross_black);

                android.support.v7.app.AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_right;
                alertDialog.show();

            }
        });
        //View Order
        feedListRowHolder.orderClickLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<ProductModel> orderList=feedItemList.get(i1).getOrderList();
//                Log.d("position orderList", "" + orderList);
                 final OrdersModel singleOrder = (OrdersModel) feedItemList.get(i1);
                Toast.makeText(acti.getActivity(),"clicked",Toast.LENGTH_SHORT).show();
//                acti.itemClick(singleOrder);
                Bundle arguments = new Bundle();
				Fragment fragment = null;
//				Log.d("position adapter", "" + position);
//                OrdersModel order = (ProductModel) products.get(position);
				arguments.putParcelable("singleOrder", singleOrder);

				// Start a new fragment
				fragment = new OrderDetailFragment();
				fragment.setArguments(arguments);

				FragmentTransaction transaction = acti.getActivity()
						.getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.container_second, fragment,
                        OrderDetailFragment.ARG_ITEM_ID);
				transaction.addToBackStack(OrderDetailFragment.ARG_ITEM_ID);
				transaction.commit();
//
            }
        });


//        feedListRowHolder.orderPayment.setText(feedItem.getPayment());
//        feedListRowHolder.orderProgress.setScaleY(3f);
//        feedListRowHolder.orderProgress.setProgress(feedItem.getProcess());

//        feedListRowHolder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                int i = Integer.parseInt(feedListRowHolder.num.getText().toString());
//                switch (view.getId()) {
//                    case R.id.incre_image:
////                        int i = Integer.parseInt(feedListRowHolder.num.getText().toString());
////                        i++;
//                        feedListRowHolder.num.setText(String.valueOf(i + 1));
////                notifyItemChanged(position);
//                        break;
//                    case R.id.decre_image:
////                        i = Integer.parseInt(feedListRowHolder.num.getText().toString());
//                        if (i > 0) {
////                            i--;
//                            feedListRowHolder.num.setText(String.valueOf(i - 1));
//                        }
//
////                notifyItemChanged(position);
//                        break;
//                }
////                notifyItemChanged(position);
////                notifyDataSetChanged();
////                notifyItemInserted(position);
//            }
//        });
        imageLoader.displayImage(
               ((OrdersModel) feedItem).getThumbnail(), feedListRowHolder.thumbnail,
               options, imageListener);

    }

    @Override
    public int getItemCount() {
        return feedItemList == null ? 0 : feedItemList.size();
    }
    public class OrdersRecycleViewRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public  ImageView thumbnail,incr,decre,statusImg;
        public TextView title, cost,num,orderId, orderDate,orderPayment;
        LinearLayout orderClickLL;
        Button cancelBtn;
        public ProgressBar orderProgress;
//        public IMyViewHolderClicks mListener;
        int count=0;
//        private ItemClickListener clickListener;
        String TAG="OrdersRecycleViewRowHolder";

        public OrdersRecycleViewRowHolder(View view) {
            super(view);
//        mListener = listener;
            this.orderClickLL = (LinearLayout) view.findViewById(R.id.order_click_ll);
            this.orderId = (TextView) view.findViewById(R.id.order_id);
            this.orderDate = (TextView) view.findViewById(R.id.order_date);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.statusImg = (ImageView) view.findViewById(R.id.status_bar);
////            this.incr = (ImageView) view.findViewById(R.id.incre_image);
////            this.decre = (ImageView) view.findViewById(R.id.decre_image);
            this.title = (TextView) view.findViewById(R.id.title);
            this.cost = (TextView) view.findViewById(R.id.prdt_total_cost);
            this.num = (TextView) view.findViewById(R.id.num_text);
            this.cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

//            this.orderPayment = (TextView) view.findViewById(R.id.order_payment);
//            this.orderProgress=(ProgressBar)view.findViewById(R.id.progressBar);
//        view.setTag(view);
//        this.incr.setTag(this.incr);
//        this.decre.setTag(this.decre);
//        this.thumbnail.setOnClickListener(this);
//        this.incr.setOnClickListener(this);
//        this.decre.setOnClickListener(this);
//        view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//        clickListener.onClick(v, getAdapterPosition() , false);
        /*switch (v.getId()) {
            case R.id.incre_image:
                int i = Integer.parseInt(num.getText().toString());
                i++;
                num.setText(String.valueOf(i));
//                notifyItemChanged(getAdapterPosition());
                break;
            case R.id.decre_image:
                i = Integer.parseInt(num.getText().toString());
                if(i>0){
                    i--;
                    num.setText(String.valueOf(i));
                }

//                notifyItemChanged(getAdapterPosition());
                break;
        }*/

//        if (v instanceof ImageView){
//            mListener.onServiceImageView((ImageView) v);
//        }
////        else if(v.getId()==R.id.incre_image){
////            mListener.onServiceViewIncre(v.getId());
////            Log.e("Poh-count++", "count++");
////            num.setText(String.valueOf(count++));
////        }else if(v.getId()==R.id.decre_image){
////            mListener.onServiceViewDecre(v.getId());
////            Log.e("Poh-count--", "count--");
////            num.setText(String.valueOf(count--));
////        }
//        else {
//            mListener.onServiceView(v);
//        }
        }
        //    public  void setClickListener(ItemClickListener itemClickListener) {
//        this.clickListener = itemClickListener;
//    }
//        public static interface IMyViewHolderClicks {
//            public void onServiceView(View caller);
//            //        public void onServiceViewIncre(int caller);
////        public void onServiceViewDecre(int caller);
//            public void onServiceImageView(ImageView callerImage);
//        }

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
    public void cancelOrderData(int orderitemlistId){
        class CancelOrderAsync extends AsyncTask<String, Void, String> {
            Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(acti.getActivity(), "", "Please wait...");
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
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject!=null) {
                        JSONObject jsonObj=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        String status=jsonObj.optString(TagName.TAG_MSG);
                        if(status.equalsIgnoreCase("success")){
                            Toast.makeText(acti.getActivity(),status,Toast.LENGTH_SHORT).show();
//                            activity.finish();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        CancelOrderAsync la = new CancelOrderAsync();
        Log.e("Utils", Utils.cancelOrderUrl+orderitemlistId+"/"+userId);
        la.execute(Utils.cancelOrderUrl+orderitemlistId+"/"+userId);

    }
}
