package org.zirco.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * the class of CollectionDatastroeSqlite to operate the database
 */

public class CollectionDataStoreSqlite extends SQLiteOpenHelper {

    private static final String DBNAME = "collection_data.db";
    private static final int VERSION = 11;
    public CollectionDataStoreSqlite(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table collectation(timeStamp float PRIMARY KEY,x float,y float,xRaw float,yRaw float,pressure float," +
                "areaCover float,pointerCount float,actionCode float,actionCodeMasked float,actionIndex float,fingerOrientation float,screenOrientationStr varchar(32)," +
                "azimuth float,pitch float,poll float,gx float,gy float,gz float,sx float,sy float,sz float);";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
