package com.btranz.ecommerceapp.json;

import com.btranz.ecommerceapp.modal.Product;
import com.btranz.ecommerceapp.modal.ProductModel;
import com.btranz.ecommerceapp.utils.TagName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {

	public static List<ProductModel> getHome(JSONObject jsonObject)
			throws JSONException {
		List<ProductModel> products = new ArrayList<ProductModel>();

		JSONArray jsonArray = jsonObject.getJSONArray(TagName.TAG_OFFER_BANNER);
		ProductModel product;
		for (int i = 0; i < jsonArray.length(); i++) {
			product = new ProductModel();
			JSONObject productObj = jsonArray.getJSONObject(i);
			product.setId(productObj.getInt(TagName.KEY_ID));
//			product.setName(productObj.getString(TagName.KEY_NAME));
			product.setThumbnail(productObj.getString(TagName.KEY_IMG_URL));

			products.add(product);
		}
		return products;
	}
}
