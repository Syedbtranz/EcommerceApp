package com.btranz.ecommerceapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.fragment.WishlistFragment;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WislistRecyclerAdapter extends RecyclerView.Adapter<WislistRecyclerAdapter.OrdersRecycleViewRowHolder> {
    AlertDialog alertDialog;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    WishlistFragment acti;
    int layout;
    private ImageLoadingListener imageListener;
    private List<ProductModel> feedItemList;
    int count=0;
    private Context mContext;

    public WislistRecyclerAdapter(WishlistFragment context, List<ProductModel> feedItemList, int layout) {
        this.feedItemList = feedItemList;
        this.acti = context;
        this.layout=layout;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(acti.getActivity()));
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
        final ProductModel feedItem = feedItemList.get(i);
        final  int i1=i;
//        Picasso.with(mContext).load(feedItem.getThumbnail())
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(feedListRowHolder.thumbnail);
        feedListRowHolder.title.setText(feedItem.getTitle());
        feedListRowHolder.price.setText(String.valueOf(feedItem.getCost()));
        feedListRowHolder.price.setPaintFlags(feedListRowHolder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        feedListRowHolder.finalPrice.setText(String.valueOf(feedItem.getFinalPrice()));
        feedListRowHolder.offerTag.setText(feedItem.getTag());
        if(feedItem.getDiscount()!=0){
            feedListRowHolder.offerTag.setText("Sale "+feedItem.getDiscount()+"% Off");
            feedListRowHolder.offerTag.setBackgroundColor(ContextCompat.getColor(acti.getActivity(), R.color.color_red));
        }else  if(feedItem.getTag().equalsIgnoreCase("best offer")){
            feedListRowHolder.offerTag.setBackgroundColor(ContextCompat.getColor(acti.getActivity(), R.color.color_blue));
        }else  if(feedItem.getTag().equalsIgnoreCase("new")){
            feedListRowHolder.offerTag.setBackgroundColor(ContextCompat.getColor(acti.getActivity(), R.color.color_green));
        }else {
            feedListRowHolder.offerTag.setVisibility(View.INVISIBLE);
        }
        //RAtingBar
        if(feedItem.getRating()==0){
            feedListRowHolder.rating.setRating(0);

        }else{
            feedListRowHolder.rating.setRating((float)feedItem.getRating()/20);
        }
        imageLoader.displayImage(
               ((ProductModel) feedItem).getThumbnail(), feedListRowHolder.thumbnail,
               options, imageListener);
        feedListRowHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(acti.getActivity());
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete the product "+feedItem.getTitle()+"?");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "Clicked some button");
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "Clicked some button");
                        //                feedItem.setId(feedItem.getId());
//                feedItem.setTitle(feedItem.getTitle());
//                feedItem.setCost(feedItem.getCost());
//                feedItem.setThumbnail(feedItem.getThumbnail());
//                feedItem.setCount(feedItem.getCount() + 1);
                        feedItemList.remove(i1);
//                db.removeCartItem(String.valueOf(feedItem.getId()));
//                feedItemList.add(i1, feedItem);
                        //update CheckOut and Cart List when click Remove button
//                        updateAfterDelete(feedItem.getId(),feedItem.getFinalPrice(),feedItem.getCount());
                        acti.reomvewishlistItem(feedItem.getId());
                        notifyDataSetChanged();
//                notifyItemChanged(i1);
                    }
                });
                builder.setIcon(R.drawable.remove_btn);

                alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_right;
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return feedItemList == null ? 0 : feedItemList.size();
    }
    public class OrdersRecycleViewRowHolder extends RecyclerView.ViewHolder  {
        public  ImageView thumbnail,incr,decre;
        public ImageView deleteBtn;
        public TextView title, price,num, finalPrice, orderDate,orderPayment;
        RatingBar rating;
        Button offerTag;
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
            this.price = (TextView) view.findViewById(R.id.price);
            this.finalPrice = (TextView) view.findViewById(R.id.final_price);
            this.deleteBtn = (ImageView) view.findViewById(R.id.wishlist_delete_btn);
            this.rating = (RatingBar) view.findViewById(R.id.ratingBar);
            this.offerTag = (Button) view.findViewById(R.id.offer_tag_btn);
//            this.num = (TextView) view.findViewById(R.id.num_text);
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
