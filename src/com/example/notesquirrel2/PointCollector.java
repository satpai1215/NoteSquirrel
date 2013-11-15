package com.example.notesquirrel2;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PointCollector implements OnTouchListener {
	
	private PointCollectorListener listener;
	private List<Point> points = new ArrayList<Point>();
	
			
	public boolean onTouch(View v, MotionEvent event) {
		int x = Math.round(event.getX());
		int y = Math.round(event.getY());
		
		String message = String.format("Coordinates: (%d, %d)", x, y);
		Log.d(MainActivity.DEBUGTAG, message);
		
		points.add(new Point(x,y));
		
		if (points.size() == 4) {
			if (listener != null) {
				listener.pointsCollected(points);
				points.clear();
			}
		}
				
		return false;
	}


	public void setListener(PointCollectorListener listener) {
		this.listener = listener;
	}

}
