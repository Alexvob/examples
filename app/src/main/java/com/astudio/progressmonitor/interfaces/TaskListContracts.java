package com.astudio.progressmonitor.interfaces;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.task.Task;

import java.util.List;

public interface TaskListContracts {


    interface PresenterOut{
        void attachView(TaskListContracts.ViewOut view);
        void detachView();
        //void viewIsReady();
        LiveData<List<Task>> getData(String changedSortItem);
        //void markToReadTask(int id);
    }


    interface ViewOut {
        void transferSelectedPosFromRecyclerView(int position);
    }

    // Различные интерфейсы для PresenterOut и ViewOut возможно нужны потому что в списке исходящих
    // задач доступны дополнительные операции (отменить, удалить и тд), а в остальных фрагментах ("Входящие", "Общие") только просмотр
    // но это херня, 18-01-2021, так как в фрагментах-списках нет никаких операций кроме отображения

    interface Presenter{
        void attachView(TaskListContracts.View view);
        void detachView();
        void markToRead(int taskId);
        //void viewIsReady();
        LiveData<List<Task>> getData(String changedSortItem);
    }


    interface View{
        //void setTasks(LiveData <List<Task>> tasks);
        //void updateUI();
        void transferSelectedPosFromRecyclerView(int position);

    }


    interface Repository{

    }


}
