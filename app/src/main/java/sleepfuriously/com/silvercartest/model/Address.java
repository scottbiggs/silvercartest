package sleepfuriously.com.silvercartest.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Address data
 */
public class Address {

    //-------------------
    //  constants
    //-------------------

    /** accessors for json objects */
    public static final String
        ID_KEY = "id",
        LINE1_KEY = "line1",
        LINE2_KEY = "line2",
        CITY_KEY = "city",
        STATE_KEY = "state",
        ZIP_KEY = "zip",
        COUNTRY_KEY = "country",
        LATITUDE_KEY = "latitude",
        LONGITUDE_KEY = "longitude";

    //-------------------
    //  data
    //-------------------

    public long id;
    public String line1;
    public String line2;
    public String city;
    public String state;
    public String zip;
    public String country;
    public double latitude;
    public double longitude;


    //-------------------
    //  methods
    //-------------------

    public Address (JSONObject jsonObject) {
        try {
            id = jsonObject.getLong(ID_KEY);
            line1 = jsonObject.getString(LINE1_KEY);
            line2 = jsonObject.getString(LINE2_KEY);
            city = jsonObject.getString(CITY_KEY);
            state = jsonObject.getString(STATE_KEY);
            zip = jsonObject.getString(ZIP_KEY);
            country = jsonObject.getString(COUNTRY_KEY);
            latitude = jsonObject.getDouble(LATITUDE_KEY);
            longitude = jsonObject.getDouble(LONGITUDE_KEY);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
