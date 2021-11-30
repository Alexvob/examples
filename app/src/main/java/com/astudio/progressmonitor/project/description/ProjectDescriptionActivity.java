package com.astudio.progressmonitor.project.description;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.adapter.AdapterRecyclerViewProjectItem;
import com.astudio.progressmonitor.group.NavigationActivity;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.project.CounterItem;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.project.edit.EditProjectActivity;
import com.astudio.progressmonitor.project.edit_item.EditProjectItemActivity;
import com.astudio.progressmonitor.project.newitem.NewProjectItemActivity;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.support.MySysUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectDescriptionActivity extends NavigationActivity  implements ProjectContracts.ProjectItemList{

    private static final String TAG = "ProjectDescActivity";
    private static final String GLOBAL_DATA = "globalData";
    private static final String PROJECT = "project";
    private Project project;
    private ProjectDescPresenter presenter;
    private RecyclerView projectItemRecyclerView;
    private AdapterRecyclerViewProjectItem adapterRV;
    private LiveData<List<ProjectItem>> ldProjectItems;
    private List<ProjectItem> projectItems = new ArrayList<>();
    private MyObserverProjectItem myObserverLiveData;
    private FloatingActionButton fabNewItem;
    private TextView projectTitleTV;
    private int countProjectItem;
    private int projectProgress;
    private boolean isMenuLock;
    private int LAUNCH_SECOND_ACTIVITY;
    //private ImageView optionMenuBtn = findViewById(R.id.image_btn_edit_project_item);
    //private ProjectItem projectItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        globalData = intent.getParcelableExtra(GLOBAL_DATA);
        project = intent.getParcelableExtra(PROJECT);

        setContentView(R.layout.activity_project_description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_third);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle(globalData.groupName);
        toolbar.setNavigationIcon(R.drawable.back_task_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setActivityNameForHelpActivity(TAG);

        presenter = new ProjectDescPresenter(globalData);

        projectItemRecyclerView = (RecyclerView) findViewById(R.id.project_item_recycler_view);
        projectItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectItemRecyclerView.setHasFixedSize(true);

        adapterRV = new AdapterRecyclerViewProjectItem(this, this, globalData, project);
        projectItemRecyclerView.setAdapter(adapterRV);

        getLiveDataProjectItems();

        projectTitleTV = findViewById(R.id.project_title);
        setProjectTitle();
        TextView projectTitleDate = findViewById(R.id.project_title_date);
        projectTitleDate.setText(MySysUtil.separateTime(project.getCreatedAt()));
        setVisibilityStatus();

        fabNewItem = findViewById(R.id.fab_new_project_item);
        setVisibilityFabNewItem();
        fabNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewProjectItemActivity();
            }
        });

        LiveData<CounterItem> ldCounter = presenter.countProjectItems(project.getId());
        ldCounter.observe(this, new Observer<CounterItem>() {
            @Override
            public void onChanged(CounterItem counterItem) {
                if (counterItem != null) {
                    countProjectItem = counterItem.countAllItem;
                    Log.e(TAG, "Counter: " + counterItem);
                    projectProgress = counterItem.countProgressProject();
                    Log.e(TAG, "Counter: " + projectProgress);
                    if (project.getStatus() != 101)
                        presenter.setLocalProjectProgress(project.getId(), projectProgress);
                    invalidateOptionsMenu();
                }
            }
        });

        Log.e(TAG, "onCreate called");
    }


    private void sortItemsByVersion(List<ProjectItem> list){
        // http://www.java2s.com/Tutorials/Java/Data_Structure_How_to/Sort/Sort_software_version_number.htm
        Collections.sort(list, new Comparator<ProjectItem>(){
            @Override
            public int compare(ProjectItem projectItem1, ProjectItem projectItem2) {
            //public int compare(String version1, String version2) {
                String[] v1 = projectItem1.getItem().split("\\.");
                String[] v2 = projectItem2.getItem().split("\\.");
                int major1 = major(v1);
                int major2 = major(v2);
                if (major1 == major2) {
                    return minor(v1).compareTo(minor(v2));
                }
                return major1 > major2 ? 1 : -1;
            }
            private int major(String[] version) {
                return Integer.parseInt(version[0]);
            }
            private Integer minor(String[] version) {
                return version.length > 1 ? Integer.parseInt(version[1]) : 0;
            }
        });
    }


    private void setProjectTitle(){
        String title;
        if (project.getStatus() == 101){
            title = project.getDesc() + " - Archival";
        } else {
            title = project.getDesc();
        }
        projectTitleTV.setText(title);
    }


    private void setVisibilityFabNewItem(){
        //if (project.getStatus() == 101 | project.getOwnerId() != globalData.userId){
        if ( project.getOwnerId() == globalData.userId & project.getStatus() != 101 ){
            fabNewItem.setVisibility(View.VISIBLE);
        } else {
            fabNewItem.setVisibility(View.GONE);
        }
    }


    private void getLiveDataProjectItems(){
        ldProjectItems = presenter.getProjectItems(project.getId());
        myObserverLiveData = new MyObserverProjectItem();
        ldProjectItems.observe(this, myObserverLiveData);
    }


    class MyObserverProjectItem implements Observer<List<ProjectItem>> {
        @Override
        public void onChanged(List<ProjectItem> value) {
            //Log.e(TAG, "LiveData: " + this);
            projectItems.clear();
            if (value != null) {
                projectItems.addAll(value);
                sortItemsByVersion(projectItems);
                adapterRV.setProjectItems(projectItems);
                adapterRV.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void delegateProjectItem(ProjectItem projectItem) {
        setMenuLock(true);
        presenter.delegateProjectItem(projectItem);
    }


    @Override
    public void scheduleProjectItem(ProjectItem projectItem) {
        setMenuLock(true);
        showDatePickerSelectPlanDate(projectItem);
    }


    public void showDatePickerSelectPlanDate(ProjectItem projectItem) {
        DialogFragment newFragment = new ProjectDescriptionActivity.DatePickerFragment(this, projectItem);
        newFragment.show(getSupportFragmentManager(), "datePickerSelectPlanDate");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private ProjectContracts.ProjectItemList viewCallback;
        private ProjectItem selectedProjectItem;
        private Calendar currentDate;

        public DatePickerFragment(ProjectContracts.ProjectItemList view, ProjectItem projectItem) {
            this.viewCallback = view;
            this.selectedProjectItem = projectItem;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            //defaultDate.set(year, month, day);
            return new DatePickerDialog(requireActivity(), 0, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day);
            //Log.e(TAG, "SelectedDate: " + selectedDate.getTime());
            if (selectedDate.before(currentDate)) {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.invalid_date,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat sdfForDB = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateForDB = sdfForDB.format(selectedDate.getTime());
                viewCallback.scheduleProjectItemSetDuration(selectedProjectItem, dateForDB);
            }
        }
    }


    @Override
    public void scheduleProjectItemSetDuration(ProjectItem projectItem, String dateForDB){
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.number_picker_dialog_layout, findViewById(android.R.id.content), false);
        NumberPicker pickerHours = npView.findViewById(R.id.duration_hours);
        pickerHours.setMinValue(0);
        pickerHours.setMaxValue(12);

        NumberPicker pickerMinutes = npView.findViewById(R.id.duration_minutes);
        pickerMinutes.setMinValue(0);
        pickerMinutes.setMaxValue(50);
        pickerMinutes.setWrapSelectorWheel(false);
        pickerMinutes.setOnValueChangedListener(new ListenerNumberPickerMinutes());

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder
                .setTitle("Select duration:")
                .setView(npView)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_button_positive,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int hours = pickerHours.getValue();
                                int minutes = pickerMinutes.getValue();
                                int duration = hours * 60 + minutes;
                                if (duration == 0){
                                    Snackbar.make(findViewById(android.R.id.content), R.string.invalid_duration,
                                            Snackbar.LENGTH_SHORT).show();
                                } else {
                                    presenter.scheduleProjectItem(projectItem, dateForDB, hours, minutes);
                                }
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        LinearLayout titleTemplate = alertDialog.findViewById(R.id.root_duration_selector);
        Objects.requireNonNull(titleTemplate).setGravity(Gravity.CENTER);
    }


    private class ListenerNumberPickerMinutes implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            int durationMinutes = 0;
            if (newVal == 0 | newVal == 50){
                picker.setValue(newVal);
                //durationMinutes = newVal;
            } else {
                picker.setValue((newVal < oldVal) ? oldVal - 10 : oldVal + 10);
                durationMinutes = (newVal < oldVal) ? oldVal - 10 : oldVal + 10;
            }
            Log.e(TAG, "durationMinutes: " + durationMinutes);
        }
    }


    @Override
    public void executedProjectItem(ProjectItem projectItem) {
        setMenuLock(true);
        presenter.executedItem(projectItem);
    }


    @Override
    public void editProjectItem(ProjectItem projectItem) {
        Intent intent = new Intent(this, EditProjectItemActivity.class);
        intent.putExtra(GLOBAL_DATA, globalData);
        intent.putExtra("project", project);
        intent.putExtra("project_item", projectItem);
        startActivity(intent);
    }


    @Override
    public void getHistoryProjectItem(ProjectItem projectItem){
        //String imploded = String.join("|" ,projectItem.getDesc());
        String fullDesc = projectItem.getDesc();
        List<String> listDesc = new ArrayList<String>(Arrays.asList(fullDesc.split("##")));
        StringBuilder desc = new StringBuilder();
        for (String str: listDesc){
            desc.append(str).append("\n").append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(desc.toString())
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
        ;
        builder.create();
        builder.show();
    }


    @Override
    public void deleteProjectItem(ProjectItem projectItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(getString(R.string.alert_dialog_mess_delete_task)) // TODO: change to DELETE PROJECT ITEM
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setMenuLock(true);
                        presenter.deleteProjectItem(projectItem);
                    }
                })
                .setNegativeButton(getString(R.string.alert_dialog_button_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
        ;
        builder.create();
        builder.show();
    }


    private void startNewProjectItemActivity(){
        Intent intent = new Intent(this, NewProjectItemActivity.class);
        intent.putExtra(GLOBAL_DATA, globalData);
        intent.putExtra("project", project);
        startActivity(intent);
    }


    private void disableUI(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void enableUI(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_description_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setMenu(menu);
        if(isMenuLock){
            menu.setGroupEnabled(R.id.group_project_description_menu,false);
        }else{
            menu.setGroupEnabled(R.id.group_project_description_menu,true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void setMenu(Menu menu){
        // сначала отрисуется меню, потом будут данные счетчика
        if (project.getOwnerId() == globalData.userId) {
            if (countProjectItem == 0 | project.getStatus() == 101) {
                menu.findItem(R.id.menu_btn_delete_project).setVisible(true);
            } else {
                menu.findItem(R.id.menu_btn_delete_project).setVisible(false);
            }
            if (projectProgress == 100) {
                menu.findItem(R.id.menu_btn_executed_project).setVisible(true);
            } else {
                menu.findItem(R.id.menu_btn_executed_project).setVisible(false);
            }
            if (project.getStatus() == 101) {
                menu.findItem(R.id.menu_btn_activate_project).setVisible(true);
                menu.findItem(R.id.menu_btn_edit_project).setVisible(false);
                menu.findItem(R.id.menu_btn_executed_project).setVisible(false);
            } else {
                menu.findItem(R.id.menu_btn_activate_project).setVisible(false);
                menu.findItem(R.id.menu_btn_edit_project).setVisible(true);
            }
        } else {
            menu.setGroupVisible(R.id.group_project_description_menu, false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_btn_delete_project:
                deleteProject();
                return true;
            case R.id.menu_btn_edit_project:
                setMenuLock(true);
                editProject();
                return true;
            case R.id.menu_btn_executed_project:
                setMenuLock(true);
                executedProject();
                return true;
            case R.id.menu_btn_activate_project:
                setMenuLock(true);
                activateProject();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setMenuLock(boolean state){
        isMenuLock = state;
        invalidateOptionsMenu();
    }


    public void editProject() {
        LAUNCH_SECOND_ACTIVITY = 1;
        Intent intent = new Intent(this, EditProjectActivity.class);
        intent.putExtra(GLOBAL_DATA, globalData);
        intent.putExtra("project", project);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                project = data.getParcelableExtra(PROJECT);
                projectTitleTV.setText(Objects.requireNonNull(project).getDesc());
                setVisibilityStatus();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }


    public void deleteProject() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(getString(R.string.alert_dialog_mess_delete_task)) // TODO: change to DELETE PROJECT ITEM
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setMenuLock(true);
                        presenter.deleteProject(project);
                    }
                })
                .setNegativeButton(getString(R.string.alert_dialog_button_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
        ;
        builder.create();
        builder.show();
    }


    public void executedProject(){
        project.setStatus(101);
        presenter.updateProject(project);
    }


    public void activateProject() {
        project.setStatus(100);
        presenter.updateProject(project);
    }


    @Override
    public void successDialog(MessageDecoder.Codes code, String operationType) {
        switch (operationType){
            case "updateProject":
                setProjectTitle();
                setVisibilityFabNewItem();
                break;
            case "deleteProject":
                finish();
                break;
            case "updateItem":
            case "deleteItem":
                break;
            case "addTask":
            case "addPlan":
                Snackbar.make(findViewById(android.R.id.content), R.string.alert_dialog_mess_success_non_details,
                        Snackbar.LENGTH_SHORT).show();
                break;
        }
        setMenuLock(false);
    }


    @Override
    public void failDialog(MessageDecoder.Codes code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(new MessageDecoder(this, code).decode())
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
        ;
        builder.create();
        builder.show();
        setMenuLock(false);
    }


    private void setVisibilityStatus(){
        TextView visibilityProject = findViewById(R.id.visibility_project);
        if (project.getVisibility() == 1){
            visibilityProject.setText("Private");
        } else {
            visibilityProject.setText("Public");
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(GLOBAL_DATA, globalData);
        outState.putParcelable(PROJECT, project);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        globalData = savedInstanceState.getParcelable(GLOBAL_DATA);
        project = savedInstanceState.getParcelable(PROJECT);
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
        setMenuLock(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
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
