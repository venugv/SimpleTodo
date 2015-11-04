package com.codepath.simpletodo.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.db.ToDoDatabaseHelper;
import com.codepath.simpletodo.model.ToDoListItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by venugopv on 11/1/15.
 */
public class EditItemActivity extends AppCompatActivity {
    private static final String TAG = EditItemActivity.class.getName();
    EditText txtItemText;
    DatePicker dueDatePicker;
    EditText txtItemNotes;
    Spinner spinnerPriority;
    Spinner spinnerStatus;
    int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fadeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.fade);
            fadeTransform.setStartDelay(0);
            fadeTransform.setDuration(500);
            getWindow().setEnterTransition(fadeTransform);
            getWindow().setExitTransition(fadeTransform);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        itemID = getIntent().getIntExtra(MainActivity.EXTRA_ITEM_ID, -1);
        if (itemID > -1) {
            loadData(itemID);
        }
    }

    private void initViews() {
        txtItemText = (EditText) findViewById(R.id.txt_task_text);
        dueDatePicker = (DatePicker) findViewById(R.id.priority_date_picker);
        txtItemNotes = (EditText) findViewById(R.id.txt_task_note);
        spinnerPriority = (Spinner) findViewById(R.id.spinner_priority);
        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
    }

    private void loadData(int itemID) {
        ToDoListItem item = ToDoDatabaseHelper.getsInstance(this).getTodo(itemID);
        if (item == null) {
            Toast.makeText(this, "Invalid item id", Toast.LENGTH_LONG).show();
            EditItemActivity.this.finish();
        }
        txtItemText.setText(item.getItemText());
        txtItemNotes.setText(item.getItemNotes());
        spinnerStatus.setSelection(item.getStatus());
        spinnerPriority.setSelection(item.getItemPriorityID());
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dueDate = format1.parse(item.getItemDueDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dueDate);
            dueDatePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE));
        } catch (Exception ex) {
            Log.d(TAG, "Exception during date parse");
        }
    }

    private boolean saveData() {
        String itemText = txtItemText.getText().toString();
        if (TextUtils.isEmpty(itemText)) {
            Toast.makeText(this, "Item text should not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        String itemNotes = txtItemNotes.getText().toString();
        if (TextUtils.isEmpty(itemNotes)) {
            itemNotes = "";
        }
        String dueDate = String.format("%d-%02d-%02d", dueDatePicker.getYear(),
                dueDatePicker.getMonth() + 1, dueDatePicker.getDayOfMonth());
        int status = spinnerStatus.getSelectedItemPosition();
        int priority = spinnerPriority.getSelectedItemPosition();
        ToDoListItem item = new ToDoListItem(itemID, itemText, itemNotes, priority, dueDate, status);
        if (itemID > -1) {
            ToDoDatabaseHelper.getsInstance(this).updateTodo(item);
        } else {
            ToDoDatabaseHelper.getsInstance(this).storeTodo(item);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (saveData()) {
                EditItemActivity.this.finish();
            }
            return true;
        } else if (id == R.id.action_cancel) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit_title)
                    .setMessage(R.string.quit_confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
                            EditItemActivity.this.finish();
                        }

                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
