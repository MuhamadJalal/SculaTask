package com.muhamadgalal.sculatask.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muhamadgalal.sculatask.R;

@SuppressLint("Registered")
public class DrawerItemEventListener extends Activity implements View.OnClickListener {

    private Context context;

    private ImageView navigateBackArrow;
    private ImageView personalImg;
    private TextView userName;
    private RelativeLayout firstSender;
    private RelativeLayout secondSender;

    private LinearLayout mySchool;
    private LinearLayout addSchool;
    private LinearLayout howTOUseApp;
    private LinearLayout logoutLayout;

    public void HeaderItemEventListener(Context context , NavigationView navigationView) {
        this.context = context;

        navigateBackArrow = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.navigateBack);
        navigateBackArrow.setOnClickListener(this);

        personalImg = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userImageView);
        personalImg.setOnClickListener(this);

        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userNameID);
        userName.setOnClickListener(this);

        firstSender = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.firstIncomingMessageNotifier);
        firstSender.setOnClickListener(this);

        secondSender = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.secondIncomingMessageNotifier);
        secondSender.setOnClickListener(this);

    }

    public void FooterItemEventListener(Context context , View view){
        this.context = context;

        mySchool = (LinearLayout) view.findViewById(R.id.footerMySchool);
        mySchool.setOnClickListener(this);

        addSchool =(LinearLayout) view.findViewById(R.id.footerAddSchool);
        addSchool.setOnClickListener(this);

        howTOUseApp = (LinearLayout) view.findViewById(R.id.footerHowToUse);
        howTOUseApp.setOnClickListener(this);

        logoutLayout = (LinearLayout) view.findViewById(R.id.footerLogout);
        logoutLayout.setOnClickListener(this);
    }


    // Handle the changes of users notification
    public void changeUserNotification(Context context , View drawerLayout , Integer firstGistImage , Integer firstGistMsgCount , Integer secondGistImage , Integer secondGistMsgCount ) {

        NavigationView navigationView = (NavigationView) drawerLayout.findViewById(R.id.upper_nav_view);

        View headerLayout = navigationView.getHeaderView(0);
        // for first sender
        ImageView firstGist = (ImageView) headerLayout.findViewById(R.id.firstSenderUserIcon);
        TextView firstNotifyBadge = (TextView) headerLayout.findViewById(R.id.firstNotifyBadge);

        // for second sender
        ImageView secondGist = (ImageView) headerLayout.findViewById(R.id.secondSenderUserIcon);
        TextView secondNotifyBadge = (TextView) headerLayout.findViewById(R.id.secondNotifyBadge);

        // now we can put appropriate value for each of them
        /**
         *
         * the right way to set Senders values ( user img && notification count )
         * is to invoke them from the server (ex: FireBase)
         *
         */
        if (firstGistMsgCount != null){
            firstNotifyBadge.setText(String.valueOf(firstGistMsgCount));

            if (firstGistImage != null){
                firstGist.setImageResource(firstGistImage);
            }else {
                firstGist.setImageResource(R.drawable.ic_menu_user);
            }
        }else {
            // there no msg
            headerLayout.findViewById(R.id.firstIncomingMessageNotifier).setVisibility(View.INVISIBLE);

            // replace the removed layout with the next notification layout
            // to show its msg if it have.
            View view = headerLayout.findViewById(R.id.testNotExistMsg);
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            View view1 = headerLayout.findViewById(R.id.secondIncomingMessageNotifier);
            view1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        }

        // showing notifications
        if (secondGistMsgCount != null){
            secondNotifyBadge.setText(String.valueOf(secondGistMsgCount));

            if (secondGistImage != null){
                secondGist.setImageResource(secondGistImage);
            }else {
                firstGist.setImageResource(R.drawable.ic_menu_user);
            }
        }else {

            headerLayout.findViewById(R.id.secondIncomingMessageNotifier).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.navigateBack :
                Toast.makeText(context, "Navigate Back", Toast.LENGTH_LONG).show();
                break;

            case R.id.userImageView :
                Toast.makeText(context, "User Image", Toast.LENGTH_LONG).show();
                break;

            case R.id.userNameID:
                Toast.makeText(context, "User Name", Toast.LENGTH_LONG).show();
                break;

            case R.id.firstIncomingMessageNotifier:
                Toast.makeText(context, "First Notifier", Toast.LENGTH_LONG).show();
                break;

            case R.id.secondIncomingMessageNotifier:
                Toast.makeText(context, "Second Notifier", Toast.LENGTH_LONG).show();
                break;
            case R.id.footerMySchool:
                Toast.makeText(context, "My School", Toast.LENGTH_LONG).show();
                break;
            case R.id.footerAddSchool:
                Toast.makeText(context, "Add School", Toast.LENGTH_LONG).show();
                break;
            case R.id.footerHowToUse:
                Toast.makeText(context, "How To Use", Toast.LENGTH_LONG).show();
                break;
            case R.id.footerLogout:
                Toast.makeText(context, "Logged out", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
