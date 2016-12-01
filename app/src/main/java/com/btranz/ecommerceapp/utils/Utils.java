package com.btranz.ecommerceapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.btranz.ecommerceapp.R;


public class Utils extends Activity{
    public static String[] dummyName={"Nike","mochi","Lenovo","Nike","Addidas"};
    public static String[] dummyBanners={"R.drawable.banner1","R.drawable.banner2"};


    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
   Resources res = getResources();

public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //Services
    public static String serverUrl = "http://btranz.website/index.php/rest//V1/marketplace/";
    public  static String sellerId =AppData.getContext().getResources().getString(R.string.seller_id);
    public static String regUrl = serverUrl+"registration/";
    public static String loginUrl = serverUrl+"userlogin/";
    public static String searchUrl = serverUrl+"productsearch/"+sellerId+"/";
    public static String offerBannerUrl = serverUrl+"getofferbanner/"+sellerId;
    public static String prdtUrl = serverUrl+"getproductlistofseller/"+sellerId;
    public static String popPrdtUrl = serverUrl+"gettopproductselling/"+sellerId;
    public static String brandUrl = serverUrl+"getbrandlist";
    public static String catgUrl = serverUrl+"getproductcategories/"+sellerId;
    public static String catgProductListUrl = serverUrl+"getcategoryproductlist/";
    public static String prdtDetailsUrl = serverUrl+"getproductdetails/";
    public static String similarPrdtsUrl = serverUrl+"simillarproducts/"+sellerId+"/";
    public static String addtocartUrl = serverUrl+"addtocart/";
    public static String getcartUrl = serverUrl+"getcartproduct/";
    public static String deletecartUrl = serverUrl+"deletecartproduct/";
    public static String updatecartUrl = serverUrl+"updatecartproduct/";
    public static String quoteBuynowUrl = serverUrl+"buynow/";
    public static String promoApplyUrl = serverUrl+"applycoupon/";
    public static String promoRemoveUrl = serverUrl+"removecoupon/";
    public static String directCheckoutUrl = serverUrl+"customcheckoutdirect/";
    public static String deliveryTypeUrl = serverUrl+"applydeleverytype/";
    public static String checkoutPaymentInfoUrl = serverUrl+"customcheckoutcarttotal/";
    public static String checkoutUrl = serverUrl+"customcheckout/";
    public static String ordersUrl = serverUrl+"orderhistory/";
    public static String getwishlistUrl = serverUrl+"getwishlist/";
    public static String addwishlistUrl = serverUrl+"addwishlist/";
    public static String itemremovewishlistUrl = serverUrl+"itemremovewishlist/";

    public static String instantServerUrl = "http://btranz.website/instantapp_services.php";
    public static String homeUrl = instantServerUrl+"?tag=home&sellerid="+sellerId;
    public static String bannerUrl = instantServerUrl+"?tag=offersbanners&sellerid="+sellerId;
    public static String prdtGlanceUrl = instantServerUrl+"?tag=productglance&sellerid="+sellerId;
    public static String addtoCartUrl = instantServerUrl+"?tag=addtocart";
    public static String forgotPwdUrl = instantServerUrl+"?tag=forgotpassword&email=";
    public static String instantRegUrl = instantServerUrl+"?tag=registration&username=";
    public static String instantLoginUrl = instantServerUrl+"?tag=userlogin&username=";


   /* public static String btranzUrl = "http://192.168.1.136/";
    public static String url = btranzUrl+"ecom/products.json";
    public static String productsUrl = btranzUrl+"ecom/product_glance.json";
    public static String ordersUrl = btranzUrl+"repitr/orders.json";
    public static String cartUrl = btranzUrl+"ecom/cart.json";
    public static String addressUrl = btranzUrl+"repitr/address.json";
    public static String servicesUrl = btranzUrl+"ecom/services.json";*/



}
