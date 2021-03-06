package com.runette.divehub;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * Simple person data access class. 
 * Defines the basic CRUD operations (Create, Read, Update, Delete)
 * for the example, and gives the ability to list all reminders as well as
 * retrieve or modify a specific reminder.
 * 
 */


public class City  extends DiveDbAdapter {
	
    
	public static final String ARG_ITEM_ID = "city_id";
    public static final String ARG_ITEM_NAME = "city_name";
	private static final String TAG = "City Object";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    public long ID ;
    private Context mCtx;

    public City(Context ctx) {
		super(ctx);
		this.mCtx = ctx;
		ID = NO_ID_FLAG;
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
    public City open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    @Override
    public void close() {
        mDbHelper.close();
    }

// 
// Person CRUD Operations
//
    /**
     * Create a new person using the dive name
     * If the action is  successfully created return the new rowId
     * for that dive, otherwise return a -1 to indicate failure.
     * 
     * @param person name

     * @return rowId or -1 if failed
     */
    public long create(String name) {
        ContentValues initialValues = new ContentValues();
        // initialValues.put(KEY_PERSON_FIRSTNAME, firstname);
        initialValues.put(KEY_CITY_NAME, name);

        return super.create(mDb, DATABASE_TABLE_CITY, initialValues, CITY_ALL_KEYS);
    }

    /**
     * Delete the person with the given rowId
     * 
     * @param rowId id of dive to delete
     * @return true if deleted, false otherwise
     */
    public boolean delete(long rowId) {

        return super.delete(rowId, mDb, DATABASE_TABLE_CITY);
    }

    /**
     * Return a Cursor over the list of all peopl;e in the database
     * 
     * @return Cursor over all people
     */
    public Cursor fetchAll() {

        return super.fetchAll(mDb, DATABASE_TABLE_CITY, CITY_ALL_KEYS, KEY_CITY_NAME + " COLLATE NOCASE ");
        		
    }
    public Cursor fetchUpdate() {
    	return super.fetchUpdate(mDb, DATABASE_TABLE_CITY);
    }

    /**
     * Return a Cursor positioned at the person that matches the given rowId
     * 
     * @param rowId id of dive to retrieve
     * @return Cursor positioned to matching dive, if found
     * @throws SQLException if dive could not be found/retrieved
     */
    public Cursor fetch() throws SQLException {

        return super.fetch(mDb, DATABASE_TABLE_CITY, CITY_ALL_KEYS, ID);

    }

    /**
     * Update the person using the key value pair provided. The person to be updated is
     * specified using the rowId, and the arg altered is specified by key
     * 
     * @param rowId id of reminder to update
     * @param String - key
     * @param String - value for the arg identified by key
     * 
     */
    public boolean update( String key, String value) {
        return super.update(ID, mDb, DATABASE_TABLE_CITY, key, value);
    }
    public long findCitybyName(String name) {
      	 long id = NO_NAME;
      	 Cursor mCursor =

                   mDb.query(true, DATABASE_TABLE_CITY, CITY_ALL_KEYS, KEY_CITY_NAME + " = ?",new String[] {name} ,
                           null, null, null, null);
      	 
           if (mCursor.getCount() >= 1) {
               mCursor.moveToFirst();   		
           id = mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID));
           ID = id;
           }
           
           return id;
           
       } 
    public long getNext(long id) {
    	ID = super.getNext(mDb, DATABASE_TABLE_CITY, id);
    	return ID;
    	
    }
    public long getPrevious(long id) {
    	ID =  super.getPrevious(mDb, DATABASE_TABLE_CITY, id);
    	return ID;
    }
}

