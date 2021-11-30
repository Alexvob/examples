package com.astudio.progressmonitor.project.description;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.interfaces.MutualContracts;
import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.interfaces.TaskContracts;
import com.astudio.progressmonitor.modules.BasePresenter;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.project.CounterItem;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;

import java.util.List;


public class ProjectDescPresenter extends BasePresenter implements ProjectContracts.DescPresenter, MutualContracts.PlanPresenter,
        MutualContracts.TaskPresenter, ProjectContracts.MutualItemPresenter, ProjectContracts.MutualProjectPresenter {

    private static final String TAG = "ProjectDescPresenter";
    private ProjectContracts.ProjectItemList view;
    private GlobalData globalData;


    ProjectDescPresenter(GlobalData globalData) {
        this.globalData = globalData;
        repositoryProject.setProjectDescPresenter(this);
    }


    @Override
    public LiveData <List<ProjectItem>> getProjectItems(int projectId) {
        return repositoryProject.getProjectItems(globalData.groupToken, projectId);
    }


    public LiveData <CounterItem> countProjectItems (int projectId) {
        return repositoryProject.countProjectItems(globalData.groupToken, projectId);
    }



    public void updateProject(Project project) {
        repositoryProject.updateProject(project, this);
    }


    @Override
    public void resultUpdateProject(Project project) {
        // метод на случай если нужно вернуть в активити обновленный экземпляр данных
        if(view == null) return;
        view.successDialog(MessageDecoder.Codes.SUCCESS, "updateProject");
    }


    @Override
    public void resultUpdateProject(MessageDecoder.Codes code) {
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code, "updateProject");
        } else {
            view.failDialog(code);
        }
    }


    void deleteProject(Project project){
        repositoryProject.deleteProject(project);
    }


    @Override
    public void resultDeleteProject (MessageDecoder.Codes code) {
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code, "deleteProject");
        } else {
            view.failDialog(code);
        }
    }


    void executedItem(ProjectItem projectItem) {
        projectItem.setStatus(100);
        repositoryProject.updateProjectItem(projectItem, this);
    }


    @Override
    public void resultUpdateProjectItem(ProjectItem projectItem) {
        // метод на случай если нужно вернуть в активити обновленный экземпляр данных
        if(view == null) return;
        view.successDialog(MessageDecoder.Codes.SUCCESS, "updateItem");
    }


    @Override
    public void resultUpdateProjectItem(MessageDecoder.Codes code) {
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code, "updateItem");
        } else {
            view.failDialog(code);
        }
    }


    void deleteProjectItem(ProjectItem projectItem) {
        repositoryProject.deleteProjectItem(projectItem);
    }


    @Override
    public void resultDeleteProjectItem (MessageDecoder.Codes code) {
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code, "deleteItem");
        } else {
            view.failDialog(code);
        }
    }


    void delegateProjectItem(ProjectItem projectItem){
        repositoryTask.createNewTask(globalData.userId, globalData.userName, projectItem.getExecutorId(), projectItem.getExecutorName(), projectItem.getDesc(),
                projectItem.getDeadline(), "a", globalData.groupToken, this);
    }


    @Override
    public void resultAddTask(MessageDecoder.Codes code) {
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code, "addTask");
        } else {
            view.failDialog(code);
        }
    }


    void scheduleProjectItem(ProjectItem projectItem, String dateForDB, int durationHours, int durationMinutes) {
        int status = 400; // not accepted
        int duration = durationHours * 60 + durationMinutes;
        Plan plan = new Plan(projectItem.getExecutorId(), projectItem.getDesc(), status, dateForDB, globalData.groupToken, duration);
        repositoryPlan.addPlan(plan, this);
        //Log.e(TAG, "scheduleProjectItem" +  plan);
    }


    @Override
    public void resultAddPlan(MessageDecoder.Codes code){
        if(view == null) return;
        if (code == MessageDecoder.Codes.SUCCESS){
            view.successDialog(code, "addPlan");
        } else {
            view.failDialog(code);
        }
    }


    void setLocalProjectProgress(int projectId, int projectProgress) {
        repositoryProject.setLocalProjectProgress(projectId, projectProgress);
    }


    public void attachView(ProjectContracts.ProjectItemList view) {
        this.view = view;
    }


    public void detachView() {
        this.view = null;
    }



}
