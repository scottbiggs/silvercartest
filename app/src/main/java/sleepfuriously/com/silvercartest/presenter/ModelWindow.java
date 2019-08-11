package sleepfuriously.com.silvercartest.presenter;

import android.content.Context;

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
import java.util.List;

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

    /** URL for accessing the album list */
    public static final String LOCATION_LIST_URL = URL_BASE + "locations";

    /** required header for api response */
    public static final String API_HEADER = "Api-Version: 2";

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
    public static ModelWindow getInstance() {
        if (mInstance == null) {
            // lazy instantiation--my fave!
            mInstance = new ModelWindow();
        }
        return  mInstance;
    }

    /**
     * Returns a list of all the albums to display.
     *
     * @param listener  The instance that implements {@link ModelWindowLocationsListener}.
     *                  Its {@link ModelWindowLocationsListener#returnLocationList(List, boolean, String)}
     *                  method will be called when the value has been retrieved.
     *
     * @param ctx   Ye good ol' Context.
     */
    public void getLocationList(final ModelWindowLocationsListener listener, Context ctx) {

        RequestQueue q = Volley.newRequestQueue(ctx);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, LOCATION_LIST_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Location> albumList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject)response.get(i);
                                albumList.add(new Location(jsonObject));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listener.returnLocationList(albumList, true, LOCATION_SUCCESS_MSG);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.returnLocationList(null, false, error.getMessage());
                    }
                });
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