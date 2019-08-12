package sleepfuriously.com.silvercartest.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sleepfuriously.com.silvercartest.model.Location;


/**
 * All access to data comes through this class (using the
 * Singleton Pattern).
 *
 * The network and caching is done here.
 */
public class ModelWindow {

    //------------------------
    //  constants
    //------------------------

    private static final String DTAG = ModelWindow.class.getSimpleName();

    /** Probably not used, but provided for completeness. */
    private static final String LOCATION_SUCCESS_MSG = "Location retrieval successful.";

    /** URL base for all data access for this project */
    public static final String URL_BASE = "https://api.rac-tst.com/";

    /** URL for accessing the location list */
    public static final String LOCATION_LIST_URL = URL_BASE + "locations";

    /** required header for api response */
    public static final String
            API_HEADER_KEY = "Api-Version",
            API_HEADER_VALUE = "2";

    /**
     * Suffix strings to specify sorting of the locations by name.
     *
     */
    public static final String
            API_SORT_ASCENDING = "?sort=name",
            API_SORT_DESCENDING = "?sort=-name";

    /**
     * Suffix string to specify a specific page of information from an api request.
     * Just supply a number immediately after this string to get that page.
     * Oh yeah, each page is 25 items.
     */
    public static final String API_PAGE_SPECIFIER = "?page=";


    /** base URL for images */
    public static final String IMAGE_BASE_URL = "https://s3.amazonaws.com/production-silvercar-static-assets/";

    /** indicates to retrieve an asset. Should be concatenated to IMAGE_BASE_URL */
    public static final String IMAGE_ASSET_SPECIFIER = "assets/location-assets/";

    /** image suffix--all images will end with this string */
    public static final String IMAGE_FILENAME_SUFFIX = "_location_3x.jpg";



    //------------------------
    //  data
    //------------------------

    private static ModelWindow mInstance = null;


    //------------------------
    //  methods
    //------------------------

    private ModelWindow() {
    }

    /**
     * As per the Singleton Pattern, return an instance of this
     * class.
     */
    public static synchronized ModelWindow getInstance() {
        if (mInstance == null) {
            // lazy instantiation--my fave!
            mInstance = new ModelWindow();
        }
        return  mInstance;
    }

    /**
     * Returns a list of all the locations to display.
     *
     * @param listener  The instance that implements {@link ModelWindowLocationsListener}.
     *                  Its {@link ModelWindowLocationsListener#returnLocationList(List, boolean, String)}
     *                  method will be called when the value has been retrieved.
     *
     * @param ctx   Ye good ol' Context.
     */
    public void getLocationList(final ModelWindowLocationsListener listener, Context ctx) {

        RequestQueue q = Volley.newRequestQueue(ctx);


/*
        StringRequest getRequest = new StringRequest(Request.Method.GET, LOCATION_LIST_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        )
            {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("User-Agent", "Nintendo Gameboy");
                params.put("Accept-Language", "fr");

                return params;
            }
        };
*/




        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, LOCATION_LIST_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Location> locationList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject)response.get(i);
                                locationList.add(new Location(jsonObject));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listener.returnLocationList(locationList, true, LOCATION_SUCCESS_MSG);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.returnLocationList(null, false, error.getMessage());
                    }
                }
                )

            // the following weirdness is needed to insert the proper header into
            // the GET request
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(API_HEADER_KEY, API_HEADER_VALUE);
                return params;
            }
        };
        q.add(request);
    }


    //------------------------
    //  interfaces
    //------------------------

    /**
     * Implement this interface to receive a callback when the
     * locations list is ready.
     */
    public interface ModelWindowLocationsListener {

        /**
         * Called when the location list is ready.
         *
         * @param locations  A List of locations
         * @param successful Tells if the request was successful
         * @param msg        If not successful, an error message
         */
        void returnLocationList(List<Location> locations, boolean successful, String msg);
    }



}
