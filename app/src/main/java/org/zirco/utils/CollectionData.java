package org.zirco.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.name;
import static android.content.Context.SENSOR_SERVICE;

/*
 * get all of data
 */

public class CollectionData implements SensorEventListener {

    //Declare the variables
    private static float downX ;
    private static float downY ;

    private static long timeStamp;
    private static float x;
    private static float y;
    private static float pressure;
    private static float areaCover;
    private static String figureOrientation;

    private static String screenOrientationStr;

    private static float[] accValues;
    private static float[] orientationValues;
    private static float[] gyroscopeValues;

    private SensorManager sensorManager;
    private Sensor accSensor;
    private Sensor orientationSensor;
    private Sensor gyroscopeSensor;
    private static float azimuth,pitch,poll;
    private static float gx,gy,gz;
    private static float sx,sy,sz;


    private static float pointerCount;
    private static float actionCode;
    private static float actionCodeMasked;
    private static float actionIndex;



    //private static float areaCover;

    //Finish the variable declaration

    //define the sql database
    private CollectionDataStoreSqlite collectionSqlite;
    SQLiteDatabase db;

    //server
    //send to to server variate

    private Map<String,String> mySetHM = new HashMap<String,String>();


    public CollectionData(Context context){
        //get the sensor manager
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,orientationSensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,gyroscopeSensor,SensorManager.SENSOR_DELAY_GAME);

        //get the database object
        collectionSqlite = new CollectionDataStoreSqlite(context);

        db = collectionSqlite.getWritableDatabase();
    }


    //get the data
    public void getCollectionData(Context context,MotionEvent event){
        timeStamp = event.getEventTime(); // this is not timestamp actually, it is just event time
        //We can try to get timestamp from SensorEvent as follows
        //private float timestamp;
        //timeStamp = event.timestamp;

        x = event.getX();
        y = event.getY();
        pressure = event.getPressure();
        areaCover = event.getSize();
        pointerCount = event.getPointerCount();
        actionCode = event.getAction();
        actionCodeMasked = event.getActionMasked();
        actionIndex = event.getActionIndex(); //can delete

     //   figureOrientation = getOrientation(event);
     //   Log.v("ltl","figureOrientation"+ figureOrientation);

        int screenOrientation = context.getResources().getConfiguration().orientation;
        if(screenOrientation == Configuration.ORIENTATION_LANDSCAPE){
            screenOrientationStr="landscape";
        } else {
            screenOrientationStr= "portrait";
        }


        Log.v("ltl","timeStamp:"+timeStamp+"actionCode"+actionCode+"actionCodeMasked"+actionCodeMasked+"actionIndex"+actionIndex+"pointerCount"+pointerCount+"x:"+x+"y:"+y+"pressure:"+pressure+"areaCover:"+areaCover+"figureOrientation:"+
                figureOrientation+"screenOrientationStr:"+screenOrientationStr);
        Log.v("ltl","gx:"+gx+" gy:"+gy+" gz:"+gz);
        Log.v("ltl","azimuth:"+azimuth+" pitch:"+pitch+" poll:"+poll);
        Log.v("ltl","sx:"+sx+" sy:"+sy+" sz:"+sz);

        //save to database
        ContentValues contentValues = new ContentValues();
        contentValues.put("timeStamp",timeStamp);
        contentValues.put("x",x);
        contentValues.put("y",y);
        contentValues.put("pressure",pressure);

        contentValues.put("areaCover",areaCover);
        contentValues.put("actionCode",actionCode);
        contentValues.put("actionCodeMasked",actionCodeMasked);
        contentValues.put("actionIndex",actionIndex);

        contentValues.put("pointerCount",pointerCount);
//        contentValues.put("figureOrientation",figureOrientation);
        contentValues.put("screenOrientationStr",screenOrientationStr);
        contentValues.put("azimuth",azimuth);
        contentValues.put("pitch",pitch);
        contentValues.put("poll",poll);
        contentValues.put("gx",gx);
        contentValues.put("gy",gy);
        contentValues.put("gz",gz);
        contentValues.put("sx",sx);
        contentValues.put("sy",sy);
        contentValues.put("sz",sz);

        db.insert("collectation",null,contentValues);
        new DBHelper(context).openDatabase();



    }

    //get the orientation
/**    private String getOrientation(MotionEvent event) {
        String action = "down";

        int orientation;
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = x;
                downY = y;

                break;
            case MotionEvent.ACTION_UP:

                float dx = x - downX;
                float dy = y - downY;

                if (Math.abs(dx) > 8 && Math.abs(dy) > 8) {
                    if (Math.abs(dx) > Math.abs(dy)) {

                        orientation = dx > 0 ? 'r' : 'l';
                    } else {

                        orientation = dy > 0 ? 'b' : 't';
                    }

                    switch (orientation) {
                        case 'r':
                            action = "right";
                            break;
                        case 'l':
                            action = "left";
                            break;
                        case 't':
                            action = "up";
                            break;
                        case 'b':
                            action = "down";
                            break;
                    }

                }
                break;
        }

        return action;
    }  **/

    //sensor values by SensorEvent
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            accValues=sensorEvent.values.clone();
            gx = accValues[0];
            gy = accValues[1];
            gz = accValues[2];
        }
       // else if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
       //     orientationValues=sensorEvent.values.clone();
       //     azimuth = orientationValues[0];
       //     pitch = orientationValues[1];
       //     poll = orientationValues[2];
       // }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
             orientationValues=sensorEvent.values.clone();
             azimuth = orientationValues[0];
             pitch = orientationValues[1];
             poll = orientationValues[2];
        }

        if(sensorEvent.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            gyroscopeValues=sensorEvent.values.clone();
            sx = gyroscopeValues[0];
            sy = gyroscopeValues[1];
            sz = gyroscopeValues[2];
        }

      //TYPE_GRAVITY was not recognized, why?
       // if(sensorEvent.sensor.getType()==Sensor.TYPE_GRAVITY){
       // }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


