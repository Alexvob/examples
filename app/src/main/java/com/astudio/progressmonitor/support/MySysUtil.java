package com.astudio.progressmonitor.support;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.astudio.progressmonitor.task.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static android.text.format.Time.getCurrentTimezone;

public class MySysUtil {

    private static final String TAG = "MySysUtil";


    public static void getDump(Object o) {

        Log.e(TAG, new GsonBuilder().setPrettyPrinting().create().toJson(o) + "\n");
    }


    private static void internalStorage(File internalStorageDir){
        //internalStorageDir;
    }


    private static void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/DirName/"), fileName);
        if (file.exists()) {
            boolean result = file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    public boolean checkBlockGroupControl(List<String> list){

        long firstTime;
        long lastTime;
        long factPeriod = 0;
        long normPeriod = 15778458; // 6 month
        DateFormat sourceUTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(list.get(0) != null) {
                Date firstDate = sourceUTCFormat.parse(list.get(0));
                Date lastDate = sourceUTCFormat.parse(list.get(1));
                Log.e(TAG, "firstUser-" + firstDate);
                Log.e(TAG, "lastUser-" + lastDate);
                firstTime = Objects.requireNonNull(firstDate).getTime();
                firstTime = firstTime / 1000;
                lastTime = Objects.requireNonNull(lastDate).getTime();
                lastTime = lastTime / 1000;
                factPeriod = lastTime - firstTime;
            }

        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return factPeriod < normPeriod;
    }


    public static String reformatDateForDB(String sourceDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = sdf.parse(sourceDate);
            SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return resultFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return "Bad format!";
    }


//    public static String reformatDateForLayout(String sourceDate){
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            Date date = sdf.parse(sourceDate);
//            SimpleDateFormat resultFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//            return resultFormat.format(Objects.requireNonNull(date));
//        } catch (ParseException e) {
//            FirebaseCrashlytics.getInstance().recordException(e);
//        }
//        return "Bad format!";
//    }



    public static String findStartDayPlan(String sourceStr){
        DateFormat sourceUTCFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = sourceUTCFormat.parse(sourceStr);
            Log.e(TAG, "date: " + date);
            if (date != null) {
                int offset = new Date().getTimezoneOffset(); // minutes
                Log.e(TAG, "offset: " + offset);
                int msOffset = offset*60*1000; // milliseconds
                long lTime = date.getTime(); // milliseconds
                long extTime = lTime + msOffset;
                date.setTime(extTime);
                DateFormat resultUTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                return resultUTCFormat.format(date);
            }

        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            return null;
        }
        //TODO: разобраться что возвращать
        return "Bad format!";
    }


    public static String findEndDayPlan(String sourceStr){
        DateFormat sourceUTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sourceUTCFormat.parse(sourceStr);
            //Log.e(TAG, "date: " + date);
            if (date != null) {
                int offset = new Date().getTimezoneOffset(); // minutes
                //Log.e(TAG, "offset: " + offset);
                int msOffset = offset*60*1000; // milliseconds
                long lTime = date.getTime(); // milliseconds
                long hours24 = 86400000;
                long extTime = lTime + hours24;
                date.setTime(extTime);
                DateFormat resultUTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                return resultUTCFormat.format(date);
            }
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            return null;
        }
        //TODO: разобраться что возвращать
        return "Bad format!";
    }


    public static String separateTime(String dateTime){
        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date sourceDate = sourceFormat.parse(Objects.requireNonNull(dateTime));
            DateFormat resultFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return resultFormat.format(Objects.requireNonNull(sourceDate));
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return null;
    }


    public static String formatDateTime (String dateTime){
        try {
            String nonFormatString = timeZoneFormatting(dateTime);
            DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date sourceDate = sourceFormat.parse(Objects.requireNonNull(nonFormatString));
            DateFormat resultFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
            return resultFormat.format(Objects.requireNonNull(sourceDate));
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return null;
    }


    public static String formatDate(String date){
        try {
            DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date sourceDate = sourceFormat.parse(Objects.requireNonNull(date));
            DateFormat resultFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return resultFormat.format(Objects.requireNonNull(sourceDate));
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return null;
    }


    public static String timeZoneFormatting(String sourceStr){

        DateFormat sourceUTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //sourceUTCFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        try {
            Date date = sourceUTCFormat.parse(sourceStr);

            if (date != null) {
                int offset = new Date().getTimezoneOffset(); // minutes
                int msOffset = offset*60*1000; // milliseconds
                //Log.e(TAG, "offset: " + offset);

                long lTime = date.getTime(); // milliseconds
                long extTime = lTime - msOffset;
                date.setTime(extTime);
                //String timezone = getCurrentTimezone();
                //Log.e("timeZoneFormatting", "Timezone: " + timezone);
                //Log.e("timeZoneFormatting", "extDate: " + sourceUTCFormat.format(date));
                return sourceUTCFormat.format(date);
            }
        } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            return null;
        }
        //TODO: разобраться что возвращать
        return "not format";
    }


    public static boolean compareDateAfter(String date){
        Calendar currentCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar planCalendar = Calendar.getInstance();
        try {
            planCalendar.setTime(Objects.requireNonNull(sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateComparator dateComparator = new DateComparator();
        int resultCompare = dateComparator.compare(planCalendar, currentCalendar);
        return resultCompare == 0 | resultCompare > 0;
    }


    private static class DateComparator implements Comparator<Calendar> {
        public int compare(Calendar c1, Calendar c2) {
            if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
                return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
            if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
                return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
            return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
        }
    }



    // Example: long start1 = System.nanoTime();
    // MySysUtil.executionTime(start1, "timeApp");
    public static void executionTime(long start, String timerName){
        //long finish = System.currentTimeMillis();
        long finish = System.nanoTime();
        long timeConsumedNano = finish - start;
        long timeConsumedMikro = timeConsumedNano / 1000;
        long timeConsumedMilli = timeConsumedMikro / 1000;
        Log.e(timerName, "NanoSec: " + timeConsumedNano + ", MikroSec: " + timeConsumedMikro + ", MilliSec: " + timeConsumedMilli);
    }


    public static class PrintMap <I,S>{

        public void executePrint(Map<I, S> map, String name){
            for(Map.Entry <I, S> entry : map.entrySet()) {
                I id = entry.getKey();
                S value = entry.getValue();
                Log.e(name, "id: " + id + " | value: " + value);
            }
        }
//  Example calling
//        SortItem.PrintMap<Integer, String> printMap = new SortItem.PrintMap<Integer, String>();
//        printMap.executePrint(someMap);

    }


    public static class PrintList <T>{

        public  void executePrint(List<T> list, String name){
            for(T value: list) {
                Log.e(name, "value: " + value);
            }
        }
    }



    public List<Task> sortTaskList(List<Task> tasks, String status){
        List<Task> sortTasks = new ArrayList<>();
        for(Task task : tasks){
            if(task.getStatus().equals(status)){
                sortTasks.add(task);
            }
        }
        return sortTasks;
    }



    // https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return null;
    }

}
