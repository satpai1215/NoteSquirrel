package com.example.notesquirrel2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String DEBUGTAG = "SMP";
	public static final String TEXTFILE = "notesquirrel.txt";
	public static final String FILESAVED = "FileSaved";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addSaveButtonListener();
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		boolean fileSaved = prefs.getBoolean(FILESAVED, false);
		
		//only load file if there was a file saved from before
		if(fileSaved) {
			loadSavedFile();
		}
	}

	private void loadSavedFile() {
		try {
			FileInputStream fis = openFileInput(TEXTFILE);

			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			
			EditText editText = (EditText) findViewById(R.id.text);
			
			String line;
			while((line = reader.readLine()) != null) {
				editText.append(line);
				editText.append("\n");
			}
			fis.close();
			
		} catch (Exception e) {
			Log.d(DEBUGTAG, "Unable to READ file");
		}
	}

	private void addSaveButtonListener() {
		Button saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText editText = (EditText) findViewById(R.id.text);
				String text = editText.getText().toString();
				try {
				
					FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_PRIVATE);
					fos.write(text.getBytes());
					fos.close();
					
					//note file was saved in Shared Preferences
					SharedPreferences prefs = getPreferences(MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean(FILESAVED, true);
					editor.commit();
									
				} catch (Exception e) {
					Toast toast = Toast.makeText(MainActivity.this, (R.string.toast_cant_save), Toast.LENGTH_LONG);
					toast.show();
				}

			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
