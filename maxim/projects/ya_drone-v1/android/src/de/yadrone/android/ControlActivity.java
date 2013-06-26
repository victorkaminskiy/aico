package de.yadrone.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.shigeodayo.ardrone.ARDrone;

public class ControlActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        
        initButtons();
        
        Toast.makeText(this, "Touch and hold the buttons", Toast.LENGTH_SHORT).show();
    }
    
    private void initButtons()
	{
    	YADroneApplication app = (YADroneApplication)getApplication();
    	final ARDrone drone = app.getARDrone();
    	
    	Button forward = (Button)findViewById(R.id.cmd_forward);
    	forward.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.forward(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});
    	
    	Button backward = (Button)findViewById(R.id.cmd_backward);
    	backward.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.backward(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});

    	
    	Button left = (Button)findViewById(R.id.cmd_left);
    	left.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.goLeft(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});

    	
    	Button right = (Button)findViewById(R.id.cmd_right);
    	right.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.goRight(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});
    	
    	Button up = (Button)findViewById(R.id.cmd_up);
    	up.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.up(40);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});
    	
    	Button down = (Button)findViewById(R.id.cmd_down);
    	down.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.down(40);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});

    	
    	Button spinLeft = (Button)findViewById(R.id.cmd_spin_left);
    	spinLeft.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.spinLeft(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});

    	
    	Button spinRight = (Button)findViewById(R.id.cmd_spin_right);
    	spinRight.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
					drone.spinRight(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
	                drone.stop();

				return true;
			}
		});
    	 
    	Button landing = (Button)findViewById(R.id.cmd_landing);
    	landing.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				drone.landing();
			}
		});
    	
    	Button emergency = (Button)findViewById(R.id.cmd_emergency);
    	emergency.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				drone.reset();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_control, menu);	    
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent i;
	    switch (item.getItemId()) 
	    {
	    	case R.id.menuitem_navdata:
	    		i = new Intent(this, NavDataActivity.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(i);
		        return true;
	    	case R.id.menuitem_main:
	    		i = new Intent(this, MainActivity.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(i);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

    
}
