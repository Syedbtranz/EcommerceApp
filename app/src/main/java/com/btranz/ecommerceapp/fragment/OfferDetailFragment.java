package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OfferDetailFragment extends Fragment {

	TextView pdtIdTxt;
	TextView pdtNameTxt;
	ImageView pdtImg;
	Activity activity;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;

	Product product;

	public static final String ARG_ITEM_ID = "pdt_detail_fragment";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.ic_error)
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheInMemory()
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();


	}

	@Override
	public void onResume() {
		super.onResume();
		setHasOptionsMenu(true);
		// update the actionbar to show the up carat/affordance

//		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
		SpannableString s = new SpannableString(getString(R.string.title_offer));
		s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		final  TextView toolTitle=((SecondActivity) getActivity()).toolbarTitle;
		//Title
		toolTitle.setText(s);
//		mToolbar.setTitle(s);
		mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Log.d("MrE", "home selected");
				getActivity().onBackPressed();
//				getActivity().finish();
//						((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//				((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//						android.R.anim.fade_out);

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_offer_detail, container,
				false);
		findViewById(view);

//		Bundle bundle = this.getArguments();
//		if (bundle != null) {
//			product = bundle.getParcelable("singleProduct");
//			setProductItem(product);
//		}
		product = activity.getIntent().getParcelableExtra("singleProduct");
		setProductItem(product);
		return view;
	}


	private void findViewById(View view) {

		pdtNameTxt = (TextView) view.findViewById(R.id.pdt_name);
		pdtIdTxt = (TextView) view.findViewById(R.id.product_id_text);

		pdtImg = (ImageView) view.findViewById(R.id.product_detail_img);
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

	private void setProductItem(Product resultProduct) {
		pdtNameTxt.setText("" + resultProduct.getName());
		pdtIdTxt.setText("Offer Id: " + resultProduct.getId());

		imageLoader.displayImage(resultProduct.getImageUrl(), pdtImg, options,
				imageListener);
	}
}
