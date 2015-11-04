package com.codepath.simpletodo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.db.ToDoDatabaseHelper;
import com.codepath.simpletodo.model.ToDoListItem;

/**
 * Created by venugopv on 11/1/15.
 */
public class ViewItemActivity extends AppCompatActivity {
    private static final String TAG = ViewItemActivity.class.getName();
    TextView txtItemText;
    TextView txtDueDate;
    TextView txtItemNotes;
    TextView txtPriority;
    TextView txtStatus;
    int itemID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fadeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.fade);
            fadeTransform.setStartDelay(0);
            fadeTransform.setDuration(500);
            getWindow().setEnterTransition(fadeTransform);
            getWindow().setExitTransition(fadeTransform);
        }
        itemID = getIntent().getIntExtra(MainActivity.EXTRA_ITEM_ID, -1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (itemID > -1) {
            initViews();
            loadData(itemID);
        } else {
            Toast.makeText(this, "Invalid item id", Toast.LENGTH_LONG).show();
            ViewItemActivity.this.finish();
        }
    }

    private void initViews() {
        txtItemText = (TextView) findViewById(R.id.txt_task_text_view);
        txtDueDate = (TextView) findViewById(R.id.txt_due_date);
        txtItemNotes = (TextView) findViewById(R.id.txt_task_note_view);
        txtPriority = (TextView) findViewById(R.id.txt_priority);
        txtStatus = (TextView) findViewById(R.id.txt_status);
    }

    private void loadData(int itemID) {
        ToDoListItem item = ToDoDatabaseHelper.getsInstance(this).getTodo(itemID);
        if (item == null) {
            Toast.makeText(this, "Invalid item id", Toast.LENGTH_LONG).show();
            ViewItemActivity.this.finish();
        }
        txtItemText.setText(item.getItemText());
        txtItemNotes.setText(item.getItemNotes());
        txtDueDate.setText(item.getItemDueDate());
        txtPriority.setText(item.getItemPriorityText());
        txtPriority.setTextColor(item.getItemPriorityColor());
        String[] statusArray = getResources().getStringArray(R.array.status_array);
        txtStatus.setText(statusArray[item.getStatus()]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_describe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent editIntent = new Intent(this, EditItemActivity.class);
            editIntent.putExtra(MainActivity.EXTRA_ITEM_ID, itemID);
            startActivity(editIntent);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewItemActivity.this.finish();
                }
            }, 500);
            return true;
        } else if (id == R.id.action_delete) {
            confirmDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                    }

                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteItem() {
        ToDoDatabaseHelper.getsInstance(this).deleteTodo(itemID);
        ViewItemActivity.this.finish();
    }
}
