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
    public static String serverUrl = "http://btranz.website/index.php/rest/V1/marketplace/";
    public  static String sellerId =AppData.getContext().getResources().getString(R.string.seller_id);
//    public static String regUrl = serverUrl+"registration/";
//    public static String loginUrl = serverUrl+"userlogin/";
    public static String searchUrl = serverUrl+"productsearch/"+sellerId+"/";
//    public static String searchAutoCompleteUrl = serverUrl+"autocompletesearch/"+sellerId+"/";
    public static String offerBannerUrl = serverUrl+"getofferbanner/"+sellerId;
    public static String prdtUrl = serverUrl+"getproductlistofseller/"+sellerId;
    public static String popPrdtUrl = serverUrl+"gettopproductselling/"+sellerId;
    public static String brandUrl = serverUrl+"getbrandlist";
//    public static String catgUrl = serverUrl+"getproductcategories/"+sellerId;
//    public static String catgProductListUrl = serverUrl+"getcategoryproductlist/";
//    public static String prdtDetailsUrl = serverUrl+"getproductdetails/";
//    public static String similarPrdtsUrl = serverUrl+"simillarproducts/"+sellerId+"/";
//    public static String addtocartUrl = serverUrl+"addtocart/";
//    public static String getcartUrl = serverUrl+"getcartproduct/";
//    public static String deletecartUrl = serverUrl+"deletecartproduct/";
//    public static String updatecartUrl = serverUrl+"updatecartproduct/";
//    public static String quoteBuynowUrl = serverUrl+"buynow/";
//    public static String promoApplyUrl = serverUrl+"applycoupon/";
//    public static String promoRemoveUrl = serverUrl+"removecoupon/";
//    public static String directCheckoutUrl = serverUrl+"customcheckoutdirect/";
//    public static String deliveryTypeUrl = serverUrl+"applydeleverytype/";
//    public static String getCountryListUrl = serverUrl+"countylist";
//    public static String checkoutPaymentInfoUrl = serverUrl+"customcheckoutcarttotal/";
    public static String checkoutUrl = serverUrl+"customcheckout/";
//    public static String ordersUrl = serverUrl+"orderhistory/";
//    public static String ordersDetailsUrl = serverUrl+"orderitemlist/";
//    public static String cancelOrderUrl = serverUrl+"cancelorderbuyer/";
//    public static String getwishlistUrl = serverUrl+"getwishlist/";
//    public static String addwishlistUrl = serverUrl+"addwishlist/";
//    public static String wishlisttagUrl = serverUrl+"wishlisttag/";
//    public static String itemremovewishlistUrl = serverUrl+"itemremovewishlist/";
//    public static String getProfileUrl = serverUrl+"getaccountinfo/";
//    public static String updateProfileUrl = serverUrl+"editaccountinfo/";
//    public static String deacivateaccountUrl = serverUrl+"deacivateaccount/";
//    public static String acivateaccountUrl = serverUrl+"acivateaccount/";
//    public static String changepasswordUrl = serverUrl+"changepassword/";
//    public static String getaddressUrl = serverUrl+"getaddress/";
//    public static String editbillingaddressUrl = serverUrl+"editbillingaddress/";
//    public static String getreviewUrl = serverUrl+"getreview/";
//    public static String postreviewUrl = serverUrl+"postreview/";

    public static String instantServerUrl = "http://btranz.website/instantapp_services.php";
    public static String homeUrl = instantServerUrl+"?tag=home&sellerid="+sellerId+"&userid=";
    public static String bannerUrl = instantServerUrl+"?tag=offersbanners&sellerid="+sellerId;
    public static String prdtGlanceUrl = instantServerUrl+"?tag=productglance&sellerid="+sellerId;
    public static String instantCategoryUrl = instantServerUrl+"?tag=getproductcategories&sellerid="+sellerId;
    public static String instantCatgProductListUrl = instantServerUrl+"?tag=getcategoryproductlist&categoryid=";
    public static String forgotPwdUrl = instantServerUrl+"?tag=forgotpassword&email=";
    public static String instantRegUrl = instantServerUrl+"?tag=registration&username=";
    public static String instantLoginUrl = instantServerUrl+"?tag=userlogin&username=";
    public static String instantChangePswUrl = instantServerUrl+"?tag=changepassword&userid=";
    public static String instantaddToCartUrl = instantServerUrl+"?tag=addtocart&quantity=";
    public static String instantUpdateCartItemUrl = instantServerUrl+"?tag=updatecartproduct&quantity=";
    public static String instantdeleteCartItemUrl = instantServerUrl+"?tag=deletecartproduct&productid=";
    public static String instantGetCartUrl = instantServerUrl+"?tag=getcartproduct&userid=";
    public static String instantSimilarPrdtsUrl = instantServerUrl+"?tag=simillarproducts&sellerid="+sellerId+"&productid=";
    public static String instantPrdtDetailsUrl = instantServerUrl+"?tag=getproductdetails&productid=";
    public static String instantOrdersUrl = instantServerUrl+"?tag=orderhistory&userid=";
    public static String instantOrdersDetailsUrl = instantServerUrl+"?tag=orderitemlist&orderid=";
    public static String instantcancelOrderUrl = instantServerUrl+"?tag=cancelorderbuyer&orderid=";
    public static String instantGetWishlistUrl = instantServerUrl+"?tag=getwishlist&userid=";
    public static String instantAddtoWishlistUrl = instantServerUrl+"?tag=addwishlist&userid=";
    public static String instantDeleteWishlistItemUrl = instantServerUrl+"?tag=itemremovewishlist&userid=";
    public static String instantQuoteBuynowUrl = instantServerUrl+"?tag=buynow&userid=";
    public static String instantCountryListUrl = instantServerUrl+"?tag=countrylist";
    public static String instantDeliveryTypeUrl = instantServerUrl+"?tag=applydeleverytype&userid=";
    public static String instantPromoApplyUrl = instantServerUrl+"?tag=applycoupon&userid=";
    public static String instantCheckoutPaymentInfoUrl = instantServerUrl+"?tag=customcheckoutcarttotal&userid=";
    public static String instantgetProfileUrl = instantServerUrl+"?tag=getaccountinfo&userid=";
    public static String instantUpdateProfileUrl = instantServerUrl+"?tag=editaccountinfo&userid=";
    public static String instantActivateAccountUrl = instantServerUrl+"?tag=acivateaccount&email=";
    public static String instantDeactivateAccountUrl = instantServerUrl+"?tag=deacivateaccount&userid=";
    public static String instantGetAddressUrl = instantServerUrl+"?tag=getaddress&userid=";
    public static String instantUpdateAddressUrl = instantServerUrl+"?tag=editbillingaddress&userid=";
    public static String instantGetReviewUrl = instantServerUrl+"?tag=getreview&product_id=";
    public static String instantPostReviewUrl = instantServerUrl+"?tag=postreview&product_id=";
    public static String instantCheckoutUrl = instantServerUrl+"?tag=customcheckout&userid=";
    public static String instantSearchAutoCompleteUrl = instantServerUrl+"?tag=autocompletesearch&sellerid="+sellerId+"&name=";



}
