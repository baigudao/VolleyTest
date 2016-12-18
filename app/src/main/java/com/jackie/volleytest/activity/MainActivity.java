package com.jackie.volleytest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jackie.volleytest.R;
import com.jackie.volleytest.util.MyStringRequest;

/**
 * Created by Administrator on 2016/12/18.
 */
public class MainActivity extends Activity {

    private Button button;
    private TextView textView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.tv_content);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNetContent();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
    }


    private void loadNetContent() {

        MyStringRequest myStringRequest = new MyStringRequest("http://www.ifeng.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                textView.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(myStringRequest);
    }
}
