package com.astudio.progressmonitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.interfaces.PlanContracts;
import com.astudio.progressmonitor.plan.Plan;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;


public class AdapterRecyclerViewPlan extends RecyclerView.Adapter<AdapterRecyclerViewPlan.MyViewHolder>{

    private List<Plan> mPlans = new ArrayList<>();
    private Context mContext;
    private int mSelectedPos;
    private PlanContracts.ViewList mObject;


    public AdapterRecyclerViewPlan(Context context, PlanContracts.ViewList object){
        mContext = context;
        mObject = object;
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
        Plan plan = mPlans.get(position);
        holder.bind(plan);
    }

    @Override
    public int getItemCount() {
        return mPlans.size();
    }


    // For DiffUtil
//    List<Plan> getItems() {
//        return mPlans;
//    }


    public void setPlans(List<Plan> plans){
        mPlans.clear();
        mPlans.addAll(plans);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView descPlanTV;
        private TextView durationTV;
        private LinearProgressIndicator indicator;
        //private TextView mUpdatedAtTextView;
        //private ImageView statusTaskRecycleView;


        MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_plan, parent, false));

            descPlanTV = (TextView)itemView.findViewById(R.id.desc_plan);
            durationTV = (TextView)itemView.findViewById(R.id.duration_item_plan);
            indicator = itemView.findViewById(R.id.status_plan_indicator);
            indicator.setBackgroundColor(Color.TRANSPARENT);
            indicator.setIndicatorColor(mContext.getResources().getColor(R.color.colorProgressProject));
            //mUpdatedAtTextView = (TextView)itemView.findViewById(R.id.task_updated_at_out);
            //statusTaskRecycleView = (ImageView) itemView.findViewById(R.id.statusImageView);
            itemView.setOnClickListener(this);

        }


        void bind(Plan plan){
            String desc= plan.getDesc();
            descPlanTV.setText(desc);
            int status = plan.getStatus();
            durationTV.setText(plan.getFormattedDuration());
            if (plan.getStatus() == 400){
                indicator.setTrackColor(mContext.getResources().getColor(R.color.colorTaskListCard));
                indicator.setProgress(0);
            } else {
                indicator.setTrackColor(Color.LTGRAY);
                indicator.setProgress(status);
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
