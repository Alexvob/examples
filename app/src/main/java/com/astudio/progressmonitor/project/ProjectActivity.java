package com.astudio.progressmonitor.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.adapter.AdapterRecyclerViewProject;
import com.astudio.progressmonitor.group.NavigationActivity;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.project.description.ProjectDescriptionActivity;
import com.astudio.progressmonitor.project.edit.EditProjectActivity;
import com.astudio.progressmonitor.project.edit.EditProjectPresenter;
import com.astudio.progressmonitor.project.newproject.NewProjectActivity;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.support.SortItem;
import com.astudio.progressmonitor.user.User;
import com.astudio.progressmonitor.user.UserEasy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectActivity extends NavigationActivity implements ProjectContracts.ProjectList {


    private static final String TAG = "ProjectActivity";
    private static final String GLOBAL_DATA = "globalData";
    private String role;
    private ProjectPresenter presenter;
    private Spinner spinnerUser;
    private LiveData<List<UserEasy>> ldUsers;
    private List<UserEasy> users = new ArrayList<>();
    private int selectedUserId;
    private RecyclerView projectRecyclerView;
    private AdapterRecyclerViewProject adapterRV;
    private LiveData<List<Project>> ldProjects;
    private List<Project> projects = new ArrayList<>();
    private MyObserverProject myObserverLiveData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getBundleExtra(GLOBAL_DATA);
        globalData = Objects.requireNonNull(bundle).getParcelable(GLOBAL_DATA);

        setContentView(R.layout.activity_project);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_third);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle(globalData.groupName);

        setActivityNameForHelpActivity(TAG);
        setBottomNav();

        presenter = new ProjectPresenter(globalData.groupToken);
        role = globalData.role;

        FloatingActionButton fabNewProject = findViewById(R.id.fab_new_project);
        fabNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewProjectActivity();
            }
        });

        List <SortItem> sortItems = new ArrayList<>();
        sortItems.add(new SortItem(0, getString(R.string.sort_active)));
        sortItems.add(new SortItem(101, getString(R.string.sort_archive)));

        Spinner spinnerSort = (Spinner) findViewById(R.id.sort_status_project);
        ArrayAdapter<SortItem> adapter = new ArrayAdapter<SortItem>(this, R.layout.project_spinner_item, R.id.customSpinnerItemTextView, sortItems);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortItem itemSpin = (SortItem) parent.getSelectedItem();
                int status = itemSpin.getPeriod();
                if(ldProjects != null) {
                    if(ldProjects.hasObservers()) {
                        ldProjects.removeObserver(myObserverLiveData);
                    }
                }
                getLiveDataProjects(status);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinnerUser = findViewById(R.id.select_user_project);
        ArrayAdapter<UserEasy> adapterUser = new ArrayAdapter<UserEasy>(this, android.R.layout.simple_spinner_item, users);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapterUser);
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserEasy item = (UserEasy) parent.getSelectedItem();
                selectedUserId = item.getId();

                if (selectedUserId != globalData.userId){
                    fabNewProject.setVisibility(View.GONE);
                } else {
                    fabNewProject.setVisibility(View.VISIBLE);
                }

                if(ldProjects != null) {
                    if(ldProjects.hasObservers()) {
                        ldProjects.removeObserver(myObserverLiveData);
                    }
                }
                spinnerSort.setSelection(0);
                getLiveDataProjects(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ldUsers = presenter.getUserList();
        ldUsers.observe(this, new Observer<List<UserEasy>>() {
            @Override
            public void onChanged(List <UserEasy> value) {
                users.clear();
                if (value != null) {
                    users.addAll(value);
                    if (!users.isEmpty()){
                        int selfPositionSpinner = findIndexYourself(users);
                        spinnerUser.setSelection(selfPositionSpinner);
                    }
                    adapterUser.notifyDataSetChanged();
                }
            }
        });

        projectRecyclerView = (RecyclerView) findViewById(R.id.project_recycler_view);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectRecyclerView.setHasFixedSize(true);

        adapterRV = new AdapterRecyclerViewProject(this, this, globalData);
        projectRecyclerView.setAdapter(adapterRV);

        //TODO: add roleObserver (PlanHost too)

        Log.e(TAG, "onCreate called");
    }


    private void getLiveDataProjects(int status){
        if(selectedUserId == globalData.userId) {
            ldProjects = presenter.getProjects(selectedUserId, status);
        } else {
            ldProjects = presenter.getProjectsWithoutPrivate(selectedUserId, status);
        }
        myObserverLiveData = new MyObserverProject();
        ldProjects.observe(this, myObserverLiveData);
    }


    class MyObserverProject implements Observer<List<Project>> {
        @Override
        public void onChanged(List<Project> value) {
            //Log.e(TAG, "LiveData: " + this);
            projects.clear();
            if (value != null) {
                projects.addAll(value);
                adapterRV.setProjects(projects);
                adapterRV.notifyDataSetChanged();
            }
        }
    }


//    private int findIndexYourself(List<UserEasy> list){
//        int index=0;
//        for (UserEasy user: list){
//            if (user.getId() == globalData.userId){
//                index = list.indexOf(user);
//            }
//        }
//        return index;
//    }


    private void startNewProjectActivity(){
        Intent intent = new Intent(this, NewProjectActivity.class);
        intent.putExtra(GLOBAL_DATA, globalData);
        startActivity(intent);
    }


    @Override
    public void transferSelectedPosFromRecyclerView(int position) {
        Project project = projects.get(position);
        gotoProjectDescriptionActivity(project);
    }


    public void gotoProjectDescriptionActivity(Project project){
        Intent intent = new Intent(this, ProjectDescriptionActivity.class);
        intent.putExtra(GLOBAL_DATA, globalData);
        intent.putExtra("project", project);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //menu.findItem(R.id.menu_lists).setVisible(false);
        //TODO: проконтролировать что везде в активностях идет проверка для админа
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_user_control).setVisible(false);
        if(Objects.requireNonNull(role).equals("a")){
            menu.findItem(R.id.menu_user_control).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(GLOBAL_DATA, globalData);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        globalData = savedInstanceState.getParcelable(GLOBAL_DATA);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);

        MenuItem currentItem = bottomNavigationView.getMenu().findItem(R.id.nav3);
        currentItem.setChecked(true);
        Log.e(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        Log.e(TAG, "onDestroy called");
    }


}
