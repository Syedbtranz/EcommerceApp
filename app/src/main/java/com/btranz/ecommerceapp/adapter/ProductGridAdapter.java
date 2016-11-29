package com.btranz.ecommerceapp.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by all on 9/2/2016.
 */
public class ProductGridAdapter extends BaseAdapter {


    ArrayList<ProductModel> modelList;
    int Resource;
    private LayoutInflater inflater;

    FragmentActivity context;
    private DisplayImageOptions options;
    boolean flag=true;
    public ProductGridAdapter(FragmentActivity context, ArrayList<ProductModel> objects) {
//        Resource = resource;
        modelList = objects;
        this.context=context;
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
            holder.like = (ImageView) view.findViewById(R.id.prdt_like);
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
        if(modelList.get(position).getTag().equalsIgnoreCase("new")){
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(context, R.color.color_green));
        }else  if(modelList.get(position).getTag().equalsIgnoreCase("best offer")){
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue));
        }else  if(modelList.get(position).getDiscount()!=0){
            holder.offerTag.setText("Sale "+modelList.get(position).getDiscount()+"% Off");
            holder.offerTag.setBackgroundColor(ContextCompat.getColor(context, R.color.color_red));
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
        TypedValue typedValue = new TypedValue();
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
        ProgressBar progressBar;
    }
}

