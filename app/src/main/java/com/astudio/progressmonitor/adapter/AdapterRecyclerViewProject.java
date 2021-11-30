package com.astudio.progressmonitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.support.MySysUtil;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;


public class AdapterRecyclerViewProject extends RecyclerView.Adapter<AdapterRecyclerViewProject.MyViewHolder>{

    private List<Project> mProjects = new ArrayList<>();
    private Context mContext;
    private int mSelectedPos;
    private GlobalData globalData;
    private ProjectContracts.ProjectList mObject;


    public AdapterRecyclerViewProject(Context context, ProjectContracts.ProjectList object, GlobalData globalData){
        mContext = context;
        mObject = object;
        this.globalData = globalData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setSelected(mSelectedPos == position);
        //Log.e("OUT-onBindViewHolder", "bind, position = " + position);
        Project project = mProjects.get(position);

//        ImageView optionsBtnEditProject = holder.itemView.findViewById(R.id.image_btn_edit_project);
//        if (project.getOwnerId() == globalData.userId){
//            optionsBtnEditProject.setVisibility(View.VISIBLE);
//        } else {
//            optionsBtnEditProject.setVisibility(View.INVISIBLE);
//        }

        holder.bind(project);

//        holder.optionsBtnEditProject.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                PopupMenu popup = new PopupMenu(v.getContext(), holder.optionsBtnEditProject);
////                MenuInflater inflater = popup.getMenuInflater();
////                inflater.inflate(R.menu.option_project_menu, popup.getMenu());
////
////                if (project.getStatus() == 101){
////                    popup.getMenu().findItem(R.id.menu_btn_executed_project).setVisible(false);
////                    popup.getMenu().findItem(R.id.menu_btn_edit_project).setVisible(false);
////                    popup.getMenu().findItem(R.id.menu_btn_delete_project).setVisible(true);
////                    popup.getMenu().findItem(R.id.menu_btn_activate_project).setVisible(true);
////                } else {
////                    if (project.getStatus() == 100){
////                        popup.getMenu().findItem(R.id.menu_btn_executed_project).setVisible(true);
////                    } else {
////                        popup.getMenu().findItem(R.id.menu_btn_executed_project).setVisible(false);
////                    }
////                    popup.getMenu().findItem(R.id.menu_btn_edit_project).setVisible(true);
////                    popup.getMenu().findItem(R.id.menu_btn_delete_project).setVisible(false);
////                    popup.getMenu().findItem(R.id.menu_btn_activate_project).setVisible(false);
////                }
////
////                popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener() {
////                    @Override
////                    public boolean onMenuItemClick(MenuItem item) {
////                        switch (item.getItemId()) {
////                            case R.id.menu_btn_executed_project:
////                                //mObject.executedProject(project);
////                                return true;
////                            case R.id.menu_btn_edit_project:
////                                //mObject.editProject(project);
////                                return true;
////                            case R.id.menu_btn_delete_project:
////                                //mObject.deleteProject(project);
////                                return true;
////                            case R.id.menu_btn_activate_project:
////                                //mObject.activateProject(project);
////                                return true;
////                            default:
////                                return false;
////                        }
////                    }
////                });
////                popup.show();
////
////            }
////        });

    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }


    // For DiffUtil
//    List<Plan> getItems() {
//        return mPlans;
//    }


    public void setProjects(List<Project> projects){
        mProjects.clear();
        mProjects.addAll(projects);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView descProjectTV;
        private TextView dateProjectTV;
        private TextView visibilityStatus;
        private TextView statusTV;
        private LinearProgressIndicator indicator;
        //private ImageView optionsBtnEditProject;


        MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_project, parent, false));

            descProjectTV = (TextView)itemView.findViewById(R.id.desc_project);
            dateProjectTV = itemView.findViewById(R.id.date_project);
            indicator = itemView.findViewById(R.id.status_project_indicator);
            indicator.setBackgroundColor(Color.TRANSPARENT);
            indicator.setIndicatorColor(mContext.getResources().getColor(R.color.colorProgressProject));
            //optionsBtnEditProject = itemView.findViewById(R.id.image_btn_edit_project);
            visibilityStatus = itemView.findViewById(R.id.visibility_status);

            itemView.setOnClickListener(this);

        }


        void bind(Project project){

            String desc = project.getDesc();
            descProjectTV.setText(desc);
            String date = MySysUtil.separateTime(project.getCreatedAt());
            // formatting date
            dateProjectTV.setText(date);
            int status = project.getStatus();
            //statusTV.setText(String.valueOf(status));
            indicator.setProgress(status);
            if (project.getVisibility() == 1){
                visibilityStatus.setText("Private");
            } else {
                visibilityStatus.setText("Public");
            }
        }


        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // вроде как для передачи в родительский фрагмент позиции выбранной строки из списка задач
            // Updating old as well as new positions
            //notifyItemChanged(mSelectedPos);
            mSelectedPos = getAdapterPosition();
            //notifyItemChanged(mSelectedPos);
            // Do your another stuff for your onClick
            mObject.transferSelectedPosFromRecyclerView(mSelectedPos);
        }

    }
}
