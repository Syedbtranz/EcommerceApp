package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.BookNowActivity;
import com.btranz.ecommerceapp.utils.RegisterUserClass;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SignUpFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = SignUpFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private ProgressDialog mProgressDialog;
    ImageView closeBtn;
    EditText nameEt,emailET,pswET,forgetPswEt;
    String name,email,password;
    Button signUpBtn;
    FragmentActivity activity;
    // shared prefernce
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    //Facebook Integration
    LoginButton facebook;
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    //google integration
    LinearLayout googleBtn;
    private GoogleApiClient mGoogleApiClient;
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .enableAutoManage(activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        FacebookSdk.sdkInitialize(activity.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                Toast.makeText(activity.getApplicationContext(),
//                        newProfile.getId(), Toast.LENGTH_SHORT).show();

//                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        findViewById(rootView);
        // Inflate the layout for this fragment
        return rootView;
    }
    private void findViewById(View view) {
        closeBtn=(ImageView) view.findViewById(R.id.signup_close_btn) ;
        nameEt=(EditText)view.findViewById(R.id.signup_name_et);
        emailET=(EditText)view.findViewById(R.id.signup_email_et);
        pswET=(EditText)view.findViewById(R.id.signup_psw_et);
        googleBtn=(LinearLayout)view.findViewById(R.id.google_btn);
        signUpBtn=(Button)view.findViewById(R.id.signup_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        facebook = (LoginButton) view.findViewById(R.id.login_button);
        facebook.setReadPermissions("user_friends");
        // If using in a fragment
        facebook.setFragment(this);
//          facebook = (LinearLayout) view.findViewById(R.id.login_button);
//        facebook.setReadPermissions("email");
//        // If using in a fragment
//        loginButton.setFragment(this);
        // Callback registration
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                Toast.makeText(activity.getApplicationContext(),
                        profile.getId(), Toast.LENGTH_SHORT).show();
                Intent in=new Intent(getActivity(), BookNowActivity.class);
                in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
                activity.startActivity(in);
                activity.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               googleSignIn();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameEt.getText().toString();
                email = emailET.getText().toString();
                password = pswET.getText().toString();
                sendData();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

//            txtName.setText(personName);
//            txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);

//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        this.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    public void sendData(){
        if (name.equals("") &&email.equals("") && password.equals("")) {

            Toast.makeText(activity,
                    "Please Enter the Details", Toast.LENGTH_SHORT).show();
            nameEt.requestFocus();
        }  else if (name.equals("")) {
            Toast.makeText(activity,
                    "Please Enter the Name", Toast.LENGTH_SHORT).show();
            nameEt.requestFocus();
        } else if (email.equals("")) {
            Toast.makeText(activity,
                    "Please Enter the Email/Mobile No", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
        } else if (!email.matches(Utils.EMAIL_PATTERN)&&email.length()!=10){
            Toast.makeText(activity,
                    "Please Enter the currect Email/Mobile No",
                    Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(activity,
                    "Please Enter the Password", Toast.LENGTH_SHORT).show();
            pswET.requestFocus();
        } else if (password.length() < 6) {
            Toast.makeText(activity,
                    "Please Enter atleast 6 digits or characters",
                    Toast.LENGTH_SHORT).show();
            pswET.requestFocus();
        } else if (email.matches(Utils.EMAIL_PATTERN)||email.length()==10
                && password.length() > 5) {
            register(email,password,name);
//            Toast.makeText(activity,
//                    "Success",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    private void register( String email, String password,String name) {
//        class RegisterUser extends AsyncTask<String, Void, String> {
//            ProgressDialog loading;
//            RegisterUserClass ruc = new RegisterUserClass();
//
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(activity, "please wait...",null, true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(activity,s,Toast.LENGTH_LONG).show();
//                Log.e("s",s);
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                HashMap<String, String> data = new HashMap<String,String>();
//                data.put("email",params[0]);
//                data.put("password",params[1]);
//                data.put("name",params[2]);
//                String result = ruc.sendPostRequest(Utils.regUrl+params[0]+"/"+params[1]+"/"+params[2]);
//
//                return  result;
//            }
//        }
        class RegisterUser extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(activity, "", "Please wait...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uemail = params[0];
                String pass = params[1];
                String uname = params[2];
//                Log.e("uname",uname);
//                Log.e("pass",pass);
                InputStream is = null;
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", uname));
//                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(Utils.regUrl+uemail+"/"+pass+"/"+uname);
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                Log.e("s",s);
                loadingDialog.dismiss();
                try {
                    JSONArray response = new JSONArray(s);
                    JSONObject jsonObject=response.getJSONObject(0);

                    if (jsonObject != null) {
                        JSONObject jobstatus=jsonObject.getJSONObject(TagName.TAG_STATUS);
                        int status = jobstatus.optInt(TagName.TAG_STATUS_CODE);
                        String message = jobstatus.optString(TagName.TAG_MSG);

                        if (status==1) {
//            boolean status = response.getBoolean(TagName.TAG_STATUS);
//                if(message.equalsIgnoreCase("success")){
                            JSONObject jobcust=jsonObject.getJSONObject(TagName.TAG_CUSTMER);
                            editor = sharedpreferences.edit();
                            editor.putString("userID", jobcust.optString("id"));
                            editor.putString("userEmail", jobcust.optString("username"));
                            editor.putString("password", jobcust.optString("password"));
                          editor.putString("userName",jobcust.optString("name"));
                            editor.putString("logged", "logged");
                            editor.commit();
                            activity.finish();
                            Intent in=new Intent(getActivity(), BookNowActivity.class);
                            in.putExtra("buynowKey", TagName.BUYNOW_USER_DETAILS);
                            activity.startActivity(in);
                            activity.overridePendingTransition(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                        }else {
                            Toast.makeText(activity, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(email,password,name);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

//    private void updateUI(boolean isSignedIn) {
//        if (isSignedIn) {
//            btnSignIn.setVisibility(View.GONE);
//            btnSignOut.setVisibility(View.VISIBLE);
//            btnRevokeAccess.setVisibility(View.VISIBLE);
//            llProfileLayout.setVisibility(View.VISIBLE);
//        } else {
//            btnSignIn.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.GONE);
//            btnRevokeAccess.setVisibility(View.GONE);
//            llProfileLayout.setVisibility(View.GONE);
//        }
//    }
}
