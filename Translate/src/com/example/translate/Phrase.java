package com.example.translate;


//have to dl jar file from 
//	https://code.google.com/p/microsoft-translator-java-api/downloads/list
// i used the one with dependencies


import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.NoNetworkException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.memetix.mst.language.Language;
import com.memetix.mst.language.SpokenDialect;
import com.memetix.mst.speak.Speak;
import com.memetix.mst.translate.Translate;

/*add these to bin/AndroidManifest.xml for internet access
 *  <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    
 */

public class Phrase {
	public String preTranslate;
	public String postTranslate;
	public Language preTranslateLanguage;
	public Language postTranslateLanguage;
	public final String ERROR_MESSAGE = " WAS UNSUCCESSFUL ";
	//Need to add an instance variable for the audio call it postTranslateAudio
	public Translate toUseForTranslation;
	private final String Cid = "Babel_Fish";
	private final String Cs = "xUeWbxRfe3XId8MEIzjP9eaWULjPgPAJMnrcilz9nT4=";
	public String urlOfAudioTranslation;
	
	
	/*Constructor takes in the 
		String to be translated
		Language the string currently is 
		Language we want the string to be
			Language objects are from com.memetix.mst.language.Language
	*/
	public Phrase (String toTranslate, Language preLanguage, 
			Language postLanguage){
		preTranslate = toTranslate;
		preTranslateLanguage = preLanguage;
		postTranslateLanguage = postLanguage;
		postTranslate = null;//redundant
		
		
		
		Speak.setClientId(Cid);
		Speak.setClientSecret(Cs);
		
	}
	
	/*
	 * If the translation is unsuccessful using the call to bing-translate-api
	 * 		THEN postTranslate will be "FAILED IN doTranslation"
	 */
	public void doTranslation(){
		try {
			Translate.setClientId(Cid);
			//FILL IN STRING
			//Was toUseForTranslation
			Translate.setClientSecret(Cs);
			postTranslate = Translate.execute(preTranslate, preTranslateLanguage
					,postTranslateLanguage);
		} catch (Exception e) {
			postTranslate = "hi";
			postTranslate = e.getLocalizedMessage(); 
					e.printStackTrace();
		}
	}
	
	public void doAudio(){
		//This is using microsofts speech, not as good as ispeech, imo.
		//But this one is not very spaghetti coded
		try {
			urlOfAudioTranslation = Speak.execute(postTranslate, 
					SpokenDialect.ENGLISH_UNITED_KINGDOM);
		} catch (Exception e) {
			System.out.println("FAILED AT Speak.execute in doAudio");
			e.printStackTrace();
		}
		
		 try {
			 MediaPlayer player = new MediaPlayer();
			 player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			 player.setDataSource(urlOfAudioTranslation);
			 player.prepare();
			 player.start();

		 } catch (Exception e) {
			 // TODO: handle exception
		 }

	}
	
	public void doAudioSecondAttempt(){
		//Better audio, but very very spaghetti coded.
		//Also, No idea how this works, but apparently you can create a whole class
		//and pass that as a parameter.
		
		//dragged and dropped ispeech-sdk-1.4.2.jar into project 
		/* added the following lines to translate manifest
		<activity android:name="org.ispeech.iSpeechFrameWork" android:theme="@android:style/Theme.Translucent" />
        <meta-data android:name="ispeech_api_key" android:value="2870398267b8f52f6fa6b6b7f19d780b" />
        <meta-data android:name="debug" android:value="false"/>
		 */
				
		
		MainActivity.tts.setSpeechSynthesisEvent(new SpeechSynthesisEvent() {
			
			public void onPlaySuccessful() {
				Log.i("tts", "onPlaySuccessful");
			}

			public void onPlayStopped() {
				Log.i("tts", "onPlayStopped");
			}

			public void onPlayFailed(Exception e) {
				Log.e("tts", "onPlayFailed");
				String error = e.toString();
				Log.e("TTS", error);

			}

			public void onPlayStart() {
				Log.i("tts", "onPlayStart");
			}

			@Override
			public void onPlayCanceled() {
				Log.i("tts", "onPlayCanceled");
			}
			
			
		});
		
		try {
			
			MainActivity.tts.speak(this.postTranslate);

		} catch (BusyException e) {
			Log.e("tts", "SDK is busy");
			e.printStackTrace();

		} catch (NoNetworkException e) {
			Log.e("tts", "Network is not available\n" + e.getStackTrace());

		}
	}
	
	public void doTranslationAndAudio(){
		this.doTranslation();
		//this.doAudio(); //uncomment this for microsofts
		this.doAudioSecondAttempt();// comment this out to switch
	}
	
	public String toString(){
		if (postTranslate == null){
			return preTranslate + ERROR_MESSAGE + "LY translated";
		}else {
			return postTranslate;
		} 
	}
}
