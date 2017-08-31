package com.runette.divehub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * Dive Data object 
 * Defines the basic CRUD operations (Create, Read, Update, Delete)
 * for the example, and gives the ability to list all reminders as well as
 * retrieve or modify a specific reminder.
 * 
 */


public class Dive  extends DiveDbAdapter {
	
    
	public static final String ARG_ITEM_ID = "dive_id";
	private static final String TAG = "Dive Object";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context mCtx;

    public Dive(Context ctx) {
		super(ctx);
		this.mCtx = ctx;
	}

	/**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    @Override
    public Dive open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    @Override
    public void close() {
        mDbHelper.close();
    }

// 
// DIVE CRUD Operations
//
    /**
     * Create a new dive using the dive number, date and time provided. 
     * If the action is  successfully created return the new rowId
     * for that dive, otherwise return a -1 to indicate failure.
     * 
     * @param dive number
     * @param dive date
     * @param dive time
     * @return rowId or -1 if failed
     */
    public long create(int divenumber, String date, String time) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NUMBER, divenumber);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TIME, time); 
        return super.create(mDb, DATABASE_TABLE_DIVE, initialValues, DIVE_ALL_KEYS);
        		
    }

    /**
     * Delete the dive with the given rowId
     * 
     * @param rowId id of dive to delete
     * @return true if deleted, false otherwise
     */
    public boolean delete(long rowId) {

        return super.delete(rowId, mDb, DATABASE_TABLE_DIVE);
        		
    }

    /**
     * Return a Cursor over the list of all dives in the database
     * 
     * @return Cursor over all reminders
     */
    public Cursor fetchAll() {

        return super.fetchAll(mDb, DATABASE_TABLE_DIVE, DIVE_ALL_KEYS, KEY_NUMBER);
        		
    }
    
   
    public Cursor fetchUpdate() {
    	return super.fetchUpdate(mDb, DATABASE_TABLE_DIVE);
    }

    /**
     * Return a Cursor positioned at the dive that matches the given rowId
     * 
     * @param rowId id of dive to retrieve
     * @return Cursor positioned to matching dive, if found
     * @throws SQLException if dive could not be found/retrieved
     */
    public Cursor fetch(long rowId) throws SQLException {

        return super.fetch(mDb, DATABASE_TABLE_DIVE, DIVE_ALL_KEYS, rowId);

    }

    /**
     * Update the dive using the key value pair provided. The reminder to be updated is
     * specified using the rowId, and the arg altered is specified by key
     * 
     * @param rowId id of reminder to update
     * @param String - key
     * @param String - value for the arg indentified by key
     * 
     */
    
    public boolean update(long rowId, String key, String value) {
    	return super.update(rowId, mDb, DATABASE_TABLE_DIVE, key, value);
    }
    //
    // Get the highest current used divenumber and add 1
    // to get the default number for the next new dive
    //
    public long  nextNumber () {
    	long next = 0 ;
    	Cursor c = mDb.query(DATABASE_TABLE_DIVE, new String[] {KEY_ROWID, KEY_NUMBER,}, null, null, null, null, KEY_NUMBER );
    	if ( c.getCount() > 0 ) {
    		c.moveToLast();
    		int i = c.getInt(c.getColumnIndex(KEY_NUMBER));	
        	if (i > 0) next = i;
    	} 
    	next ++ ;
    	return next ;
    }
    
    public long getNext(long id) {
    	return super.getNext(mDb, DATABASE_TABLE_DIVE, id);
    	
    }
    public long getPrevious(long id) {
    	return super.getPrevious(mDb, DATABASE_TABLE_DIVE, id);
    	
    }
}

