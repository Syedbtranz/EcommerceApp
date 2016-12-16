package com.btranz.ecommerceapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.adapter.ProductGridAdapter;
import com.btranz.ecommerceapp.fragment.ProductsFragment;
import com.btranz.ecommerceapp.modal.Post;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CheckNetworkConnection;
import com.btranz.ecommerceapp.utils.DatabaseHandler;
import com.btranz.ecommerceapp.utils.TagName;
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
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {
    public Toolbar toolbar;
    private Dialog loadingDialog;
    TextView clearHistory;
    SearchView searchView;
    private ListView recentSearchListView,searchingListview;
    private MyAppAdapter myAppAdapter,seachingAdapter;
    private ArrayList<Post> recentSearchList, searchingList;
    ArrayList<ProductModel> services;
    AsyncHttpTask task;
    AlertDialog alertDialog;
    String message,searchQuery;
    List<String> searchList= new ArrayList<String>();
    List<String> checkList= new ArrayList<String>();
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DatabaseHandler(getApplicationContext());
        setContentView(R.layout.activity_search);
        recentSearchListView= (ListView) findViewById(R.id.recent_search_listView);
        recentSearchList=new ArrayList<>();

        try {
            getSearchDbList();
        }catch (Exception e){

        }
        searchingList();
//        sendRequest();
        initToolbar();
        myAppAdapter=new MyAppAdapter(recentSearchList,SearchActivity.this,R.layout.recent_search_inflate);
        recentSearchListView.setAdapter(myAppAdapter);
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_layout, null, false);
        recentSearchListView.addHeaderView(footerView);
        clearHistory= (TextView)footerView.findViewById(R.id.clear_history);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recentSearchList.clear();
                db.removeSearchList();
                myAppAdapter.notifyDataSetChanged();
            }
        });
        recentSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String qry= recentSearchList.get(position-1).getPostTitle();
                if(db.hasSearchObject(qry)){

//                    Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
                    if(db.deleteSearchTitle(qry)){
                        db.insertSearchItem(qry);
                    }
                }else{
                    db.insertSearchItem(qry);
//                    Toast.makeText(getApplicationContext(), "Item Not Added!", Toast.LENGTH_SHORT).show();
                }
//                try {
//                    checkList = db.checkSearchList(qry);
//                    System.out.println(checkList.size());
//                    Log.e("sss",""+checkList.size());
//                    if(checkList.size()<1){
//                        Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
//                        db.insertSearchItem(qry);
//
//                    }else{
//                        Toast.makeText(getApplicationContext(), "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    db.insertSearchItem(qry);
//                }

               sendRequest(qry);
            }
        });
    }
    public void searchingList(){
        searchingListview= (ListView) findViewById(R.id.searching_listView);
        searchingList=new ArrayList<>();
        searchingList.add(new Post("mobile", "in mobile"));
        searchingList.add(new Post("computer", "All category"));
        searchingList.add(new Post("refrizirator", "in Electornics"));
        searchingList.add(new Post("shoes", "in shoes"));
        searchingList.add(new Post("sandels", "in sandales"));
        searchingList.add(new Post("loafers", "in sandales"));
        searchingList.add(new Post("bottle", "in home needs"));
        searchingList.add(new Post("cooker", "in home needs"));
        seachingAdapter=new MyAppAdapter(searchingList,SearchActivity.this,R.layout.searching_inflate);
        searchingListview.setAdapter(seachingAdapter);
//        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_layout, null, false);
//        searchingListview.addHeaderView(footerView);
        searchingListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String qry= searchingList.get(position).getPostTitle();
                if(db.hasSearchObject(qry)){
//                    db.getUpdatesSearchList(qry);
//                    Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
                    if(db.deleteSearchTitle(qry)){
                        db.insertSearchItem(qry);
                    }
                }else{
                    db.insertSearchItem(qry);
//                    Toast.makeText(getApplicationContext(), "Item Not Added!", Toast.LENGTH_SHORT).show();
                }
//                try {
//                    checkList = db.checkSearchList(qry);
//                    System.out.println(checkList.size());
//                    Log.e("sss",""+checkList.size());
//                    if(checkList.size()<1){
//                        Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
//                        db.insertSearchItem(qry);
//
//                    }else{
//                        Toast.makeText(getApplicationContext(), "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    db.insertSearchItem(qry);
//                }
              sendRequest(qry);
            }
        });
    }
public void getSearchDbList(){
    searchList=db.getSearchList();
    for(int i=0;i<searchList.size();i++){
        String searchListStr=searchList.get(i);
//        String cartListArr[]=cartListStr.split(Pattern.quote("***"));
        recentSearchList.add(new Post(searchListStr, ""));
//        Post item = new Post();
//        item.setId(Integer.valueOf(cartListArr[0]));
//        item.setTitle(cartListArr[1]);
//        item.setCost(Double.valueOf(cartListArr[2]));
//        item.setFinalPrice(Double.valueOf(cartListArr[3]));
//        item.setThumbnail(cartListArr[4]);
//        item.setCount(Integer.valueOf(cartListArr[5]));
////            Log.e("name","name");
//
//        services.add(item);


    }
}
    public class MyAppAdapter extends BaseAdapter {

        public class ViewHolder {
            TextView txtTitle,txtSubTitle;
            ImageView searchArrow;


        }

        public List<Post> searchAdpterList;

        public Context context;
        int layout_inflate;
        ArrayList<Post> arraylist;

        private MyAppAdapter(List<Post> apps, Context context, int layout_inflate) {
            this.searchAdpterList = apps;
            this.context = context;
            this.layout_inflate=layout_inflate;
            arraylist = new ArrayList<Post>();
            arraylist.addAll(searchAdpterList);

        }

        @Override
        public int getCount() {
            return searchAdpterList.size();
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

            View rowView = convertView;
            ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(layout_inflate, null);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.title);
                viewHolder.txtSubTitle = (TextView) rowView.findViewById(R.id.subtitle);
                viewHolder.searchArrow = (ImageView) rowView.findViewById(R.id.search_up_arrow);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtTitle.setText(searchAdpterList.get(position).getPostTitle() + "");
            viewHolder.txtSubTitle.setText(searchAdpterList.get(position).getPostSubTitle() + "");
            viewHolder.searchArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    addtosearchText(searchAdpterList.get(position).getPostTitle());
                    searchView.setQuery(searchAdpterList.get(position).getPostTitle(),false);

                }
            });
            return rowView;


        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            searchAdpterList.clear();
            if (charText.length() == 0) {
                searchAdpterList.addAll(arraylist);

            } else {
                for (Post postDetail : arraylist) {
                    if (charText.length() != 0 && postDetail.getPostTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                        searchAdpterList.add(postDetail);
                    }

                    else if (charText.length() != 0 && postDetail.getPostSubTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                        searchAdpterList.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
//    public void addtosearchText(String str){
//        searchView.setQuery(str,false);
//    }
    private void sendRequest(String query) {
        searchQuery=query;
    if (CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
//            task = new RequestImgTask(activity);
//            task.execute(url);
        task = new AsyncHttpTask();
//            task.execute(Utils.productsUrl);
        task.execute(Utils.searchUrl+searchQuery);
//            task.execute(Utils.instantServerUrl);
        Log.e("sendrequest","sendrequest");
    } else {
        message = getResources().getString(R.string.no_internet_connection);
        showAlertDialog(message, true);
    }
}
    public void showAlertDialog(String message, final boolean finish) {
        alertDialog = new AlertDialog.Builder(SearchActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            finish();
                    }
                });
        alertDialog.show();
    }
    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_title =(TextView)toolbar.findViewById(R.id.toolbar_txt);
//        toolbar.setLogo(R.mipmap.logo_icon);
//        toolbar.setTitle(;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);
//        searchView.clearFocus();
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(db.hasSearchObject(query)){
//                    Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
                    if(db.deleteSearchTitle(query)){
                        db.insertSearchItem(query);
                    }
                }else{
                    db.insertSearchItem(query);
//                    Toast.makeText(getApplicationContext(), "Item Not Added!", Toast.LENGTH_SHORT).show();
                }

//                try {
//                    checkList = db.checkSearchList(query);
//                    System.out.println(checkList.size());
//                    Log.e("sss",""+checkList.size());
//                    if(checkList.size()<1){
//                    Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
//                    db.insertSearchItem(query);
//
//                }else{
//                    Toast.makeText(getApplicationContext(), "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
//                }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    db.insertSearchItem(query);
//                }
//                if(checkList.size()<1){
//                    Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
//                    db.insertSearchItem(query);
//
//                }else{
//                    Toast.makeText(getApplicationContext(), "This Product is already Added please check in CART!", Toast.LENGTH_SHORT).show();
//                }
                sendRequest(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                if(!searchQuery.equals("")) {
                    recentSearchListView.setVisibility(View.GONE);
                    searchingListview.setVisibility(View.VISIBLE);
                }else{
                    searchingListview.setVisibility(View.GONE);
                    recentSearchListView.setVisibility(View.VISIBLE);
                }

                seachingAdapter.filter(searchQuery.toString().trim());
                searchingListview.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                finish();
//                searchView.setIconified(false);
//                searchView.clearFocus();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
            loadingDialog = ProgressDialog.show(SearchActivity.this, "", "Loading...");
            try {
//                progressLL.setVisibility(View.VISIBLE);
//                pb.setVisibility(View.VISIBLE);
            }catch(Exception e){

            }

        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url(params[0])
//                        .build();

            try {
//                    Response response = client.newCall(request).execute();
//                    String jsonString = response.body().string();
//                    Log.e("NGVL", jsonString);

//                 forming th java.net.URL object
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

//                 for Get request
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();
//                int statusCode = response.code();
//                    Log.e("response.code", ""+response.hashCode());
//                 200 represents HTTP OK
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
            loadingDialog.dismiss();
//            progressLL.setVisibility(View.GONE);
//            pb.setVisibility(View.GONE);
//             Download complete. Lets update UI
            if (result == 1) {
                Log.e("onPostExecute", "onPostExecute");
//                if(services!=null&&services.size()!=0) {
//                    adapter = new ProductGridAdapter(activity, services);
//                    ProductsGrid.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    progressLL.setVisibility(View.GONE);
////                pb.setVisibility(View.GONE);
//
////                recyclerView.setAdapter(new ServicesRecyclerAdapter(activity, services));
//                }else {
//                    message = getResources().getString(R.string.no_products);
//                    showAlertDialog(message, true);
//                }
                finish();
                Intent in=new Intent(SearchActivity.this,SecondActivity.class);
                in.putExtra("key", TagName.FRAGMENT_PRODUCTS);
                in.putExtra("prdtsUrl", Utils.searchUrl);
                in.putExtra("prdtsTitle", "Products for "+searchQuery);
                in.putParcelableArrayListExtra("productList",services);
                Log.e("prdtsUrl",Utils.searchUrl+searchQuery);
                startActivity(in);
                SearchActivity.this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            } else {
                Log.e("hello", "Failed to fetch data!");
//                pb.setVisibility(View.GONE);
////                message = getResources().getString(R.string.no_products);
                showAlertDialog("No Products Found", true);
//                Toast.makeText(activity,"No Prodructs Found",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /* private void parseResult(String result) {
         try {
 //            JSONArray response = new JSONArray(result);
             JSONObject jsonObject=new JSONObject(result);

             if (jsonObject != null) {
                 JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                 int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
 //                message = jsonObject.optString(TagName.TAG_MSG);

 //               if (message.equals("success")) {
 //            boolean status = response.getBoolean(TagName.TAG_STATUS);

                 if (status!=0) {
 //                JSONObject jsonData = jsonObject
 //                        .getJSONObject(TagName.TAG_PRODUCT);
 //                    }
                     JSONArray posts = jsonObject.optJSONArray(TagName.TAG_PRODUCT);

             *//*Initialize array if null*//*
                    if (null == services) {
                        services = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        ProductModel item = new ProductModel();
                        item.setId(post.optInt(TagName.KEY_ID));
                        item.setTitle(post.optString(TagName.KEY_NAME));
                        item.setDescription(post.optString(TagName.KEY_DES));
                        item.setCost(post.optDouble(TagName.KEY_PRICE));
                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                    Log.e("name", "name"+ post.optDouble("finalPrice"));
                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item.setShare(post1.optString(TagName.KEY_SHARE));
                        item.setTag(post1.optString(TagName.KEY_TAG));
                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item.setRating(post1.optInt(TagName.KEY_RATING));
                        services.add(item);
                    }
                } else {
                    message = jsonObject.optString(TagName.TAG_MSG);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            JSONObject jsonObject=response.getJSONObject(0);

            if (jsonObject != null) {
                JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);

                if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);

//                    if (status) {
//                JSONObject jsonData = jsonObject
//                        .getJSONObject(TagName.TAG_PRODUCT);
//                    }
                    JSONArray posts = jsonObject.optJSONArray(TagName.TAG_PRODUCT);

//            Initialize array if null
                    if (null == services) {
                        services = new ArrayList<ProductModel>();
                    }

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);

                        ProductModel item = new ProductModel();
                        item.setId(post.optInt(TagName.KEY_ID));
                        item.setTitle(post.optString(TagName.KEY_NAME));
                        item.setDescription(post.optString(TagName.KEY_DES));
                        item.setCost(post.optDouble(TagName.KEY_PRICE));
                        item.setFinalPrice(post.optDouble(TagName.KEY_FINAL_PRICE));
//                    item.setCount(post.optInt("finalPrice"));
//                    Log.e("name", "name");
                        item.setThumbnail(post.optString(TagName.KEY_THUMB));
                        JSONObject post1 = post.optJSONObject(TagName.TAG_OFFER_ALL);
                        item.setShare(post1.optString(TagName.KEY_SHARE));
                        item.setTag(post1.optString(TagName.KEY_TAG));
                        item.setDiscount(post1.optInt(TagName.KEY_DISC));
                        item.setRating(post1.optInt(TagName.KEY_RATING));
                        services.add(item);
                    }
                } else {
                    message = jsonObject.getString(TagName.TAG_PRODUCT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
