package com.btranz.ecommerceapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CheckNetworkConnection {
	static int timeout=1500;
	public static boolean isConnectionAvailable(Context context)  {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()
					&& netInfo.isConnectedOrConnecting()
					&& netInfo.isAvailable()) {

//				try {
//					if (InetAddress.getByName("www.btranz.com").isReachable(timeout))
//                    {
//                        // host reachable
//						Log.e("host reachable","host reachable");
//						return true;
//                    }
//                    else
//                    {
//                        Log.e("host not reachable","host not reachable");
//						return false;
//                        // host not reachable
//                    }
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
                Log.e("net is connected","net is connected");
                return true;
			}
		}
        Log.e("net","net ");
		return false;
	}
}

//	public void checkOnlineState() {
//		ConnectivityManager CManager =
//				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo NInfo = CManager.getActiveNetworkInfo();
//		if (NInfo != null && NInfo.isConnectedOrConnecting()) {
//			if (InetAddress.getByName("www.xy.com").isReachable(timeout))
//			{
//				// host reachable
//			}
//			else
//			{
//				// host not reachable
//			}
//		}
//		return;
//	}

