package com.btranz.ecommerceapp.adapter;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.CircleCheckBox;

/**
 * Created by all on 10/6/2016.
 */


import java.util.ArrayList;
        import java.util.List;

        import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.TextView;

public class FilterBrandAdapter extends ArrayAdapter<ProductModel> implements Filterable {

    private List<ProductModel> productList;
//    private Context context;
    private FragmentActivity context;
    private Filter planetFilter;
    private List<ProductModel> origProductList;

    public FilterBrandAdapter(List<ProductModel> productList, FragmentActivity ctx) {
        super(ctx, R.layout.filter_brandlist_inflate, productList);
        this.productList = productList;
        this.context = ctx;
        this.origProductList = productList;
    }

    public int getCount() {
        return productList.size();
    }

    public ProductModel getItem(int position) {
        return productList.get(position);
    }

    public long getItemId(int position) {
        return productList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ProductHolder holder = new ProductHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.filter_brandlist_inflate, null);
            // Now we can fill the layout with the right values
            TextView tv = (TextView) v.findViewById(R.id.filter_brand_name);
//            TextView distView = (TextView) v.findViewById(R.id.dist);
//            holder.checkBox =(CircleCheckBox)v.findViewById(R.id.chbx);

            holder.productHolderNameView = tv;
//            holder.distView = distView;

            v.setTag(holder);
        }
        else
            holder = (ProductHolder) v.getTag();

        ProductModel p = productList.get(position);
        holder.productHolderNameView.setText(p.getTitle());
//        holder.distView.setText("" + p.getDistance());
//        holder.checkBox.setOnCheckedChangeListener(new CircleCheckBox.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CircleCheckBox view, boolean isChecked) {
//                System.out.println("CHECK "+isChecked);
//            }
//        });

        return v;
    }

    public void resetData() {
        productList = origProductList;
    }


	/* *********************************
	 * We use the holder pattern
	 * It makes the view faster and avoid finding the component
	 * **********************************/

    private static class ProductHolder {
        public TextView productHolderNameView;
        public TextView distView;
        CircleCheckBox checkBox;
    }



	/*
	 * We create our filter
	 */

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }



    private class PlanetFilter extends Filter {



        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origProductList;
                results.count = origProductList.size();
            }
            else {
                // We perform filtering operation
                List<ProductModel> nPlanetList = new ArrayList<ProductModel>();

                for (ProductModel p : productList) {
                    if (p.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                productList = (List<ProductModel>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
