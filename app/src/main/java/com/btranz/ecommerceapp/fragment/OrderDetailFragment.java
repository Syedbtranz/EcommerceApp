package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.CartServicesRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.OrderDetailsRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.OrdersRecyclerAdapter;
import com.btranz.ecommerceapp.modal.OrdersModel;
import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
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

public class OrderDetailFragment extends Fragment {
	ArrayList<ProductModel> orderList;
	private OrderDetailsRecyclerAdapter adapter;
	AlertDialog alertDialog;
	AsyncHttpTask task;
	int orderitemlistId;
	boolean stopSliding = false;
	String message;
	TextView orderId;
	TextView bookingDate;
	ImageView status;
	Button cancelOrderBtn;
	RecyclerView recyclerView;
	FragmentActivity activity;
	private Dialog loadingDialog;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;

	OrdersModel Order;
	// shared preference
	SharedPreferences sharedpreferences;
	String PREFS_NAME = "MyPrefs";
//	SharedPreferences.Editor editor;
	public  String userName,userEmail,userId,msg;
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
		sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
//		editor = sharedpreferences.edit();
		userId = sharedpreferences.getString("userID", "");

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
			orderitemlistId=Order.getId();
			setOrderItems(Order);
		}
		sendRequest();
//		OrdersFragment or=new OrdersFragment();
//		Order = or.singleOrder;
//		setOrderItems(Order);
		return view;
	}


	private void findViewById(View view) {

		orderId = (TextView) view.findViewById(R.id.order_id);
		bookingDate = (TextView) view.findViewById(R.id.booking_date);

		status = (ImageView) view.findViewById(R.id.status_bar);
		cancelOrderBtn = (Button) view.findViewById(R.id.cancel_order_btn);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        int paddingBottom = Utils.getToolbarHeight(activity);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), paddingBottom);

		recyclerView.setLayoutManager(new LinearLayoutManager(activity));
		cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
				builder.setTitle("Cancel?");
				builder.setMessage("Do you want to Cancel the Order ?");
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "Clicked some button");
					}
				});
				builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
                     cancelOrderData();
					}
				});
//				builder.setIcon(R.drawable.cross_black);

				android.support.v7.app.AlertDialog alertDialog = builder.create();
				alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_right;
				alertDialog.show();

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

	private void setOrderItems(OrdersModel resultOrder) {
		Log.e("resultOrder",""+resultOrder);
		orderId.setText("" + resultOrder.getOrderId());
		bookingDate.setText("" + resultOrder.getDate());
		if(resultOrder.getStatus().equalsIgnoreCase("pending")){
			status.setImageResource(R.drawable.pending_statusbar);
			cancelOrderBtn.setText("Cancel Order");
		}else if(resultOrder.getStatus().equalsIgnoreCase("canceled")){
			status.setImageResource(R.drawable.cancelled_statusbar);
			cancelOrderBtn.setVisibility(View.GONE);
			cancelOrderBtn.setEnabled(false);
		}else if(resultOrder.getStatus().equalsIgnoreCase("processing")){
			status.setImageResource(R.drawable.success_statusbar);
			cancelOrderBtn.setVisibility(View.GONE);
		}else if(resultOrder.getStatus().equalsIgnoreCase("complete")){
			status.setImageResource(R.drawable.delivered_statusbar);
			cancelOrderBtn.setText("Return Order");
		}
//		ArrayList<ProductModel> orderList=resultOrder.getOrderList();
		recyclerView.setAdapter(new OrderDetailsRecyclerAdapter(activity, orderList ,R.layout.order_details_inflate));
		recyclerView.scrollToPosition(0);
//		imageLoader.displayImage(resultOrder.getImageUrl(), pdtImg, options,
//				imageListener);
	}
	private void sendRequest() {
		if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
			task = new AsyncHttpTask();
			task.execute(Utils.instantOrdersDetailsUrl+orderitemlistId);
//            task.execute(prdtsUrl);
			Log.e("sendrequest","sendrequest");
		} else {
			message = getResources().getString(R.string.no_internet_connection);
			showAlertDialog(message, true);
		}
	}
	public void showAlertDialog(String message, final boolean finish) {
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (finish)
							activity.finish();
					}
				});
		alertDialog.show();
	}
	public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
			loadingDialog = ProgressDialog.show(activity, "", "Loading...");
		}

		@Override
		protected Integer doInBackground(String... params) {
			InputStream inputStream = null;
			Integer result = 0;
			HttpURLConnection urlConnection = null;

			try {
                /* forming th java.net.URL object */
				URL url = new URL(params[0]);

				urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
				urlConnection.setRequestMethod("GET");

				int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
				if (statusCode ==  200) {

					BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
						response.append(line);
					}
					Log.e("response.toString()", response.toString());
					parseResult(response.toString());
					result = 1; // Successful
				}else{
					result = 0; //"Failed to fetch data!";
				}

			} catch (Exception e) {
				Log.d("catch", e.getLocalizedMessage());
			}

			return result; //"Failed to fetch data!";
		}

		@Override
		protected void onPostExecute(Integer result) {

//            setProgressBarIndeterminateVisibility(false);
			loadingDialog.dismiss();
            /* Download complete. Lets update UI */
			if (result == 1) {
				Log.e("onPostExecute", "onPostExecute");
				Log.e("services",""+orderList);
//                if(services.size()!=0) {
//                    for (int i = 0; i < services.size(); i++) {
//                        ProductModel item = services.get(i);
//                        int count1 = tempCount + item.getCount();
//                        double amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
//                        Log.e("onPostExecute", " " + item.getFinalPrice());
//                        coutTxt.setText(String.valueOf(count1));
//                        amtTxt.setText(String.valueOf(amt1));
//                        tempCount = count1;
//                        tempAmt = amt1;
//                    }
//                    emptyCart.setVisibility(View.GONE);
				adapter = new OrderDetailsRecyclerAdapter(activity, orderList ,R.layout.order_details_inflate);
				recyclerView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//                }else{
//                    emptyCart.setVisibility(View.VISIBLE);
//                }
			} else {
				Log.e("hello", "Failed to fetch data!");
//                emptyCart.setVisibility(View.VISIBLE);
			}
		}
	}
	private void parseResult(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
//			JSONObject jsonObject=response.getJSONObject(0);

			if (jsonObject != null) {
				JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
				int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
				String message = jobstatus.optString(TagName.TAG_MSG);

				if (status==1) {
					JSONArray jarr=jsonObject.optJSONArray("orderitemlist");
					JSONObject job1=jarr.optJSONObject(0);
					JSONObject jobstat=job1.getJSONObject(TagName.TAG_STATUS);
					int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
					String message1 = jobstat.optString(TagName.TAG_MSG);
					if(status1==1) {
						JSONArray jarray = job1.optJSONArray("order_item_list");

            /*Initialize array if null*/
						if (null == orderList) {
							orderList = new ArrayList<ProductModel>();
						}

						for (int j = 0; j < jarray.length(); j++) {
							JSONObject job = jarray.optJSONObject(j);
							ProductModel item1 = new ProductModel();
							item1.setId(job.optInt("product_id"));
							item1.setTitle(job.optString(TagName.KEY_NAME));
//                        item.setDescription(post.optString(TagName.KEY_DES));
							item1.setCost(job.optDouble(TagName.KEY_PRICE));
//                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                            item.setPayment(post.optString("sku"));
							item1.setCount(job.optInt(TagName.KEY_COUNT));
////                    Log.e("name", "name");
							item1.setThumbnail(job.optString(TagName.KEY_THUMB));
							orderList.add(item1);
						}


					}else{
							Toast.makeText(activity, "No Orders", Toast.LENGTH_SHORT).show();
						}

				} else {
					Toast.makeText(activity, "Net Work Error", Toast.LENGTH_SHORT).show();
//					message = jsonObject.getString(TagName.TAG_PRODUCT);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void cancelOrderData(){
		class CancelOrderAsync extends AsyncTask<String, Void, String> {
			Dialog loadingDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
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
					JSONObject jsonObject=new JSONObject(response);
//					JSONObject jsonObject=jsonArray.getJSONObject(0);
					if(jsonObject!=null) {
						JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
						int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
						String message = jobstatus.optString(TagName.TAG_MSG);

						if (status==1) {
							JSONArray jarr=jsonObject.optJSONArray("cancelorderbuyer");
							JSONObject job=jarr.optJSONObject(0);
							JSONObject jobstat=job.getJSONObject(TagName.TAG_STATUS);
							int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
							String message1 = jobstat.optString(TagName.TAG_MSG);
							if(status1==1) {
								Toast.makeText(activity, "Order Cancelled", Toast.LENGTH_SHORT).show();
								activity.finish();
							}else{
								Toast.makeText(activity, "Order Not Cancelled", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(activity, "Net Work Error", Toast.LENGTH_SHORT).show();
//							message = jsonObject.getString(TagName.TAG_PRODUCT);
						}


					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		CancelOrderAsync la = new CancelOrderAsync();
		Log.e("Utils",Utils.instantcancelOrderUrl+orderitemlistId+"&userid="+userId);
		la.execute(Utils.instantcancelOrderUrl+orderitemlistId+"&userid="+userId);

	}
}
