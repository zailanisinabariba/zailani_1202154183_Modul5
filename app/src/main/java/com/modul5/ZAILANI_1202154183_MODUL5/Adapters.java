package com.modul5.ZAILANI_1202154183_MODUL5;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.modul5.ZAILANI_1202154183_MODUL5.TodoPointer.*;

/**
 * Created by waski on 25/03/2018.
 */

public class Adapters extends RecyclerView.Adapter<Adapters.AdapterViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public Adapters(Context contex,Cursor cursor){
        mContext =contex;
        mCursor = cursor;

    }
    public class AdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView namaText,deskripText,prioText,idtable;



        public AdapterViewHolder(View itemView) {
            super(itemView);
            namaText = itemView.findViewById(R.id.textViewTodo);
            deskripText =itemView.findViewById(R.id.textViewDekripsi);
            prioText =itemView.findViewById(R.id.textViewPriority);
            SharedPreferences pref = itemView.getContext().getSharedPreferences("pref",0);
            String colorss = pref.getString("shapeColorTXT","#FFFFFF");
            CardView kartu12 = (CardView)itemView.findViewById(R.id.cardviewl);
            kartu12.setCardBackgroundColor(Color.parseColor(colorss));
        }
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_main,parent,false);
        return  new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        mCursor.moveToFirst();
        if(!mCursor.move(position)){return;}
        long ident = mCursor.getLong(mCursor.getColumnIndex(TodoEntry._ID));
        String aktifitas = mCursor.getString(mCursor.getColumnIndex(TodoEntry.COLUMN_NAMA_));
        String deskripsi = mCursor.getString(mCursor.getColumnIndex(TodoEntry.COLUMN_DESKRIPSI));
        int priority = mCursor.getInt(mCursor.getColumnIndex(TodoEntry.COLUMN_PRIOTIRTY));
        holder.itemView.setTag(ident);
        holder.namaText.setText(aktifitas);
        holder.deskripText.setText(deskripsi);
        holder.prioText.setText(String.valueOf(priority));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

}


