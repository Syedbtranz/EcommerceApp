package com.btranz.ecommerceapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btranz.ecommerceapp.R;
import com.btranz.ecommerceapp.activity.SecondActivity;
import com.btranz.ecommerceapp.utils.TagName;
import com.btranz.ecommerceapp.utils.TypefaceSpan;


/**
 * Created by Ravi on 29/07/15.
 */
public class ReferEarnFragment extends Fragment {
    FragmentActivity activity;
    ImageView referShare;
    TextView referCodeTxt;
    Button inviteBtn;

    public ReferEarnFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_refer_earn, container, false);
        findViewById(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.refer_ern_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refer_faqs) {
            Toast.makeText(activity, "Faqs", Toast.LENGTH_SHORT).show();
            Fragment fragment = null;
//                Log.d("position adapter", "" + position);
//                Product product = (Product) products.get(position);
//                arguments.putParcelable("singleProduct", product);

            // Start a new fragment
            fragment = new ReferEarnFaqsFragment();
//                fragment.setArguments(arguments);

            FragmentTransaction transaction = activity
                    .getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_credentials, fragment,
                    ReferEarnFaqsFragment.RE_FAQS_FRAG);
            transaction.addToBackStack(ReferEarnFaqsFragment.RE_FAQS_FRAG);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void findViewById(View view) {
        final Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final TextView toolTitle = (TextView) mToolbar.findViewById(R.id.toolbar_txt);
        ((AppCompatActivity) activity).setSupportActionBar(mToolbar);
        SpannableString s = new SpannableString(getString(R.string.title_ref_ern));
        s.setSpan(new TypefaceSpan(getActivity(), "hallo_sans_black.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Title
        toolTitle.setText(s);
//        mToolbar.setTitle(s);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.back_btn));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
//                getActivity().finish();
//                ((MainActivity) getActivity()).startActivity(new Intent(((MainActivity) getActivity()), MainActivity.class));
//                ((MainActivity) getActivity()).overridePendingTransition(android.R.anim.fade_in,
//                        android.R.anim.fade_out);

            }
        });
        referCodeTxt=(TextView) view.findViewById(R.id.refer_code);
        referShare=(ImageView)view.findViewById(R.id.refer_ern_share);
        inviteBtn=(Button)view.findViewById(R.id.invite_btn);

        //SHARE ACTION
        referShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, modelItem.getTitle()+" shared ", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wow! Your Friend just gifted you");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Use my invite code "+referCodeTxt.getText().toString()+"and get Rs. 100 on your first order. Click to download app and get rewarded: ");
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "invited", Toast.LENGTH_SHORT).show();
            }
        });
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
