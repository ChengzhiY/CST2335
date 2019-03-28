package com.phucnb.androidlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<MessageModel> listMessage = new ArrayList<>();
    Button sendBtn;
    Button receiveBtn;
    DatabaseHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chatroom);

        listView = (ListView)findViewById(R.id.ListView);
        editText = (EditText)findViewById(R.id.ChatEditText);
        sendBtn = (Button)findViewById(R.id.SendBtn);
        receiveBtn = (Button)findViewById(R.id.ReceiveBtn);
        db = new DatabaseHelper(this);
        boolean isTable = findViewById(R.id.fragmentLocation) != null;

        viewData();

        listView.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString("item", listMessage.get(position).message);
            dataToPass.putInt("id", position);
            dataToPass.putLong("db_id", listMessage.get(position).messageID);


           if (isTable){
               DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
               dFragment.setArguments( dataToPass ); //pass it a bundle for information
               dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
               getSupportFragmentManager()
                       .beginTransaction()
                       .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                       .addToBackStack("AnyName") //make the back button undo the transaction
                       .commit(); //actually load the fragment.
           }else {
               Intent emptyActivity = new Intent(this, EmptyActivity.class);
               emptyActivity.putExtras(dataToPass);
               startActivityForResult(emptyActivity, 345);
           }

        });
//
//        sendBtn.setOnClickListener(c -> {
//            String message = editText.getText().toString();
//            if (!message.equals("")){
////                MessageModel model = new MessageModel(message, true);
////                listMessage.add(model);
////
////                ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
////                listView.setAdapter(adt);
//                db.insertData(message, true);
//                editText.setText("");
//                listMessage.clear();
//                viewData();
//            }
//        });

        receiveBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            if (!message.equals("")) {
//                MessageModel model = new MessageModel(message, false);
//                listMessage.add(model);
//                editText.setText("");
//                ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
//                listView.setAdapter(adt);
                db.insertData(message, false);
                editText.setText("");
                listMessage.clear();
                viewData();
            }
        });
        Log.d("ChatRoomActivity","onCreate");



    }

    private void viewData(){
        Cursor cursor = db.viewData();

        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                MessageModel model = new MessageModel(cursor.getString(1), cursor.getInt(2)==0?true:false, cursor.getLong(0));
                listMessage.add(model);
                ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
                listView.setAdapter(adt);

            }
        }
    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 345)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra("db_id", 0);
                deleteMessageId((int)id);
            }
        }
    }

    public void deleteMessageId(int id)
    {

        db.deleteEntry(id);
        listMessage.clear();
        viewData();
    }

}

