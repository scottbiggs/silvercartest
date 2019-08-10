package sleepfuriously.com.silvercartest.view;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

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
        setContentView(R.layout.activity_main);

        // First things first, gotta be on the  internet!
//        if (!isInternetAvailable()) {
//            View v = findViewById(android.R.id.content);
//            Snackbar.make(v, R.string.no_internet_warning, Snackbar.LENGTH_LONG).show();
//            finish();
//        }

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

    } // onCreate(savedInstanceState)


    private boolean isInternetAvailable() {
        return false;
    }


}
