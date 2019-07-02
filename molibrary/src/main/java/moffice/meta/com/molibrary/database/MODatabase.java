
package moffice.meta.com.molibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MODatabase extends SQLiteOpenHelper
{

	public static boolean isNewInstallation=false;

	private static final String DATABASE_NAME = "MicroOfficeDB";
	private static final int DATABASE_VERSION = 3;
	SQLiteDatabase Database;

	public static final String userList="create table UserList(_id integer primary key autoincrement,FName string,LName string,Mobile string,InstUniqID string,Email string,Password string,UserUUID string, RegStatus integer)";
	public static final String attnLog="create table AttnLog(_id integer primary key autoincrement,RecUUID string,InstUniqID string,UserID integer,LogType integer, InOut integer,Lati double,Longi double,Office string,LogYear integer,LogMonth integer,LogDayOfMonth integer,LogHour integer,LogMinute integer,SendStatus boolean)";

	public MODatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try
		{
			isNewInstallation=true;
			database.execSQL(userList);
			database.execSQL(attnLog);
		}
		catch (Exception e)
		{
			Log.i("SmartGardOMDatabase","Caught:"+e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion)
	{
       // Drop older table if existed
        database.execSQL("DROP TABLE IF EXISTS " + userList);
        database.execSQL("DROP TABLE IF EXISTS " + attnLog);

        // Create tables again
        onCreate(database);
	}

}
