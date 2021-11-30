package com.astudio.progressmonitor.project.newproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.group.NavigationActivity;
import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.plan.newplan.NewPlanPresenter;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.support.MySysUtil;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewProjectActivity extends NavigationActivity implements ProjectContracts.View {


    private MenuItem btnCreateProject;
    private NewProjectPresenter mPresenter;
    private static final String TAG = "NewProjectActivity";
    private int visibility;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        globalData = intent.getParcelableExtra(GLOBAL_DATA);

        setContentView(R.layout.activity_new_project);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_third);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getString(R.string.hint_create_task));
        toolbar.setTitle("Добавить проект"); //TODO: add string resource

        toolbar.setNavigationIcon(R.drawable.back_task_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setActivityNameForHelpActivity(TAG);

        mPresenter = new NewProjectPresenter(globalData);

        SwitchMaterial switchvisibilityProject = findViewById(R.id.switch_visibility_project);
        switchvisibilityProject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                // unchecked = public (0), checked = private (1)
                visibility = (isChecked) ? 1 : 0;
            }
        });

        Log.e(TAG, "onCreate called ");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_project_menu, menu);
        btnCreateProject = menu.findItem(R.id.menu_btn_create_project);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.menu_btn_create_project:
                createNewProject();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createNewProject() {
        boolean errorCheckLocal = true;
        int maxLength = 300;
        int status = 0;
        EditText descET = findViewById(R.id.desc_project);
        String desc = descET.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            descET.setError(getString(R.string.empty_text_error_message));
            errorCheckLocal = false;
        }

        if (desc.length() > maxLength) {
            descET.setError(getString(R.string.max_length_long_text_error_message) + " " + maxLength + getString(R.string.symbol));
            errorCheckLocal = false;
        }

        if(errorCheckLocal){
            btnCreateProject.setEnabled(false);
            mPresenter.createNewProject(new Project(globalData.userId, desc, status, visibility, globalData.groupToken));
        }
    }


    @Override
    public void successDialog(MessageDecoder.Codes code) {
        finish();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.app_name))
//                .setMessage(new MessageDecoder(this, code).decode())
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        onBackPressed();
//                    }
//                })
//        ;
//        builder.create();
//        builder.show();
    }



    @Override
    public void failDialog(MessageDecoder.Codes code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(new MessageDecoder(this, code).decode())
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        btnCreateProject.setEnabled(true);
                    }
                })
        ;
        builder.create();
        builder.show();
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
        Log.e(TAG, "onStart called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart called");
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.attachView(this);
        Log.e(TAG, "onResume called");
        //mPresenter.viewIsReady();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        Log.e(TAG, "onDestroy called");
    }

}
