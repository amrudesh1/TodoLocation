package amrudesh.com.locationtodoreminder;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import amrudesh.com.locationtodoreminder.MapsActivity;
import  amrudesh.com.locationtodoreminder.AddReminderActivity;
import amrudesh.com.locationtodoreminder.data.AlarmReminderContract;
public class location_activity extends AppCompatActivity {
    String title;
    private Toolbar mToolbarloc;
    EditText rem;
    TextView loc;
    String latitude, longitude = null;
    MapsActivity mapsAct;
    ImageView current, maps;
    private Uri mCurrentReminderUri;
    AddReminderActivity add=new AddReminderActivity();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        mCurrentReminderUri = i.getData();
        setContentView(R.layout.activity_location_add_reminder);
        mToolbarloc = (Toolbar) findViewById(R.id.toolbar_loc);
        loc = (TextView) findViewById(R.id.loc_txt);
        rem = (EditText) findViewById(R.id.reminder_title);
        setSupportActionBar(mToolbarloc);
        getSupportActionBar().setTitle("Location Reminder");
        maps = (ImageView) findViewById(R.id.maps_icon);
        current = (ImageView) findViewById(R.id.current_icon);
        Intent intent = getIntent();
        Bundle bd = intent.getBundleExtra("personBdl");
        if (bd != null) {
            latitude = bd.getString("lat");
            longitude = bd.getString("lng");
            if ((latitude != null) && (longitude != null)) {
                loc.setText(latitude + ":" + longitude);
            } else {
                loc.setText("Could Not Fetch The Location");
            }

        } else {
            loc.setText("Please pick the Location from the Map");
        }
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(location_activity.this, MapsActivity.class));
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_location_reminder, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save_reminder:


                if (rem.getText().toString().length() == 0){
                    rem.setError("Reminder Title cannot be blank!");
                }

                else {
                    String title=rem.getText().toString();

                    finish();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }



}

