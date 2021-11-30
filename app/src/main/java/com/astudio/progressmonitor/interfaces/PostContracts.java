package com.astudio.progressmonitor.interfaces;

import com.astudio.progressmonitor.group.Group;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.modules.Owner;
import com.astudio.progressmonitor.support.MessageDecoder;

public interface PostContracts {


    interface Presenter{
       void resultBindPost(MessageDecoder.Codes code);
       void resultChangeParentForGroupEmployees(MessageDecoder.Codes code);
    }


    interface View{
        void successDialog(MessageDecoder.Codes code);
        void failDialog(MessageDecoder.Codes code);
    }


    interface Repository{
        void resultLocalBindPost(int result);
    }


}
