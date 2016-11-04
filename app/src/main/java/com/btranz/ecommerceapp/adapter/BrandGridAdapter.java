package com.btranz.ecommerceapp.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by all on 9/2/2016.
 */
public class BrandGridAdapter extends BaseAdapter {


    ArrayList<ProductModel> modelList;
    int Resource;
    private LayoutInflater inflater;

    FragmentActivity context;
    private DisplayImageOptions options;

    public BrandGridAdapter(FragmentActivity context, ArrayList<ProductModel> objects) {
//        Resource = resource;
        modelList = objects;
        this.context=context;
        inflater = LayoutInflater.from(context);
        ImageLoader.getInstance().destroy();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
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
            view = inflater.inflate(R.layout.brand_grid_inflate, parent, false);
            holder = new ViewHolder();
//            holder.text = (TextView) view.findViewById(R.id.grid_text);
//            holder.desc = (TextView) view.findViewById(R.id.num_text);
//            holder.price = (TextView) view.findViewById(R.id.price);
//            holder.finalPrice = (TextView) view.findViewById(R.id.final_price);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        holder.text.setText(modelList.get(position).getTitle());
//        holder.price.setText(""+modelList.get(position).getCost());
//        holder.finalPrice.setText(""+modelList.get(position).getFinalPrice());
//        holder.desc.setText(modelList.get(position).getDescription());

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
        TextView desc,price, finalPrice;
        ImageView image;
        ProgressBar progressBar;
    }
}

