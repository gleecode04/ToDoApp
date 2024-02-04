package com.example.to_do_v3;

import com.example.to_do_v3.TaskBackend.DataBaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.to_do_v3.TaskBackend.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogClosedListener {

    private RecyclerView taskRecyclerView;
    private FloatingActionButton fab;
    private DataBaseHelper db;
    
    private List<Task> tlist;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRecyclerView = findViewById(R.id.recyclerViewTasks);
        fab = findViewById(R.id.fabAddTask);
        db = new DataBaseHelper(MainActivity.this);
        tlist = new ArrayList<>();
        adapter= new ToDoAdapter(db, MainActivity.this);

        tlist = db.getAllTasks();
        Collections.reverse(tlist);
        adapter.setTasks(tlist);
        adapter.notifyDataSetChanged();

        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.tag);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewHelper(adapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);


    }

    @Override
    public void onDialogClose(DialogInterface dialoginterface) {
        tlist = db.getAllTasks();
        Collections.reverse(tlist);
        adapter.setTasks(tlist);
        adapter.notifyDataSetChanged();
    }
}