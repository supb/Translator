package com.example.translate;

import org.ispeech.SpeechSynthesis;
import org.ispeech.error.InvalidApiKeyException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.memetix.mst.language.Language;

public class MainActivity extends Activity {
	private EditText editText;
	public static SpeechSynthesis tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        try {
			tts = SpeechSynthesis.getInstance(this);
		} catch (InvalidApiKeyException e) {
			Log.i("Error", "Key was invalid");
			Log.i("Key was invalid","Error");
			e.printStackTrace();
		}
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void OnButtonClickChangeTextOfTextView1( View view){
    	editText = (EditText) findViewById(R.id.editText1);
    	String toTranslate = editText.getText().toString();
    	
    	Phrase phraseToTranslate = new Phrase(toTranslate, Language.ENGLISH, 
    			Language.SPANISH);
    	
    	PhraseExtraThread task = new PhraseExtraThread();
    	task.execute(new Phrase[]{phraseToTranslate});
    }
    
    public class PhraseExtraThread extends AsyncTask<Phrase,Void,String> {

    	@Override
    	protected String doInBackground(Phrase... phrases) {
    		
    	    	Phrase test = phrases[0];
    	    	
    	    	test.doTranslationAndAudio();
    	    	return test.toString();

    	}

    	@Override
    	protected void onPostExecute(String result){
    		editText.setText(result);
    	}

    }

}
