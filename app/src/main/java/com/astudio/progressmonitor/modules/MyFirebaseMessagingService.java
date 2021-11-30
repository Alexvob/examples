package com.astudio.progressmonitor.modules;

import android.util.Log;

import androidx.annotation.NonNull;

import com.astudio.progressmonitor.workers.DataUpdateWorker;
import com.astudio.progressmonitor.workers.NewTaskNoticeWorker;
import com.astudio.progressmonitor.workers.UpdateServerTokenWorker;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    //TODO: читать! https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());


// не нужно, так как FCM только запускает SyncServiceTask, уведомления формируются там
//        String eventType = remoteMessage.getData().get("body");
//        if (eventType != null && eventType.equals("create")) {
//            String sendUserId = remoteMessage.getData().get("title");
//            int userIdTemp = App.getInstance().getUserData().getCurrentUserId();
//            //String userId = Integer.toString(userIdTemp);
//            String userId = "54"; // изменить когда будет модуль UserData
//            if (sendUserId != null && !sendUserId.equals(userId)){
//                sendNotification ("Создана новая задача");
//            }
//        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

                String collapseKey = remoteMessage.getCollapseKey();
                Log.e(TAG, "collapseKey: " + collapseKey);

                String eventType = remoteMessage.getData().get("body");
                //String groupToken = remoteMessage.getData().get("topic");
                if(eventType != null && eventType.equals("updateTask")){
                    String forIdTask = remoteMessage.getData().get("for_id");
                    String fromIdTask = remoteMessage.getData().get("from_id");
                    checkTaskForMeNotification(forIdTask, fromIdTask);
                    scheduleJob();
                }
                if(eventType != null && eventType.equals("updateUser")){
                    syncUserBD();
                }
                if(eventType != null && eventType.equals("deleteUser")){
                    syncUserAndTaskBD();
                }
                if(eventType != null && eventType.equals("updatePlan")){
                    syncPlan();
                }
                if(eventType != null && eventType.equals("updateProject")){
                    syncProject();
                }
                if(eventType != null && eventType.equals("updateGroup")){
                    syncGroup();
                }
                if(eventType != null && eventType.equals("updatePromo")){
                    syncPromo();
                }

            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification("My custom notification");
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    private void checkTaskForMeNotification(String forIdTask, String fromIdTask){
        int forId = Integer.valueOf(forIdTask);
        int fromId = Integer.valueOf(fromIdTask);
        App.getInstance().registerCalledWorker(new NewTaskNoticeWorker(forId, fromId));
    }


    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.e(TAG, "onNewToken called: " + token);
        App.getInstance().setDeviceToken(token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]


    // для установки слуйчаной задержки перед запуском синхронизации, чтобы в группе не одновременно пошли запросы на сервер
    private int randomDelay(){
        Random rn = new Random();
        int minimum = 1000;
        int maximum = 10000;
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }


    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        Log.e(TAG, "schedule - JobStart FCM syncTaskBD");
        //App.getInstance().triggerForFCM();

        App.getInstance().registerCalledWorker(new DataUpdateWorker("task", randomDelay()));
        //Data.Builder data = new Data.Builder();
        //data.putString("groupToken", groupToken);

        /*
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // наличие интернета Wi-fi или Mobile data
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceTaskWorker.class)
                //.setConstraints(constraints)
                .setInitialDelay(1000, TimeUnit.MILLISECONDS)
                .addTag("pushFCM-syncTask")
                //.setInputData(data.build())
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("OneTimeSyncTask", ExistingWorkPolicy.REPLACE, work);
        */

        //TODO: проблема в том что когда не доставились предыдущие сообщения, то есть не выполнилась работа, то при запуске приложения они все запускаются
        //а нужно чтобы только новая последняя видимо
        // https://developer.android.com/topic/libraries/architecture/workmanager/how-to/managing-work
        // [END dispatch_job]
    }


    private void syncUserBD() {
        // [START dispatch_job]

        Log.e(TAG, "Start FCM syncUserBD");

        App.getInstance().registerCalledWorker(new DataUpdateWorker("user", 1000));

        //App.getInstance().setPeriodicSyncUsers();
        /*
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // наличие интернета Wi-fi или Mobile data
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceUserWorker.class)
                //.setConstraints(constraints)
                .setInitialDelay(1000, TimeUnit.MILLISECONDS)
                .addTag("pushFCM-syncUser")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("OneTimeSyncUser", ExistingWorkPolicy.REPLACE, work);
        */
        //WorkManager.getInstance(getApplicationContext()).beginWith(work).enqueue();
        //WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId()).observe(this, );
        // [END dispatch_job]
    }


    private void syncUserAndTaskBD() {
        // [START dispatch_job]

        Log.e(TAG, "Start FCM syncUserAndTaskBD");

        App.getInstance().registerCalledWorker(new DataUpdateWorker("task", 1000));
        App.getInstance().registerCalledWorker(new DataUpdateWorker("user", 2000));

        //App.getInstance().setPeriodicSyncUsers();
        /*
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // наличие интернета Wi-fi или Mobile data
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceUserWorker.class)
                //.setConstraints(constraints)
                .setInitialDelay(1000, TimeUnit.MILLISECONDS)
                .addTag("pushFCM-syncUser")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("OneTimeSyncUser", ExistingWorkPolicy.REPLACE, work);
        */
        //WorkManager.getInstance(getApplicationContext()).beginWith(work).enqueue();
        //WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId()).observe(this, );
        // [END dispatch_job]
    }


    private void syncPlan(){
        App.getInstance().registerCalledWorker(new DataUpdateWorker("plan", 1000));
    }

    private void syncProject(){
        App.getInstance().registerCalledWorker(new DataUpdateWorker("project", 1000));
    }

    private void syncGroup(){
        App.getInstance().registerCalledWorker(new DataUpdateWorker("group", 1000));
    }


    private void syncPromo(){
        App.getInstance().registerCalledWorker(new DataUpdateWorker("promo", 1000));
    }


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.e(TAG, "Short lived task is done.");
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Implement this method to send token to your app server.
        App.getInstance().registerCalledWorker(new UpdateServerTokenWorker(token));
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e(TAG, "onDeletedMessages called");
    }


    /** NOT USED
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
//    private void sendNotification(String messageBody) {
//
//        Intent intent = new Intent(this, TabHostActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //TODO: разбираться с флагами здесь?
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.home_page)
//                        //.setContentTitle(getString(R.string.fcm_message))
//                        .setContentTitle("Title FCM")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        //.setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }


}
