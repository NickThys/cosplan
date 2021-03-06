package com.example.cosplan.data.cosplay.checkList;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplan.R;

import java.util.List;

public class CheckListPartAdapter extends RecyclerView.Adapter<CheckListPartAdapter.CheckListPartViewHolder> {
    private List<ChecklistPart> mCheckListParts;
    private final LayoutInflater mLayoutInflater;
    private final Application mApplication;

    public CheckListPartAdapter(Context context, Application application) {
        mLayoutInflater = LayoutInflater.from(context);
        mApplication = application;
    }

    public static class CheckListPartViewHolder extends RecyclerView.ViewHolder {
        private final TextView mCheckListPartName;
        private final CheckBox mCheckListPartPacked;

        public CheckListPartViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckListPartName = itemView.findViewById(R.id.TextView_CheckListPartName);
            mCheckListPartPacked = itemView.findViewById(R.id.CheckBox_CheckListPartPacked);
        }
    }

    public ChecklistPart getChecklistPartAtPosition(int position) {
        return mCheckListParts.get(position);
    }

    public void setCheckListParts(List<ChecklistPart> checkListParts) {
        mCheckListParts = checkListParts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CheckListPartAdapter.CheckListPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.checklist_part_row, parent, false);
        return new CheckListPartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckListPartAdapter.CheckListPartViewHolder holder, int position) {
        final ChecklistPart mCurrentCheckListPart = mCheckListParts.get(position);
        String mCosplayCheckListPartName = mCurrentCheckListPart.mCosplayCheckListPartName;
        boolean mCosplayCheckListPartChecked = mCurrentCheckListPart.mCosplayCheckListPartChecked;
        holder.mCheckListPartName.setText(mCosplayCheckListPartName);
        holder.mCheckListPartPacked.setChecked(mCosplayCheckListPartChecked);
        holder.mCheckListPartPacked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckListPartViewModel viewModel = new CheckListPartViewModel(mApplication);
                ChecklistPart tempPart = new ChecklistPart(mCurrentCheckListPart.mCosplayId, mCurrentCheckListPart.mCosplayCheckListPartId, holder.mCheckListPartName.getText().toString(), holder.mCheckListPartPacked.isChecked());
                viewModel.update(tempPart);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCheckListParts != null) {
            return mCheckListParts.size();
        } else {
            return 0;
        }
    }
}
