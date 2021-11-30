package com.astudio.progressmonitor.interfaces;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public interface UserContracts {


    interface UserControlPresenter{
        LiveData<List<UserEasy>> getUserList();
        void resultChangeAdmin(MessageDecoder.Codes code);
    }

    interface UserCreationPresenter{
        void addNewUser(String dirtyFirstName, String dirtyName, String dirtyPhone, String dirtyEmail);
        void resultAddNewUser(MessageDecoder.Codes code);
        LiveData<List<UserEasy>> getUserList();
    }

    interface UserEditingPresenter{
        LiveData<List<UserEasy>> getUserList();
    }

    interface UserDeletingPresenter{
        LiveData<List<UserEasy>> getUserList();
        void deleteUser(int userId);
        void resultDeleteUser(MessageDecoder.Codes code);
    }


    interface RestorePassPresenter{
        void successRewritePass(Integer numberUpdatedRows);
        void failRewritePass(MessageDecoder.Codes code);
    }

    interface ViewRestorePass{
        void successRewritePass();
        void failRewritePass(MessageDecoder.Codes code);
    }


    interface View{
        void successAlertDialog(MessageDecoder.Codes code);
        //void successAddNewUser(String message);
        //void successAddNewUser(String phone, String password);
        void successSetAnotherAdmin(MessageDecoder.Codes code);
        void failAlertDialog(MessageDecoder.Codes code);
        //void launchPurchaseFlow();
    }

    interface ViewCreationUser{
        //void successAlertDialog(MessageDecoder.Codes code);
        //void successAddNewUser(String message);
        void successAddNewUser(String phone, String password);
        //void successSetAnotherAdmin(MessageDecoder.Codes code);
        void failAlertDialog(MessageDecoder.Codes code);
        //void launchPurchaseFlow();
    }

    interface ViewEditingUser{
        //void successAlertDialog(MessageDecoder.Codes code);
       // void failAlertDialog(MessageDecoder.Codes code);
    }

    interface ViewDeletingUser{
        void successAlertDialog(MessageDecoder.Codes code);
        void failAlertDialog(MessageDecoder.Codes code);
    }


    interface Repository{
        void resultLocalDeleteUserTasksAndUser(Integer result);
        void resultChangeAdmin(boolean result);

    }


}
