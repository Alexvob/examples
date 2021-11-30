package com.astudio.progressmonitor.project;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.database.AppDatabase;
import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.plan.RemoteDataPlan;
import com.astudio.progressmonitor.plan.description.PlanDescriptionPresenter;
import com.astudio.progressmonitor.project.edit.EditProjectPresenter;
import com.astudio.progressmonitor.support.MessageDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RepositoryProject implements RemoteDataProject.RepositoryProjectCallback {

    private static final String TAG = "RepositoryProject";
    private AppDatabase db;
    private RemoteDataProject remoteDataProject;
    private ProjectContracts.NewProjectPresenter presenterNewProject;
    private ProjectContracts.NewProjectItemPresenter presenterNewProjectItem;
    private ProjectContracts.MutualItemPresenter presenterMutualItem;
    private ProjectContracts.MutualProjectPresenter presenterMutualProject;
    private ProjectContracts.DescPresenter presenterDesc;
    private ProjectContracts.ProjectPresenter presenterProject;


    public RepositoryProject(AppDatabase database, RemoteDataProject remoteDataExternal) {
        db = database;
        this.remoteDataProject = remoteDataExternal;
        remoteDataProject.setCallback(this);
    }


    void setProjectPresenter(ProjectContracts.ProjectPresenter presenter){
        this.presenterProject = presenter;
    }


    public void setProjectDescPresenter(ProjectContracts.DescPresenter presenter){
        this.presenterDesc = presenter;
    }


    public void setNewProjectItemPresenter(ProjectContracts.NewProjectItemPresenter presenter){
        this.presenterNewProjectItem = presenter;
    }


    public void setNewProjectPresenter(ProjectContracts.NewProjectPresenter presenter){
        this.presenterNewProject = presenter;
    }


    LiveData<List<Project>> getProjects(String groupToken, int userId, int status) {
        if (status == 101){
            return db.projectDao().getProjects(groupToken, userId, status);
        } else {
            return db.projectDao().getProjects(groupToken, userId);
        }
    }


    LiveData<List<Project>> getProjectsWithoutPrivate(String groupToken, int userId, int status) {
        if (status == 101){
            return db.projectDao().getProjectsWithoutPrivate(groupToken, userId, status);
        } else {
            return db.projectDao().getProjectsWithoutPrivate(groupToken, userId);
        }
    }

    public void addProject(Project project) {
        remoteDataProject.addProject(project);
    }


    @Override
    public void resultRemoteAddProject(MessageDecoder.Codes code) {
        presenterNewProject.resultAddProject(code);
    }


    @Override
    public void resultRemoteAddProject(Project project) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            long id = db.projectDao().insertProject(project);
            handler.post(() -> {
                resultLocalAddProject(id);
                service.shutdown();
            });
        });
    }


    private void resultLocalAddProject(long id){
        if (id != 0){
            presenterNewProject.resultAddProject(MessageDecoder.Codes.SUCCESS);
        } else {
            presenterNewProject.resultAddProject(MessageDecoder.Codes.FAIL_CUT);
        }
    }



    // ProjectDescription

    public LiveData<List<ProjectItem>> getProjectItems(String groupToken, int projectId) {
        return db.projectDao().getProjectItems(groupToken, projectId);
    }


    //NewProjectItem

    public void addProjectItem(ProjectItem projectItem) {
        remoteDataProject.addProjectItem(projectItem);
    }

    @Override
    public void resultRemoteAddProjectItem(MessageDecoder.Codes code) {
        presenterNewProjectItem.resultAddProjectItem(code);
    }


    @Override
    public void resultRemoteAddProjectItem(ProjectItem projectItem) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            long id = db.projectDao().insertProjectItem(projectItem);
            handler.post(() -> {
                resultLocalAddProjectItem(id);
                service.shutdown();
            });
        });
    }


    private void resultLocalAddProjectItem(long id){
        if (id != 0){
            presenterNewProjectItem.resultAddProjectItem(MessageDecoder.Codes.SUCCESS);
        } else {
            presenterNewProjectItem.resultAddProjectItem(MessageDecoder.Codes.FAIL_CUT);
        }
    }



    public void updateProjectItem(ProjectItem projectItem, ProjectContracts.MutualItemPresenter presenter) {
        this.presenterMutualItem = presenter;
        remoteDataProject.updateProjectItem(projectItem);
    }


    @Override
    public void resultRemoteUpdateProjectItem(MessageDecoder.Codes code) {
        presenterMutualItem.resultUpdateProjectItem(code);
    }


    private void resultLocalUpdateProjectItem(ProjectItem projectItem){
        if (projectItem != null){
            presenterMutualItem.resultUpdateProjectItem(projectItem);
        } else {
            presenterMutualItem.resultUpdateProjectItem(MessageDecoder.Codes.FAIL_CUT);
        }
    }


    @Override
    public void resultRemoteUpdateProjectItem(ProjectItem projectItem) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            db.projectDao().updateProjectItem(projectItem);
            handler.post(() -> {
                resultLocalUpdateProjectItem(projectItem);
                service.shutdown();
            });
        });
    }



    public void deleteProjectItem(ProjectItem projectItem) {
        remoteDataProject.deleteProjectItem(projectItem);
    }


    @Override
    public void resultRemoteDeleteProjectItem(MessageDecoder.Codes code) {
        presenterDesc.resultDeleteProjectItem(code);
    }


    private void resultLocalDeleteProjectItem(int resultId){
        if (resultId == 1){
            presenterDesc.resultDeleteProjectItem(MessageDecoder.Codes.SUCCESS);
        } else {
            presenterDesc.resultDeleteProjectItem(MessageDecoder.Codes.FAIL_CUT);
        }
    }


    @Override
    public void resultRemoteDeleteProjectItem(Integer id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            int resultId = db.projectDao().deleteProjectItem(id);
            handler.post(() -> {
                resultLocalDeleteProjectItem(resultId);
                service.shutdown();
            });
        });
    }


//    public void executedProjectItem(ProjectItem projectItem) {
//        //remoteDataProject.update
//    }


    public void deleteProject(Project project) {
        remoteDataProject.deleteProject(project);
    }


    @Override
    public void resultRemoteDeleteProject(MessageDecoder.Codes code) {
        presenterDesc.resultDeleteProject(code);
    }


    private void resultLocalDeleteProject(int resultId){
        if (resultId == 1){
            presenterDesc.resultDeleteProject(MessageDecoder.Codes.SUCCESS);
        } else {
            presenterDesc.resultDeleteProject(MessageDecoder.Codes.FAIL_CUT);
        }
    }


    @Override
    public void resultRemoteDeleteProject(Integer id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            int resultId = db.projectDao().deleteProject(id);
            handler.post(() -> {
                resultLocalDeleteProject(resultId);
                service.shutdown();
            });
        });
    }


    public void updateProject(Project project, ProjectContracts.MutualProjectPresenter presenter) {
        this.presenterMutualProject = presenter;
        remoteDataProject.updateProject(project);
    }


    @Override
    public void resultRemoteUpdateProject(MessageDecoder.Codes code) {
        presenterMutualProject.resultUpdateProject(code);
    }


    private void resultLocalUpdateProject(Project project){
        if (project != null){
            presenterMutualProject.resultUpdateProject(project);
        } else {
            presenterMutualProject.resultUpdateProject(MessageDecoder.Codes.FAIL_CUT);
        }
    }


    @Override
    public void resultRemoteUpdateProject(Project project) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            db.projectDao().updateProject(project);
            handler.post(() -> {
                resultLocalUpdateProject(project);
                service.shutdown();
            });
        });
    }


    public LiveData<CounterItem> countProjectItems(String groupToken, int projectId) {
        return db.projectDao().countProjectItems(groupToken, projectId);
    }


    public void setLocalProjectProgress(int id, int projectProgress) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            db.projectDao().setProjectProgress(id, projectProgress);
            handler.post(service::shutdown);
        });
    }


}
