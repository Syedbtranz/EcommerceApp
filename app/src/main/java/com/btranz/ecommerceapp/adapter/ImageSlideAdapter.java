package com.btranz.ecommerceapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.fragment.HomeFragment;
import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.utils.TagName;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;
	FragmentActivity activity;
	List<Product> products;
	HomeFragment homeFragment;

	public ImageSlideAdapter(FragmentActivity activity, List<Product> products,
							 HomeFragment homeFragment) {
		this.activity = activity;
		this.homeFragment = homeFragment;
		this.products = products;
		imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.ic_error)
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheInMemory()
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		final ViewHolder  holder = new ViewHolder();;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.vp_image, container, false);

		holder.image = (ImageView) view
				.findViewById(R.id.image_display);
		holder.image .setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				((MainActivity) activity).showFabs();
//				Bundle arguments = new Bundle();
//				Fragment fragment = null;
//				Log.d("position adapter", "" + position);
				Product product = (Product) products.get(position);
//				arguments.putParcelable("singleProduct", product);
//
//				// Start a new fragment
//				fragment = new OfferDetailFragment();
//				fragment.setArguments(arguments);
//
//				FragmentTransaction transaction = activity
//						.getSupportFragmentManager().beginTransaction();
//				transaction.replace(R.id.container_body, fragment,
//						OfferDetailFragment.ARG_ITEM_ID);
//				transaction.addToBackStack(OfferDetailFragment.ARG_ITEM_ID);
//				transaction.commit();
				Intent in=new Intent(activity,SecondActivity.class);
				in.putExtra("key", TagName.FRAGMENT_OFFER_BANNER);
				in.putExtra("singleProduct", product);
				activity.startActivity(in);
				activity.overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		imageLoader.displayImage(
				((Product) products.get(position)).getImageUrl(), holder.image ,
				options, imageListener);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
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
	public class ViewHolder {
		TextView text;
		TextView desc,price, finalPrice;
		ImageView image;
		ProgressBar progressBar;
	}
}