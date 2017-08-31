package com.runette.divehub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 *Dive Sute data access object
 * Defines the basic CRUD operations (Create, Read, Update, Delete)
 * for the example, and gives the ability to list all reminders as well as
 * retrieve or modify a specific reminder.
 * 
 */


public class DiveSite  extends DiveDbAdapter {
	
    
	public static final String ARG_ITEM_ID = "site_id";
    public static final String ARG_ITEM_NAME = "site_name";
	private static final String TAG = "Site Object";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context mCtx;

    public DiveSite(Context ctx) {
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
    public DiveSite open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    @Override
    public void close() {
        mDbHelper.close();
    }

 // 
    // SITE CRUD Operations
    //
        /**
         * Create a new site using the name
         * If the action is  successfully created return the new rowId
         * for that site, otherwise return a -1 to indicate failure.
         * 
         * @param divesite name
         * @return rowId or -1 if failed
         */
       public long create(String sitename) {
           ContentValues args = new ContentValues();
           args.put(KEY_SITE_NAME, sitename);
           return super.create(mDb, DATABASE_TABLE_SITE, args, SITE_ALL_KEYS);
        	
       }

        /**
         * Delete the site with the given rowId
         * 
         * @param rowId id of site to delete
         * @return true if deleted, false otherwise
         */
       public boolean delete(long rowId) {

           return super.delete(rowId, mDb, DATABASE_TABLE_SITE);
        		  
       }

        /**
         * Return a Cursor over the list of all sites in the database
         * 
         * @return Cursor over all reminders
         */
        public Cursor fetchAll() {

            return super.fetchAll(mDb, DATABASE_TABLE_SITE, SITE_ALL_KEYS, KEY_SITE_NAME + " COLLATE NOCASE ");
            		
        }
        public Cursor fetchUpdate() {
        	return super.fetchUpdate(mDb, DATABASE_TABLE_SITE);
        }

        /**
         * Return a Cursor positioned at the site that matches the given rowId
         * 
         * @param rowId id of dive to retrieve
         * @return Cursor positioned to matching dive, if found
         * @throws SQLException if dive could not be found/retrieved
         */
        public Cursor fetch(long rowId) throws SQLException {

           return super.fetch(mDb, DATABASE_TABLE_SITE, SITE_ALL_KEYS, rowId);

        }

        /**
         * Update the site using the key value pair provided. The reminder to be updated is
         * specified using the rowId, and the arg altered is specified by key
         * 
         * @param rowId id of reminder to update
         * @param String - key
         * @param String - value for the arg indentified by key
         * 
         */
        public boolean update(long rowId, String key, String value) {
            return super.update(rowId, mDb, DATABASE_TABLE_SITE, key, value);
        }
        
        public long findSitebyName(String name) {
       	 long id = NO_NAME;
       	 Cursor mCursor =

                    mDb.query(true, DATABASE_TABLE_SITE, SITE_ALL_KEYS, KEY_SITE_NAME + " = ?",new String[] {name} ,
                            null, null, null, null);
       	 
            if (mCursor.getCount() >= 1) {
                mCursor.moveToFirst();   		
            id = mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID));
            } 		           	
            return id;
            
        } 
        public long getNext(long id) {
        	return super.getNext(mDb, DATABASE_TABLE_SITE, id);
        	
        }
        public long getPrevious(long id) {
        	return super.getPrevious(mDb, DATABASE_TABLE_SITE, id);
        }
        	
}

