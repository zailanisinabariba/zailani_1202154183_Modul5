package com.modul5.ZAILANI_1202154183_MODUL5;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private Adapters mAdapter;
    private SQLiteDatabase mDatabases;
    private EditText mName, mDesc, mPro;
    private Button mButton,buttonMsubmit;
    private CardView kartu;
    private int shapeColor;
    private static int optionShapeColor;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("pref",0);
        prefEdit=pref.edit();
        Databasesql databses = new Databasesql(this);
        mDatabases = databses.getWritableDatabase();
        setContentView(R.layout.activity_main);
        RecyclerView  recyclerView = findViewById(R.id.recyleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
       //if(ambilData().getCount()<1){return;}
        mAdapter = new Adapters(this,ambilData());
        recyclerView.setAdapter(mAdapter);
        //showdatas();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_add_item,null);
                mName = (EditText) mView.findViewById(R.id.editTextTodo);
                mDesc = (EditText) mView.findViewById(R.id.editTextDes);
                mPro = (EditText) mView.findViewById(R.id.editTextPrio);
                mButton = (Button) mView.findViewById(R.id.buttonTambah);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cek()) {
                            Snackbar.make(view, "Isi Disini", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;}
                        String nama = mName.getText().toString();
                        String deskrip = mDesc.getText().toString();
                        String prio = mPro.getText().toString();
                        ContentValues cv = new ContentValues();
                        cv.put(TodoPointer.TodoEntry.COLUMN_NAMA_,nama);
                        cv.put(TodoPointer.TodoEntry.COLUMN_DESKRIPSI,deskrip);
                        cv.put(TodoPointer.TodoEntry.COLUMN_PRIOTIRTY,prio);
                        mDatabases.insert(TodoPointer.TodoEntry.TABLE_NAME,null,cv);
                        showdatas();
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("DATA::SLIE","id:"+viewHolder.itemView.getTag());
                deleteItem((long)viewHolder.itemView.getTag());
                showdatas();
            }
        }).attachToRecyclerView(recyclerView);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            shapeColor = R.color.colDefault;
            optionShapeColor = pref.getInt("optionShapeColorSelected",R.id.colordefault);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.radio_fragment,null);
            final RadioGroup rColor = (RadioGroup)mView.findViewById(R.id.colorChange);
            buttonMsubmit = (Button)mView.findViewById(R.id.colorswipe1);
            rColor.check(optionShapeColor);
            mBuilder.setView(mView);
            final AlertDialog dialog2 = mBuilder.create();
            buttonMsubmit.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    kartu = (CardView)findViewById(R.id.cardviewl);
                    int id =rColor.getCheckedRadioButtonId();
                    switch (id){
                        case R.id.colordefault : optionShapeColor=R.id.colordefault;shapeColor=R.color.colDefault;break;
                        case R.id.colorRed : optionShapeColor=R.id.colordefault;shapeColor=R.color.colRed;break;
                        case R.id.colorBlue : optionShapeColor=R.id.colordefault;shapeColor=R.color.colBlue;break;
                        case R.id.colorGreen : optionShapeColor=R.id.colordefault;shapeColor=R.color.colGreen;break;
                    }dialog2.dismiss();
                    String color =getResources().getString(shapeColor);
                    prefEdit.putInt("optionShapeColorSelected",optionShapeColor);
                    prefEdit.putInt("shapeColor",shapeColor);
                    prefEdit.putString("shapeColorTXT",color);
                    prefEdit.commit();
                    showdatas();
                }
            });

            dialog2.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public void colorChanger(){

    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    private Cursor ambilData(){
        return mDatabases.query(
                TodoPointer.TodoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TodoPointer.TodoEntry._ID +" DESC"
        );
    }
    private void deleteItem(long id){
        mDatabases.delete(TodoPointer.TodoEntry.TABLE_NAME,
                TodoPointer.TodoEntry._ID+"="+id,null);

    }

    private boolean cek() {
        if (mName.getText().toString().isEmpty()) {
            return true;
        }if(mDesc.getText().toString().isEmpty()){
            return true;
        }if (mPro.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }
    private void showdatas(){
        RecyclerView  recyclerView = findViewById(R.id.recyleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mAdapter = new Adapters(this,ambilData());
        recyclerView.setAdapter(mAdapter);
    }
}
