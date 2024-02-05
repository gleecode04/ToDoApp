package com.example.to_do_v3;

import com.example.to_do_v3.TaskBackend.DataBaseHelper;
import com.example.to_do_v3.TaskBackend.Task;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder>{

    private List<Task> tlist;
    private MainActivity activity;
    private DataBaseHelper db;

    public ToDoAdapter(DataBaseHelper db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Task item = tlist.get(position);
        holder.checkBox.setText(item.getName());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item.setStatus(1);
                    db.updateTask(item.getId(),"status","1");
                } else {
                    item.setStatus(0);
                    db.updateTask(item.getId(),"status","0");
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<Task> tlist) {
        this.tlist = tlist;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        Task item = tlist.get(position);
        db.deleteTask(item.getId());
        tlist.remove(position);
        notifyItemRemoved(position);
    }

        //could make a custom dialog instead.
    public void editItem(int position) {
        Task item = tlist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("name", item.getName());
        bundle.putInt("status", item.getStatus());
        bundle.putString("dueDate", item.getDueDate());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return tlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_task);
        }
    }
}
