package com.astudio.progressmonitor.interfaces;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.task.Task;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public interface TaskContracts {

    interface PresenterDesc{
        void resultDeleteTask(boolean result);
        void resultChangedStatus(boolean result);
        //void resultGetAllPromo(List<File> list);
    }



    interface UnusedPresenter{
        //void resultAddTask(MessageDecoder.Codes code);

        //void successAddNewTask(Task task);
        //void failAddNewTask(MessageDecoder.Codes code);
        //void createNewTask(int forId, String forfio, String desc, String timeLimit);
        //LiveData<List<UserEasy>> getUserList();
    }


    interface View{
        //void gotoTaskDescriptionActivity(Task task, String listType);
        void gotoPreviousTaskListActivity();
        void failToast(MessageDecoder.Codes code);
        void resultGetPromoImages(List<Bitmap> bitmapList);
        //void successChangedStatus(String status); //для варианта когда при смене статуса остаемся на том же экране
    }

    interface RepositoryTask{

    }


}
