package com.btranz.ecommerceapp.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by all on 9/2/2016.
 */
public class CategoryGadGridAdapter extends BaseAdapter {


    ArrayList<ProductModel> modelList;
    int Resource;
    private LayoutInflater inflater;

    FragmentActivity context;
    private DisplayImageOptions options;

    public CategoryGadGridAdapter(FragmentActivity context, ArrayList<ProductModel> objects) {
//        Resource = resource;
        modelList = objects;
        this.context=context;
        inflater = LayoutInflater.from(context);
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
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
            view = inflater.inflate(R.layout.catg_inflate, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.catg_txt);
//            holder.desc = (TextView) view.findViewById(R.id.num_text);
            holder.image = (ImageView) view.findViewById(R.id.catg_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(modelList.get(position).getTitle());
//        holder.desc.setText(modelList.get(position).getDescription());
        ImageLoader.getInstance().displayImage(modelList.get(position).getThumbnail(), holder.image, options);

        return view;
    }
    public class ViewHolder {
        TextView text;
        TextView desc;
        ImageView image;
    }
}

