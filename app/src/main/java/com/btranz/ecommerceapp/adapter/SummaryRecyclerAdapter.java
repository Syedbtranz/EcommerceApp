package com.btranz.ecommerceapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SummaryRecyclerAdapter extends RecyclerView.Adapter<SummaryRecyclerAdapter.OrdersRecycleViewRowHolder> {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    FragmentActivity acti;
    int layout;
    private ImageLoadingListener imageListener;
    private List<ProductModel> feedItemList;
    int count=0;
    private Context mContext;

    public SummaryRecyclerAdapter(FragmentActivity context, List<ProductModel> feedItemList, int layout) {
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
        imageListener = new ImageDisplayListener();
    }

    @Override
    public OrdersRecycleViewRowHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
       final View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        return new OrdersRecycleViewRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final OrdersRecycleViewRowHolder feedListRowHolder, int i) {
        ProductModel feedItem = feedItemList.get(i);
//        Picasso.with(mContext).load(feedItem.getThumbnail())
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(feedListRowHolder.thumbnail);
        feedListRowHolder.title.setText(feedItem.getTitle());

        feedListRowHolder.cost.setText(String.valueOf(feedItem.getFinalPrice()));
        feedListRowHolder.num.setText(String.valueOf(feedItem.getCount()));
//        feedListRowHolder.orderId.setText(String.valueOf(feedItem.getId()));
//        feedListRowHolder.orderDate.setText(feedItem.getDate());
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
               ((ProductModel) feedItem).getThumbnail(), feedListRowHolder.thumbnail,
               options, imageListener);

    }

    @Override
    public int getItemCount() {
        return feedItemList == null ? 0 : feedItemList.size();
    }
    public class OrdersRecycleViewRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public  ImageView thumbnail,incr,decre;
        public TextView title, cost,num,orderId, orderDate,orderPayment;
        public ProgressBar orderProgress;
//        public IMyViewHolderClicks mListener;
        int count=0;
//        private ItemClickListener clickListener;
        String TAG="OrdersRecycleViewRowHolder";

        public OrdersRecycleViewRowHolder(View view) {
            super(view);
//        mListener = listener;
//            this.orderId = (TextView) view.findViewById(R.id.order_id);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            this.incr = (ImageView) view.findViewById(R.id.incre_image);
//            this.decre = (ImageView) view.findViewById(R.id.decre_image);
            this.title = (TextView) view.findViewById(R.id.title);
            this.cost = (TextView) view.findViewById(R.id.prdt_total_cost);
            this.num = (TextView) view.findViewById(R.id.num_text);
//            this.orderDate = (TextView) view.findViewById(R.id.order_date);
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
}
