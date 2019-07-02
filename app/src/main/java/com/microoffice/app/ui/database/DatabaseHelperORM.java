package com.microoffice.app.ui.database;

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
import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.models.dbmodels.VisitLog;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 */
public class DatabaseHelperORM extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/
    private static final String DATABASE_NAME = "MicroOfficeDB1.db";
    private static final int DATABASE_VERSION = 3;

    private Dao<UserList, Integer> userListDao;
    private Dao<AttnLog, Integer> attnLogDao;
    private Dao<Offices, Integer> officesDao;
    private Dao<VisitLog, Integer> visitLogDao;
    private Dao<EnquiryDetails, Integer> enquiryDao;

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
            TableUtils.createTable(connectionSource, EnquiryDetails.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelperORM.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {

        // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
        //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
        // existing database etc.
        try {
            TableUtils.dropTable(connectionSource, UserList.class, true);
            TableUtils.dropTable(connectionSource, Offices.class, true);
            TableUtils.dropTable(connectionSource, EnquiryDetails.class, true);
//            TableUtils.dropTable(connectionSource, InformationPojo.class, true);
//            TableUtils.dropTable(connectionSource, Article.class, true);
//            TableUtils.dropTable(connectionSource, ChecklistItem.class, true);
//            TableUtils.dropTable(connectionSource, Tip.class, true);
//            TableUtils.dropTable(connectionSource, Datum.class, true);
//            TableUtils.dropTable(connectionSource, Appointment.class, true);
//            TableUtils.dropTable(connectionSource, Data.class, true);
//            TableUtils.dropTable(connectionSource, VacinationData.class, true);
//            TableUtils.dropTable(connectionSource, AppointMentMain.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        Dao<AddChild, Integer> dao = null;
//        try {
//
//            Dao<Appointment, Integer> dao1 = null;
//
//            dao1 = getTermineDao();
//
//            dao1.executeRaw("ALTER TABLE `appointment` ADD COLUMN childColor STRING;");
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        try {
//            dao = getChildDao();
//
//            dao.executeRaw("ALTER TABLE `addchild` ADD COLUMN child_color STRING;");
//            // onCreate(sqliteDatabase, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<UserList, Integer> getUserListDao() throws SQLException {
        if (userListDao == null) {
            userListDao = getDao(UserList.class);
        }
        return userListDao;
    }

    public Dao<Offices, Integer> getOfficesDao() throws SQLException {
        if (officesDao == null) {
            officesDao = getDao(Offices.class);
        }
        return officesDao;
    }
    public Dao<EnquiryDetails, Integer> getEnquiryDao() throws SQLException {
        if (enquiryDao == null) {
            enquiryDao = getDao(EnquiryDetails.class);
        }
        return enquiryDao;
    }

    public Dao<AttnLog, Integer> getAttnLogDao() throws SQLException {
        if (attnLogDao == null) {
            attnLogDao = getDao(AttnLog.class);
        }
        return attnLogDao;
    }

    public <T> List<T> getAllWhere(Class<T> clazz, String startDate, String endDate) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().where().between("date", startDate, endDate).query();
    }

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