package com.codepath.simpletodo.ui;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.db.ToDoDatabaseHelper;
import com.codepath.simpletodo.model.ToDoListItem;

import org.w3c.dom.Text;

/**
 * Created by venugopv on 10/29/15.
 */
public class ToDoListAdapter extends CursorAdapter {
    LayoutInflater inflater;
    public ToDoListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = inflater.inflate(R.layout.list_item, null);
        ViewHolder holder = new ViewHolder();
        holder.txtTaskName = (TextView)v.findViewById(R.id.task_name);
        holder.txtTaskPriority = (TextView) v.findViewById(R.id.task_priority);
        holder.txtTaskDueDate = (TextView) v.findViewById(R.id.task_due_date);
        holder.imgStatus = (ImageView) v.findViewById(R.id.imgStatus);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.KEY_ITEM_TEXT));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.KEY_ITEM_PRIORITY));
        int itemID = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.KEY_ITEM_ID));
        String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.KEY_ITEM_DUE_DATE));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.KEY_ITEM_STATUS));
        String notes = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseHelper.KEY_ITEM_NOTES));
        ToDoListItem item = new ToDoListItem(itemID, body, notes, priority, dueDate, status);
        holder.txtTaskName.setText(item.getItemText());
        holder.txtTaskPriority.setText(item.getItemPriorityText());
        holder.txtTaskPriority.setTextColor(item.getItemPriorityColor());
        holder.txtTaskDueDate.setText(item.getItemDueDate());
        holder.itemID = item.getItemID();
        if (item.getStatus() == 0) {
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pending));
        } else {
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.done));
        }
    }

    public class ViewHolder {
        TextView txtTaskName;
        TextView txtTaskPriority;
        TextView txtTaskDueDate;
        ImageView imgStatus;
        int itemID;
    }
}
