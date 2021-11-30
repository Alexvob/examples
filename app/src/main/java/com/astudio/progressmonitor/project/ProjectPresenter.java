package com.astudio.progressmonitor.project;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.BasePresenter;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ProjectPresenter extends BasePresenter implements ProjectContracts.ProjectPresenter   {

    private static final String TAG = "ProjectPresenter";
    private ProjectContracts.ProjectList view;
    private String groupToken;


    ProjectPresenter(String groupToken) {
        this.groupToken = groupToken;
        repositoryProject.setProjectPresenter(this);
    }


    public LiveData<List<UserEasy>> getUserList(){
        return repositoryUser.getUserEasyList(groupToken);
    }


    LiveData <List<Project>> getProjects(int userId, int status) {
        return repositoryProject.getProjects(groupToken, userId, status);
    }


    LiveData <List<Project>> getProjectsWithoutPrivate(int userId, int status) {
        return repositoryProject.getProjectsWithoutPrivate(groupToken, userId, status);
    }


//    public void updateProject(Project project) {
//        repositoryProject.updateProject(project, this);
//    }
//
//
//    @Override
//    public void resultUpdateProject(Project project) {
//        // метод на случай если нужно вернуть в активити обновленный экземпляр данных
//        if(view == null) return;
//        view.successDialog(MessageDecoder.Codes.SUCCESS);
//    }
//
//
//    @Override
//    public void resultUpdateProject(MessageDecoder.Codes code) {
//        if(view == null) return;
//        if (code == MessageDecoder.Codes.SUCCESS){
//            view.successDialog(code);
//        } else {
//            view.failDialog(code);
//        }
//    }


//    void deleteProject(Project project){
//        repositoryProject.deleteProject(project);
//    }
//
//
//    @Override
//    public void resultDeleteProject (MessageDecoder.Codes code) {
//        if(view == null) return;
//        if (code == MessageDecoder.Codes.SUCCESS){
//            view.successDialog(code);
//        } else {
//            view.failDialog(code);
//        }
//    }


    public void attachView(ProjectContracts.ProjectList view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }


}
