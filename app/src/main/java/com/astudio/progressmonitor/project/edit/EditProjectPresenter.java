package com.astudio.progressmonitor.project.edit;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.BasePresenter;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public class EditProjectPresenter extends BasePresenter implements ProjectContracts.MutualProjectPresenter {

    private ProjectContracts.View view;
    private static final String TAG = "EditProjectPresenter";
    private GlobalData globalData;


    public EditProjectPresenter(GlobalData globalData) {
        super();
        this.globalData = globalData;
    }


    public LiveData<List<UserEasy>> getUserList(){
        return repositoryUser.getUserEasyList(globalData.groupToken);
    }


    public void updateProject(Project project) {
        repositoryProject.updateProject(project, this);
    }


    @Override
    public void resultUpdateProject(Project project) {
        // метод на случай если нужно вернуть в активити обновленный экземпляр данных
        if(view == null) return;
        view.successDialog(MessageDecoder.Codes.SUCCESS);
    }


    @Override
    public void resultUpdateProject(MessageDecoder.Codes code) {
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
