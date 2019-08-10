package sleepfuriously.com.silvercartest.model;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data representation of a Location.
 */
public class Location {

    //-------------------
    //  constants
    //-------------------

    /** date pattern used by server data */
    private static final String DATE_PATTERN = "yyyy-mm-dd";

    /** keys to get class data from JSON objects */
    public static final String
        ID_KEY = "id",
        NAME_KEY = "name",
        AIRPORT_CODE_KEY = "airport_code",
        PHONE_NUMBER_KEY = "phone_number",
        TEXT_NUMBER_KEY = "text_number",
        FLEET_TYPE_KEY = "fleet_type",
        MULTI_CAR_DISPLAY_NAME_KEY = "multi_car_display_name",
        OPEN_DATE_KEY = "open_date",
        CLOSE_DATE_KEY = "close_date",
        GDS_CODE_KEY = "gds_code",
        DELIVERABLE_KEY = "deliverable",
        DELIVERABLE_DESC_KEY = "deliverable_description",
        DESCRIPTION_KEY = "description",
        TIME_ZONE_KEY = "time_zone",
        BOOKABLE_KEY = "bookable",
        HOURS_KEY = "hours",
        ASSET_CODE_KEY = "asset_code",
        ADDRESS_KEY = "address",
        CURBSIDE_DETAIL_KEY = "curbside_detail";

    //-------------------
    //  data
    //-------------------

    // NOTE: public for simplicity.  Also, class names reflect C version from server (presumably)

    public long id;
    public String name;
    public String airport_code;
    public String phone_number;
    public String text_number;
    public String fleet_type;
    public String multi_car_display_name;
    public Date open_date;
    public Date close_date;
    public String gds_code;
    public boolean deliverable;
    public String deliverable_description;
    public String description;
    public String time_zone;
    public boolean bookable;
    public String hours;
    public String asset_code;
    public Address address;
    public String curbside_detail;

    //-------------------
    //  methods
    //-------------------


    public Location(JSONObject jsonObject) {
        try {
            id = jsonObject.getLong(ID_KEY);
            name = jsonObject.getString(NAME_KEY);
            airport_code = jsonObject.getString(AIRPORT_CODE_KEY);

            phone_number = jsonObject.getString(PHONE_NUMBER_KEY);
            text_number = jsonObject.getString(TEXT_NUMBER_KEY);

            fleet_type = jsonObject.getString(FLEET_TYPE_KEY);
            multi_car_display_name = jsonObject.getString(MULTI_CAR_DISPLAY_NAME_KEY);

            String openDateStr = jsonObject.getString(OPEN_DATE_KEY);
            open_date = convertStringToDate(openDateStr);

            String closeDateStr = jsonObject.getString(CLOSE_DATE_KEY);
            close_date = convertStringToDate(closeDateStr);

            gds_code = jsonObject.getString(GDS_CODE_KEY);
            deliverable = jsonObject.getBoolean(DELIVERABLE_KEY);

            deliverable_description = jsonObject.getString(DELIVERABLE_DESC_KEY);
            description = jsonObject.getString(DESCRIPTION_KEY);

            time_zone = jsonObject.getString(TIME_ZONE_KEY);
            bookable = jsonObject.getBoolean(BOOKABLE_KEY);

            hours = jsonObject.getString(HOURS_KEY);
            asset_code = jsonObject.getString(ASSET_CODE_KEY);

            JSONObject addressObject = jsonObject.getJSONObject(ADDRESS_KEY);
            address = new Address(addressObject);

            curbside_detail = jsonObject.getString(CURBSIDE_DETAIL_KEY);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a string in the format yyyy-mm-dd to a Date instance, which is
     * returned.
     *
     * @return  An instance of the Date class set to the string's date.
     *          Null on error.
     */
    private Date convertStringToDate(String dateStr) {

        @SuppressLint("SimpleDateFormat")   // pattern is not location-dependent
        DateFormat format = new SimpleDateFormat(DATE_PATTERN);

        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
