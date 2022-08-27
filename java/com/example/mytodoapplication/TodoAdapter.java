package com.example.mytodoapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DatabaseHandler database;
    private MainActivity activity;

    public TodoAdapter(DatabaseHandler database, MainActivity activity) {
        this.database = database;
        this.activity = activity;
    }

    //This method must not be null and creates the viewholder for application the todo application.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    //This method is used to update the viewholder for the recycler view for items at a given position.
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        database.openDatabase();
        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));

        //This code checks to see if the status of the item is requiring an update.
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    database.updateStatus(item.getId(), 1);
                } else {
                    database.updateStatus(item.getId(), 0);
                }
            }
        });

        //This is the click listener for the delete button. It opens up an alert dialog box to delete the task.
        holder.itemView.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this Task?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deletes the task of the CardView selected.
                                deleteItem(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancels the
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.itemView.findViewById(R.id.editBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(position);
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    //Returns the size of the arraylist of tasks.
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    //Method to allow for the activity context to be called.
    public Context getContext() {
        return activity;
    }

    //Updates the data change when called.
    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    //method to delete the itemView(task) that is currently selected by the user.
    public void deleteItem(int position) {
        //creates the object for the method to use to execute any actions.
        ToDoModel item = todoList.get(position);
        database.deleteTask(item.getId());
        todoList.remove(position);
        //Notifies current observers of the deletion.
        notifyItemRemoved(position);
    }

    //Allows for the tasks to be edited.
    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    //This class Viewholder that defines the constructor for the task in the recycler view.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
