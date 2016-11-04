package com.btranz.ecommerceapp.utils;

import com.btranz.ecommerceapp.modal.ProductModel;

import java.util.Comparator;

/**
 * Created by all on 9/27/2016.
 */
public class NameSorter implements Comparator<ProductModel> {

    public int compare(ProductModel one, ProductModel another){
        return one.getTitle().compareTo(another.getTitle());
    }

}
