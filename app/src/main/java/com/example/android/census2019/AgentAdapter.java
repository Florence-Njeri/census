package com.example.android.census2019;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.census2019.Activities.AgentActivity;

import org.w3c.dom.Text;

public class AgentAdapter  extends RecyclerView.Adapter<AgentAdapter.AgentAdapterViewHolder> {

public AgentAdapter(){}

    @NonNull
    @Override
    public AgentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
     View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_header_navigation_drawer, viewGroup, false);
        return new AgentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentAdapterViewHolder agentAdapterViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class AgentAdapterViewHolder extends RecyclerView.ViewHolder{
            TextView agentJob;
            TextView agentID;

        public AgentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            agentJob=itemView.findViewById(R.id.agent_job);
            agentID=itemView.findViewById(R.id.header_id);
        }
    }
}
