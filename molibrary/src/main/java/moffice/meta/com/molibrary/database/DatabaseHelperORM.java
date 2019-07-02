package moffice.meta.com.molibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.Employees;
import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.models.dbmodels.Products;
import moffice.meta.com.molibrary.models.dbmodels.SupportDetails;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.models.dbmodels.VisitLog;
import moffice.meta.com.molibrary.models.dbmodels.VisitMeetLog;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 */
public class DatabaseHelperORM extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/
    public static final String DATABASE_NAME = "MicroOfficeDB1.db";


    // private static final int DATABASE_VERSION = 5;
    // Added next visit date in VistMeetLog
    private static final int DATABASE_VERSION = 6;
    //Added PlaceVisi,TodaysPLan, DailyAllovance fields in AttenLog

    private Dao<UserList, Integer> userListDao;
    private Dao<AttnLog, Integer> attnLogDao;
    private Dao<LeaveList, Integer> leaveLogDao;

    private Dao<Offices, Integer> officesDao;
    private Dao<VisitLog, Integer> visitLogDao;
    private Dao<UserList, Integer> employeeDao;
    private Dao<EnquiryDetails, Integer> enquiryDao;
    private Dao<SupportDetails, Integer> supportDao;
    private Dao<Products, Integer> productDao;
    private Dao<VisitMeetLog, Integer> visitMeetingDao;

    public DatabaseHelperORM(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.

            TableUtils.createTable(connectionSource, UserList.class);
            TableUtils.createTable(connectionSource, AttnLog.class);
            TableUtils.createTable(connectionSource, Offices.class);
            TableUtils.createTable(connectionSource, VisitLog.class);
            TableUtils.createTable(connectionSource, Employees.class);
            TableUtils.createTable(connectionSource, EnquiryDetails.class);
            TableUtils.createTable(connectionSource, SupportDetails.class);
            TableUtils.createTable(connectionSource, Products.class);
            TableUtils.createTable(connectionSource, VisitMeetLog.class);
            TableUtils.createTable(connectionSource, LeaveList.class);


        } catch (SQLException e) {
            Log.e(DatabaseHelperORM.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
        //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
        // existing database etc.
        for (int i = oldVer; i <= newVer; ++i) {
            upgradeDB(i,sqliteDatabase,connectionSource);
        }
    }

    void upgradeDB(int oldVer,SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        switch (oldVer) {
            case 7: //leave managament
                try {
                    TableUtils.createTable(connectionSource, LeaveList.class);
                }catch(Exception exp){
                    LogWriter.writeLogException("Upgrade Db",exp);
                }
                break;
            case 6:
                try {

                    getAttnLogDao().executeRaw("ALTER TABLE `AttnLog` ADD COLUMN TodaysPlan STRING DEFAULT '_NA_';");// .executeRaw("ALTER TABLE `account` ADD COLUMN age INTEGER;");
                    getAttnLogDao().executeRaw("ALTER TABLE `AttnLog` ADD COLUMN DailyAllowance int DEFAULT 0;");// .executeRaw("ALTER TABLE `account` ADD COLUMN age INTEGER;");
                    getAttnLogDao().executeRaw("ALTER TABLE `AttnLog` ADD COLUMN placeVisit STRING DEFAULT '_NA_';");// .executeRaw("ALTER TABLE `account` ADD COLUMN age INTEGER;");

                } catch (Exception exp) {
                    LogWriter.writeLogException("ORM Lite", exp);
                }
                break;

            case 4:
            default:

                try {
                    // we added the age column in version 2
                    getVisitMeetingDao().executeRaw("ALTER TABLE `VisitMeetLog` ADD COLUMN NextVisDate STRING DEFAULT '_NA_';");// .executeRaw("ALTER TABLE `account` ADD COLUMN age INTEGER;");
                } catch (Exception exp) {
                    LogWriter.writeLogException("ORM Lite", exp);
                }
                break;
       /* }else {
            try {
                TableUtils.dropTable(connectionSource, UserList.class, true);
                TableUtils.dropTable(connectionSource, Offices.class, true);
                TableUtils.dropTable(connectionSource, AttnLog.class, true);
                TableUtils.dropTable(connectionSource, VisitLog.class, true);
                TableUtils.dropTable(connectionSource, Employees.class, true);
                TableUtils.dropTable(connectionSource, EnquiryDetails.class, true);
                TableUtils.dropTable(connectionSource, SupportDetails.class, true);
                TableUtils.dropTable(connectionSource, Products.class, true);
                TableUtils.dropTable(connectionSource, VisitMeetLog.class, true);
                TableUtils.dropTable(connectionSource, LeaveList.class, true);

            } catch (SQLException e) {
                e.printStackTrace();
            }*/
        }


    }





    public void DoVacuum(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("VACUUM;");
        }catch(Exception exp){

        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs



    public Dao<UserList, Integer> getUserListDao() throws SQLException {
        if (userListDao == null) {
            userListDao = getDao(UserList.class);
            userListDao.setAutoCommit(true);
        }
        return userListDao;
    }

    public Dao<Offices, Integer> getOfficesDao() throws SQLException {
        if (officesDao == null) {
            officesDao = getDao(Offices.class);
        }
        return officesDao;
    }

    public Dao<AttnLog, Integer> getAttnLogDao() throws SQLException {
        if (attnLogDao == null) {
            attnLogDao = getDao(AttnLog.class);
        }
        return attnLogDao;
    }

    public Dao<LeaveList, Integer> getLeaveLogDao() throws SQLException {
        if (leaveLogDao == null) {
            leaveLogDao = getDao(LeaveList.class);
        }
        return leaveLogDao;
    }

    public Dao<VisitLog, Integer> getVisitLogDao() throws SQLException {
        if (visitLogDao == null) {
            visitLogDao = getDao(VisitLog.class);
        }
        return visitLogDao;
    }

    public Dao<EnquiryDetails, Integer> getEnquiryDao() throws SQLException {
        if (enquiryDao == null) {
            enquiryDao = getDao(EnquiryDetails.class);
        }
        return enquiryDao;
    }

    public Dao<Products, Integer> getProductDao() throws SQLException {
        if (productDao == null) {
            productDao = getDao(Products.class);
        }
        return productDao;
    }

    public Dao<VisitMeetLog, Integer> getVisitMeetingDao() throws SQLException {
        if (visitMeetingDao == null) {
            visitMeetingDao = getDao(VisitMeetLog.class);
        }
        return visitMeetingDao;
    }

    public void createVisitMeetingTable() {
        try {
            TableUtils.createTable(connectionSource, VisitMeetLog.class);
        } catch (Exception exp) {

        }
    }

    public Dao<SupportDetails, Integer> getSupportDao() throws SQLException {
        if (supportDao == null) {
            supportDao = getDao(SupportDetails.class);
        }
        return supportDao;
    }

    /* public Dao<UserList, Integer> getEmployeeDao() throws SQLException {
         if (employeeDao == null) {
             employeeDao = getDao(UserList.class);
         }
         return employeeDao;
     }*/
    public <T> List<T> getAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryForAll();
    }

    public <T> List<T> getAllOrdered(Class<T> clazz, String orderBy, boolean ascending) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().orderBy(orderBy, ascending).query();
    }

    public <T> void fillObject(Class<T> clazz, T aObj) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.createOrUpdate(aObj);
    }

    public <T> void fillObjects(Class<T> clazz, Collection<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        for (T obj : aObjList) {
            dao.createOrUpdate(obj);
        }
    }

    public <T> T getById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.queryForId(aId);
    }


    public <T> List<T> query(Class<T> clazz, Map<String, Object> aMap) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.queryForFieldValues(aMap);
    }

    public <T> List<T> queryNot(Class<T> clazz, String columnName, int value) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.queryBuilder().where().ne(columnName, value).query();
    }

    public <T> T queryFirst(Class<T> clazz, Map<String, Object> aMap) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        List<T> list = dao.queryForFieldValues(aMap);
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
        Dao<T, ?> dao = (Dao<T, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }

    public <T> int deleteById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.deleteById(aId);
    }

    public <T> int deleteObjects(Class<T> clazz, Collection<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.delete(aObjList);
    }

    public <T> void deleteAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.deleteBuilder().delete();
    }

    public static HashMap<String, Object> where(String aVar, Object aValue) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put(aVar, aValue);
        return result;
    }

    /*public void clearAppointMentMainTable() {
        try {
            TableUtils.clearTable(connectionSource, AppointMentMain.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

   /* public void clearAppointMentTable() {
        try {
            TableUtils.clearTable(connectionSource, Appointment.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}