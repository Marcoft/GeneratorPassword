package com.pass.GeneratorPassword;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FragmentHome extends Fragment {

    private String[] symbols_mas = {
            "!",
            "@",
            "$",
            "&",
            "*",
            "_",
            "-",
            "?",
            "№",
            "%"
    };


    private Switch lowerCase,upCase,numb,symbols;
    private boolean lowerCase_checked = false,upCase_checked = false,numbers_checked = false,symbols_checked = false;
    private SeekBar seekBar;
    private Button generate,save_buff,shuffle;
    private ImageButton save_pass,clear_log,clear_pass;
    private EditText login;

    private ImageButton  clear_save1,clear_save2,save_save;
    private EditText edt_Name, edt_Password;

    private TextView sizeSeek,result,choosepar,nizn,verh,numbers,symbolss,minePass;

    private int progress_value = 8;
    private int like_id;

    private Random r;
    private Random r2;
    private int a;
    private String res = "";

    private DbHelperS dbHelperS;
    private SQLiteDatabase sb;
    private String Key_One = "";
    private String Password_One = "";
    private String Id_One = "";
    private boolean pass_true_false = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView =  inflater.inflate(R.layout.activity_frag_home,container,false);



        r = new Random();
        r2 = new Random();

        dbHelperS = new DbHelperS(getActivity());
        sb = dbHelperS.getWritableDatabase();

        lowerCase = (Switch) layoutView.findViewById(R.id.lowerCase);
        upCase = (Switch) layoutView.findViewById(R.id.upCase);
        numb = (Switch) layoutView.findViewById(R.id.numb);
        symbols = (Switch) layoutView.findViewById(R.id.symbols);

        lowerCase_checked = lowerCase.isChecked();
        upCase_checked = upCase.isChecked();
        numbers_checked = numb.isChecked();
        symbols_checked = symbols.isChecked();

        save_pass = (ImageButton) layoutView.findViewById(R.id.save_pass);
        clear_log = (ImageButton) layoutView.findViewById(R.id.clear_log);
        clear_pass = (ImageButton) layoutView.findViewById(R.id.clear_pass);

        clear_save1 = (ImageButton) layoutView.findViewById(R.id.clear_save1);
        clear_save2 = (ImageButton) layoutView.findViewById(R.id.clear_save2);
        save_save = (ImageButton) layoutView.findViewById(R.id.save_save);

        edt_Name = (EditText) layoutView.findViewById(R.id.edt_Name);
        edt_Password = (EditText) layoutView.findViewById(R.id.edt_Password);

        login = (EditText) layoutView.findViewById(R.id.login) ;


        seekBar = (SeekBar) layoutView.findViewById(R.id.progress);

        result = (TextView) layoutView.findViewById(R.id.result);
        choosepar = (TextView) layoutView.findViewById(R.id.choosepar);
        nizn = (TextView) layoutView.findViewById(R.id.nizn);
        verh = (TextView) layoutView.findViewById(R.id.verh);
        symbolss = (TextView) layoutView.findViewById(R.id.symbolss);
        numbers = (TextView) layoutView.findViewById(R.id.numbers);
        minePass = (TextView) layoutView.findViewById(R.id.minePass);
        generate = (Button) layoutView.findViewById(R.id.generate);
        save_buff = (Button) layoutView.findViewById(R.id.save_pass_buff);
        shuffle = (Button) layoutView.findViewById(R.id.shuffle);

        sizeSeek = (TextView) layoutView.findViewById(R.id.sizeSeek);

        seekBa();

        clear_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("");
            }
        });
        clear_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
            }
        });
        save_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!result.getText().toString().equalsIgnoreCase("")) {
                    if (like_id == 0) {
                        save_pass.setImageResource(R.drawable.ic_like);
                        like_id = 1;
                    } else {
                        save_pass.setImageResource(R.drawable.ic_like_off);
                        like_id = 0;
                    }

                    String save = null;
                    String no = null;
                    String yes = null;
                    save = getResources().getString(R.string.saveFragmentHome);
                    no = getResources().getString(R.string.noFragmentHome);
                    yes = getResources().getString(R.string.yesFragmentHome);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(save)
                            .setCancelable(false)
                            .setPositiveButton(no,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                            save_pass.setImageResource(R.drawable.ic_like_off);
                                            like_id = 0;
                                        }
                                    })
                            .setNegativeButton(yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            getIdByName(result.getText().toString());
                                            if(pass_true_false == true){
                                                FragmentSave fragment = new FragmentSave();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("Key", login.getText().toString());
                                                bundle.putString("Password", result.getText().toString());
                                                fragment.setArguments(bundle);
                                                FragmentManager manager = getFragmentManager();
                                                manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                            }else{
                                                save_pass.setImageResource(R.drawable.ic_like_off);
                                                like_id = 0;
                                                Toast.makeText(getActivity(), getResources().getString(R.string.PasswordExist), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.FillFields), Toast.LENGTH_SHORT).show();
                }

            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res = "";
                lowerCase_checked = lowerCase.isChecked();
                upCase_checked = upCase.isChecked();
                numbers_checked = numb.isChecked();
                symbols_checked = symbols.isChecked();

                sorT();
                result.setText(res);

            }
        });
        save_buff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", result.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getActivity(), getResources().getString(R.string.PasswordClipBoard), Toast.LENGTH_SHORT).show();


            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result.setText(result.getText().toString().replace(" ", ""));
                result.setText(shuffleWord(result.getText().toString()));

            }
        });



        clear_save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_Name.setText("");
            }
        });
        clear_save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_Password.setText("");
            }
        });

        save_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edt_Password.getText().toString().equalsIgnoreCase("")) {
                    if (like_id == 0) {
                        save_save.setImageResource(R.drawable.ic_like);
                        like_id = 1;
                    } else {
                        save_save.setImageResource(R.drawable.ic_like_off);
                        like_id = 0;
                    }

                    String save = null;
                    String no = null;
                    String yes = null;
                    save = getResources().getString(R.string.saveFragmentHome);
                    no = getResources().getString(R.string.noFragmentHome);
                    yes = getResources().getString(R.string.yesFragmentHome);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(save)
                            .setCancelable(false)
                            .setPositiveButton(no,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                            save_save.setImageResource(R.drawable.ic_like_off);
                                            like_id = 0;
                                        }
                                    })
                            .setNegativeButton(yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            getIdByName(edt_Password.getText().toString());
                                            if(pass_true_false == true){
                                                FragmentSave fragment = new FragmentSave();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("Key", edt_Name.getText().toString());
                                                bundle.putString("Password", edt_Password.getText().toString());
                                                fragment.setArguments(bundle);
                                                FragmentManager manager = getFragmentManager();
                                                manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                            }else{
                                                save_save.setImageResource(R.drawable.ic_like_off);
                                                like_id = 0;
                                                Toast.makeText(getActivity(), getResources().getString(R.string.PasswordExist), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.FillFields), Toast.LENGTH_SHORT).show();

                }
            }
        });




        return layoutView;
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
                    pass_true_false = false;
                    break;
                }
                else {
                    pass_true_false = true;
                }
            } while(cursor2.moveToNext());
        }
        cursor2.close();
    }

    public String sorT(){
        if(lowerCase_checked || upCase_checked || numbers_checked ||  symbols_checked ) {
            save_pass.setImageResource(R.drawable.ic_like_off);
            for(int i = 0; i < progress_value; ++i) {
                a = r.nextInt(4) + 1;
                switch (a) {
                    case 1:
                        if (lowerCase_checked)
                            res += String.valueOf(((char) (Math.random() * ('z' - 'a') + 'a' + 1)));
                        else --i;
                        break;
                    case 2:
                        if (upCase_checked)
                            res += String.valueOf(((char) (Math.random() * ('Z' - 'A') + 'A' + 1)));
                        else --i;
                        break;
                    case 3:
                        if (numbers_checked)
                            res += String.valueOf((int) (Math.random() * 9 + 1));
                        else --i;
                        break;
                    case 4:
                        if (symbols_checked)
                            res += String.valueOf(symbols_mas[r2.nextInt(symbols_mas.length)]);
                        else --i;
                        break;
                }
            }
        } else{
            Toast.makeText(getActivity(), getResources().getString(R.string.ChooseSomething), Toast.LENGTH_SHORT).show();
        }

        return res;
    }


    public void seekBa(){

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                if (progress == 1) {
                    String symb = getResources().getString(R.string.symbol1);
                    sizeSeek.setText(progress + symb);
                } else if (progress > 1 && progress < 5) {
                    String symb = getResources().getString(R.string.symbol15);
                    sizeSeek.setText(progress + symb);
                } else {
                    String symb = getResources().getString(R.string.symbol5);
                    sizeSeek.setText(progress + symb);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progress_value == 1) {
                    String symb = getResources().getString(R.string.symbol1);
                    sizeSeek.setText(progress_value + symb);
                } else if (progress_value > 1 && progress_value < 5) {
                    String symb = getResources().getString(R.string.symbol15);
                    sizeSeek.setText(progress_value + symb);
                } else {
                    String symb = getResources().getString(R.string.symbol5);
                    sizeSeek.setText(progress_value + symb);
                }

            }
        });
    }



    private String shuffleWord(String word) {
        List<String> letters = Arrays.asList(word.split(""));
        Collections.shuffle(letters);

        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }



}

