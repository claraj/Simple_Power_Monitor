package com.example.power;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends ActionBarActivity {

	PowerReceiver mPowerReceiver;
	IntentFilter mDisconnectIntentFilter;
	IntentFilter mConnectIntentFilter;

	final static String CHARGER_CONNECTED = "Charger is ** connected **";
	final static String CHARGER_NOT_CONNECTED = "Charger is ** not connected **";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPowerReceiver = new PowerReceiver();
		mDisconnectIntentFilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
		mConnectIntentFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);

		//Determine current charging state so can display appropriate message when app starts (or device is rotated)

		TextView powerTV = (TextView) findViewById(R.id.power);

		//Need to create a filter for the type of Intents we wish to receive
		IntentFilter batteryStatusFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		//Don't need to specify a receiver - for sticky intents, anything can read them at any time.
		//registerReceiver will return the Intent for the filter we specify; can read data from this intent
		Intent batteryStatus = registerReceiver(null, batteryStatusFilter);
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

		if (status == BatteryManager.BATTERY_PLUGGED_AC) {
			powerTV.setText(CHARGER_CONNECTED);
		} else {
			powerTV.setText(CHARGER_NOT_CONNECTED);
		}


		//Calculate the current battery level, as a percentage
		//The battery has a maximum level, which is obtained from BatteryManager.EXTRA_SCALE
		//And the current level, from BatteryManager.EXTRA_LEVEL, which will be between 0 and the max
		//NOTE that the following two method calls both return int values,
		//but since we need to do math and deal with floating point, then store as a float to avoid rounding.
		float maxLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

		//Use these two numbers to calculate a percentage value
		int batteryPercentage = (int) (level / maxLevel  * 100);

		//Display current battery in TextView. Also note that this is the percentage when this code
		//runs - it won't update until onCreate runs again. If you need to continually monitor battery status,
		//you'll need to keep reading these values from the batteryStatus.
		TextView batteryLevelTV = (TextView) findViewById(R.id.battery);

		//So to remind us that this is a snapshot of the battery status, add the
		//current date and time to the message. A new Date object is initialized to the date/time of its creation.

		batteryLevelTV.setText("The battery level at " +  new Date() + " is " + batteryPercentage + "%");


	}

	@Override
	protected void onResume() {
		super.onResume();

		//Register receivers. One BroadcastReceiver will handle both types of Intents
		registerReceiver(mPowerReceiver, mDisconnectIntentFilter);
		registerReceiver(mPowerReceiver, mConnectIntentFilter);

	}

	@Override
	protected void onPause(){
		super.onPause();
		//Unregister receivers here - no point being notified if Activity is paused
		//One call will unregister all BroadcastReceivers
		unregisterReceiver(mPowerReceiver);

	}






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
