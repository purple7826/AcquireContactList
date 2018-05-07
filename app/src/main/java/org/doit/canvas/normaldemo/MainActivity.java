package org.doit.canvas.normaldemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
            }
        }

        mListView = (ListView)findViewById(R.id.contact_list);
        mButton = (Button)findViewById(R.id.fetcher_contact);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getContacts());
                mListView.setAdapter(adapter);
            }
        });
    }

    private String[] getContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projects = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = getContentResolver().query(uri,projects,null,null,null);
        String[] contacts = null;
        int index=0;
        try{
            Log.i("Contact-C","count : "+cursor.getCount());
            contacts = new String[cursor.getCount()];
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                Log.i("Contact-C",name+" : "+number+" ---------------  index = "+index);
                contacts[index] = name+" : "+number;
                index++;
                cursor.moveToNext();
            }
        }finally {
            index = 0;
            cursor.close();
        }

        return contacts;
    }
}
