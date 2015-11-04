package com.codepath.simpletodo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodo.model.ToDoListItem;

/**
 * Created by venugopv on 10/29/15.
 */
public class ToDoDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = ToDoDatabaseHelper.class.getName();
    private static final String DATABASE_NAME = "todoListDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TODO = "todo_list";
    public static final String KEY_ITEM_ID = "_id";
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_NOTES = "item_notes";
    public static final String KEY_ITEM_PRIORITY = "item_priority";
    public static final String KEY_ITEM_DUE_DATE = "item_due_date";
    public static final String KEY_ITEM_STATUS = "item_status";

    private static ToDoDatabaseHelper sInstance;

    private ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ToDoDatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoDatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTodoTable = "CREATE TABLE " + TABLE_TODO +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TEXT + " TEXT," +
                KEY_ITEM_NOTES + " TEXT," +
                KEY_ITEM_PRIORITY + " INTEGER," +
                KEY_ITEM_DUE_DATE + " TEXT, " +
                KEY_ITEM_STATUS + " INTETER" +
                ")";
        db.execSQL(createTodoTable);
    }

    public long storeTodo(ToDoListItem item) {
        SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.getItemText());
            values.put(KEY_ITEM_NOTES, item.getItemNotes());
            values.put(KEY_ITEM_PRIORITY, item.getItemPriorityID());
            values.put(KEY_ITEM_DUE_DATE, item.getItemDueDate());
            values.put(KEY_ITEM_STATUS, item.getStatus());

            return database.insertOrThrow(TABLE_TODO, null, values);
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add todo list item to database");
            return 0L;
        } catch (Throwable t) {
            Log.d(TAG, "Error while trying to add todo list item to database");
            return 0L;
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }
    }

    public Cursor getAllTodos() {
        String query = "Select * from " + TABLE_TODO;

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        return cursor;
    }

    public int updateTodo(ToDoListItem item) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.getItemText());
            values.put(KEY_ITEM_NOTES, item.getItemNotes());
            values.put(KEY_ITEM_PRIORITY, item.getItemPriorityID());
            values.put(KEY_ITEM_DUE_DATE, item.getItemDueDate());
            values.put(KEY_ITEM_STATUS, item.getStatus());
            return database.update(TABLE_TODO, values, KEY_ITEM_ID + "=?",
                    new String[]{String.valueOf(item.getItemID())});
        } catch (Exception ex) {
            Log.d(TAG, "Exception during update");
            return 0;
        } catch (Throwable t) {
            Log.d(TAG, "Exception during update");
            return 0;
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }
    }

    public ToDoListItem getTodo(int itemID) {
        String query = "Select * from " + TABLE_TODO + " where " + KEY_ITEM_ID + "=?";

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(itemID)});
        ToDoListItem item = null;
        try {
            if (cursor.moveToFirst()) {
                String itemText = cursor.getString(1);
                String itemNotes = cursor.getString(2);
                int itemPriority = cursor.getInt(3);
                String itemDueDate = cursor.getString(4);
                int itemStatus = cursor.getInt(5);
                item = new ToDoListItem(itemID, itemText, itemNotes,
                        itemPriority, itemDueDate, itemStatus);
            }

        } catch (Exception ex) {
            Log.d(TAG, "Exception during get");
            item = null;

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return item;
    }

    public void deleteAllTodos() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            database.delete(TABLE_TODO, null, null);
        } catch (Exception ex) {
            Log.d(TAG, "Unable to delete all entries in the table");
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    public int deleteTodo(ToDoListItem item) {
        if (item == null) {
            return 0;
        } else {
            return deleteTodo(item.getItemID());
        }
    }

    public int deleteTodo(int itemID) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            return database.delete(TABLE_TODO, KEY_ITEM_ID + "=?",
                    new String[]{String.valueOf(itemID)});
        } catch (Exception ex) {
            Log.d(TAG, "Exception during update");
            return 0;
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }
}
