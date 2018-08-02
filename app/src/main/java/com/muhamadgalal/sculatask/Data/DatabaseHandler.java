package com.muhamadgalal.sculatask.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muhamadgalal.sculatask.Model.SculaEvent;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.muhamadgalal.sculatask.Data.Constants.TABLE_NAME;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context context;

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create DB table
        String CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY, " +
                Constants.KEY_TITLE + " TEXT, " + Constants.KEY_DESCRIPTION + " TEXT, " + Constants.KEY_DATE + " LONG);");

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD Operations ( create read update delete )
     */

    // adding event to the DB
    public void AddEvent(SculaEvent event) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_TITLE, event.getTitle());
        contentValues.put(Constants.KEY_DESCRIPTION, event.getDescription());
        contentValues.put(Constants.KEY_DATE, event.getEventAddedDate());

        database.insert(TABLE_NAME, null, contentValues);
    }

    // delete event from DB
    public void DeleteEvent(int id) {

        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    // get events count
    public int getSculaEventsCount() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // get event from DB
    public SculaEvent getSculaEvent(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, new String[]{Constants.KEY_ID, Constants.KEY_TITLE, Constants.KEY_DESCRIPTION, Constants.KEY_DATE}
                , Constants.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SculaEvent event = new SculaEvent();

        assert cursor != null;
        event.setEventID(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
        event.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));

        // getting correct format of time
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formatted = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
        event.setEventAddedDate(formatted);

        cursor.close();
        return event;
    }

    // get all events
    public List<SculaEvent> getSculaEventsList() {

        List<SculaEvent> sculaEventList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, new String[]{Constants.KEY_ID, Constants.KEY_TITLE, Constants.KEY_DESCRIPTION, Constants.KEY_DATE}
                , null, null, null, null, Constants.KEY_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                SculaEvent event = new SculaEvent();

                event.setEventID(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
                // re-format date
                String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
                event.setEventAddedDate(currentDateTimeString);

                sculaEventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sculaEventList;
    }

    // update the event
    public int updateEvent(SculaEvent event) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_TITLE, event.getTitle());
        values.put(Constants.KEY_DESCRIPTION, event.getDescription());
        values.put(Constants.KEY_DATE, event.getEventAddedDate());

        return database.update(TABLE_NAME, values, Constants.KEY_ID + "=?", new String[]{String.valueOf(event.getEventID())});
    }
}