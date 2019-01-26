package com.augmntd.kayastaff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private static final String TAG = "AttendanceAdapter";

    private ArrayList<String> mSubjectNames = new ArrayList<>();
    private ArrayList<String> mTeacherNames = new ArrayList<>();
    private ArrayList<String> mClassroomNames = new ArrayList<>();
    private Context mContext;
    OnSubjectClickListener onSubjectClickListener;

    public AttendanceAdapter(Context mContext, ArrayList<String> mSubjectNames, ArrayList<String> mTeacherNames, ArrayList<String> mClassroomNames, OnSubjectClickListener onSubjectClickListener) {
        this.mSubjectNames = mSubjectNames;
        this.mTeacherNames = mTeacherNames;
        this.mClassroomNames = mClassroomNames;
        this.mContext = mContext;
        this.onSubjectClickListener = onSubjectClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item, parent, false);
        return new ViewHolder(view, onSubjectClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.tvSubjectName.setText(mSubjectNames.get(position));
        holder.tvTeacherName.setText(mTeacherNames.get(position));
        holder.tvClassroom.setText(mClassroomNames.get(position));
    }

    @Override
    public int getItemCount() { return mSubjectNames.size(); }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvSubjectName, tvTeacherName, tvClassroom;
        ConstraintLayout mParentLayout;
        OnSubjectClickListener onSubjectClickListener;

        public ViewHolder(View itemView, OnSubjectClickListener onSubjectClickListener) {
            super(itemView);

            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvClassroom = itemView.findViewById(R.id.tvClassroom);
            mParentLayout = itemView.findViewById(R.id.mParentLayout);
            this.onSubjectClickListener = onSubjectClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSubjectClickListener.onSubjectClick(getAdapterPosition());
        }
    }

    public interface OnSubjectClickListener {
        void onSubjectClick(int position);
    }

}
