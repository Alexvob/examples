package com.astudio.progressmonitor.modules;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.astudio.progressmonitor.task.TabHostActivity;
import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.database.AppDatabase;
import com.astudio.progressmonitor.task.RemoteDataTask;
import com.astudio.progressmonitor.workers.SyncServiceTaskWorker;

import java.util.concurrent.TimeUnit;


//public class BackgroundIntentService extends IntentService implements SyncService2.BackgroundCallback {
public class BackgroundIntentService extends IntentService {

    private static final String ACTION_SYNC_DATA = "com.astudio.progressmonitor.modules.action.FOO";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.astudio.progressmonitor.modules.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.astudio.progressmonitor.modules.extra.PARAM2";

    private static final String TAG = "QW BackIntentService";
    //private SyncService2 syncService;
    private AppDatabase db;
    private RemoteDataTask remoteDataTask;

    private static String CHANNEL_ID = "My channel";
    private static final int NOTIFY_ID = 101;

    private PendingIntent contentIntent;
    private NotificationCompat.Builder builder;


    public BackgroundIntentService() {
        super("BackgroundIntentService");
        setIntentRedelivery(true); // autostart service after killing system
    }


    public static void startSyncData(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BackgroundIntentService.class);
        intent.setAction(ACTION_SYNC_DATA);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId());
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC_DATA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);

                try {
                    handleSyncData(param1, param2);
                } catch (UnsupportedOperationException ex){
                    notifyOnChanges(ex.getMessage()); // на время тестирования
                    //remoteDataTask.loggingToServer(new ErrorMessage(TAG, 1, ex.getMessage() ));
                }
            }
        }
    }


    private void handleSyncData(String param1, String param2) throws UnsupportedOperationException {
        // https://stackoverflow.com/questions/8964031/using-startforeground-with-an-intent-service

        NotificationCompat.Builder builder = createNotificationForeground();
        startForeground(NOTIFY_ID, builder.build());

//        db = App.getInstance().getDatabase();
//        remoteDataTask = App.getInstance().getRemoteDataTask();
//        syncService = new SyncService2(db, remoteDataTask);
//        syncService.setBackgroundCallback(this);
//        syncService.startSync();

        //setPeriodicSync();

//        SyncServiceTask syncService = App.getInstance().getSyncService();
//        syncService.startSync();

        //throw new UnsupportedOperationException("Цикл синхронизации прерван");
    }


    private void setPeriodicSync(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // наличие интернета Wi-fi или Mobile data
                .build();
        PeriodicWorkRequest periodicSync = new PeriodicWorkRequest.Builder(SyncServiceTaskWorker.class, 15, TimeUnit.MINUTES)
                .addTag("periodic-sync")
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR,
                        PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.SECONDS) //TODO: разбираться с политикой
                .build();
        Log.e(TAG, "Start PeriodicWorkRequest");
        //WorkManager.getInstance(getApplicationContext()).enqueue(periodicSync);
        //WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("SyncServiceTaskWorker", ExistingPeriodicWorkPolicy.REPLACE, periodicSync);
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("SyncServiceTaskWorker", ExistingPeriodicWorkPolicy.KEEP, periodicSync);
        //ListenableFuture<List<WorkInfo>> workInfo = WorkManager.getInstance(this).getWorkInfosForUniqueWork("SyncServiceTaskWorker");
    }


    //@Override
    public void notifyOnChanges(String message){
        createNotification(message, NOTIFY_ID + 1);
    }

    //@Override
    public void notifyOnException(String message) {
        createSilenceNotification(message, NOTIFY_ID);
    }

    // https://stackoverflow.com/questions/45919392/disable-sound-from-notificationchannel
    // https://stackoverflow.com/questions/7655164/android-notification-sound-disable
    // Наличие Intent.FLAG_ACTIVITY_SINGLE_TOP в основной деятельности опасно ... это может привести к
    // определенным ограничениям по времени ... то есть каждый раз, когда вы снова запускаете приложение,
    // нажимая значок ..., оно удалит все действия выше mainAcitivity, даже не вызывая onDestroy


    private NotificationCompat.Builder createNotificationForeground(){
        Intent notificationIntent = new Intent(getApplicationContext(), TabHostActivity.class);
        //notificationIntent.putExtra("isStartFromNotification", true);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.statistic)
                .setContentTitle(getResources().getString(R.string.app_name))
                //.setDefaults(Notification.DEFAULT_ALL)
                .setContentText("Подключен к сети")
                //.setGroup("group")
                //.setGroupSummary(true)
                .setSound(null)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent);
        return builder;
    }


    public void createSilenceNotification(String message, int channel){
        builder
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentText(message);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(channel, builder.build());
    }


    private void createNotification(String message, int channel){
        //  в идеале чтобы в уведомлении показывался текущий статус (онлайн-оффлайн) с беззвучной сменой статуса, а при новой задаче
        //  уведомление со звуком обновлялось, и чтобы при нажатии по уведомлению setContextText становился по умолчанию (либо возвращалось NotificationForeground)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.statistic)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(channel, builder.build());

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "someName", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationChannel.setDescription("My channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(false);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }

    }


    private void createNotification2(String message){
        //Context context = getBaseContext();
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context,
                message, Toast.LENGTH_SHORT);
        toast.show();
        Log.e(TAG, "Toast-" + message);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        //syncService.setStartSync(false);
        //syncService = null;
        Log.e(TAG, "onDestroy");
    }

}
