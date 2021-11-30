package com.astudio.progressmonitor.project.newitem;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.BasePresenter;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public class NewProjectItemPresenter extends BasePresenter implements ProjectContracts.NewProjectItemPresenter {

    private ProjectContracts.View view;
    private static final String TAG = "NewProjectItemPresenter";
    private GlobalData globalData;


    NewProjectItemPresenter(GlobalData globalData) {
        super();
        this.globalData = globalData;
        repositoryProject.setNewProjectItemPresenter(this);
    }


    public LiveData<List<UserEasy>> getUserList(){
        return repositoryUser.getUserEasyList(globalData.groupToken);
    }


    void createNewProjectItem(ProjectItem projectItem) {
        repositoryProject.addProjectItem(projectItem);
    }


    @Override
    public void resultAddProjectItem(MessageDecoder.Codes code) {
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
