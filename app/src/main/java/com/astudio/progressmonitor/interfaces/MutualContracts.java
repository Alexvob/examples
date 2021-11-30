package com.astudio.progressmonitor.interfaces;

import com.astudio.progressmonitor.support.MessageDecoder;

public interface MutualContracts {

    interface TaskPresenter{
        void resultAddTask(MessageDecoder.Codes code);
    }


    interface PlanPresenter{
        void resultAddPlan(MessageDecoder.Codes code);
    }



}
