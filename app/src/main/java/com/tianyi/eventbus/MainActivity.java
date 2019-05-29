package com.tianyi.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Eventbus.getDefault().register(this);

    }

    public void change(View view)
    {
        startActivity(new Intent(this,SecondActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Eventbus.getDefault().unregister(this);

    }


    @Subscribe
    public void receive(Friend friend) {
        Toast.makeText(this,friend.getName(),Toast.LENGTH_SHORT).show();
        Log.i("yyh", "thread: " + Thread.currentThread().getName());
    }
}
