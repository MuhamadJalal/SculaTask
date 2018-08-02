package com.muhamadgalal.sculatask.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.muhamadgalal.sculatask.Data.DatabaseHandler;
import com.muhamadgalal.sculatask.Model.SculaEvent;
import com.muhamadgalal.sculatask.R;
import com.muhamadgalal.sculatask.Utils.CustomNavigationHandler;
import com.muhamadgalal.sculatask.Utils.DrawerItemEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView remTextIconCount;
    public int reminderCount = 10;
    private DatabaseHandler database;
    private Button previewBtn;
    private Button addEventBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView eventTitle;
    private TextView eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // instantiate Database
        database = new DatabaseHandler(this);

        // set scalability to TextView
        ((TextView) findViewById(R.id.previewTextView)).setMovementMethod(new ScrollingMovementMethod());
        ;
        // Navigate Back to Event list
        previewBtn = (Button) findViewById(R.id.previewBtn);
        previewBtn.setOnClickListener(this);

        // adding new event from main activity
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(this);

        /**
         // passing activity if there is any events
         bypassActivity();
         **/
        // Handle event listener for the upper Navigation view
        new CustomNavigationHandler(MainActivity.this, (View) findViewById(R.id.drawer_layout), drawerLayout);
        // handle navHeader notification icons
        new DrawerItemEventListener().changeUserNotification(this, drawerLayout, null, 3, null, 8);


    }

    private void CreatePopupDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.popup_event, null);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        eventTitle = (TextView) view.findViewById(R.id.popupTitle);
        eventDescription = (TextView) view.findViewById(R.id.popupDescription);
        Button saveBtn = (Button) view.findViewById(R.id.popupButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if there is no empty fields
                if (!eventTitle.getText().toString().equals("") && !eventDescription.getText().toString().equals("")) {
                    saveToDatabaseAndListview(v);
                } else {
                    Snackbar.make(view, "All fields are Required!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveToDatabaseAndListview(View v) {
        SculaEvent event = new SculaEvent();
        event.setTitle(eventTitle.getText().toString());
        event.setDescription(eventDescription.getText().toString());

        database = new DatabaseHandler(this);
        // adding event to DB
        database.AddEvent(event);
        // notify user about saving items
        Snackbar.make(v, "Event Saved Correctly", Snackbar.LENGTH_LONG).show();
        // set Delaying
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EventListActivity.class));
            }
        }, 1200);
    }

    private void bypassActivity() {
        if (database.getSculaEventsCount() > 0) {
            startActivity(new Intent(MainActivity.this, EventListActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        // inflate the user plus sign action bar icon
        final MenuItem menuItem = menu.findItem(R.id.userActionBar);

        View actionView = menuItem.getActionView();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        // inflate the reminderCounter action bar icon
        final MenuItem reminderMenuItem = menu.findItem(R.id.reminderActionBar);
        // invoke text to modify counter badge
        View remActionView = reminderMenuItem.getActionView();
        remTextIconCount = (TextView) remActionView.findViewById(R.id.counterActionBadge);
        setupBadge();

        remActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(reminderMenuItem);
            }
        });
        return true;
    }


    private void setupBadge() {

        if (remTextIconCount != null) {
            if (reminderCount == 0) {
                if (remTextIconCount.getVisibility() != View.GONE) {
                    remTextIconCount.setVisibility(View.GONE);
                }
            } else {
                remTextIconCount.setText(String.valueOf(reminderCount));
                if (remTextIconCount.getVisibility() != View.VISIBLE) {
                    remTextIconCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.userActionBar:
                Toast.makeText(this, "userActionBar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reminderActionBar:
                Toast.makeText(this, "reminderActionBar", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previewBtn:
                if (database.getSculaEventsCount() > 0) {
                    startActivity(new Intent(MainActivity.this, EventListActivity.class));
                    this.finish();
                } else
                    Toast.makeText(this, "There is no any coming event!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addEventBtn:
                CreatePopupDialog();
                break;
        }
    }

}