package com.tusharmalik.todowithdb;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tusharmalik.todowithdb.Models.Todo;
import com.tusharmalik.todowithdb.db.TodoDatabaseHelper;
import com.tusharmalik.todowithdb.db.TodoTable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Todo> tasks;
    ListView lvtasks;
    Button btnadd;
    SQLiteDatabase readDb;
    SQLiteDatabase writeDb;
    public static final String TAG = "gybu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvtasks=findViewById(R.id.lvItems);
        tasks=new ArrayList<>();
        TodoDatabaseHelper myDbHelper = new TodoDatabaseHelper(this);
        writeDb = myDbHelper.getWritableDatabase();
        readDb = myDbHelper.getReadableDatabase();
        
        final TaskAdapter taskAdapter = new TaskAdapter();
        lvtasks.setAdapter(taskAdapter);
        if(tasks!= null) {
            tasks = TodoTable.getAllTodos(readDb);
            taskAdapter.notifyDataSetChanged();
        }

        btnadd = (Button) findViewById(R.id.b1);
        btnadd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText et= (EditText) findViewById(R.id.e1);
                String itemText = et.getText().toString();
                if(!itemText.isEmpty()) {
                    Todo t = new Todo(1,itemText,false);
                    t.setData(itemText);
                    TodoTable.insertTodo(t,writeDb);
                    tasks.add(t);
                    taskAdapter.notifyDataSetChanged();

                    et.setText("");
                    Toast.makeText(MainActivity.this,"Added to the list!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Please enter the item you want to enter",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    class TaskAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Object getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = getLayoutInflater();
            View itemView = li.inflate(R.layout.finalact, parent, false);
            TextView tvitem= (TextView) itemView.findViewById(R.id.tv1);
            CheckBox cbitem= (CheckBox) itemView.findViewById(R.id.cb1);
            Button delitem= (Button) itemView.findViewById(R.id.b2);
            final Todo thistask= (Todo) getItem(position);
            position++;
            tvitem.setText(thistask.getData());
            cbitem.setChecked(thistask.isChecked());

            delitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int thisID = thistask.getId();
                    tasks.remove(thistask);
                    tasks.trimToSize();
                    Log.e(TAG, "onClick: "+thisID);
                    TodoTable.deleteTask(writeDb, thisID);

                    Toast.makeText(MainActivity.this,"DELETED!!!",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });



            cbitem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int thisid=thistask.getId();

                    if (isChecked) {
                        thistask.setChecked(true);
                        TodoTable.check(writeDb,true, thisid);
                        Toast.makeText(MainActivity.this,"Checked!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(!isChecked){
                        thistask.setChecked(false);
                        TodoTable.check(writeDb,false, thisid);
                        Toast.makeText(MainActivity.this,"Unchecked!!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return itemView;
        }
    }
}
