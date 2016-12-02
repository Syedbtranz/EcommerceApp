package com.btranz.ecommerceapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHandler extends SQLiteOpenHelper {
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "cart_db";

	// OrdersList table name
	private static final String TABLE_ORDERLIST= "order_list";
	//  Tables Columns Id
	private static final String O_ID ="id";
	private static final String O_ORID ="o_orid";
	private static final String O_TITLE ="o_title";
	private static final String O_COST ="o_cost";
	private static final String O_COUNT ="o_count";
	private static final String O_THUMBNAIL ="o_thumbnail";
	private static final String O_INC_ID ="o_inc_id";



	public OrderDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Category table create query
		String CREATE_ORD_TABLE = "CREATE TABLE " + TABLE_ORDERLIST + "("
				+ O_ID + " INTEGER PRIMARY KEY,"
				+ O_ORID +" TEXT,"
				+ O_TITLE + " TEXT,"
				+ O_COST + " TEXT,"
				+ O_COUNT + " TEXT,"
				+ O_THUMBNAIL + " TEXT,"
				+ O_INC_ID + " TEXT)";

		db.execSQL(CREATE_ORD_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERLIST);

		onCreate(db);
 	}

	//********* INSERTING VALUES IN TO ORDER TABLE******************************//

	public void insertOrderlist(String or_id,String title,String cost,String count, String thumbnail,String inc_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(O_ORID, or_id);
		values.put(O_TITLE , title);
		values.put(O_COST, cost);
		values.put(O_COUNT, count);
		values.put(O_THUMBNAIL , thumbnail);
		values.put(O_INC_ID, inc_id);
		// Inserting Row
		db.insert(TABLE_ORDERLIST, null, values);
Log.i("inserted",title);    
		db.close(); // Closing database connection
	}


public List<String>getOrderList(){

	List<String> cartList=new ArrayList<String>();
	String selectQuery = "SELECT * FROM "+TABLE_ORDERLIST;
	SQLiteDatabase db=this.getReadableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,null);
	if (cursor.moveToFirst()) {
		do {

			cartList.add(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6));
//		System.out.println(cursor.getString(1)+"***"+cursor.getString(2)+"***"+cursor.getString(3)+"***"+cursor.getString(4)+"***"+cursor.getString(5)+"***"+cursor.getString(6));
		} while (cursor.moveToNext());
	}
	// closing connection
	cursor.close();
	db.close();

	// returning lables
	return cartList;


}

	public void removeOrderList() {
		// Select All Query
		String deleteQuery = "DELETE  FROM " + TABLE_ORDERLIST;
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL(deleteQuery);
	}



}
