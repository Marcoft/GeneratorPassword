package com.pass.GeneratorPassword;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FragmentSave extends Fragment {

    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    private String Key_One = "";
    private String Password_One = "";
    private String Id_One = "";

    private String[] Key ;
    private String[] Password ;
    int i = 0;
    SQLiteDatabase sb;
    DbHelperS dbHelperS;

    public void sqliteReadCount(){
        SQLiteDatabase database = dbHelperS.getWritableDatabase();
        int z = 0;
        Cursor cursor = database.query(DbHelperS.TABLE_CONTACTS,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                z++;
            } while(cursor.moveToNext());
            Key = new String[z+1];
            Password = new String[z+1];
        }
        cursor.close();
    }


    public void sqliteRead(){
        SQLiteDatabase database = dbHelperS.getWritableDatabase();
        int z = 0;
        Cursor cursor = database.query(DbHelperS.TABLE_CONTACTS,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            int passindex = cursor.getColumnIndex(DbHelperS.KEY_PASSWORD);
            int keyind = cursor.getColumnIndex(DbHelperS.KEY_K);
            do {
                if(z == 0){
                    Password[z] = cursor.getString(passindex);
                    Key[z] = cursor.getString(keyind);
                    z++;
                    i = z;
                }
                else {
                    if (!cursor.getString(passindex).equals(Password[i - 1])) {
                        Password[z] = cursor.getString(passindex);
                        Key[z] = cursor.getString(keyind);
                        z++;
                        i = z;
                    }
                }
            } while(cursor.moveToNext());
        }
        cursor.close();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(false);
        View v = inflater.inflate(R.layout.activity_save, container, false);

        dbHelperS = new DbHelperS(getActivity());
        sb = dbHelperS.getWritableDatabase();
        mExampleList = new ArrayList<ExampleItem>();
        sqliteReadCount();
        sqliteRead();

        Bundle bundle = getArguments();
        if (bundle != null){
            if(i!=0) {
                if (!bundle.getString("Password", "").equals("")) {
                    Key[i] = bundle.getString("Key", "");
                    Password[i] = bundle.getString("Password", "");
                    ADDDATA(bundle.getString("Key", ""), bundle.getString("Password", ""));
                    i++;
                }
            }
            else if(!bundle.getString("Password", "").equalsIgnoreCase("")) {
                Key = new String[1];
                Password = new String[1];
                Key[0] = bundle.getString("Key", "");
                Password[0] = bundle.getString("Password", "");
                ADDDATA(bundle.getString("Key", ""), bundle.getString("Password", ""));
                i++;
            }
        }

        for (int j = 0; j < i; j++) {
            if (!Password[j].equals("")) {
                mExampleList.add(new ExampleItem(R.drawable.ic_lightbulb_outline_black_24dp, Key[j], Password[j]));
            }
        }



        buildRecyclerView(v,getActivity());

        dbHelperS.close();
        return v;
    }




    public void ADDDATA(String item,String item2){
        ContentValues cv = new ContentValues();
        cv.put(DbHelperS.KEY_K,item);
        cv.put(DbHelperS.KEY_PASSWORD,item2);

        long res = sb.insert(DbHelperS.TABLE_CONTACTS,null,cv);
        if (res == -1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.ErrorAddDate), Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    public void getIdByName(String name_k){

        SQLiteDatabase database = dbHelperS.getWritableDatabase();
        Cursor cursor2 = database.query(DbHelperS.TABLE_CONTACTS,null,null,null,null,null,null);
        if(cursor2.moveToFirst()){
            int KEY_PASSWORD = cursor2.getColumnIndex(DbHelperS.KEY_PASSWORD);
            int KEY_ID = cursor2.getColumnIndex(DbHelperS.KEY_ID);
            int KEY_K = cursor2.getColumnIndex(DbHelperS.KEY_K);
            do{
                Password_One = cursor2.getString(KEY_PASSWORD);
                if(Password_One.equalsIgnoreCase(name_k)) {
                    Key_One = cursor2.getString(KEY_K);
                    Id_One = cursor2.getString(KEY_ID);
                    break;
                }
            } while(cursor2.moveToNext());
        }
        cursor2.close();
    }

    public Integer deleteBase(String password){
        SQLiteDatabase db = dbHelperS.getWritableDatabase();
        return db.delete(DbHelperS.TABLE_CONTACTS,"password = ?",new String[]{ password });
    }


    public void removeItem(int position) {
        getIdByName(mExampleList.get(position).getText2());
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);

        deleteBase(String.valueOf(Password_One));
    }

    public void saveItem(int position) {
        //TODO Сохраняет данные в буфер обмена
        String pass = getResources().getString(R.string.Password);
        String copy = getResources().getString(R.string.copied);
        Toast.makeText(getActivity(), pass + mExampleList.get(position).getText2() + copy, Toast.LENGTH_SHORT).show();

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", mExampleList.get(position).getText2());
        clipboard.setPrimaryClip(clip);
    }


    public void buildRecyclerView(View v,Context c) {
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(c);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                saveItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

    }



}

