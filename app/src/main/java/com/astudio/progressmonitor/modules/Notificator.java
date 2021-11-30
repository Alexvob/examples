package com.astudio.progressmonitor.modules;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.astudio.progressmonitor.group.LoginActivity;
import com.astudio.progressmonitor.task.TabHostActivity;
import com.astudio.progressmonitor.R;

import java.util.Objects;

public class Notificator extends Service {

    private static final String TAG = "NotificatorService";
    private static final String GLOBAL_DATA = "globalData";
    private static String CHANNEL_ID_FOREGROUND;
    private static String CHANNEL_ID_PUSH;
    private static final int NOTIFY_ID = 3;
    private String appName;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private PendingIntent pendingTabHostIntent;
    private final IBinder binder = new LocalBinder();


    class LocalBinder extends Binder {
        Notificator getNotificator() {
            return Notificator.this;
        }
    }


    @Override
    public void onCreate() {
        CHANNEL_ID_FOREGROUND = getResources().getString(R.string.notification_channel_id_foreground);
        CHANNEL_ID_PUSH = getResources().getString(R.string.notification_channel_id_push);
        appName = getResources().getString(R.string.app_name);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setChannelForOreo();
        //buildForegroundNotification(); // 20-04-2021 зачем здесь это?
        //Log.e(TAG, "onCreate: " + this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //intent.setExtrasClassLoader(GlobalData.class.getClassLoader());
        //if(intent != null) {
            GlobalData globalData = intent.getParcelableExtra(GLOBAL_DATA);
            //Log.e(TAG, "onStartCommand: " + globalData);
//            Bundle arguments = intent.getExtras();
//            Objects.requireNonNull(arguments).setClassLoader(GlobalData.class.getClassLoader());
//            GlobalData appGlobalData = (GlobalData) Objects.requireNonNull(arguments).get(APP_GLOBAL_DATA);

//            GlobalData globalData = new GlobalData(Objects.requireNonNull(appGlobalData).userId, appGlobalData.userName,
//                    appGlobalData.role, appGlobalData.groupToken, appGlobalData.groupName);

            // насчет Foreground https://stackoverflow.com/questions/34502249/android-is-killing-my-foreground-service-after-force-stop-my-package
            buildTabHostActivityIntent(globalData);
            buildForegroundNotification();
            //Log.e(TAG, "instanceId: " + this);
            //Log.e(TAG, "onStartCommand: " + this);
        //}
        //return START_STICKY;
        return START_NOT_STICKY; // сервис не будет перезапущен после того, как был убит системой
    }


    public void setAppGlobalData(GlobalData data){
        //GlobalData globalData = new GlobalData(data.userId, data.userName, data.role, data.groupToken, data.groupName);
        buildTabHostActivityIntent(data);
    }


    private void buildTabHostActivityIntent(GlobalData globalData){
        //Log.e(TAG, "buildTabHostActivityIntent: " + this);
        Intent notificationIntent = new Intent(getApplicationContext(), TabHostActivity.class);
        //notificationIntent.setExtrasClassLoader(GlobalData.class.getClassLoader());
        //notificationIntent.putExtra("globalData", globalData);

        Bundle bundle = new Bundle();
        //Log.e(TAG, "buildTabHostActivityIntent: " + globalData);
        bundle.putParcelable("globalData", globalData);
        notificationIntent.putExtra("globalData", bundle);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // FLAG_ACTIVITY_SINGLE_TOP полезен только тогда, когда вы запускаете то же действие, которое в данный момент отображается на экране,
        // и вы хотите запретить запуск нового экземпляра существующего действия.
        // FLAG_ACTIVITY_CLEAR_TOP – ищет в таске создаваемое Activity. Если находит, то открывает, а все, что выше – закрывает.
        // Можно сказать, что этот флаг в комбинации с FLAG_ACTIVITY_NEW_TASK является аналогом singleTask в launchMode.
        pendingTabHostIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private void buildForegroundNotification(){
        //Log.e(TAG, "buildForegroundNotification: " + this);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID_FOREGROUND)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.main_launcher_icon5_foreground))
                .setSmallIcon(R.drawable.statistic)
                .setContentTitle(appName)
                .setContentText(getResources().getString(R.string.network_status_online))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSound(null)
                .setVibrate(null)
                //.setOnlyAlertOnce(true)
                .setContentIntent(pendingTabHostIntent);
        startForeground(NOTIFY_ID, builder.build());
    }


    public void newStatusNotification(String message){
        //Log.e(TAG, "NewStatus-instanceId: " + this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_FOREGROUND)
                .setSmallIcon(R.drawable.statistic)
                .setContentTitle(appName)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSound(null)
                .setVibrate(null)
                .setContentIntent(pendingTabHostIntent);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }


    // For new Task
    public void newNotification(String message){
        //Log.e(TAG, "NewNotify-instanceId: " + this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_PUSH)
                .setSmallIcon(R.drawable.statistic)
                .setContentTitle(appName)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingTabHostIntent);
        notificationManager.notify(NOTIFY_ID + 1, builder.build());
    }


    public void notificationForCheckUserInGroup(String message) {
        //Log.e(TAG, "notificationForCheckUserInGroup: " + this);
        Intent notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_PUSH)
                .setSmallIcon(R.drawable.statistic)
                .setContentTitle(appName)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setContentIntent(pendingIntent);
        notificationManager.notify(NOTIFY_ID + 2, builder.build());
    }


    private void setChannelForOreo(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String nameForeground = getResources().getString(R.string.notification_channel_name_foreground);
            String descForeground = getResources().getString(R.string.notification_channel_description_foreground);
            NotificationChannel foregroundChannel = new NotificationChannel(CHANNEL_ID_FOREGROUND, nameForeground,
                    NotificationManager.IMPORTANCE_LOW);
            foregroundChannel.setDescription(descForeground);
            foregroundChannel.enableVibration(false);
            notificationManager.createNotificationChannel(foregroundChannel);

            String namePush = getResources().getString(R.string.notification_channel_name_push);
            String descPush = getResources().getString(R.string.notification_channel_description_push);
            NotificationChannel pushChannel = new NotificationChannel(CHANNEL_ID_PUSH, namePush,
                    NotificationManager.IMPORTANCE_DEFAULT);
            pushChannel.setDescription(descPush);
            pushChannel.enableLights(true);
            pushChannel.setLightColor(Color.RED);
            pushChannel.enableVibration(false);
            //channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(pushChannel);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
    }



//TODO: проблема с Орео
    // https://stackoverflow.com/questions/55894636/android-9-pie-context-startforegroundservice-did-not-then-call-service-star/56338570#56338570

    //TODO: NOT USED


    //    private void buildForegroundNotificationLogin(){
//        Intent notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingLoginIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.statistic)
//                .setContentTitle(appName)
//                .setSound(null)
//                .setPriority(NotificationCompat.PRIORITY_MIN)
//                //.setOnlyAlertOnce(true)
//                .setContentIntent(pendingLoginIntent);
//        startForeground(NOTIFY_ID, builder.build());
//        Log.e(TAG, "onStartForegroundService-Login");
//        stopSelf();
//    }


//    private void startForegroundService(Group group){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //startMyOwnForeground();
//        }else{
//            //startForeground(1, new Notification());
//        }
//        Intent notificationIntent = new Intent(getApplicationContext(), TabHostActivity.class);
//        //if(group != null) {
//        //GlobalData globalData = new GlobalData(owner.getId(), owner.getName(), owner.getRole(), group.getGroupToken(), group.getName());
//        //notificationIntent.putExtra("globalData", globalData);
//        //}
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Foreground Service")
//                //.setContentText(input)
//                .setSmallIcon(R.drawable.statistic)
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(1, notification);
//        Log.e(TAG, "onStartForegroundService");
//    }


//    private NotificationCompat.Builder buildNotification2(){
//        //TODO: нужны данные в интент для ТабХост Активити
//        Intent notificationIntent = new Intent(this, TabHostActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent contentIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.statistic)
//                .setContentTitle(appName)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setContentIntent(contentIntent);
//        return builder;
//    }

//    private NotificationCompat.Builder buildNotification(){
//        //TODO: нужны данные в интент для ТабХост Активити
//        Intent notificationIntent = new Intent(context, TabHostActivity.class);
//        //notificationIntent.putExtra("isStartFromNotification", true);
//        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent contentIntent = PendingIntent.getActivity(context,
//                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.statistic)
//                .setContentTitle(appName)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true)
//                //.setContentText("Подключен к сети")
//                //.setGroup("group")
//                //.setGroupSummary(true)
//                //.setSound(null)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                //.setOnlyAlertOnce(true)
//                .setContentIntent(contentIntent);
//        return builder;
//    }

}
