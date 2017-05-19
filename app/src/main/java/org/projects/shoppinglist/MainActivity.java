package org.projects.shoppinglist;

import android.content.ClipData;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "com.example.StateChange" ;
    private String name;
    private int Value = 0;
    private String mMessage;
    private int q;
   Product p = new Product();

    FirebaseListAdapter<Product> adapter;
    ListView listView;
    ArrayList<String> bag = new ArrayList<String>();
    DatabaseReference chrisshopinglistapp;



    public FirebaseListAdapter<Product> getMyAdapter()
    {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.name);

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview



        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        chrisshopinglistapp = FirebaseDatabase.getInstance().getReference().child("items");


       adapter = new FirebaseListAdapter<Product>(this,Product.class,android.R.layout.simple_list_item_checked, chrisshopinglistapp){

            @Override
            protected void populateView(View view, Product product, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1); //standard android id.
                textView.setText(product.toString());
            }
        };

        listView.setAdapter(adapter);

       /* if (savedInstanceState!=null)
        {

            ArrayList<String> saved = savedInstanceState.getStringArrayList("saved bag");
            if (saved!=null) //did we save something
                bag = saved;

        }
        */

        final Button removedButton = (Button) findViewById(R.id.removedButton);

        removedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int index = listView.getCheckedItemPosition();
                getMyAdapter().getRef(index).setValue(null);
                //bag.remove(name);
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                //getMyAdapter().notifyDataSetChanged();
                Snackbar snackbar = Snackbar
                        .make(listView, "Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getMyAdapter().notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(listView, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                snackbar.show();


            }
        });



        final Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.productname);
                EditText number = (EditText) findViewById(R.id.number);

              //  if(name != null & q != 0){

                name = editText.getText().toString();
                q = Integer.parseInt(number.getText().toString());
              p.setName(name);
                p.setNumber(q);
                chrisshopinglistapp.push().setValue(p);

                    getMyAdapter().notifyDataSetChanged();
                //}

              //  bag.add(name);

/*
                if(name == null & number == null)
                else
                {
                    Toast.makeText(MainActivity.this, "This is my Toast message!",
                            Toast.LENGTH_LONG).show();
                }

                else{*/

             /*   else
                {
                    Toast.makeText(MainActivity.this, "du har ikke indsat navn og nummer",
                            Toast.LENGTH_SHORT).show();
                }*/
            }



        });

        //add some stuff to the list so we have something
        // to show on app startup





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


        switch (item.getItemId()) {


            case R.id.action_settings:
               onDestroy();
                return true;

            case R.id.action_email:
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;


            //return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD - To be nice!
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
		/* Here we put code now to save the state */
        outState.putStringArrayList("saved bag", bag);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        //adapter.cleanup();
        chrisshopinglistapp.removeValue();
    }




}