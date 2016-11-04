package com.btranz.ecommerceapp.adapter;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ViewHolder> {
    ImageLoader imageLoader = ImageLoader.getInstance();
    private FragmentActivity activity;
    ArrayList<ProductModel> modelList;
    private DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    int layout;
    boolean flag=true;
    int tempPos=0;
    public HorizontalListAdapter(FragmentActivity activity, ArrayList<ProductModel> objects,int layout) {
        this.activity = activity;
        this.modelList = objects;
        this.layout=layout;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        imageListener = new ImageDisplayListener();
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
        Log.e("horizalAdapter",modelItem.getTitle());
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

        //image color change when change the theme colors
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
//        int color = Color.parseColor("#2e4567"); //The color u want
        holder.like.setColorFilter(color);
        //Like Action
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, modelItem.getTitle()+" liked ", Toast.LENGTH_SHORT).show();
                if(flag){
//
                    ((ImageView)v).setImageResource(R.drawable.like_icon_filled);
                    flag=false;
                }else{

                    ((ImageView)v).setImageResource(R.drawable.like_icon);
                  flag=true;
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
            like = (ImageView) view.findViewById(R.id.prdt_like);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(view);
        }
    }
}