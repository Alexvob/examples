package com.astudio.progressmonitor.project.edit_item;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.BasePresenter;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public class EditProjectItemPresenter extends BasePresenter implements ProjectContracts.MutualItemPresenter {

    private ProjectContracts.View view;
    private static final String TAG = "EditProjectItemPresenter";
    private GlobalData globalData;


    EditProjectItemPresenter(GlobalData globalData) {
        super();
        this.globalData = globalData;
        //repositoryProject.setEditProjectItemPresenter(this);
    }


    public LiveData<List<UserEasy>> getUserList(){
        return repositoryUser.getUserEasyList(globalData.groupToken);
    }


    void updateProjectItem(ProjectItem projectItem) {
        repositoryProject.updateProjectItem(projectItem, this);
    }


    @Override
    public void resultUpdateProjectItem(ProjectItem projectItem) {
        // метод на случай если нужно вернуть в активити обновленный экземпляр данных
        if(view == null) return;
        view.successDialog(MessageDecoder.Codes.SUCCESS);
    }


    @Override
    public void resultUpdateProjectItem(MessageDecoder.Codes code) {
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code);
        } else {
            view.failDialog(code);
        }
    }


    public void attachView(ProjectContracts.View view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }


}
