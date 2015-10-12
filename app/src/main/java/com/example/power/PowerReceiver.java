package com.example.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;


public class PowerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		//Context should be the current Activity on the screen
		MainActivity activity = (MainActivity) context;

		TextView powerTV = (TextView)activity.findViewById(R.id.power);

		if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
			powerTV.setText(MainActivity.CHARGER_NOT_CONNECTED);
		}

		if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
			powerTV.setText(MainActivity.CHARGER_CONNECTED);
		}

	}
}
