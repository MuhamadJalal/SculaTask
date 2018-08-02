package com.muhamadgalal.sculatask.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muhamadgalal.sculatask.Data.DatabaseHandler;
import com.muhamadgalal.sculatask.Data.SculaEventAdapter;
import com.muhamadgalal.sculatask.Model.SculaEvent;
import com.muhamadgalal.sculatask.R;
import com.muhamadgalal.sculatask.Utils.CustomNavigationHandler;
import com.muhamadgalal.sculatask.Utils.DrawerItemEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private SculaEventAdapter sculaEventAdapter;
    private RecyclerView recyclerView;
    private List<SculaEvent> eventList;
    private List<SculaEvent> getEventListDB;

    private EditText eventTitle;
    private EditText eventDescription;
    private Button saveButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView remTextIconCount;
    private int reminderCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // instantiate Database
        databaseHandler = new DatabaseHandler(this);

        // setting up layout for items
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // referring and attaching lists
        eventList = new ArrayList<>();
        getEventListDB = new ArrayList<>();

        getEventListDB = databaseHandler.getSculaEventsList();
        for (SculaEvent se : getEventListDB) {

            SculaEvent event = new SculaEvent();

            event.setEventID(se.getEventID());
            event.setTitle(se.getTitle());
            event.setDescription(se.getDescription());
            event.setEventAddedDate(se.getEventAddedDate());

            eventList.add(event);
        }

        sculaEventAdapter = new SculaEventAdapter(this, eventList);
        recyclerView.setAdapter(sculaEventAdapter);
        sculaEventAdapter.notifyDataSetChanged();

        Button addEventBtn = (Button) findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePopupDialog();
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle event listener for the upper Navigation view
        new CustomNavigationHandler(this, (View) findViewById(R.id.drawer_layout), drawerLayout);
        // handle navHeader notification icons
        new DrawerItemEventListener().changeUserNotification(this, drawerLayout, null, null, null, 7);
    }

    private void CreatePopupDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.popup_event, null);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        eventTitle = (EditText) view.findViewById(R.id.popupTitle);
        eventDescription = (EditText) view.findViewById(R.id.popupDescription);
        saveButton = (Button) view.findViewById(R.id.popupButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
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

        databaseHandler = new DatabaseHandler(this);
        // adding event to DB
        databaseHandler.AddEvent(event);
        // notify user about saving items
        Snackbar.make(v, "Event Saved Correctly", Snackbar.LENGTH_LONG).show();
        // set Delaying
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(EventListActivity.this, EventListActivity.class));
                finish();
            }
        }, 1200);
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
        getMenuInflater().inflate(R.menu.event_list, menu);

        // inflate the user plus sign action bar icon
        final MenuItem menuItem = menu.findItem(R.id.userActionBar);
        // invoke text to modify counter badge
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
}