package com.example.cosplan.data.Coplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplan.R;
import com.example.cosplan.ui.home.CosplayFragmentDirections;

import java.util.List;

public class CosplayAdapter extends RecyclerView.Adapter<CosplayAdapter.CosplayViewHolder> {
    private List<Cosplay> mCosplays;
    private final LayoutInflater mInflater;

    public CosplayAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CosplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cosplay_normalview, parent, false);
        return new CosplayViewHolder(itemView);
    }

    public void setCosplays(List<Cosplay> cosplays) {
        mCosplays = cosplays;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CosplayViewHolder holder, int position) {
        final Cosplay current = mCosplays.get(position);
        String tempName = current.mCosplayName;
        String tempEndDate = current.mCosplayEndDate;
        String tempPercentage = "% Complete";
        Bitmap bitmap = current.mCosplayIMG;

        holder.CosplayName.setText(tempName);
        holder.CosplayEndDate.setText(tempEndDate);
        holder.CosplayPercentage.setText(tempPercentage);
        holder.CosplayImg.setImageBitmap(bitmap);

        View itemView=holder.itemView;
        itemView.findViewById(R.id.cosplayrowLayout);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CosplayFragmentDirections.ActionNavHomeToCosplayScreen action=CosplayFragmentDirections.actionNavHomeToCosplayScreen(current);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCosplays != null) {
            return mCosplays.size();
        } else {
            return 0;
        }
    }

    public class CosplayViewHolder extends RecyclerView.ViewHolder {
        private final TextView CosplayName;
        private final TextView CosplayEndDate;
        private final TextView CosplayPercentage;
        private final ImageView CosplayImg;

        public CosplayViewHolder(@NonNull View itemView) {
            super(itemView);
            CosplayName = itemView.findViewById(R.id.TextView_CosplayName);
            CosplayEndDate = itemView.findViewById(R.id.TextView_CosplayEndDate);
            CosplayPercentage = itemView.findViewById(R.id.TextView_CosplayPercentage);
            CosplayImg = itemView.findViewById(R.id.ImageView_CosplayImage);
        }
    }
    public Cosplay getCosplayAtPosition(int position){
        return mCosplays.get(position);
    }
}
