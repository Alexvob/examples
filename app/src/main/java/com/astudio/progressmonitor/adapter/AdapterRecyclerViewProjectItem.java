package com.astudio.progressmonitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.interfaces.ProjectContracts;
import com.astudio.progressmonitor.modules.GlobalData;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectItem;
import com.astudio.progressmonitor.support.MySysUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AdapterRecyclerViewProjectItem extends RecyclerView.Adapter<AdapterRecyclerViewProjectItem.MyViewHolder> {

    private List<ProjectItem> mProjects = new ArrayList<>();
    private Context mContext;
    private int mSelectedPos;
    private GlobalData globalData;
    private Project project;
    private ProjectContracts.ProjectItemList mObject;
    // https://www.simplifiedcoding.net/create-options-menu-recyclerview-item-tutorial/


    public AdapterRecyclerViewProjectItem(Context context, ProjectContracts.ProjectItemList object, GlobalData globalData, Project project){
        mContext = context;
        mObject = object;
        this.globalData = globalData;
        this.project = project;
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
        ProjectItem projectItem = mProjects.get(position);

//        if (projectItem.getItem().contains(".")){
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(30,0,0,0);
//            holder.itemView.setLayoutParams(params);
//        }

        holder.bind(projectItem);
        holder.optionsBtnEditProjectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.optionsBtnEditProjectItem);
                MenuInflater inflater = popup.getMenuInflater();

                if (project.getOwnerId() == globalData.userId){

                    inflater.inflate(R.menu.option_project_item_menu_extended, popup.getMenu());

//                    if ( !projectItem.getDesc().contains("##") ){
//                        popup.getMenu().findItem(R.id.menu_btn_history_project_item).setVisible(false);
//                    }

                    if (projectItem.getStatus() == 100){
                        popup.getMenu().findItem(R.id.menu_btn_executed_project_item).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_btn_edit_project_item).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_btn_delegate_project_item).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_btn_schedule_project_item).setVisible(false);
                    }

                    if (project.getStatus() == 101){
                        popup.getMenu().findItem(R.id.menu_btn_delete_project_item).setVisible(false);
                    }

                    if (projectItem.getExecutorId() != globalData.userId){
                        popup.getMenu().findItem(R.id.menu_btn_schedule_project_item).setVisible(false);
                    } else {
                        popup.getMenu().findItem(R.id.menu_btn_delegate_project_item).setVisible(false);
                    }

                } else {

                    inflater.inflate(R.menu.option_project_item_menu, popup.getMenu());

                    //когда пустое меню - ProjectDescriptionActivity has leaked window android.widget.PopupWindow$PopupDecorView that was originally added here
//                    if ( !projectItem.getDesc().contains("##") ){
//                        popup.getMenu().findItem(R.id.menu_btn_history_project_item).setVisible(false);
//                    }

                    if (projectItem.getStatus() == 100){
                        popup.getMenu().findItem(R.id.menu_btn_schedule_project_item).setVisible(false);
                    }

                    if (projectItem.getExecutorId() != globalData.userId){
                        popup.getMenu().findItem(R.id.menu_btn_schedule_project_item).setVisible(false);
                    }
                }


                popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_btn_executed_project_item:
                                mObject.executedProjectItem(projectItem);
                                return true;
                            case R.id.menu_btn_edit_project_item:
                                mObject.editProjectItem(projectItem);
                                return true;
                            case R.id.menu_btn_delete_project_item:
                                mObject.deleteProjectItem(projectItem);
                                return true;
                            case R.id.menu_btn_delegate_project_item:
                                mObject.delegateProjectItem(projectItem);
                                return true;
                            case R.id.menu_btn_schedule_project_item:
                                mObject.scheduleProjectItem(projectItem);
                                return true;
                            case R.id.menu_btn_history_project_item:
                                mObject.getHistoryProjectItem(projectItem);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }



    public void setProjectItems(List<ProjectItem> projects){
        mProjects.clear();
        mProjects.addAll(projects);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
    //class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemNumberProjectTV;
        private TextView descProjectItemTV;
        private TextView executorProjectItemTV;
        private TextView deadlineProjectItemTV;
        private ImageView optionsBtnEditProjectItem;



        MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_project_item, parent, false));

            itemNumberProjectTV = (TextView)itemView.findViewById(R.id.item_number);
            descProjectItemTV = (TextView)itemView.findViewById(R.id.desc_project_item);
            executorProjectItemTV = (TextView)itemView.findViewById(R.id.executor_project_item);
            deadlineProjectItemTV = (TextView)itemView.findViewById(R.id.deadline_project_item);

            optionsBtnEditProjectItem = itemView.findViewById(R.id.image_btn_edit_project_item);
            //itemView.setOnClickListener(this);

        }


        void bind(ProjectItem projectItem){
            if (projectItem.getDesc().contains("##")){
                String fullString = projectItem.getDesc();
                List<String> list = new ArrayList<>(Arrays.asList(fullString.split("##")));
                //Log.e("ADAPTER", "listDesc: " + listDesc);
                descProjectItemTV.setText(list.get( list.size()-1 ));
            } else{
                String desc= projectItem.getDesc();
                descProjectItemTV.setText(desc);
            }

            //float item = projectItem.getItem();
            //String itemText = String.valueOf(item);
            String itemText = projectItem.getItem();

            if (projectItem.getStatus() == 100){
                itemText += " - выполнено";
                itemNumberProjectTV.setAlpha(0.4f);
                descProjectItemTV.setAlpha(0.4f);
                executorProjectItemTV.setAlpha(0.4f);
                deadlineProjectItemTV.setAlpha(0.4f);
            } else {
                if (!MySysUtil.compareDateAfter(projectItem.getDeadline())){
                    //deadlineProjectItemTV.setTextColor(Color.rgb(146, 71, 71));
                    deadlineProjectItemTV.setTextColor(mContext.getResources().getColor(R.color.colorOverdueTasks));
                }  else {
                    deadlineProjectItemTV.setTextColor(Color.rgb(128, 128,128));
                }
                itemNumberProjectTV.setAlpha(1f);
                descProjectItemTV.setAlpha(1f);
                executorProjectItemTV.setAlpha(1f);
                deadlineProjectItemTV.setAlpha(1f);
            }

            itemNumberProjectTV.setText(itemText);

            String executorName = projectItem.getExecutorName();
            executorProjectItemTV.setText(executorName);

            String deadline = projectItem.getDeadline();
            deadlineProjectItemTV.setText(MySysUtil.formatDate(deadline));
        }


        //@Override
        public void onClick(View v) {
//            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
//            mSelectedPos = getAdapterPosition();
//            ProjectItem projectItem = mProjects.get(mSelectedPos);
            //mObject.transferSelectedPosFromRecyclerView(mSelectedPos);

//            PopupMenu popup = new PopupMenu(v.getContext(), v);
//            MenuInflater inflater = popup.getMenuInflater();
//            inflater.inflate(R.menu.option_project_item_menu, popup.getMenu());
//            popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.menu_btn_edit_project_item:
//                            Log.e("Holder", "edit" + projectItem.getDesc());
//                            return true;
//                        case R.id.menu_btn_delete_project_item:
//                            //handle menu2 click
//                            return true;
//                        default:
//                            return false;
//                    }
//                }
//            });
//            popup.show();
        }



    }
}
