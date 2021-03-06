package com.astudio.progressmonitor.project.newitem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.group.NavigationActivity;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.project.newproject.NewProjectPresenter;
import com.astudio.progressmonitor.support.MessageDecoder;
import com.astudio.progressmonitor.support.MySysUtil;
import com.astudio.progressmonitor.user.UserEasy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewProjectItemActivity extends NavigationActivity implements ProjectContracts.View {


    private NewProjectItemPresenter mPresenter;
    private static final String TAG = "NewProjectItemActivity";
    private MenuItem btnCreateProjectItem;
    private Project project;
    private LiveData<List<UserEasy>> ldUsers;
    private List<UserEasy> users = new ArrayList<>();
    private int selectedExecutorId;
    private String selectedExecutorName;
    private TextView deadLineTV;
    private static boolean errorCheck;
    private static String dateForDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        globalData = intent.getParcelableExtra(GLOBAL_DATA);
        project = intent.getParcelableExtra("project");

        setContentView(R.layout.activity_new_project_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_third);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getString(R.string.hint_create_task));
        toolbar.setTitle("???????????????? ?????????? ??????????????"); //TODO: add string resource

        toolbar.setNavigationIcon(R.drawable.back_task_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setActivityNameForHelpActivity(TAG);

        mPresenter = new NewProjectItemPresenter(globalData);

        Spinner spinnerSelectExecutor = (Spinner)findViewById(R.id.spinner_executor_project_item);
        ArrayAdapter<UserEasy> adapterSelectExecutor = new ArrayAdapter<UserEasy>(this, android.R.layout.simple_spinner_item, users);
        adapterSelectExecutor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectExecutor.setAdapter(adapterSelectExecutor);
        spinnerSelectExecutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserEasy item = (UserEasy) parent.getSelectedItem();
                selectedExecutorId = (item.getId());
                selectedExecutorName = (item.getName());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ldUsers = mPresenter.getUserList();
        ldUsers.observe(this, new Observer<List<UserEasy>>() {
            @Override
            public void onChanged(List <UserEasy> value) {
                users.clear();
                if (value != null) {
                    users.addAll(value);
                    int selfPositionSpinner = findIndexYourself(users);
                    spinnerSelectExecutor.setSelection(selfPositionSpinner);

                    switch (Objects.requireNonNull(globalData.parseSettings().get("PROJECTS_ACCESS_TO_USERS"))){
                        // ????????
                        case "all":
                            break;
                        case "child":
                            // ??????????????????????, ???????? ?? ???????????? ????????????????????????
                            List <UserEasy> childUsersAndImmediateParent = findChildUsersAndImmediateParent(users);
                            users.clear();
                            users.addAll(childUsersAndImmediateParent);
                            break;
                        // ??????????????????????, ????????, ?? ?????????????????????? ??????????????????????????
                        case "parent":
                            List <UserEasy> childList = findChildUsers(users);
                            List<UserEasy> trimmedUserList = new ArrayList<>(childList);
                            trimmedUserList.addAll(findAllParent(globalData.parentId, users));
                            users.clear();
                            users.addAll(trimmedUserList);
                            Collections.sort(users);
                            break;
                    }

                    //findChildForRemove();
                    adapterSelectExecutor.notifyDataSetChanged();
                }
            }
        });

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String installationDate = sdf1.format(Calendar.getInstance().getTime());
        deadLineTV = (TextView) findViewById(R.id.deadline_select_tv);
        deadLineTV.setText(installationDate);
        dateForDB = MySysUtil.reformatDateForDB(installationDate);

        errorCheck = true;

        Log.e(TAG, "onCreate called ");
    }


//    private void findChildForRemove(){
//        UserEasy userHimSelf = new UserEasy();
//        List<UserEasy> usersForRemove = new ArrayList<>();
//        for (UserEasy user: users){
//            if (user.getParentId() != globalData.userId) {
//                usersForRemove.add(user);
//            } else {
//                //childListForDescriptionPlan.add(user.getId());
//            }
//            if (user.getId() == globalData.userId){
//                userHimSelf = user;
//            }
//        }
//        users.removeAll(usersForRemove);
//        users.add(0, userHimSelf); //TODO: ?????????????????? ?????????????????? ?? ???????????? ?? ?????????? ???? ?????????????? users - ?????????? ?????????????????? ???????????? LiveData!
//    }


    public void showDatePickerSelectDeadline(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePickerSelectDeadline");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(requireActivity(), 0, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView deadlineTV = requireActivity().findViewById(R.id.deadline_select_tv);

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day);
            if (selectedDate.before(Calendar.getInstance())) {
                deadlineTV.setText(getString(R.string.invalid_date));
                deadlineTV.setError(getString(R.string.invalid_date));
                errorCheck = false;
            } else {
                deadlineTV.setError(null);
                errorCheck = true;
                //SimpleDateFormat sdfForTV = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat sdfForTV = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String dateForTextView = sdfForTV.format(selectedDate.getTime());
                deadlineTV.setText(dateForTextView);

                SimpleDateFormat sdfForDB = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dateForDB = sdfForDB.format(selectedDate.getTime());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_project_item_menu, menu);
        btnCreateProjectItem = menu.findItem(R.id.menu_btn_create_project_item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.menu_btn_create_project_item:
                createNewProjectItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createNewProjectItem() {
        boolean errorCheckLocal = true;
        int maxLength = 2000;
        EditText descET = findViewById(R.id.desc_project_item_et);
        String desc = descET.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            descET.setError(getString(R.string.empty_text_error_message));
            errorCheckLocal = false;
        }

        if (desc.length() > maxLength) {
            descET.setError(getString(R.string.max_length_long_text_error_message) + " " + maxLength + getString(R.string.symbol));
            errorCheckLocal = false;
        }

        EditText itemNumberET = findViewById(R.id.item_project_item_et);
        String itemString = itemNumberET.getText().toString();
        if (TextUtils.isEmpty(itemString)){
            itemNumberET.setError(getString(R.string.empty_text_error_message));
            errorCheckLocal = false;
        }

        int status = 0;

        if (TextUtils.isEmpty(selectedExecutorName) ){
            // selectedExecutorId == null ||
            errorCheckLocal = false;
        }

        if(errorCheck && errorCheckLocal){
            //float item = Float.valueOf(itemString);
            btnCreateProjectItem.setEnabled(false);
            mPresenter.createNewProjectItem( new ProjectItem(project.getId(), globalData.groupToken, itemString, desc, status, selectedExecutorId, selectedExecutorName, dateForDB ) );
        }
    }


    @Override
    public void successDialog(MessageDecoder.Codes code) {
        finish();
    }


    @Override
    public void failDialog(MessageDecoder.Codes code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(new MessageDecoder(this, code).decode())
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        btnCreateProjectItem.setEnabled(true);
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
