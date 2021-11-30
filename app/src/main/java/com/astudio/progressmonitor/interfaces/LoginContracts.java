package com.astudio.progressmonitor.interfaces;

import com.astudio.progressmonitor.group.Group;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.modules.Owner;
import com.astudio.progressmonitor.support.MessageDecoder;

public interface LoginContracts {


    interface StartPresenter{
        void returnLocalOwnerData(Owner owner);
        void returnCheckLocalGroupData(Group group);
    }


    interface LoginPresenter{
        void resultRemoteAuthentication2(GlobalData globalData);
        void resultRemoteAuthentication(Owner owner);
        void resultRemoteAuthentication(MessageDecoder.Codes code);
        void resultGetGroupData(Group group);
    }


    interface View{
        void successLogin(GlobalData globalData);
        void failLogin(MessageDecoder.Codes code);
    }


    interface Repository{
        void returnLocalOwnerData(Owner owner);
        void resultSetLocalOwnerData(Owner owner);
    }


}
