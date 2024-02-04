package com.example.to_do_v3.TaskBackend;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SQLiteDatabase db;
    private static final String db_name ="TODO_DATABASE";
    private static final String table_name ="TODO_TABLE";

    private static final String[] columns = {"NAME","DESCRIPTION","DUE_DATE","TYPE","STATUS"};;
    //name, description, due_date, type, status

    public DataBaseHelper(@Nullable Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableCols = "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, DUE_DATE TEXT, TYPE TEXT, STATUS INTEGER )";
        db.execSQL("CREATE TABLE IF NOT EXISTS " + table_name + tableCols);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    private ContentValues putValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(columns[0], task.getName()); // set name
        values.put(columns[1], task.getDescription()); // set description
        values.put(columns[2], task.getDueDate()); // set due date
        values.put(columns[3], task.getType()); //set type
        values.put(columns[4], 0); // set check status to unchecked
        return values;
    }

    private ContentValues putValues(String name, String description, String dueDate, String type, int status) {
        ContentValues values = new ContentValues();
        values.put(columns[0], name); // set name
        values.put(columns[1], description); // set description
        values.put(columns[2], dueDate); // set due date
        values.put(columns[3], type); //set type
        values.put(columns[4], status); // set check status to unchecked
        return values;
    }

    public void insertTask(Task task) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        putValues(task);
        db.insert(table_name,null, values);
    }

    public void updateTask(int id, String type, String value) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        switch(type) {
            case "name":
                values.put(columns[0], value);
                break;
            case "description":
                values.put(columns[1], value);
                break;
            case "dueDate":
                values.put(columns[2], value);
                break;
            case "type":
                values.put(columns[3], value);
                break;
            case "status":
                values.put(columns[4], Integer.parseInt(value));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        //putValues(t.getName(),t.getDescription(),t.getDueDate(),t.getType(),t.getStatus());
        db.update(table_name, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.delete(table_name,"ID=?", new String[]{String.valueOf(id)});
    }

    public List<Task> getAllTasks() {
        Cursor cursor = null;
        List<Task> taskList = new ArrayList<>();

        db.beginTransaction();

        try {
            cursor = db.query(table_name, null,null,null,null,null,null);
            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    do {
                        Date d = null;
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(columns[0]));
                        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(columns[1]));
                        @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex(columns[2]));
                        @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(columns[3]));
                        @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(columns[4]));
                        Task task = new Task(id, name, description, dueDate, type, status);
                        taskList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return taskList;
    }
}

