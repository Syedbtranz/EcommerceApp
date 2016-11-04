package com.btranz.ecommerceapp.utils;

import com.btranz.ecommerceapp.modal.ProductModel;

import java.util.Comparator;

/**
 * Created by all on 10/14/2016.
 */

/*public class NewArrivalSorter {
}*/
public class NewArrivalSorter implements Comparator<ProductModel> {

    private String getter;

    public NewArrivalSorter(String field) {
        this.getter = field;
    }

    public int compare(ProductModel o1, ProductModel o2) {
        if(o1.getTag().compareTo(o2.getTag())==0)
            return -1*o1.getCost().compareTo(o2.getCost());
        else if(o1.getTag().equalsIgnoreCase(getter))
        return -1;
        else if(o2.getTag().equalsIgnoreCase(getter))
        return 1;
        else
        return  o1.getTag().compareTo(o2.getTag());
    }

}
