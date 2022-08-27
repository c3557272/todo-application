package com.example.mytodoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

//Main Activity class that provides the main area where tasks will be displayed and modified.

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler database;
    private RecyclerView tasksRecyclerView;
    private TodoAdapter tasksAdapter;
    private FloatingActionButton floatBtn;
    private List<ToDoModel> taskList;

    //Override for the onCreate method for this activity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the correct view.
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        database = new DatabaseHandler(this);
        database.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TodoAdapter(database,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        floatBtn = findViewById(R.id.fab);

        taskList = database.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);

        //Float button that allows for new tasks to be created.

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    //Override for the handleDialogClose method that ensures the taskList is loaded correctly. The collection is reversed to assure the most recent
    //task is at the top. The adapter sets the tasks and notifies that the data has been changed.
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = database.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}