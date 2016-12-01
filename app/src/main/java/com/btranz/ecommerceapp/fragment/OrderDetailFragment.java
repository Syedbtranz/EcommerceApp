package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.CartServicesRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.OrderDetailsRecyclerAdapter;
import com.btranz.ecommerceapp.modal.OrdersModel;
import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OrderDetailFragment extends Fragment {

	TextView orderId;
	TextView bookingDate;
	ImageView status;
	RecyclerView recyclerView;
	FragmentActivity activity;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;

	OrdersModel Order;

	public static final String ARG_ITEM_ID = "order_details_fragment";


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
		SpannableString s = new SpannableString(getString(R.string.title_order_details));
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
		View view = inflater.inflate(R.layout.fragment_order_details, container,
				false);
		findViewById(view);
//		Bundle b = activity.getIntent().getExtras();
//        if (b != null) {
//			Order = b.getParcelable("singleOrder");
//			setOrderItems(Order);
//        }
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			Order = bundle.getParcelable("singleOrder");
			setOrderItems(Order);
		}
//		OrdersFragment or=new OrdersFragment();
//		Order = or.singleOrder;
//		setOrderItems(Order);
		return view;
	}


	private void findViewById(View view) {

		orderId = (TextView) view.findViewById(R.id.order_id);
		bookingDate = (TextView) view.findViewById(R.id.booking_date);

		status = (ImageView) view.findViewById(R.id.status_bar);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        int paddingBottom = Utils.getToolbarHeight(activity);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);

		recyclerView.setLayoutManager(new LinearLayoutManager(activity));
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

	private void setOrderItems(OrdersModel resultOrder) {
		Log.e("resultOrder",""+resultOrder);
		orderId.setText("" + resultOrder.getOrderId());
		bookingDate.setText("" + resultOrder.getDate());
		if(resultOrder.getStatus().equalsIgnoreCase("pending")){
			status.setImageResource(R.drawable.pending_statusbar);
		}else if(resultOrder.getStatus().equalsIgnoreCase("delivered")){
			status.setImageResource(R.drawable.delivered_statusbar);
		}else if(resultOrder.getStatus().equalsIgnoreCase("cancelled")){
			status.setImageResource(R.drawable.cancelled_statusbar);
		}
		ArrayList<ProductModel> orderList=resultOrder.getOrderList();
		recyclerView.setAdapter(new OrderDetailsRecyclerAdapter(activity, orderList ,R.layout.order_details_inflate));
		recyclerView.scrollToPosition(0);
//		imageLoader.displayImage(resultOrder.getImageUrl(), pdtImg, options,
//				imageListener);
	}
}
