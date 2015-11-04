package com.codepath.simpletodo.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.db.ToDoDatabaseHelper;
import com.codepath.simpletodo.model.ToDoListItem;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getName();
    public static final String EXTRA_ITEM_ID = "extra_item_id";
    ListView listItems;
    boolean createCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        listItems = (ListView) findViewById(R.id.main_list);
        listItems.setOnItemClickListener(this);
        // load the data
        loadData();
        createCalled = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!createCalled) {
            Cursor newCursor = ToDoDatabaseHelper.getsInstance(this).getAllTodos();
            ((CursorAdapter) listItems.getAdapter()).changeCursor(newCursor);
        }
        createCalled = false;
    }

    private void loadData() {
        Cursor itemsCursor = ToDoDatabaseHelper.getsInstance(this).getAllTodos();
        ToDoListAdapter adapter = new ToDoListAdapter(this, itemsCursor, 0);
        listItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
            startActivity(editIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToDoListAdapter.ViewHolder holder = (ToDoListAdapter.ViewHolder) view.getTag();
        int itemID = holder.itemID;
        Intent viewIntent = new Intent(this, ViewItemActivity.class);
        viewIntent.putExtra(MainActivity.EXTRA_ITEM_ID, itemID);
        startActivity(viewIntent);
    }
}
