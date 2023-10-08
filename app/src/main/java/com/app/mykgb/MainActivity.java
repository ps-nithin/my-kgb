
package com.app.mykgb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import java.util.List;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    public static String MENU_NAME = "MENU_NAME";
    String DB_PATH="/data/data/com.app.mykgb/databases/kgbmenu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.toolbar1);
        my_toolbar.setTitleTextColor(Color.WHITE);
        my_toolbar.setTitle("My KGB");
        setSupportActionBar(my_toolbar);
        //getSupportActionBar().setElevation(0);
        final EditText hint=(EditText)findViewById(R.id.edittext1);
        //final TextView result=(TextView)findViewById(R.id.textview1);
        final ListView listview=(ListView)findViewById(R.id.listview1);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        /*
        SQLiteDatabase db;
        try {
            db=SQLiteDatabase.openDatabase(DB_PATH,null,0);
            //result.setText("Database found.");
        } catch (SQLiteException e0){
            //result.setText("Creating database.");
            db=openOrCreateDatabase("kgbmenu", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS MENU_DICT (HINT VARCHAR, MENUS VARCHAR);");
            db.execSQL("CREATE TABLE IF NOT EXISTS MENU_INFO (MENU VARCHAR, INFO VARCHAR);");

            BufferedReader dictReader=null;
            try {
                dictReader=new BufferedReader(new InputStreamReader(getAssets().open("mdict.txt")));
                String mLine;

                while((mLine=dictReader.readLine()) != null){
                    String[] mLineSplit=mLine.split("\\:");
                    db.execSQL("INSERT INTO MENU_DICT VALUES (\'"+mLineSplit[0]+"\','"+mLineSplit[1]+"\');");
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } finally {
                if(dictReader!=null){
                    try {
                        dictReader.close();
                    } catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }
            }
            BufferedReader infoReader=null;
            try {
                infoReader=new BufferedReader(new InputStreamReader(getAssets().open("info.txt")));
                String mLine;

                while((mLine=infoReader.readLine()) != null){
                    String[] mLineSplit=mLine.split("\\s+");
                    //String[] mInfoArr=Arrays.copyOfRange(mLineSplit,1,mLineSplit.length);
                    //String info=Arrays.toString(mInfoArr);
                    StringBuilder info=new StringBuilder();
                    for(int i=1;i<mLineSplit.length;i++){
                        info.append(mLineSplit[i]);
                        info.append(" ");
                    }
                    info.toString().trim();

                    db.execSQL("INSERT INTO MENU_INFO VALUES (\'"+mLineSplit[0]+"\','"+info+"\');");
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } finally {
                if(infoReader!=null){
                    try {
                        infoReader.close();
                    } catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }
            }
        }

         */
        //final SQLiteDatabase db1=SQLiteDatabase.openDatabase("/data/data/com.app.mykgb/databases/kgbmenu",null,0);
        dbHelper dbHelper = new dbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        hint.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final List<RowData> rowData=new ArrayList<RowData>();
                String hintWithOutQuotesLower=hint.getText().toString().replaceAll("\\'","").toLowerCase();
                List<String> hints=Arrays.asList(hintWithOutQuotesLower.trim().split("\\s+"));
                List<List<String>> menus=new ArrayList<List<String>>();
                Cursor cur=null;
                if(hints.size()>0){
                    for (String hint: hints){
                        cur=db.rawQuery("SELECT * FROM MENU_DICT WHERE HINT=\'"+hint+"\';",null);
                        if(cur.getCount()>0){
                            cur.moveToFirst();
                            List<String> menusArr=Arrays.asList(cur.getString(1).trim().split("\\,"));
                            if (menusArr.size()>0){
                                menus.add(menusArr);
                            }
                        }
                    }

                    if(menus.size()>1){
                        boolean allCommands=false;
                        for(int i=0;i<menus.size();i++){
                            if(menus.get(i).size()==1){
                                allCommands=true;
                            } else {
                                allCommands=false;
                                break;
                            }
                        }
                        if(allCommands){
                            for (int i=0;i<menus.size();i++){
                                RowData data=new RowData();
                                data.setTitle(i+1+". " + menus.get(i).get(0));
                                cur=db.rawQuery("SELECT * FROM MENU_INFO WHERE MENU=\'"+menus.get(i).get(0)+"\';",null);
                                cur.moveToFirst();
                                data.setSubtitle(cur.getString(1));
                                rowData.add(data);
                            }
                            MyAdapter adapter=new MyAdapter(MainActivity.this, rowData);
                            listview.setAdapter(adapter);
                            listview.setAdapter(adapter);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                                    RowData row_data=rowData.get(position);
                                    intent.putExtra("menu_name",row_data.getTitle());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            List<String> common=new ArrayList<String>();
                            common=menus.get(0);
                            for(int i=1;i<menus.size();i++){
                                common=getIntersect(common,menus.get(i));
                            }

                            if(common.size()>0){
                                for (int i=0;i<common.size();i++){
                                    RowData data=new RowData();
                                    data.setTitle(i+1+". "+common.get(i));
                                    cur=db.rawQuery("SELECT * FROM MENU_INFO WHERE MENU=\'"+common.get(i)+"\';",null);
                                    cur.moveToFirst();
                                    data.setSubtitle(cur.getString(1));
                                    rowData.add(data);
                                }
                                MyAdapter adapter=new MyAdapter(MainActivity.this, rowData);
                                listview.setAdapter(adapter);
                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                                        RowData row_data=rowData.get(position);
                                        intent.putExtra(MENU_NAME,row_data.getTitle());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                RowData data=new RowData();
                                data.setSubtitle("No results found.");
                                data.setTitle("Oops!");
                                rowData.add(data);
                                MyAdapter adapter=new MyAdapter(MainActivity.this, rowData);
                                listview.setAdapter(adapter);
                                listview.setOnItemClickListener(null);
                            }
                        }
                    } else if (menus.size()==1){
                        for (int i=0;i<menus.get(0).size();i++){
                            RowData data=new RowData();
                            data.setTitle(i+1+". " + menus.get(0).get(i));
                            cur=db.rawQuery("SELECT * FROM MENU_INFO WHERE MENU=\'"+menus.get(0).get(i)+"\';",null);
                            cur.moveToFirst();
                            data.setSubtitle(cur.getString(1));
                            rowData.add(data);
                        }
                        MyAdapter adapter=new MyAdapter(MainActivity.this, rowData);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                                RowData row_data=rowData.get(position);
                                intent.putExtra(MENU_NAME,row_data.getTitle());
                                startActivity(intent);
                            }
                        });

                    } else {
                        RowData data=new RowData();
                        data.setSubtitle("No results found.");
                        data.setTitle("Oops!");
                        rowData.add(data);
                        MyAdapter adapter=new MyAdapter(MainActivity.this, rowData);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(null);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    public List<String> getIntersect (List<String> a1,List<String> a2){
        List<String> intersect=new ArrayList<String>();
        for (String a11: a1){
            for (String a22: a2){
                if(a11.equalsIgnoreCase(a22)){
                    if(!intersect.contains(a11)){
                        intersect.add(a11);
                    }
                }
            }
        }
        return intersect;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.toolbar_exit){
            MainActivity.this.finish();
        }
        return  super.onOptionsItemSelected(item);
    }
}