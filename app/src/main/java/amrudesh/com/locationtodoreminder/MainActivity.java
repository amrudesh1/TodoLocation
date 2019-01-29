package  amrudesh.com.locationtodoreminder;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;



import amrudesh.com.locationtodoreminder.data.AlarmReminderContract;
import amrudesh.com.locationtodoreminder.data.AlarmReminderDbHelper;
import amrudesh.com.locationtodoreminder.data.AlarmReminderDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mMainButton,mAddReminderButton,mMapsButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    Animation fabOpen,fabClose,fabRotateClock,fabRotateAnti;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;
    Boolean isOpen=false;
    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }
    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;
    private static final int VEHICLE_LOADER = 0;
    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabRotateClock=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabRotateAnti=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setTitle(R.string.app_name);
        mMainButton=(FloatingActionButton)findViewById(R.id.fab);
        mMapsButton=(FloatingActionButton)findViewById(R.id.fab_two);
        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab_one);

        reminderListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);
                Log.i("msg",currentVehicleUri.toString());

                startActivity(intent);

            }
        });
        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen)
                {
                    mMapsButton.startAnimation(fabClose);
                    mAddReminderButton.startAnimation(fabClose);
                    mMainButton.startAnimation(fabRotateAnti);
                    mAddReminderButton.setClickable(false);
                    mMapsButton.setClickable(false);
                    isOpen=false;
                }
                else
                {
                    mMapsButton.startAnimation(fabOpen);
                    mMapsButton.setImageResource(R.drawable.round_location_on_black_18dp);
                    mAddReminderButton.setImageResource(R.drawable.round_calendar_today_black_18dp);
                    mAddReminderButton.startAnimation(fabOpen);
                    mMainButton.startAnimation(fabRotateClock);
                    mAddReminderButton.setClickable(true);
                    mMapsButton.setClickable(true);
                    isOpen=true;
                }
            }
        });

        mMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,location_activity.class));
            }
        });
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE,
                AlarmReminderContract.AlarmReminderEntry.KEY_LOCATION,
                AlarmReminderContract.AlarmReminderEntry.latitude,
                AlarmReminderContract.AlarmReminderEntry.longitude

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
    private void doExit() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("AppTitle");
        alertDialog.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            doExit();
        }

        return super.onKeyDown(keyCode, event);
    }
}
