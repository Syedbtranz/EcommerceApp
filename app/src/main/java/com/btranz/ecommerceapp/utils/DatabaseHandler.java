package com.btranz.ecommerceapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "cart_db";

	// Exhibitors table name
	private static final String TABLE_CART = "cart";
	private static final String TABLE_WISHLIST = "wishlist";
	private static final String TABLE_SEARCH = "search_autofill";



	//  Tables Columns Id
	private static final String ID = "c_id";
	private static final String S_ID = "s_id";
	private static final String W_ID = "w_id";
	private static final String S_NAME = "s_name";
	private static final String S_TAG = "s_tag";
	List<String> searchList=new ArrayList<String>();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Category table create query
		String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
				+ ID + " INTEGER PRIMARY KEY," + TagName.KEY_ID +" TEXT," + TagName.KEY_NAME + " TEXT," + TagName.KEY_PRICE + " TEXT,"+
				TagName.KEY_FINAL_PRICE + " TEXT,"+
				TagName.KEY_THUMB + " TEXT," +
				TagName.KEY_COUNT + " TEXT,"+ TagName.KEY_SHARE + " TEXT,"+
				TagName.KEY_TAG + " TEXT,"+
				TagName.KEY_DISC + " TEXT," +
				TagName.KEY_RATING + " TEXT)";

		String CREATE_WISHLIST_TABLE = "CREATE TABLE " + TABLE_WISHLIST + "("
				+ W_ID + " INTEGER PRIMARY KEY," + TagName.KEY_ID +" TEXT," + TagName.KEY_NAME + " TEXT," + TagName.KEY_PRICE + " TEXT,"+
				TagName.KEY_FINAL_PRICE + " TEXT,"+
				TagName.KEY_THUMB +  " TEXT,"+
				TagName.KEY_TAG + " TEXT,"+
				TagName.KEY_DISC + " TEXT," +
				TagName.KEY_RATING + " TEXT)";

		String CREATE_SEARCH_TABLE = "CREATE TABLE " + TABLE_SEARCH + "("
				+ S_ID + " INTEGER PRIMARY KEY," + S_NAME + " TEXT," + S_TAG + " TEXT)";


		db.execSQL(CREATE_CART_TABLE);
		db.execSQL(CREATE_WISHLIST_TABLE);
		db.execSQL(CREATE_SEARCH_TABLE);



	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
//
		onCreate(db);
	}

	//********* INTETRTING VALUES IN TO CART TABLE******************************//

	public void insertCart(String prdt_id,String prdt_name,String prdt_price,String prdt_final_price, String prdt_thumb,String prdt_count,String share, String tag,String discount,String rating) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TagName.KEY_ID, prdt_id);
		values.put(TagName.KEY_NAME , prdt_name);
		values.put(TagName.KEY_PRICE, prdt_price);
		values.put(TagName.KEY_FINAL_PRICE, prdt_final_price);
		values.put(TagName.KEY_THUMB , prdt_thumb);
		values.put(TagName.KEY_COUNT , prdt_count);
		values.put(TagName.KEY_SHARE , share);
		values.put(TagName.KEY_TAG , tag);
		values.put(TagName.KEY_DISC , discount);
		values.put(TagName.KEY_RATING , rating);

		// Inserting Row
		db.insert(TABLE_CART, null, values);

		db.close(); // Closing database connection
	}


public List<String>getCartList(){

	List<String> cartList=new ArrayList<String>();
	String selectQuery = "SELECT * FROM "+TABLE_CART;
	SQLiteDatabase db=this.getReadableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,null);
	if (cursor.moveToFirst()) {
		do {

			cartList.add(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6)+"***"+cursor.getString(7)+"***"+cursor.getString(8)+"***"+cursor.getString(9)+"***"+cursor.getString(10));
//		System.out.println(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6)+"***"+cursor.getString(7)+"***"+cursor.getString(8)+"***"+cursor.getString(9)+"***"+cursor.getString(10));
		} while (cursor.moveToNext());
	}
	// closing connection
	cursor.close();
	db.close();

	// returning lables
	return cartList;

}

	public void removeCartItem(String id) {
		// Select All Query
		String deleteQuery = "DELETE  FROM " + TABLE_CART+ " Where "+TagName.KEY_ID+"="+id;
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL(deleteQuery);
	}



	public void removeCartList() {
		// Select All Query
		String deleteQuery = "DELETE  FROM " + TABLE_CART;
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL(deleteQuery);
	}

	public void updateCartItem(String id,String count){
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE "+ TABLE_CART+ " SET "+TagName.KEY_COUNT+" = "+count+ " WHERE "+TagName.KEY_ID+"="+id;

		db.execSQL(strSQL);
       /* SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_P_STUDENT_STATUS,"1");
        db.update(TABLE_STUDENTS_DATA, values,P_P_STUDENT_STATUS + " = ? ",new String[]{ String.valueOf(id) });*/
	}

	public List<String> checkProduct(String id){

		List<String> cartData=new ArrayList<String>();

		String selectQuery = "SELECT * FROM "+TABLE_CART +" WHERE "+TagName.KEY_ID+"="+id;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if (cursor.moveToFirst()) {
			do {

				cartData.add(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6)+"***"+cursor.getString(7)+"***"+cursor.getString(8)+"***"+cursor.getString(9)+"***"+cursor.getString(10));

			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();

		// returning studentData
		return cartData;


	}

	//********* INTETRTING VALUES IN TO CART TABLE******************************//

	public void insertWishlist(String prdt_id,String prdt_name,String prdt_price,String prdt_final_price, String prdt_thumb,String tag, String discount, String rating) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TagName.KEY_ID, prdt_id);
		values.put(TagName.KEY_NAME , prdt_name);
		values.put(TagName.KEY_PRICE, prdt_price);
		values.put(TagName.KEY_FINAL_PRICE, prdt_final_price);
		values.put(TagName.KEY_THUMB , prdt_thumb);
		values.put(TagName.KEY_TAG , tag);
		values.put(TagName.KEY_DISC , discount);
		values.put(TagName.KEY_RATING , rating);
		System.out.println("prdt_id" +prdt_id);
		// Inserting Row
		db.insert(TABLE_WISHLIST, null, values);

		db.close(); // Closing database connection
	}


	public List<String>getWishlist(){

		List<String> wishList=new ArrayList<String>();
		String selectQuery = "SELECT * FROM "+TABLE_WISHLIST;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if (cursor.moveToFirst()) {
			do {

				wishList.add(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6)+"***"+cursor.getString(7)+"***"+cursor.getString(8));
		System.out.println(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6)+"***"+cursor.getString(7)+"***"+cursor.getString(8));
			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return wishList;


	}
	public void removeWishlistItem(String id) {
		// Select All Query
		String deleteQuery = "DELETE  FROM " + TABLE_WISHLIST+ " Where "+TagName.KEY_ID+"="+id;
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL(deleteQuery);
	}



	public void removeWishlist() {
		// Select All Query
		String deleteQuery = "DELETE  FROM " + TABLE_WISHLIST;
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL(deleteQuery);
	}

	public void updateWishlistItem(String id,String count){
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE "+ TABLE_WISHLIST+ " SET "+TagName.KEY_COUNT+" = "+count+ " WHERE "+TagName.KEY_ID+"="+id;

		db.execSQL(strSQL);
       /* SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_P_STUDENT_STATUS,"1");
        db.update(TABLE_STUDENTS_DATA, values,P_P_STUDENT_STATUS + " = ? ",new String[]{ String.valueOf(id) });*/
	}

	public List<String> checkWishlistProduct(String id){

		List<String> wishListData=new ArrayList<String>();

		String selectQuery = "SELECT * FROM "+TABLE_CART +" WHERE "+TagName.KEY_ID+"="+id;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if (cursor.moveToFirst()) {
			do {

				wishListData.add(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+"***"+cursor.getString(6)+"***"+cursor.getString(7)+"***"+cursor.getString(8));

			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();

		// returning studentData
		return wishListData;


	}

	//********* INTETRTING VALUES IN TO SEARCH TABLE******************************//

	public void insertSearchItem(String prdt_name,String tag) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
//		values.put(TagName.KEY_ID, prdt_id);
		values.put(S_NAME , prdt_name);
		values.put(S_TAG , tag);
//		values.put(TagName.KEY_PRICE, prdt_price);
//		values.put(TagName.KEY_FINAL_PRICE, prdt_final_price);
//		values.put(TagName.KEY_THUMB , prdt_thumb);
//		values.put(TagName.KEY_COUNT , prdt_count);

		// Inserting Row
		db.insert(TABLE_SEARCH, null, values);

		db.close(); // Closing database connection
	}

	public List<String>getSearchList(){

//		List<String> searchList=new ArrayList<String>();
		String selectQuery = "SELECT * FROM "+TABLE_SEARCH+" order by "+S_ID+" DESC";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if (cursor.moveToFirst()) {
			do {

				searchList.add(cursor.getString(1)+"***"+cursor.getString(2));
//		System.out.println(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6));
			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return searchList;


	}
	public void removeSearchList() {
		// Select All Query
		String deleteQuery = "DELETE  FROM " + TABLE_SEARCH;
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL(deleteQuery);
	}
//	public List<String> checkSearchList(String name){
//
//		List<String> searchData=new ArrayList<String>();
//		String selectQuery = "SELECT * FROM "+TABLE_SEARCH+" where "+S_NAME+"="+name;
////		String selectQuery = "SELECT * FROM "+TABLE_CART +" WHERE "+TagName.KEY_ID+"="+id;
//			SQLiteDatabase db = this.getReadableDatabase();
//			Cursor cursor = db.rawQuery(selectQuery, null);
//			if (cursor.moveToFirst()) {
//				do {
//
//					searchData.add(cursor.getString(1));
//
//				} while (cursor.moveToNext());
//			}
//			// closing connection
//			cursor.close();
//			db.close();
//
//
//		// returning studentData
//		return searchData;
//
//
//	}
	public boolean hasSearchObject(String name) {
		SQLiteDatabase db = getWritableDatabase();
		String selectString = "SELECT * FROM " + TABLE_SEARCH + " WHERE " + S_NAME + " =?";

		// Add the String you are searching by here.
		// Put it in an array to avoid an unrecognized token error
		Cursor cursor = db.rawQuery(selectString, new String[] {name});

		boolean hasObject = false;
		if(cursor.moveToFirst()){
			hasObject = true;

			//region if you had multiple records to check for, use this region.

			int count = 0;
			while(cursor.moveToNext()){
				count++;
			}
			//here, count is records found
			Log.e("TAG", String.format("%d records found", count));

			//endregion

		}

		cursor.close();          // Dont forget to close your cursor
		db.close();              //AND your Database!
		return hasObject;
	}
	//---deletes a particular title---
	public boolean deleteSearchTitle(String name)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_SEARCH, S_NAME + " =?", new String[]{name}) > 0;
	}

}
