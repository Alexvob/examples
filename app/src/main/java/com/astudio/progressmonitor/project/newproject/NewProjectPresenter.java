package com.astudio.progressmonitor.project.newproject;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.BasePresenter;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public class NewProjectPresenter extends BasePresenter implements ProjectContracts.NewProjectPresenter {

    private ProjectContracts.View view;
    private static final String TAG = "NewProjectPresenter";
    private GlobalData globalData;


    NewProjectPresenter(GlobalData globalData) {
        super();
        this.globalData = globalData;
        repositoryProject.setNewProjectPresenter(this);
    }


    public LiveData<List<UserEasy>> getUserList(){
        return repositoryUser.getUserEasyList(globalData.groupToken);
    }


    void createNewProject(Project project) {
        repositoryProject.addProject(project);
    }


    @Override
    public void resultAddProject(MessageDecoder.Codes code) {
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
