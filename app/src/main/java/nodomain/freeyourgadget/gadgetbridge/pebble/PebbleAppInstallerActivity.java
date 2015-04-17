package nodomain.freeyourgadget.gadgetbridge.pebble;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nodomain.freeyourgadget.gadgetbridge.BluetoothCommunicationService;
import nodomain.freeyourgadget.gadgetbridge.GBDeviceApp;
import nodomain.freeyourgadget.gadgetbridge.R;


public class PebbleAppInstallerActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    TextView debugTextView;
    Button installButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinstaller);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        debugTextView = (TextView) findViewById(R.id.debugTextView);
        installButton = (Button) findViewById(R.id.installButton);

        final Uri uri = getIntent().getData();
        PBWReader pbwReader = new PBWReader(uri, getApplicationContext());
        GBDeviceApp app = pbwReader.getGBDeviceApp();

        if (pbwReader != null) {
            if (pbwReader.isFirmware()) {
                debugTextView.setText("YOUR ARE TRYING TO INSTALL A FIRMWARE, PROCEED AT YOUR OWN RISK, MAKE SURE THIS FIRMWARE IS FOR YOUR PEBBLE REVISION, THERE ARE NO CHECKS.\n\n\n");

            } else if (app != null) {
                debugTextView.setText("You are about to install the following app:\n\n\n" + app.getName() + " Version " + app.getVersion() + " by " + app.getCreator() + "\n");
            }
            installButton.setEnabled(true);
            installButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startIntent = new Intent(PebbleAppInstallerActivity.this, BluetoothCommunicationService.class);
                    startIntent.setAction(BluetoothCommunicationService.ACTION_INSTALL_PEBBLEAPP);
                    startIntent.putExtra("app_uri", uri.toString());
                    startService(startIntent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
