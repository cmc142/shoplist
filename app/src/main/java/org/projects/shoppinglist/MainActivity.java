package org.projects.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "com.example.StateChange" ;
    private String name;
    public String username;
    private int q;
    Product p = new Product();


    ListView listView;
    FirebaseListAdapter adapter;
    ArrayList<Product> fortyde = new ArrayList<>();
    DatabaseReference chrisshopinglistapp;



    public FirebaseListAdapter getMyAdapter()
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
                //if(index != null)
                //
              /*  if(adapter != null)
                {
                    Snackbar snackbar2 = Snackbar
                            .make(listView, "ikke valgt noget", Snackbar.LENGTH_LONG);
                    snackbar2.show();
                }*/

                if(adapter != null){

                    onRemoved();
                }

            }



        });



        final Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final EditText editText = (EditText) findViewById(R.id.productname);
                final EditText number = (EditText) findViewById(R.id.number);



                //  if(name != null & q != 0){

                name = editText.getText().toString();

                q = Integer.parseInt(number.getText().toString());
                p.setName(name);
                p.setNumber(q);

      /*          if(p == null) {
                    p.setName("test");
                    p.setNumber(1);

                }

                else{
                    return;
                }*/

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
    protected void onSaveInstanceState(Bundle outState) {
        if(username != "") {

            TextView t = (TextView)findViewById(R.id.textView4);
            t.setText(username + "  shopping list");
        }
        super.onSaveInstanceState(outState);
        //  outState.putStringArrayList("saved bag", bag);

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


                if(adapter != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {chrisshopinglistapp.removeValue();
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                else {
                    Toast.makeText(this, "listen er tom og kan derfor ikke slettes!", Toast.LENGTH_SHORT)
                            .show();
                }
                return true;



            case R.id.action_email:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "hey try my shoppinglist.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.btnSignIn:
                startActivity(new Intent(this, SignIn.class));

                return true;

            //return super.onOptionsItemSelected(item);

            case R.id.action_settings1:

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1);


                username = PreferencesFragment.UName(this);

                return true;
        }


        return true;

    }



    protected void onRemoved() {


        final int index = listView.getCheckedItemPosition();
        fortyde.add((Product) getMyAdapter().getItem(index));
        getMyAdapter().getRef(index).removeValue();
        Snackbar snackbar = Snackbar
                .make(listView, "Item Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        for(Product product: fortyde)
                        {
                            chrisshopinglistapp.push().setValue(product);
                            fortyde.clear();
                        }
                        getMyAdapter().notifyDataSetChanged();
                        Snackbar snackbar = Snackbar.make(listView ,"Item restored!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });

        snackbar.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //adapter.cleanup();
        //  chrisshopinglistapp.removeValue();
        //  p.setName(null);
        // p.setNumber(0);
        //   Log.d(TAG, "onDestroy" + chrisshopinglistapp);

    }

}