package com.astudio.progressmonitor.interfaces;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;

public interface ProjectContracts {


    interface MutualProjectPresenter{
        void resultUpdateProject(Project project);
        void resultUpdateProject(MessageDecoder.Codes code);
    }


    interface MutualItemPresenter{
        void resultUpdateProjectItem(ProjectItem projectItem);
        void resultUpdateProjectItem(MessageDecoder.Codes code);
    }


    interface ProjectPresenter{
        //LiveData<List<UserEasy>> getUserList();
        //LiveData<List<Project>> getProjects(int userId, int status);
        //void resultDeleteProject(MessageDecoder.Codes code);
    }


    interface DescPresenter{
        LiveData<List<ProjectItem>> getProjectItems(int projectId);
        void resultDeleteProjectItem(MessageDecoder.Codes code);
        void resultDeleteProject(MessageDecoder.Codes code);
    }


    interface NewProjectPresenter{
        void resultAddProject(MessageDecoder.Codes code);
    }


    interface NewProjectItemPresenter{
        void resultAddProjectItem(MessageDecoder.Codes code);
    }



    interface ProjectList{
        void transferSelectedPosFromRecyclerView(int selectedPosition);
        //void failDialog(MessageDecoder.Codes code);
       // void successDialog(MessageDecoder.Codes code);

        //void executedProject(Project project);
        //void editProject(Project project);
        //void deleteProject(Project project);
        //void activateProject(Project project);
    }


    interface ProjectItemList{
        void failDialog(MessageDecoder.Codes code);
        void successDialog(MessageDecoder.Codes code, String operationType);

        void editProjectItem(ProjectItem projectItem);
        void deleteProjectItem(ProjectItem projectItem);
        void getHistoryProjectItem(ProjectItem projectItem);
        void delegateProjectItem(ProjectItem projectItem);
        void scheduleProjectItem(ProjectItem projectItem);
        void executedProjectItem(ProjectItem projectItem);

        void scheduleProjectItemSetDuration(ProjectItem projectItem, String dateForDB);
    }


    interface View{
        void successDialog(MessageDecoder.Codes code);
        void failDialog(MessageDecoder.Codes code);
    }


    interface Repository{
        //void resultLocalBindPost(int result);
    }


}
