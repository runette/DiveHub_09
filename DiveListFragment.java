package com.runette.divehub;



import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class DiveListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private Dive mDbHelper;
    private SimpleCursorAdapter mAdapter;

    public interface Callbacks {

        public void onItemSelected(long id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        public void onItemSelected(long id) {
        }
    };

    public DiveListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new Dive(getActivity());
        mDbHelper.open();
        setListAdapter(createAdapter());   
        }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         if (savedInstanceState != null && savedInstanceState
              .containsKey(STATE_ACTIVATED_POSITION)) {
           setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mDbHelper.close(); 
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mDbHelper.open(); 
        setListAdapter(createAdapter()); 
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected( id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    SimpleCursorAdapter createAdapter () {
    
    Cursor diveList = mDbHelper.fetchAll();
    String[] from = new String[] {Dive.KEY_NUMBER,Dive.KEY_DATE,Dive.KEY_TIME,Dive.KEY_SITENAME};        
    int[] to = new int[] {R.id.list_number,R.id.list_date,R.id.list_time,R.id.list_site} ;
    mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.dive_list_row, diveList, from, to, 0);
    
    return mAdapter ;
    }
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getActivity().getMenuInflater(); 
		mi.inflate(R.menu.list_menu_item_longpress, menu); 
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case R.id.menu_delete:
    		// AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        
	        return true;
		}
		return super.onContextItemSelected(item);
	}
    
}
