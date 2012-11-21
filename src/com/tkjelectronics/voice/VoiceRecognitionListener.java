package com.tkjelectronics.voice;

import java.util.ArrayList;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.ArrayAdapter;

class VoiceRecognitionListener implements RecognitionListener {
	VoiceRecognition mVoiceRecognition;
	//private static final String TAG = "VoiceRecognitionListener";
	
	public VoiceRecognitionListener(VoiceRecognition instance) {
		mVoiceRecognition = instance;
	}
	public void onResults(Bundle data) {
		//Log.d(TAG, "onResults " + data);
		ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		float[] value = data.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
		mVoiceRecognition.mText.setText("Results: " + String.valueOf(matches.size()));
		
		if(value != null) { // CONFIDENCE_SCORES wasn't added until API level 14
        	String[] combined = new String[matches.size()];
            for(int i = 0; i < matches.size(); i++) // The size of the data and value is the same
            	combined[i] = matches.get(i).toString() + "\nScore: " + Float.toString(value[i]);                
            mVoiceRecognition.mList.setAdapter(new ArrayAdapter<String>(mVoiceRecognition, android.R.layout.simple_list_item_1,combined));            	
        } else
        	mVoiceRecognition.mList.setAdapter(new ArrayAdapter<String>(mVoiceRecognition, android.R.layout.simple_list_item_1,matches));
	}
	
	public void onBeginningOfSpeech() {
		//Log.d(TAG, "onBeginningOfSpeech");
		mVoiceRecognition.mText.setText("Sounding good!");
	}
	public void onBufferReceived(byte[] buffer) {
		//Log.d(TAG, "onBufferReceived");
	}
	public void onEndOfSpeech() {
		//Log.d(TAG, "onEndofSpeech");
		mVoiceRecognition.mText.setText("Waiting for result...");
	}
	public void onError(int error) {
		//Log.d(TAG, "error " + error);		
		mVoiceRecognition.mText.setText("error " + error);
	}
	public void onEvent(int eventType, Bundle params) {
		//Log.d(TAG, "onEvent " + eventType);
	}
	public void onPartialResults(Bundle partialResults) {
		//Log.d(TAG, "onPartialResults");
	}
	public void onReadyForSpeech(Bundle params) {
		//Log.d(TAG, "onReadyForSpeech");
	}		
	public void onRmsChanged(float rmsdB) {
		//Log.d(TAG, "onRmsChanged");
	}
}