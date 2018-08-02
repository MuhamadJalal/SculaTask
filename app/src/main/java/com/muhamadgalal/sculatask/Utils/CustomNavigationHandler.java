package com.muhamadgalal.sculatask.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhamadgalal.sculatask.Activities.EventListActivity;
import com.muhamadgalal.sculatask.Activities.MainActivity;
import com.muhamadgalal.sculatask.R;

public class CustomNavigationHandler implements NavigationView.OnNavigationItemSelectedListener {

    private View view;
    private Context context;
    private DrawerLayout drawerLayout;

    public CustomNavigationHandler(final Context context, View view, DrawerLayout drawerLayout) {
        this.view = view;
        this.context = context;
        this.drawerLayout = drawerLayout;

        // Handle event listener for the upper Navigation view
        NavigationView upperNavigationView = (NavigationView) view.findViewById(R.id.upper_nav_view);
        upperNavigationView.setNavigationItemSelectedListener(this);
        // Handle event listener for the Lower Navigation view
        NavigationView lowerNavigationView = (NavigationView) view.findViewById(R.id.lower_nav_view);
        lowerNavigationView.setNavigationItemSelectedListener(this);

        // finding the notification icon of MenuItem inbox
        MenuItem item = upperNavigationView.getMenu().findItem(R.id.nav_inbox);
        View iconCounterLayout = (View) item.getActionView();
        TextView iconCounter = (TextView) iconCounterLayout.findViewById(R.id.iconNotificationText);
        // set event listener for the notification icon
        iconCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Icon Notification Text", Toast.LENGTH_SHORT).show();
            }
        });


        // Handle the header items event click listener
        // the menu items of the two navigators was handled with onNavigationItemSelected
        // the other items of DrawerLayout will be handled with DrawerItemEventListener Class
        new DrawerItemEventListener().HeaderItemEventListener(context, upperNavigationView);

        // Handle footer Drawer items event
        // using View for referring to the footer of the DrawerLayout As it's a LinearLayout
        View v = (View) view.findViewById(R.id.footerLayout);
        new DrawerItemEventListener().FooterItemEventListener(context, v);

        // set radius for images
        ImageView circularImageView = (ImageView) upperNavigationView.getHeaderView(0).findViewById(R.id.userImageView);
        radiusImage(circularImageView , R.drawable.unnamed);
        // for first sender
        ImageView firstGist = (ImageView) upperNavigationView.getHeaderView(0).findViewById(R.id.firstSenderUserIcon);
        // for second sender
        ImageView secondGist = (ImageView) upperNavigationView.getHeaderView(0).findViewById(R.id.secondSenderUserIcon);

    }

    private void radiusImage(ImageView circularImageView, int drawableResID) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResID);
        Bitmap circularBitmap = ImageConverter.getRoundedImage(bitmap, 1500);
        circularImageView.setImageBitmap(circularBitmap);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // navigate to MainActivity
            context.startActivity(new Intent(context, MainActivity.class));
            ((Activity) context).finish();
        } else if (id == R.id.nav_profile) {
            Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_events) {
            // navigate to MainActivity
            context.startActivity(new Intent(context, EventListActivity.class));
            ((Activity) context).finish();
        } else if (id == R.id.nav_favorites) {
            Toast.makeText(context, "Favorites", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_inbox) {
            Toast.makeText(context, "Inbox", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contact_us) {
            Toast.makeText(context, "Contact us", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_feedback) {
            Toast.makeText(context, "Feedback", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_setting) {
            Toast.makeText(context, "Setting", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}