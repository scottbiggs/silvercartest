package sleepfuriously.com.silvercartest.view;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import sleepfuriously.com.silvercartest.R;

/**
 * Starting point.  Works with both tablets and phones.
 */
public class MainActivity extends AppCompatActivity {

    //-------------------------
    //  constants
    //-------------------------

    private static final String DTAG = MainActivity.class.getSimpleName();

    //-------------------------
    //  widgets
    //-------------------------

    /** displays locations list */
    private RecyclerView mLocationsRV;

    /** give the user something to look at while waiting for data access */
    ProgressDialog mProgressDialog;

    //-------------------------
    //  data
    //-------------------------

    /**
     * Whether or note the Activity is in two-pain mode, ie running on a
     * tablet.
     */
    private boolean mTwoPane;


    /** Adapter for {@link MainActivity#mLocationsRV}. */
    private LocationRVAdapter mLocationAdapter;


    //-------------------------
    //  methods
    //-------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // First things first, gotta be on the  internet!
        if (!isInternetAvailable()) {
            View v = findViewById(android.R.id.content);
            Toast.makeText(this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
            finish();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        String titleStr = getString(R.string.app_name) + " (" +
                getString(R.string.toolbar_title) + ")";
        toolbar.setTitle(titleStr);

        // back button
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // just exit program for this simple programming challenge
                finish();
            }
        });


    } // onCreate(savedInstanceState)


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null)
                && activeNetworkInfo.isAvailable()
                && activeNetworkInfo.isConnected();
    }


}
