package com.astudio.progressmonitor.interfaces;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public interface PlanContracts {


    interface PersonalPresenter{
        LiveData<List<UserEasy>> getUserList();
        LiveData<List<Plan>> getPlan(int userId, String startDay, String endDay);
        void resultPersonalStatistic(List<Integer> list);
    }


    interface GroupPresenter{
        LiveData<List<UserEasy>> getUserList();
        LiveData<List<Plan>> getPlan(int userId, String startDay, String endDay);
        void resultPersonalStatistic(List<Integer> list);
    }




    interface DescPresenter  {
        void resultUpdatePlan(Plan plan);
        void resultUpdatePlan(MessageDecoder.Codes code);
        void resultDeletePlan(MessageDecoder.Codes code);
    }


    interface View{
        void successDialog(MessageDecoder.Codes code);
        void failDialog(MessageDecoder.Codes code);
        void resultUpdatePlan(Plan plan);
    }


    interface ViewList{
        void transferSelectedPosFromRecyclerView(int selectedPosition);
        void setDayLimit(String startDay, String endDay);
        void resultPlanStatistics(List<Integer> list);
    }


    interface PersonalView{
    }

    interface GroupView{
    }

    interface Repository{
        //void resultLocalBindPost(int result);
    }


}
