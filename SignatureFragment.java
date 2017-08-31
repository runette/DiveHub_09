package com.runette.divehub;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SignatureFragment extends Fragment {
	private SignatureView sigedit  ;
	private Button mAcceptButton ;
	private Button mCancelButton ;
	private Signature signature  ;

	
	
	public SignatureFragment() {
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.	onCreate(savedInstanceState);
        setHasOptionsMenu(false);  
        signature = new Signature(getActivity());
    }
	//
    // Create the layout
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signature, container, false);
        
        
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		sigedit = (SignatureView) getActivity().findViewById(R.id.signature_view);
		sigedit.setSignature(signature);

        //
        // get the relevant dive by ID passed in from the parent activity using args.
        // if this is a new dive to be created then NEW_DIVE_FLAG will be passed in 
        //
        if (getArguments().containsKey(Dive.ARG_ITEM_ID)) {
        	long id = getArguments().getLong(Dive.ARG_ITEM_ID);
            signature.get(id );
        }
		if (getActivity().findViewById(R.id.signature_accept) != null) {
	        mAcceptButton = (Button) getActivity().findViewById(R.id.signature_accept);
	        mCancelButton = (Button) getActivity().findViewById(R.id.signature_clear); 
	        mAcceptButton.setOnClickListener(new View.OnClickListener() {
	    		//
	    		// The aCCEPT Button Triggers a getSignature from the SignatureView, 
	    		// a callback to put into the activity and a shutdwon
	    		// 
	    		//	
	    			public void onClick(View v) {
	    				signature.put();
	    				getActivity().finish();
	    		    	return;
	    		    }
	    		}); 
	        mCancelButton.setOnClickListener(new View.OnClickListener() {
	    		//
	    		// The cancel Button Triggers a getSignature from the SignatureView, 
	    		// a callback to put into the activity and a shutdwon
	    		// 
	    		//	
	    			public void onClick(View v) {
	    				signature.clear();
	    		    	sigedit.clear();
	    		    	sigedit.setSignature(signature);
	    		    	return ;
	    		    }
	    		}); 
		}
	}
	@Override
    public void onResume() {
		
		super.onResume();
        
        
	}
	@Override
    public void onPause() {
        super.onPause();
        signature.put();
	}
	
	public void put() {
		signature.put() ;
	}
	
	public void clear () {
		signature.clear();
    	sigedit.clear();
    	sigedit.setSignature(signature);
	}

}
