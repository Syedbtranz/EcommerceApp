package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.MainActivity;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.CategoryGridAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;
import com.btranz.ecommerceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Ravi on 29/07/15.
 */
public class CategoriesFragment extends Fragment {
    public static final String CATG_FRAG = "catg_fragment";
    LinearLayout progressLL;
    TextView faTxt, gadEleTxt, homeLiveTxt;
    GridView fagrid,gegrid,hlgrid;
    ArrayList<ProductModel> services, gadServices, hlServices;

    FragmentActivity activity;
    CategoryGridAdapter adapter,gadAdapter,hlAdapter;
    AsyncHttpTask task;
    AlertDialog alertDialog;
    String message, userId;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    public CategoriesFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userID", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        progressLL=(LinearLayout)rootView.findViewById(R.id.progress_ll);
        faTxt = (TextView) rootView.findViewById(R.id.fas_txt);
        gadEleTxt = (TextView) rootView.findViewById(R.id.gad_ele_txt);
        homeLiveTxt = (TextView) rootView.findViewById(R.id.home_live_txt);
        fagrid = (GridView)rootView.findViewById(R.id.fashion_grid);
        gegrid = (GridView)rootView.findViewById(R.id.ge_grid);
        hlgrid = (GridView)rootView.findViewById(R.id.hl_grid);
       //FASHION ACTION
        fagrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id1= services.get(position).getId();
                String user;
                if(userId.equals("")){
                    user="0";
                }else{
                    user=userId;
                }
//                Intent in=new Intent(activity,SecondActivity.class);
//                in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
//                in.putExtra("prdtsUrl", Utils.catgProductListUrl+id1+"/"+Utils.sellerId);
////                in.putExtra("id", services.get(position).getId());
//                startActivity(in);
//                Toast.makeText(getActivity(), "Id:" + services.get(position).getTitle(), Toast.LENGTH_LONG).show();
                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);
                arguments.putString("prdtsUrl", Utils.instantCatgProductListUrl+id1+"&sellerid="+Utils.sellerId+"&userid="+user);
                arguments.putString("prdtsTitle", services.get(position).getTitle());
                // Start a new fragment
                fragment = new ProductsFragment();
                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_second, fragment,
                        ProductsFragment.PRDTS_FRAG);
                transaction.addToBackStack(ProductsFragment.PRDTS_FRAG);
                transaction.commit();
            }
        });
        //GADGET & ELECTONICS ACTION
        gegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent in=new Intent(activity,SecondActivity.class);
                int gadId= gadServices.get(position).getId();
                String user;
                if(userId.equals("")){
                    user="0";
                }else{
                    user=userId;
                }
//                Toast.makeText(getActivity(), "Id:" + gadServices.get(position).getId(), Toast.LENGTH_LONG).show();
//                in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
//                in.putExtra("prdtsUrl", Utils.catgProductListUrl+gadId+"/"+Utils.sellerId);
////                in.putExtra("id", services.get(position).getId());
//                startActivity(in);
//                Toast.makeText(getActivity(), "Id:" + services.get(position).getTitle(), Toast.LENGTH_LONG).show();
                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);
                arguments.putString("prdtsUrl", Utils.instantCatgProductListUrl+gadId+"&sellerid="+Utils.sellerId+"&userid="+user);
                arguments.putString("prdtsTitle", gadServices.get(position).getTitle());

                // Start a new fragment
                fragment = new ProductsFragment();
                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_second, fragment,
                        ProductsFragment.PRDTS_FRAG);
                transaction.addToBackStack(ProductsFragment.PRDTS_FRAG);
                transaction.commit();
            }
        });
        //HOME LIVING ACTION
        hlgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent in=new Intent(activity,SecondActivity.class);
                int hlId= hlServices.get(position).getId();
                String user;
                if(userId.equals("")){
                    user="0";
                }else{
                    user=userId;
                }
//                Toast.makeText(getActivity(), "Id:" + hlServices.get(position).getId(), Toast.LENGTH_LONG).show();
//                in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
//                in.putExtra("prdtsUrl", Utils.catgProductListUrl+hlId+"/"+Utils.sellerId);
////                in.putExtra("id", services.get(position).getId());
//                startActivity(in);
//                Toast.makeText(getActivity(), "Id:" + services.get(position).getTitle(), Toast.LENGTH_LONG).show();
                Bundle arguments = new Bundle();
                Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);
                arguments.putString("prdtsUrl", Utils.instantCatgProductListUrl+hlId+"&sellerid="+Utils.sellerId+"&userid="+user);
                arguments.putString("prdtsTitle", hlServices.get(position).getTitle());
                // Start a new fragment
                fragment = new ProductsFragment();
                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_second, fragment,
                        ProductsFragment.PRDTS_FRAG);
                transaction.addToBackStack(ProductsFragment.PRDTS_FRAG);
                transaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onResume() {
        final Toolbar mToolbar= ((SecondActivity) getActivity()).mToolbar;
        SpannableString s = new SpannableString(getString(R.string.title_catg));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final  TextView toolTitle=((SecondActivity) getActivity()).toolbarTitle;
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);

//        setHasOptionsMenu(true);
//        final Toolbar mToolbar= ((MainActivity) getActivity()).toolbar;
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("MrE", "home selected");
                activity.onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });
        if (services == null) {
            services = new ArrayList<ProductModel>();
            gadServices = new ArrayList<ProductModel>();
            hlServices = new ArrayList<ProductModel>();
            sendRequest();
//            adapter = new ServicesRecyclerAdapter(activity, services);
            Log.e("onResume", "onResume");
        } else {
            Log.e("onResume else", "onResume else");
            progressLL.setVisibility(View.GONE);
            fagrid.setAdapter(new CategoryGridAdapter(activity, services));
            gegrid.setAdapter(new CategoryGridAdapter(activity, gadServices));
            hlgrid.setAdapter(new CategoryGridAdapter(activity, hlServices));
//            recyclerView.scrollToPosition(0);
        }
        super.onResume();
    }
    private void sendRequest() {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
            task = new AsyncHttpTask();
            task.execute(Utils.instantCategoryUrl);
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
            progressLL.setVisibility(View.VISIBLE);
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
                Log.d("hello", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

//            setProgressBarIndeterminateVisibility(false);
            progressLL.setVisibility(View.GONE);
            /* Download complete. Lets update UI */
            if (result == 1) {
                try {
                    Log.e("onPostExecute", "onPostExecute");
                    adapter = new CategoryGridAdapter(activity, services);
                    gadAdapter = new CategoryGridAdapter(activity, gadServices);
                    hlAdapter = new CategoryGridAdapter(activity, hlServices);
                    fagrid.setAdapter(adapter);
                    gegrid.setAdapter(gadAdapter);
                    hlgrid.setAdapter(hlAdapter);
                    adapter.notifyDataSetChanged();
                    gadAdapter.notifyDataSetChanged();
                    hlAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                Log.e("hello", "Failed to fetch data!");
            }
        }
    }
    private void parseResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
//            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);

                if (status==1) {
                    JSONArray jarr=jsonObject.optJSONArray("categories");
                    JSONObject jobCat=jarr.optJSONObject(0);
                    JSONObject jobstat=jobCat.getJSONObject(TagName.TAG_STATUS);
                    int status1 = jobstat.optInt(TagName.TAG_STATUS_CODE);
                    String message1 = jobstat.optString(TagName.TAG_MSG);
                    if(status1==1) {
                    JSONArray posts = jobCat.optJSONArray(TagName.TAG_PRODUCT_CATG);


                    for (int i = 0; i < posts.length(); i++) {

                        if(i==0) {
                            if (null == services) {
                                services = new ArrayList<ProductModel>();
                            }
                         JSONObject post = posts.optJSONObject(i);
//                            String fasTxt=post.optString("category_name");
                            faTxt.setText(post.getString(TagName.KEY_CAT_NAME));
                        JSONArray jArrySub=post.optJSONArray(TagName.KEY_SUB_CAT);
                        for (int j = 0; j < jArrySub.length(); j++) {
                                JSONObject job = jArrySub.optJSONObject(j);
                                ProductModel item = new ProductModel();
                                item.setId(job.optInt(TagName.KEY_ID));
                                item.setTitle(job.optString(TagName.KEY_NAME));
//                item.setDescription(post.optString("description"));
//                item.setCost(post.optInt("cost"));
//                item.setCount(post.optInt("count"));
                                Log.e("name", "name");
                                item.setThumbnail(job.optString(TagName.KEY_IMAGE_URL));
                                services.add(item);
                            }
                        }else{

                        }
                        if(i==1) {
                            if (null == gadServices) {
                                gadServices = new ArrayList<ProductModel>();
                            }
                            JSONObject post = posts.optJSONObject(i);
                            gadEleTxt.setText(post.getString(TagName.KEY_CAT_NAME));
                            JSONArray jArrySub=post.optJSONArray(TagName.KEY_SUB_CAT);
                            for (int j = 0; j < jArrySub.length(); j++) {
                                JSONObject job = jArrySub.optJSONObject(j);
                                ProductModel item = new ProductModel();
                                item.setId(job.optInt(TagName.KEY_ID));
                                item.setTitle(job.optString(TagName.KEY_NAME));
//                item.setDescription(post.optString("description"));
//                item.setCost(post.optInt("cost"));
//                item.setCount(post.optInt("count"));
                                Log.e("name", "name");
                                item.setThumbnail(job.optString(TagName.KEY_IMAGE_URL));
                                gadServices.add(item);
                            }
                        }else{

                        }
                        try {
                            if (i == 2) {
                                Log.e("i==2", "i==2");
                                if (null == hlServices) {
                                    hlServices = new ArrayList<ProductModel>();
                                }
                                JSONObject post = posts.optJSONObject(i);
                            homeLiveTxt.setText(post.getString(TagName.KEY_CAT_NAME));
                                JSONArray jArrySub = post.optJSONArray(TagName.KEY_SUB_CAT);
                                for (int j = 0; j < jArrySub.length(); j++) {
                                    JSONObject job = jArrySub.optJSONObject(j);
                                    ProductModel item = new ProductModel();
                                    item.setId(job.optInt(TagName.KEY_ID));
                                    item.setTitle(job.optString(TagName.KEY_NAME));
//                item.setDescription(post.optString("description"));
//                item.setCost(post.optInt("cost"));
//                item.setCount(post.optInt("count"));
                                    Log.e("name", "name");
                                    item.setThumbnail(job.optString(TagName.KEY_IMAGE_URL));
                                    hlServices.add(item);
                                }
                            } else {
                                Toast.makeText(activity, "no products in hl", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){

                        }
                    }
                    }else{
                        Toast.makeText(activity, "No Products", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
