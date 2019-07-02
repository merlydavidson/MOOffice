package com.microoffice.app.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.utility.LogWriter;

public class ExportImportDB {

    public static boolean importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "com.microoffice.app"
                        + "//databases//" + DatabaseHelperORM.DATABASE_NAME;
                String backupDBPath  = "/MOffice/"+ DatabaseHelperORM.DATABASE_NAME;
                File  backupDB= new File(data, currentDBPath);
                File currentDB  = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                return  true;
            }else return false;
        } catch (Exception e) {
            LogWriter.writeLogException("ImportDB",e);
            return false;

        }
    }
    //exporting database
    public static boolean exportDB(Context con) {
        // TODO Auto-generated method stub

        try {
            String  currentDBPath;// =con.getDatabasePath(DatabaseHelperORM.DATABASE_NAME).getAbsolutePath();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                currentDBPath= "//data//" + "com.microoffice.app"
                        + "//databases//" + DatabaseHelperORM.DATABASE_NAME;
                String backupDBPath  = "/MOffice/MicroOfficeDB1"+ System.currentTimeMillis()+".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                return  true;
            }else return false;
        } catch (Exception e) {
            LogWriter.writeLogException("ExportDB",e);
            return false;
        }
    }

}
