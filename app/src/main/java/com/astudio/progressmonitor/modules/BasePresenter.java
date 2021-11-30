package com.astudio.progressmonitor.modules;

import androidx.lifecycle.LiveData;

import com.astudio.progressmonitor.group.Group;
import com.astudio.progressmonitor.group.RepositoryGroup;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.plan.RepositoryPlan;
import com.astudio.progressmonitor.post.RepositoryPost;
import com.astudio.progressmonitor.project.RepositoryProject;
import com.astudio.progressmonitor.promo.RepositoryPromo;
import com.astudio.progressmonitor.statistic.RepositoryStatistic;
import com.astudio.progressmonitor.task.RepositoryTask;
import com.astudio.progressmonitor.user.RepositoryUser;
import com.astudio.progressmonitor.voting.RepositoryVoting;

import java.util.Random;

public abstract class BasePresenter {

    protected RepositoryTask repositoryTask = App.getInstance().getRepositoryTask();
    protected RepositoryUser repositoryUser = App.getInstance().getRepositoryUser();
    protected RepositoryPost repositoryPost = App.getInstance().getRepositoryPost();
    protected RepositoryPlan repositoryPlan = App.getInstance().getRepositoryPlan();
    protected RepositoryProject repositoryProject = App.getInstance().getRepositoryProject();
    protected RepositoryPromo repositoryPromo = App.getInstance().getRepositoryPromo();
    protected RepositoryGroup repositoryGroup = App.getInstance().getRepositoryGroup();
    protected RepositoryStatistic repositoryStatistic = App.getInstance().getRepositoryStatistic();
    protected RepositoryVoting repositoryVoting = App.getInstance().getRepositoryVoting();
    public Validator validator = App.getInstance().getValidator();
    private static final String TAG = "BasePresenter";


//    public interface ProxyInstance{
//        void checkRole(String s);
//    }


    protected String generatePass(){
        Password passGenerator = new Password();
        return passGenerator.generate(8);
    }


    class Password {
        private static final String DICT = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789";
        private Random random = new Random();

        String generate(int length) {
            StringBuilder sb = new StringBuilder();

            for ( ; length > 0; --length )
                sb.append(DICT.charAt(random.nextInt(DICT.length())));
            return sb.toString();
        }

    }



}
