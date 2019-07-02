package moffice.meta.com.molibrary.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import moffice.meta.com.molibrary.firebase.MOMesgHandler;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.utility.CommonDataArea;

import static moffice.meta.com.molibrary.core.MOMain.context;

/**
 * Created by com.moffice.com.microoffice.app on 10/02/2017.
 */

public class MOOfficeLocManager  {

    public static String officeName;
    public static String officeID;
    public static int markType; //0-Office ID ,1-Office Loc Name, 2-Office Co-ordinates
    public static int MOA_MARK_OFFICEID=0;
    public static int MOA_MARK_OFFICE_LOCNAME=1;
    public static int MOA_MARK_OFFICE_COORDINATES=2;

    private LocationManager locationManager;
    private String provider;
    public static void markOfficeLocation(Activity context, int requID) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            context.startActivityForResult(builder.build(context), requID);
        } catch (Exception exp) {

        }
    }
    /*
* Calculate distance between two points in latitude and longitude taking
* into account height difference. If you are not interested in height
* difference pass 0.0. Uses Haversine method as its base.
*
* lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
* el2 End altitude in meters
* @returns Distance in Meters
*/
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }

    public static boolean detectOffice(Context context, Location loc) {
        try {
            String officeNameLoc,officeAddr;
            Dao<Offices, Integer> officeList = CommonDataArea.getHelper(context).getOfficesDao();
            Double longi;
            Double lati;
            for (Offices office : officeList) {
//                officeAddr=office.getAddress();
                officeNameLoc = office.getOfficeName();
                String latiStr = Double.toString(office.getLatitude());
                String longiStr = Double.toString(office.getLongitude());
                longi = Double.valueOf(longiStr);
                lati = Double.valueOf(latiStr);
                double distance = distance(lati, loc.getLatitude(), longi, loc.getLongitude(), 0.0, 0.0);
                if (distance < 500)
                {
                    officeName=officeNameLoc;
                    officeID= office.getOfficeID();
                    return true;
                }
            }
            officeName=null;
            return false;
        }catch (Exception exp){
            return false;
        }
    }

    public static boolean createOfficeLocation(Place place,String placeName, Context context) {
            Offices NewOffice = new Offices();
            String officeUUID = UUID.randomUUID().toString();
            if(placeName==null)
                NewOffice.setOfficeName(String.format("%s", place.getName()));
            else
                NewOffice.setOfficeName(String.format("%s", placeName));
            NewOffice.setOfficeID(officeUUID);
            NewOffice.setLatitude(place.getLatLng().latitude);
            NewOffice.setLongitude(place.getLatLng().longitude);
            NewOffice.setAddress(String.format("%s", place.getAddress()));
            try {
                CommonDataArea.getHelper(context).getOfficesDao().create(NewOffice);
                CommonDataArea.closeHelper();
            } catch (SQLException e) {
               return  false;
            }
            return true;
    }

    public static void deleteOffice(int officeID){
        try {
//Get helper
            DeleteBuilder<Offices, Integer> deleteBuilder =
                    MOMain.getHelper().getOfficesDao().deleteBuilder();

            deleteBuilder.where().eq("ID", officeID);
            deleteBuilder.delete();
        }catch (Exception exp){

        }
    }

    public static boolean saveOfficeLocation(String officeName, String officeID,String longi, String lati, String address,Context context) {
        boolean insert = true;
        List<Offices> officeList = null;
        try {
            // userList = MOMain.getHelper().getById(UserList.class, uuid);
            QueryBuilder<Offices, Integer> queryBuilder =
                    MOMain.getHelper().getOfficesDao().queryBuilder();
// get the WHERE object to build our query
            Where<Offices, Integer> where = queryBuilder.where();
// the name field must be equal to "foo"UserList
            where.eq("OfficeID", officeID);
            officeList =queryBuilder.query();
            if((officeList!=null)&&(officeList.size()>0)){
                insert=false;
            }else insert =true;

        Dao<Offices, Integer> officeListDb = CommonDataArea.getHelper(context).getOfficesDao();
        if(!insert) officeListDb.delete(officeList.get(0));
        Offices NewOffice = new Offices();
        NewOffice.setOfficeName(String.format("%s", officeName));
        NewOffice.setLatitude(Double.valueOf(lati));
        NewOffice.setLongitude(Double.valueOf(longi));
        NewOffice.setAddress(String.format("%s", address));
        NewOffice.setOfficeID(officeID);
        officeListDb.create(NewOffice);
        return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static  void sendOfficeLocListRequest()
    {
        MOMesgHandler attnMesg = new MOMesgHandler();
        attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), "Admin", MOMain.MOMESG_OFFICELOC_REQ);
        JSONObject mesg = attnMesg.assembleJSONMesg();
        boolean sendSuccess = attnMesg.sendMessageOnThread(mesg);
    }
    public static void sendOfficeLocations(Context context, String userUUID) {
        try {
            String officeName;
            Dao<Offices, Integer> officeList = CommonDataArea.getHelper(context).getOfficesDao();
            int i=0;
            for( Offices office :officeList ){

                    MOMesgHandler attnMesg = new MOMesgHandler();
                    attnMesg.MoMesgCreate(CommonDataArea.getUserUUID(), userUUID, MOMain.MOMESG_OFFICELOC_MESG);
                    officeName = office.getOfficeName();
                    String latiStr = Double.toString(office.getLatitude());
                    String longiStr = Double.toString(office.getLongitude());
                    String address = office.getAddress();
                    attnMesg.addAttribute("OfficeName", officeName);
                    attnMesg.addAttribute("Longi", longiStr);
                    attnMesg.addAttribute("Lati", latiStr);
                    attnMesg.addAttribute("Address", address);
                    attnMesg.addAttribute("OfficeID", office.getOfficeID());
                    ++i;
                    JSONObject mesg = attnMesg.assembleJSONMesg();
                    boolean sendSuccess = attnMesg.sendMessageOnThread(mesg);
            }
        }catch(Exception exp){

        }
    }

    public static JSONArray getOfficeLocations(Context context) {
        try {
            String officeName;
            Dao<Offices, Integer> officeList = CommonDataArea.getHelper(context).getOfficesDao();
            int i=0;
           // JSONObject officeListMain = new JSONObject();
            JSONArray officeListArray = new JSONArray();
            for( Offices office :officeList ){

                JSONObject officeJson = new JSONObject();

                officeName = office.getOfficeName();
                String latiStr = Double.toString(office.getLatitude());
                String longiStr = Double.toString(office.getLongitude());
                String address = office.getAddress();
                officeJson.put("OfficeName", officeName);
                officeJson.put("Longi", longiStr);
                officeJson.put("Lati", latiStr);
                officeJson.put("Address", address);
                officeJson.put("OfficeID", office.getOfficeID());
                officeListArray.put(officeJson);
            }

            //officeListMain.put("OfficeList",officeListArray);
            return officeListArray;//officeListMain;
        }catch(Exception exp){
            return  null;
        }
    }



    public static Location getLocation(Context context)
    {
        // Get the location manager
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return null;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
//        locationManager.requestLocationUpdates(provider, 400, 1, (android.location.LocationListener) context);

        Location myLocation = getLastKnownLocation();

        return myLocation;

    }

    private static Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers =  locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l =  locationManager.getLastKnownLocation(provider);


            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
//                ALog.d("found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;    }

    public static void onOfficeDeleted(Activity context, int officeNum) {
        SharedPreferences settings = context.getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = settings.edit();
        ed.putBoolean(MOMain.SHAREDPREF_OFFICEENABLED + officeNum, false);
    }


}
