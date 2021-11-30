package com.astudio.progressmonitor.interfaces;

import com.astudio.progressmonitor.group.Group;
import com.astudio.progressmonitor.modules.GlobalData;
//import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.User;

public interface GroupContracts {


    interface NewGroupPresenter {
        void resultCheckUserExist(boolean result);
        void resultCreateGroup(Group group);
        void resultCreateMainAdmin(User user);
    }

    //interface NavigationPresenter{
        //void checkBlockGroupControl(String groupToken);
        //void resultCheckBlockGroupControl(List <String> list);
    //}

    //interface NavigationView{
        //void resultCheckBlockGroupControl(boolean result);
    //}


    interface Repository{
        void returnCheckLocalGroupData(Group group);
        void returnLocalGroupData(Group group);
        void returnLocalCurrentGroupData(Group group);
        void returnAppGlobalData(GlobalData globalData);
    }

    interface GroupPoliciesPresenter{
        void resultUpdateGroup(Group group);
        void resultUpdateGroup(MessageDecoder.Codes code);
    }


    interface GroupPoliciesView{
        void successDialog(MessageDecoder.Codes code);
        void failDialog(MessageDecoder.Codes code);
    }



    interface View{
        void successCreateGroup(String adminPhone, String password);
        void failCreateGroup(MessageDecoder.Codes code);
    }

    interface GlobalDataBuilder{
        void onResponse(GlobalData globalData);
    }


}
