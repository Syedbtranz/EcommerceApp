package com.btranz.ecommerceapp.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.adapter.OrdersRecyclerAdapter;
import com.btranz.ecommerceapp.adapter.SummaryRecyclerAdapter;
import com.btranz.ecommerceapp.modal.ProductModel;

import java.util.ArrayList;


/**
 *
 */
public class CheckOutFragment extends Fragment {
    AlertDialog dialog;
    Button backBtn;
    TextView   typeTxt, addressTxt, nameTxt,emailTxt, mobileTxt,typeED, addressED, nameED, emailED, mobileED, summaryView, modeTXT, amtTXT;
//    Button buyNowBtn;
    LinearLayout buyNowBtn;
    String deliveryType, address, name, email, contact;
    FragmentActivity activity;
    ArrayList<ProductModel> prdtSummeryList;
    int tempCount, count1;
    double  tempAmt, amt1;
    private RecyclerView recyclerView;
    private SummaryRecyclerAdapter adapter;
    // shared preference
    SharedPreferences sharedpreferences;
    String PREFS_NAME = "MyPrefs";
    SharedPreferences.Editor editor;
    public CheckOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        sharedpreferences = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();
        deliveryType = sharedpreferences.getString("checkoutUserDeliveryType", "");
        address = sharedpreferences.getString("checkoutUserAddress", "");
        name = sharedpreferences.getString("checkoutUserName", "");
        email = sharedpreferences.getString("checkoutUserEmail", "");
        contact = sharedpreferences.getString("checkoutUserContact", "");

               prdtSummeryList = activity.getIntent().getParcelableArrayListExtra("CheckOutProductList");
        Log.e("checkOutProductsArray",""+prdtSummeryList);
        for (int i = 0; i < prdtSummeryList.size(); i++) {
            ProductModel item = prdtSummeryList.get(i);
            count1 = tempCount + item.getCount();
             amt1 = tempAmt + (item.getFinalPrice() * item.getCount());
            Log.e("onPostExecute", " " + item.getFinalPrice());
            //cart badge
//            ((SecondActivity) getActivity()).writeBadge(services.size());
//            editor.putString("cartBadge", String.valueOf(services.size()));
//            editor.commit();
//            coutTxt.setText(String.valueOf(count1));
//            amtTxt.setText(String.valueOf(amt1));
            tempCount = count1;
            tempAmt = amt1;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_checkout, container, false);
        typeTxt=(TextView) rootView.findViewById(R.id.type_txt);
        addressTxt=(TextView) rootView.findViewById(R.id.address_txt);
        nameTxt=(TextView) rootView.findViewById(R.id.name_txt);
        emailTxt=(TextView) rootView.findViewById(R.id.email_txt);
        mobileTxt=(TextView) rootView.findViewById(R.id.contact_txt);
        typeED=(TextView) rootView.findViewById(R.id.type_ed);
        addressED=(TextView) rootView.findViewById(R.id.address_ed);
        nameED=(TextView) rootView.findViewById(R.id.name_ed);
        emailED=(TextView) rootView.findViewById(R.id.email_ed);
        mobileED=(TextView) rootView.findViewById(R.id.mobile_ed);
        summaryView=(TextView) rootView.findViewById(R.id.summery_vw);
        modeTXT=(TextView) rootView.findViewById(R.id.mode_txt);
        amtTXT=(TextView) rootView.findViewById(R.id.due_amt_txt);
        backBtn=(Button)rootView.findViewById(R.id.back_btn);
        buyNowBtn=(LinearLayout) rootView.findViewById(R.id.buynow_btn);

        typeTxt.setText(deliveryType);
        addressTxt.setText(address);
        nameTxt.setText(name);
        emailTxt.setText(email);
        mobileTxt.setText(contact);
        amtTXT.setText(String.valueOf(String.valueOf(amt1)));

        typeED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(1);
//                Toast.makeText(getActivity(),"Type Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        addressED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(2);
                Toast.makeText(getActivity(),"Address Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        nameED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(3);
                Toast.makeText(getActivity(),"Address Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        emailED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(4);
                Toast.makeText(getActivity(),"Address Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        mobileED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(5);
                Toast.makeText(getActivity(),"mobile Edit Selected",Toast.LENGTH_SHORT).show();
            }
        });
        summaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prdtSummary();
//                Toast.makeText(getActivity(),"summaryView Selected",Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"ThanQ ! Your Order is Placed",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
    public void editDialog(final int id){
        final View dialogView = View.inflate(getActivity(), R.layout.edit_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        final EditText ed = (EditText) dialogView.findViewById(R.id.edit);
        if(id==1){
            ed.setText(typeTxt.getText().toString());
        }else if(id==2){
            ed.setText(addressTxt.getText().toString());
        }else if(id==3){
            ed.setText(nameTxt.getText().toString());
        }else if(id==4){
            ed.setText(emailTxt.getText().toString());
        }else if(id==5){
            ed.setText(mobileTxt.getText().toString());
        }
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
//                submitForgotForm();
                if(id==1){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT);
                    typeTxt.setText(ed.getText().toString());
                }else if(id==2){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    addressTxt.setText(ed.getText().toString());
                }else if(id==3){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    nameTxt.setText(ed.getText().toString());
                }else if(id==4){
                    ed.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    emailTxt.setText(ed.getText().toString());
                }else if(id==5){
                    ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mobileTxt.setText(ed.getText().toString());

                }
            }
        });
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                revealShow(dialogView, true, null);
            }
        });
//        TextView summaryQnty=(TextView)dialogView.findViewById(R.id.summary_qnty);
//        TextView summaryAmt=(TextView)dialogView.findViewById(R.id.summary_amt);
//        summaryQnty.setText(String.valueOf(count1));
//        summaryAmt.setText(String.valueOf(amt1));
        dialogView.findViewById(R.id.close_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShow(dialogView, false, dialog);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnimations_right;
        dialog.show();

    }
    public void prdtSummary(){
        final View dialogView = View.inflate(getActivity(), R.layout.prdts_summary_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        recyclerView = (RecyclerView) dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new SummaryRecyclerAdapter(activity, prdtSummeryList, R.layout.product_summary_inflate);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                revealShow(dialogView, true, null);
            }
        });
        TextView summaryQnty=(TextView)dialogView.findViewById(R.id.summary_qnty);
        TextView summaryAmt=(TextView)dialogView.findViewById(R.id.summary_amt);
        summaryQnty.setText(String.valueOf(count1));
        summaryAmt.setText(String.valueOf(amt1));
        dialogView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShow(dialogView, false, dialog);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealShow(View rootView, boolean reveal, final AlertDialog dialog) {
        final View view = rootView;//.findViewById(R.id.reveal_view);
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if(reveal){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                    w / 2, h / 2, 0, maxRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });

            anim.start();
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
