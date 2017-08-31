package com.runette.divehub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Divelog {
	
	private Context ctx;
	private static final String  TAG = "Divelog";
	private SQLiteDatabase mDB;
	private Dive mDive;
	private DiveSite mSite;
	private Person mPerson;
	private City mCity;
	private Country mCountry;
	private long mDiveID;
	private long mSiteID;
	private long mPersonID;
	private String auuid;
    private String buuid;
    private String aupdated;
    private String bupdated;
    private Date adate;
    private Date bdate;
	//
	// Database Column names - the B string matches columnwised the appropriate ALL_KEYS string from DiveDbAdapter
	//
    public static final String SYNCH_FILE = "divinglogsynch";
	public static final String DB_PATH = "/data/data/com.runette.divehub/";
	public static final String SOURCE = "Divelog/";
	public static final String FILE1 = "DivelogSync.xml";
	private static final String B_KEY_ROWID = "ID";
	private static final String B_KEY_UUID = "UUID";
	private static final String B_KEY_UPDATE_DATE ="Updated";
	public static final String B_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	SimpleDateFormat adateTimeFormat ;
	SimpleDateFormat bdateTimeFormat ;
	
	private static final String B_KEY_NUMBER = "Number";
    private static final String B_KEY_DATE = "Divedate";
    private static final String B_KEY_TIME = "Entrytime";
    private static final String B_KEY_DURATION = "Divetime";
    private static final String B_KEY_DEPTH = "Depth";
    private static final String B_KEY_SITENAME = "Place";
    private static final String B_KEY_SITE= "PlaceID";
    private static final String B_KEY_BUDDY_NAME = "Buddy";
    private static final String B_KEY_BUDDY = "BuddyIDs";
    private static final String B_KEY_COMMENTS = "Comments";
    private static final String B_KEY_SIGNATURE = "Signature";
    private static final String B_KEY_DM_NAME = "Divemaster";
    
	
	private static final String[] B_DIVE_ALL_KEYS =  {	B_KEY_ROWID,
		"Number",
		"Divedate",
		"Entrytime",
		"Divetime",
		"Depth",
		"Place",
		"PlaceID",
		"Buddy",
		"BuddyIDs",
		"Comments",
		"Signature",
		"Divemaster",
		null
	};
	
	 private static final String B_KEY_SITE_NAME = "Place";
	 private static final String B_KEY_SITE_LOCATION = "City";
	 private static final String B_KEY_SITE_COUNTRY = "Country";
	 private static final String B_KEY_SITE_LAT = "Lat";
	 private static final String B_KEY_SITE_LON = "Lon";
				
	 private static final String B_KEY_PERSON_FIRSTNAME = "FirstName";
	 private static final String B_KEY_PERSON_LASTNAME = "LastName";
	 private static final String B_KEY_PERSON_PHONE = "Phone";
	 private static final String B_KEY_PERSON_MOBILE = "Mobile";
	 private static final String B_KEY_PERSON_EMAIL = "Email";
	 
	 private static final String B_KEY_COUNTRY_NAME = "Country";
	 private static final String[] B_COUNTRY_ALL_KEYS = {
	    												B_KEY_ROWID,
	    												B_KEY_COUNTRY_NAME};
			

   
  
       

	
	
	public Divelog ( Context c) {
		ctx = c ;
        adateTimeFormat = new SimpleDateFormat(DiveDbAdapter.SYSTEM_TIME, Locale.US);
    	bdateTimeFormat = new SimpleDateFormat(B_DATE_TIME_FORMAT, Locale.US);
    	adateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    	bdateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    	mDive = new Dive(ctx);
	    mDive.open();
	    mDiveID=0;
	    mSite = new DiveSite(ctx);
	    mSite.open();
	    mSiteID = 0;
	    mPerson = new Person(ctx);
	    mPerson.open();
	    mPersonID = 0;
	    mCity = new City(ctx);
	    mCity.open();
	    mCountry = new Country(ctx);
	    mCountry.open();
	}
	
	public void open(String path) {
		mDB = ctx.openOrCreateDatabase(path,0,null);
	}
	
	public void sync () {
		new syncDive().execute();
		
		return;
	}
	private class syncDive extends AsyncTask<Void, Void, boolean[]> {
	   	
		
		/** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() 
		 * @return */
	    protected boolean[] doInBackground(Void... voids) {
		
	    	Cursor bPlace = mDB.query("place", null, null, null, null, null, null);
			bPlace.moveToFirst();
			Cursor bCity = mDB.query("city", null, null, null, null, null, null);
			bCity.moveToFirst();
			Cursor bCountry = mDB.query("country", null, null, null, null, null, null);
			bCity.moveToFirst();
			Cursor bPerson = mDB.query("buddy", null, null, null, null, null, null);
			bPerson.moveToFirst();
			int bi;
			for ( bi = 0; bi < bCountry.getCount(); bi++) {
				try {
					buuid = bCountry.getString(bCountry.getColumnIndexOrThrow(B_KEY_UUID));
					boolean present = false;
					long ai = mCountry.findByUuid(buuid);
					Cursor aCountry = mCountry.fetch();
					if (ai != Country.NO_NAME) {
						present = true;
						aupdated = aCountry.getString(aCountry.getColumnIndexOrThrow(Dive.KEY_UPDATE_DATE));
						bupdated = bCountry.getString(bCountry.getColumnIndexOrThrow(B_KEY_UPDATE_DATE));
						adate = adateTimeFormat.parse(aupdated);
						bdate = bdateTimeFormat.parse(bupdated);
						int cp = adate.compareTo(bdate);
						if (cp <= 0) {
							for (int i = 1 ; i < Country.COUNTRY_ALL_KEYS.length; i++) {
								if (B_DIVE_ALL_KEYS[i] != null ) {
									mCountry.update( Country.COUNTRY_ALL_KEYS[i], bCountry.getString(bCountry.getColumnIndexOrThrow(B_COUNTRY_ALL_KEYS[i])));

								}
							Log.d(TAG, "updating country incomeing" + auuid);
							break;
							} 
								
							} else {
							
								Log.d(TAG, "updating country outgoing" + auuid);
							}
					}
						
				} catch (ParseException e) {
					Log.e(TAG, e.getMessage(), e); 
				} catch (IllegalArgumentException e) {
					Log.e(TAG, e.getMessage(), e); 
				}
				
			}
			/*	for (int bi = 0; bi < bPlaces.getCount(); bi++) {
					aSi.moveToFirst();
					try {
						buuid = bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_UUID));
						boolean present = false;
						for (int ai = 0; ai < aLogs.getCount(); ai++) {
							mID = aLogs.getLong(aLogs.getColumnIndexOrThrow(Dive.KEY_ROWID));
							auuid = aLogs.getString(aLogs.getColumnIndexOrThrow(Dive.KEY_UUID));
								if (auuid.equalsIgnoreCase(buuid)) {
									present = true;
									aupdated = aLogs.getString(aLogs.getColumnIndexOrThrow(Dive.KEY_UPDATE_DATE));
									bupdated = bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_UPDATE_DATE));
									adate = adateTimeFormat.parse(aupdated);
									bdate = bdateTimeFormat.parse(bupdated);
									int cp = adate.compareTo(bdate);
									if (cp <= 0) {
										for (int i = 1 ; i < Dive.DIVE_ALL_KEYS.length; i++) {
											if (B_DIVE_ALL_KEYS[i] != null ) {
												mDive.update(mID, Dive.DIVE_ALL_KEYS[i], bLogs.getString(bLogs.getColumnIndexOrThrow(B_DIVE_ALL_KEYS[i])));

											}
										Log.d(TAG, "updating item incomeing" + auuid);
										break;
										} 
											
										} else {
										
											Log.d(TAG, "updating item outgoing" + auuid);
										}
								}
							aLogs.moveToNext();	
						} 
						if (! present) {
							mID = mDive.create(bLogs.getInt(bLogs.getColumnIndexOrThrow(B_KEY_NUMBER)),bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_DATE)),bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_TIME)));
							mDive.update(mID, Dive.KEY_UUID, buuid);
							for (int i = 4 ; i < Dive.DIVE_ALL_KEYS.length; i++) {
								if (B_DIVE_ALL_KEYS[i] != null ) {
									mDive.update(mID, Dive.DIVE_ALL_KEYS[i], bLogs.getString(bLogs.getColumnIndexOrThrow(B_DIVE_ALL_KEYS[i])));
									Log.d(TAG, "creating item" + buuid);
								}
							}
						}
					}
					catch (ParseException e) {
								Log.e(TAG, e.getMessage(), e); 
							}
					catch (IllegalArgumentException e) {
						Log.e(TAG, e.getMessage(), e); 
					}
					bLogs.moveToNext();
	    	*/
	 		Cursor bLogs = mDB.query("logbook", null, null, null, null, null, null);
		bLogs.moveToFirst();
	    Cursor aLogs  = mDive.fetchUpdate();
	    
			for ( bi = 0; bi < bLogs.getCount(); bi++) {
				aLogs.moveToFirst();
				try {
					buuid = bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_UUID));
					boolean present = false;
					for (int ai = 0; ai < aLogs.getCount(); ai++) {
						mDiveID = aLogs.getLong(aLogs.getColumnIndexOrThrow(Dive.KEY_ROWID));
						auuid = aLogs.getString(aLogs.getColumnIndexOrThrow(Dive.KEY_UUID));
							if (auuid.equalsIgnoreCase(buuid)) {
								present = true;
								aupdated = aLogs.getString(aLogs.getColumnIndexOrThrow(Dive.KEY_UPDATE_DATE));
								bupdated = bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_UPDATE_DATE));
								adate = adateTimeFormat.parse(aupdated);
								bdate = bdateTimeFormat.parse(bupdated);
								int cp = adate.compareTo(bdate);
								if (cp <= 0) {
									for (int i = 1 ; i < Dive.DIVE_ALL_KEYS.length; i++) {
										if (B_DIVE_ALL_KEYS[i] != null ) {
											mDive.update(mDiveID, Dive.DIVE_ALL_KEYS[i], bLogs.getString(bLogs.getColumnIndexOrThrow(B_DIVE_ALL_KEYS[i])));

										}
									Log.d(TAG, "updating item incomeing" + auuid);
									
									} 
										
									} else {
									
										Log.d(TAG, "updating item outgoing" + auuid);
									}
							}
						aLogs.moveToNext();	
					} 
					if (! present) {
						mDiveID = mDive.create(bLogs.getInt(bLogs.getColumnIndexOrThrow(B_KEY_NUMBER)),bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_DATE)),bLogs.getString(bLogs.getColumnIndexOrThrow(B_KEY_TIME)));
						mDive.update(mDiveID, Dive.KEY_UUID, buuid);
						for (int i = 4 ; i < Dive.DIVE_ALL_KEYS.length; i++) {
							if (B_DIVE_ALL_KEYS[i] != null ) {
								mDive.update(mDiveID, Dive.DIVE_ALL_KEYS[i], bLogs.getString(bLogs.getColumnIndexOrThrow(B_DIVE_ALL_KEYS[i])));
								Log.d(TAG, "creating item" + buuid);
							}
						}
					}
				}
				catch (ParseException e) {
							Log.e(TAG, e.getMessage(), e); 
						}
				catch (IllegalArgumentException e) {
					Log.e(TAG, e.getMessage(), e); 
				}
				bLogs.moveToNext();
				} 
					
		return new boolean[]{true};
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(Void... voids) {
	    	
	    
	 // Create a Notification//
	 		NotificationManager mgr = (NotificationManager) ctx.getSystemService("notification");
	 						
	 		
	 		Notification noti =  new NotificationCompat.Builder(ctx)
	 			.setAutoCancel(true)
	 			.setContentTitle(ctx.getString(R.string.notify_sync_title))
	 			.setContentText(ctx.getString(R.string.notify_sync_message))
	 			.setSmallIcon(android.R.drawable.stat_sys_warning)
	 			.getNotification();
	 		 
	 		
	 		// An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value). 
	 		// I highly doubt this will ever happen. But is good to note. 
	 	
	 		mgr.notify(ctx.getString(R.string.sync_notification_dives),10, noti); 
	    }
	    
	}
	
	
	
}

