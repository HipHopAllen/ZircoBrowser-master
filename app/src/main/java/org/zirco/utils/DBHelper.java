package org.zirco.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

//copy the database to sdcard
public class DBHelper {

    private final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/zircoBrowser";
    private final Activity activity;

    private final String DATABASE_FILENAME;

    public DBHelper(Context context) {

        DATABASE_FILENAME = "collection_data.db";
        activity = (Activity) context;
    }

    public SQLiteDatabase openDatabase() {
        try {
            boolean b = false;

            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                b = dir.mkdir();

            if (new File(databaseFilename).exists()){
                new File(databaseFilename).delete();
            }
            if (!(new File(databaseFilename)).exists()) {

                InputStream is = new FileInputStream("/data/data/org.zirco/databases/collection_data.db");

                FileOutputStream fos = new FileOutputStream(databaseFilename);

                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();
            }

            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            return database;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
