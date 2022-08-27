package com.example.mytodoapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

//This class handles all the database aspects of the application.
public class DatabaseHandler extends SQLiteOpenHelper {

    //Defining the constants that will be used in the database.
    private static final int VERSION = 1; //version of the database
    private static final String NAME = "toDoListDatabase"; //Database's name
    private static final String TODO_TABLE = "todo"; //The table that holds the information of each task.
    private static final String ID = "id"; //ID of the task.
    private static final String TASK = "task"; //Task's text field information.
    private static final String STATUS = "status"; //Status of the task, whether it's been completed or not.
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)"; // Creates the table with the appropriate columns via query.

    //Creates the SQL Database reference.
    private SQLiteDatabase database;

    //Initialises the class method.
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    //Override for the onCreate method that executes the table creation string as an SQL code.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    //This override onUpgrade method is called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops the table if it already exists
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Creates table again via the oncreate method.
        onCreate(db);
    }

    //Opens the database to allow for it's modification
    public void openDatabase() {
        database = this.getWritableDatabase();
    }

    //Method that adds the task to the database.
    public void insertTask(ToDoModel task){
        //Content values are used to store the information for each task.
        ContentValues cv = new ContentValues();
        //assigns the task to the correct value and then the same actions are applied to the status.
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        //inserts the correct information to the correct table.
        database.insert(TODO_TABLE, null, cv);
    }


    //This method gets all the tasks from the database and sends it to the arrayList so it can be visible on the recycler view.
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        database.beginTransaction();
        try{
            //returns the rows from the database without any conditions.
            cursor = database.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    //Allows for reading and writing from the data set.
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    //runs for every column until there is no more subsequent column to read.
                    while(cursor.moveToNext());
                }
            }
        }
        //closes the cursor function and ends the transaction process of the database.
        finally {
            database.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return taskList;
    }

    //Updates the database with the new status of the task.
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        database.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    //Updates the database with the new task details for a given task.
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        database.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    //Deletes the task from the database.
    public void deleteTask(int id){
        database.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}

