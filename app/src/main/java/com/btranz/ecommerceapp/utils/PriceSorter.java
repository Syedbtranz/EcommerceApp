package com.btranz.ecommerceapp.utils;

import com.btranz.ecommerceapp.modal.ProductModel;

import java.util.Comparator;

/**
 * Created by all on 9/27/2016.
 */
public class PriceSorter implements Comparator<ProductModel> {

public int compare(ProductModel one, ProductModel another){
        int returnVal = 0;

        if(one.getFinalPrice() < another.getFinalPrice()){
        returnVal =  -1;
        }else if(one.getFinalPrice() > another.getFinalPrice()){
        returnVal =  1;
        }else if(one.getFinalPrice() == another.getFinalPrice()){
        returnVal =  0;
        }
        return returnVal;

        }
}
