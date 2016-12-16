package com.btranz.ecommerceapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.fragment.CartFragment;
import com.btranz.ecommerceapp.fragment.ProductItemFragment;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.ItemClickListener;
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


public class CartServicesRecyclerAdapter extends RecyclerView.Adapter<CartServicesRecyclerAdapter.CartServicesRecycleViewRowHolder> {
    AlertDialog alertDialog;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    CartFragment acti;
    DatabaseHandler db;//=new DatabaseHandler();
    private ImageLoadingListener imageListener;
    private List<ProductModel> feedItemList;
    int count=0;
    private Context mContext;


    public CartServicesRecyclerAdapter(CartFragment context, List<ProductModel> feedItemList) {
        this.feedItemList = feedItemList;
        this.acti = context;
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
        db=new DatabaseHandler(acti.getContext());
        imageListener = new ImageDisplayListener();
    }

    @Override
    public CartServicesRecycleViewRowHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
       final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_inflate, viewGroup, false);
     /*  ServicesRecycleViewRowHolder mh = new ServicesRecycleViewRowHolder(v, new ServicesRecycleViewRowHolder.IMyViewHolderClicks() {
           final public void onServiceView(View caller) { Log.e("Poh-tah-tos","onServiceView");
//                Bundle arguments = new Bundle();
//                Fragment fragment = null;
//                Log.d("position adapter", "" + i);
//                ServicesModel product = (ServicesModel) feedItemList.get(i);
//                arguments.putParcelable("singleProduct", product);
//
//                // Start a new fragment
//                fragment = new OfferDetailFragment();
//                fragment.setArguments(arguments);
//
//                FragmentTransaction transaction = acti
//                        .getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_services, fragment,
//                        OfferDetailFragment.ARG_ITEM_ID);
//                transaction.addToBackStack(OfferDetailFragment.ARG_ITEM_ID);
//                transaction.commit();
 };
//            public void onServiceViewIncre(int caller) {
//                Log.e("Poh-tah-tos1","onServiceViewIncre");}
//            public void onServiceViewDecre(int caller) { Log.e("Poh-tah-tos2","onServiceViewDecre");}
            public void onServiceImageView(ImageView callerImage) {

                if(callerImage.getId()==R.id.thumbnail){
                    Log.e("To-m8-tohs","thumbnail");
                }else if(callerImage.getId()==R.id.incre_image){
                    Log.e("To-m8-tohs", "incre_image");
                    int in = Integer.parseInt(((TextView)v.findViewById(R.id.num_text)).getText().toString());
                    in++;
                    ((TextView)v.findViewById(R.id.num_text)).setText(String.valueOf(in));
                    notifyItemChanged(i);
//                    ((TextView)v.findViewById(R.id.num_text)).setText(String.valueOf(count++));
                }else if(callerImage.getId()==R.id.decre_image){
                    Log.e("To-m8-tohs", "decre_image");
                    int in = Integer.parseInt(((TextView)v.findViewById(R.id.num_text)).getText().toString());
                    in--;
                    ((TextView)v.findViewById(R.id.num_text)).setText(String.valueOf(in));
                    notifyItemChanged(i);
//                    ((TextView)v.findViewById(R.id.num_text)).setText(String.valueOf(count--));
                }
                Log.e("To-m8-tohs","onServiceImageView"); }
        });
*/

        return new CartServicesRecycleViewRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final CartServicesRecycleViewRowHolder feedListRowHolder, int i) {
        final ProductModel feedItem = feedItemList.get(i);
        final  int i1=i;
        feedListRowHolder.title.setText(feedItem.getTitle());

        feedListRowHolder.price.setText(String.valueOf(feedItem.getCost()));
        feedListRowHolder.price.setPaintFlags(feedListRowHolder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        feedListRowHolder.finalPrice.setText(String.valueOf(feedItem.getFinalPrice()));
        feedListRowHolder.num.setText(String.valueOf(feedItem.getCount()));
        feedListRowHolder.prdtTotPrice.setText(String.valueOf(feedItem.getFinalPrice()*feedItem.getCount()));

        feedListRowHolder.offerTag.setText(feedItem.getTag());
        if(feedItem.getTag().equalsIgnoreCase("new")){
            feedListRowHolder.offerTag.setBackgroundColor(ContextCompat.getColor(acti.getActivity(), R.color.color_green));
        }else  if(feedItem.getTag().equalsIgnoreCase("best offer")){
            feedListRowHolder.offerTag.setBackgroundColor(ContextCompat.getColor(acti.getActivity(), R.color.color_blue));
        }else  if(feedItem.getDiscount()!=0){
            feedListRowHolder.offerTag.setText("Sale "+feedItem.getDiscount()+"% Off");
            feedListRowHolder.offerTag.setBackgroundColor(ContextCompat.getColor(acti.getActivity(), R.color.color_red));
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
       //click
        feedListRowHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                ProductModel product = (ProductModel) feedItemList.get(position);
//                arguments.putParcelable("singleProduct", product);
                arguments.putInt("singleProductId", feedItem.getId());
                // Start a new fragment
                fragment = new ProductItemFragment();
                fragment.setArguments(arguments);

                FragmentTransaction transaction = acti
                        .getFragmentManager().beginTransaction();
                transaction.replace(R.id.container_second, fragment,
                        ProductItemFragment.ARG_ITEM_ID);
                transaction.addToBackStack(ProductItemFragment.ARG_ITEM_ID);
                transaction.commit();
            }
        });
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
                        updateAfterDelete(feedItem.getId(),feedItem.getFinalPrice(),feedItem.getCount());
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

        feedListRowHolder.incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedItem.setId(feedItem.getId());
                feedItem.setTitle(feedItem.getTitle());
                feedItem.setDescription(feedItem.getDescription());
                feedItem.setCost(feedItem.getCost());
                feedItem.setThumbnail(feedItem.getThumbnail());
                feedItem.setCount(feedItem.getCount() + 1);
                feedItemList.remove(i1);
                feedItemList.add(i1, feedItem);
                //update CheckOut data when click Increment button
                updateCheckOutAdd(feedItem.getCost());
                //update product data when click Increment button
                (acti).updateData(feedItem.getId(),feedItem.getCount());
                notifyItemChanged(i1);
//                db.updateCartItem(String.valueOf(feedItem.getId()),String.valueOf(feedItem.getCount()));
            }
        });
        feedListRowHolder.decre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedItem.getCount()>1) {
                    feedItem.setId(feedItem.getId());
                    feedItem.setTitle(feedItem.getTitle());
                    feedItem.setDescription(feedItem.getDescription());
                    feedItem.setCost(feedItem.getCost());
                    feedItem.setThumbnail(feedItem.getThumbnail());
                    feedItem.setCount(feedItem.getCount() - 1);
                    feedItemList.remove(i1);
                    feedItemList.add(i1, feedItem);
                    //update CheckOut when click decrement button
                    updateCheckOutRemove(feedItem.getCost());
                   //update data when click decrement button
                    (acti).updateData(feedItem.getId(),feedItem.getCount());
                    notifyItemChanged(i1);
//                    db.updateCartItem(String.valueOf(feedItem.getId()),String.valueOf(feedItem.getCount()));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return feedItemList == null ? 0 : feedItemList.size();
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

    public class CartServicesRecycleViewRowHolder extends RecyclerView.ViewHolder  {
        public ImageView thumbnail,incr,decre;
        public TextView title, price,num, finalPrice,prdtTotPrice;
        public ImageView deleteBtn;
        RatingBar rating;
        Button offerTag;
        //    public IMyViewHolderClicks mListener;
        int count=0;
        private ItemClickListener clickListener;

        public CartServicesRecycleViewRowHolder(View view) {
            super(view);
//        mListener = listener;
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.incr = (ImageView) view.findViewById(R.id.incre_image);
        this.decre = (ImageView) view.findViewById(R.id.decre_image);
            this.title = (TextView) view.findViewById(R.id.title);
            this.price = (TextView) view.findViewById(R.id.price);
            this.finalPrice = (TextView) view.findViewById(R.id.final_price);
            this.num = (TextView) view.findViewById(R.id.num_text);
            this.prdtTotPrice = (TextView) view.findViewById(R.id.prdt_total_cost);
            this.deleteBtn = (ImageView) view.findViewById(R.id.cart_delete_btn);
            this.rating = (RatingBar) view.findViewById(R.id.ratingBar);
            this.offerTag = (Button) view.findViewById(R.id.offer_tag_btn);
            view.setTag(view);
        this.incr.setTag(this.incr);
        this.decre.setTag(this.decre);
//        this.thumbnail.setOnClickListener(this);
//        this.incr.setOnClickListener(this);
//        this.decre.setOnClickListener(this);
//        view.setOnClickListener(this);
        }
//    @Override
//    public void onClick(View v) {
//        clickListener.onClick(v, getPosition() , false);
//        /*switch (v.getId()) {
//            case R.id.incre_image:
//                int i = Integer.parseInt(num.getText().toString());
//                i++;
//                num.setText(String.valueOf(i));
////                notifyItemChanged(getAdapterPosition());
//                break;
//            case R.id.decre_image:
//                i = Integer.parseInt(num.getText().toString());
//                if(i>0){
//                    i--;
//                    num.setText(String.valueOf(i));
//                }
//
////                notifyItemChanged(getAdapterPosition());
//                break;
//        }*/
//
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
//    }
    public  void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
//    public static interface IMyViewHolderClicks {
//        public void onServiceView(View caller);
////        public void onServiceViewIncre(int caller);
////        public void onServiceViewDecre(int caller);
//        public void onServiceImageView(ImageView callerImage);
//    }

    }

    public void  updateAfterDelete(int prdt_id,double amt, int count){
        (acti).afterDelete(prdt_id,amt,count);
//        ServicesFragment sf=new ServicesFragment();
//        sf.checkOut(count, amt);
    }
    public void  updateCheckOutRemove(double amt){
        (acti).checkOutRemove(amt);
//        ServicesFragment sf=new ServicesFragment();
//        sf.checkOut(count, amt);
    }
    public void  updateCheckOutAdd(double amt){
        (acti).checkOutAdd(amt);
//        ServicesFragment sf=new ServicesFragment();
//        sf.checkOut(count, amt);
    }

}
