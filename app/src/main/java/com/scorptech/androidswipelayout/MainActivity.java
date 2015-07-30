package com.scorptech.androidswipelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.scorptech.relativeswipelayout.RelativeSwipeLayout;


public class MainActivity extends AppCompatActivity {

    RelativeSwipeLayout swipeLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Manual swipe layout with two inner view
        swipeLayout1 = (RelativeSwipeLayout) findViewById(R.id.swipeLayout1);
        swipeLayout1.init();
        /////////////////////////////////////////
        //// Auto swipe /////////////
        RelativeSwipeLayout swipeLayout2 = (RelativeSwipeLayout) findViewById(R.id.swipeLayout2);
        swipeLayout2.init(3000);
        //////////////////////////
        ////Only Auto swipe ///////
        RelativeSwipeLayout swipeLayout3 = (RelativeSwipeLayout) findViewById(R.id.swipeLayout3);
        swipeLayout3.init(5000);
        swipeLayout3.setEnableManualSwipe(false);
        /////////////////////////
        // Custom runnable on swipe /////
        RelativeSwipeLayout swipeLayout4 = (RelativeSwipeLayout) findViewById(R.id.swipeLayout4);
        swipeLayout4.init(7000);
        swipeLayout4.postRunnableOnSwipe(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Custom runable on swipe toast",Toast.LENGTH_SHORT).show();
            }
        });
        ////////////////////////////////
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
