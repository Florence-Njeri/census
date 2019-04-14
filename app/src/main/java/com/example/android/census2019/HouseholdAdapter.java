package com.example.android.census2019;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HouseholdAdapter  extends RecyclerView.Adapter<HouseholdAdapter.HouseholdViewHolder>{
    //Constructor
    private Context mContext;
    private ArrayList<Household> mList;

    public HouseholdAdapter(Context context,ArrayList<Household> list){
        this.mContext=context;
        this.mList=list;
    }

    @NonNull
    @Override
    public HouseholdViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View to inflate with data from firestore
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.household_view, viewGroup, false);
        return new HouseholdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseholdViewHolder householdViewHolder, int position) {
        Household house=  mList.get(position);

        householdViewHolder.head.setText(house.getHead());
        householdViewHolder.county.setText(house.getCounty());
        householdViewHolder.subCounty.setText(house.getSubCounty());

    }

    @Override
    public int getItemCount() {
        return   mList == null ? 0 : mList.size();
    }
    //View holder
    public static class HouseholdViewHolder extends RecyclerView.ViewHolder{
        //Initialize the views

        TextView head;
        TextView county;
        TextView subCounty;

        public HouseholdViewHolder(@NonNull View itemView) {
            super(itemView);
            //        Find the views by id

            head=itemView.findViewById(R.id.head_placeholder);
            county =itemView.findViewById(R.id.county_placeholder);
            subCounty =itemView.findViewById(R.id.sub_county_placeholder);


        }
    }
}

