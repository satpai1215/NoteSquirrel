package com.example.notesquirrel2;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class ImageActivity extends Activity implements PointCollectorListener {
	
	private PointCollector pointCollector = new PointCollector();
	private Database db = new Database(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		addTouchListener();
		showPrompt();
		
		pointCollector.setListener(this);
	}
	
	private void showPrompt() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		builder.setTitle("Create Your Passpoint Sequence");
		builder.setMessage("Touch four points on the image to set the Passpoint Sequence. You must click the same points in the future to gain access to your notes.");
		
		AlertDialog dlg = builder.create();
		
		dlg.show();
		
	}

	private void addTouchListener() {
		ImageView image = (ImageView) findViewById(R.id.touch_image);
		
		image.setOnTouchListener(pointCollector);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

	public void pointsCollected(final List<Point> points) {
		Log.d(MainActivity.DEBUGTAG, "Collected " + points.size() + " points.");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.storing_data);
		
		final AlertDialog dlg = builder.create();
		dlg.show();
		
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				//delay to see dialog
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
				}
				
				//stores points to notes.db (see Database.java)
				db.storePoints(points);
				Log.d(MainActivity.DEBUGTAG, "PassPoints Stored");
				
				return null;
			}

			//runs after doInBackground method finishes
			protected void onPostExecute(Void result) {
				dlg.dismiss();
			}
			
		};
		
		task.execute();
		
		List<Point> list = db.getPoints();
		
		for(Point point: list) {
			Log.d(MainActivity.DEBUGTAG, String.format("Got point: (%d, %d)",  point.x, point.y));
		}
		
	}

}
