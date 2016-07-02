package edu.scu.mmhatre.mhatremahendrapa3;

/**
 * Created by mahendramhatre on 5/17/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.support.v7.widget.helper.ItemTouchHelper;

import static java.lang.String.*;

class VivzHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "vixzvdatabase.db";
    protected static final int DATABASE_VERSION = 9;

    protected static final String TABLE_NAME = "TAB12";

    protected static final String UID = "_id";
    protected static final String NAME = "name";
    protected static final String PATH = "path";
    protected static final String ORDER = "ord";
    //  protected static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+ NAME + " VARCHAR(255), " + PATH + " VARCHAR(255));";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+
            UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NAME + " TEXT, " +
            PATH + " TEXT, " +
            ORDER + " INTEGER )";
    protected static final String DROP_TABLE ="DROP TABLE IF EXISTS " +TABLE_NAME;
    protected Context context;

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (android.database.SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            Log.i("MOVIE_ADAPTER", "onCreate: " + e.toString());
        }
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        cursor.close();
        return cnt;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (android.database.SQLException e) {
                Toast.makeText(context,"onUpgrade Called",Toast.LENGTH_LONG).show();
            }
        }
    }

    VivzHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //     Toast.makeText(context,"Constructor called",Toast.LENGTH_LONG).show();
    }




}

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    public VivzHelper helper;
    public String path;
    public ArrayList<String> photo,note;
    public  ArrayList<Integer> id, order;
    Context context;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(order, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(order, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



    @Override
    public void onItemDismiss(int position) {

        int removeNote = id.get(position);
        deleteData(removeNote);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView year;
        public ImageView iv;

        public MyViewHolder(final View view) {
            super(view);

            iv = (ImageView)view.findViewById(R.id.imageView2);
            year = (TextView) view.findViewById(R.id.year);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(itemView.getContext(), ViewActivity.class).putExtra("Position", Integer.toString(pos));
                    itemView.getContext().startActivity(intent);

                }
            });
        }
    }

    public boolean deleteData(int removeID) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            return db.delete(VivzHelper.TABLE_NAME, VivzHelper.UID + "=" + removeID, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    public void getAllData( Context context) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {VivzHelper.UID, VivzHelper.NAME, VivzHelper.PATH, VivzHelper.ORDER};
        try {
            Cursor cursor = db.query(VivzHelper.TABLE_NAME, columns, null, null, null,null,null);
            int size = cursor.getCount();
            StringBuffer buffer = new StringBuffer();

            while (cursor.moveToNext()) {

                int cid = cursor.getInt(0);
                String name = cursor.getString(1);
                String password = cursor.getString(2);
                int ord = cursor.getInt(3);
                photo.add(password);
                id.add(cid);
                note.add(name);
                order.add(ord);

            }
            MainActivity.setCOUNT(cursor.getCount() + 1);

        } catch (Exception e) {
            Log.i("AllData", "getAllData: " + e.toString());
            Toast.makeText(context, "getAllData: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public long insertData(String path, String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VivzHelper.NAME, name);

        int count = MainActivity.getCOUNT() + 1;
//        contentValues.put(VivzHelper.ORDER, count);
        contentValues.put(VivzHelper.PATH, path);
        long idz = db.insert(VivzHelper.TABLE_NAME, null, contentValues);

        if (idz != -1) {
            ContentValues cv = new ContentValues();
            cv.put("ord",idz);
            int i = db.update(VivzHelper.TABLE_NAME, cv,"_id=" + idz, null);
            note.add(name);
            order.add((int) idz);
            id.add((int) idz);
            photo.add(path);
            MainActivity.setCOUNT(count);
        }
        return idz;
    }
    public String getPath(int pos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = { VivzHelper.PATH};
        Cursor cursor = db.query(VivzHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        cursor.move(pos);
        String name = cursor.getString(0);
        return name;
    }

    public String getText(int pos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = { VivzHelper.NAME};
        Cursor cursor = db.query(VivzHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        cursor.move(pos);

        String name = cursor.getString(0);


        return name;
    }



    public MoviesAdapter(Context context) {
        helper = new VivzHelper(context);
        photo = new ArrayList<>();
        note = new ArrayList<>();
        id = new ArrayList<>();
        order =new ArrayList<>();
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row, parent, false);

        getAllData(parent.getContext());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.year.setText(note.get(position));
        path = photo.get(position);
        File img = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap b = BitmapFactory.decodeFile(img.getAbsolutePath());
        holder.iv.setImageBitmap(b);
        Log.i("LIST SIZE", "onBindViewHolder: " + note.size());
    }

    @Override
    public int getItemCount() {
        return helper.getProfilesCount();
    }
}