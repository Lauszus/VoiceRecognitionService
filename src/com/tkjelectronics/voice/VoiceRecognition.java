package com.tkjelectronics.voice;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceRecognition extends Activity implements OnClickListener {

	public TextView mText;
	public ListView mList;
	public SpeechRecognizer sr;
	public static final String TAG = "VoiceTest";
	
	private static final int REQUEST_CODE = 1234;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button buttonList = (Button) findViewById(R.id.buttonList);
		Button buttonIntent = (Button) findViewById(R.id.buttonIntent);

		buttonList.setOnClickListener(this);
		buttonIntent.setOnClickListener(this);

		mText = (TextView) findViewById(R.id.textView1);
		mList = (ListView) findViewById(R.id.list);

		initSpeechRecognizer();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.buttonList) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			// Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			//intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.tkjelectronics.voice");
			// intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
			sr.startListening(intent);
		}
		if (v.getId() == R.id.buttonIntent) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    	//Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
	        startActivityForResult(intent, REQUEST_CODE);
		}
	}
	
	/**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);            
            float[] value = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);            
            mText.setText("Results: " + String.valueOf(matches.size()));
            
            if(value != null) { // EXTRA_CONFIDENCE_SCORES wasn't added until API level 14
            	String[] combined = new String[matches.size()];
                for(int i = 0; i < matches.size(); i++) // The size of the data and value is the same
                	combined[i] = matches.get(i).toString() + "\nScore: " + Float.toString(value[i]);                
                mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,combined));            	
            } else
            	mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,matches));                        
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void initSpeechRecognizer() {
    	if(sr == null) {
    		sr = SpeechRecognizer.createSpeechRecognizer(this);
    		if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
    			Toast.makeText(getApplicationContext(),"Speech Recognition is not available",Toast.LENGTH_LONG).show();
    			finish();
    		}
    		sr.setRecognitionListener(new VoiceRecognitionListener(this));    		
    	}
    }
    @Override
    protected void onResume() {    	
    	super.onResume();
    	initSpeechRecognizer();
    }
    
    @Override
	protected void onDestroy() {
    	if (sr != null) {
        	sr.stopListening();
        	sr.cancel();
        	sr.destroy();
        }
    	super.onDestroy();
    }
}