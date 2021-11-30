package com.astudio.progressmonitor.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.group.StartActivity;

import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {


    private static final String TAG = "BootCompletedReceiver";
    public static final String ACTION = "android.intent.action.ACTION_BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //Log.e(TAG, "onReceive!");

        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            //Log.e(TAG, "onReceive! and Action: ACTION_BOOT_COMPLETED");

    // Здесь ничего не вызываем, Арр запускается и так, и выполняется вся инициализация в Арр, и приложение запустилось в фоне
    // на переднем плане, и запустились все синхронизации.
    //  Ресивер регистрируется в Арр, то есть в этот момент система где-то записывает кого запускать при следующем запуске,
    //  и для запуска уже не требуется регистрация ресивера.

/*
            String packageName = context.getResources().getString(R.string.package_name);
            Intent i = new Intent();
            i.setClassName(packageName, packageName+".modules.Notificator");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // уточнить насчет флага
            i.putExtra("event", "ACTION_BOOT_COMPLETED");

            //context.startService(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
*/
            // получается надо запускать
//            Intent i = new Intent();
//            i.setClassName("com.astudio.progressmonitor", "com.astudio.progressmonitor.group.StartActivity");
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
