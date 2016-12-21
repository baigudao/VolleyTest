package com.jackie.volleytest.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jackie.volleytest.R;
import com.jackie.volleytest.util.BitmapCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/18.
 */
public class MainActivity extends Activity {

    private Button button;
    private Button button_json;
    private Button button_image;
    private Button button_image_load;
    private Button button_post;
    private ImageView imageView;
    private TextView textView;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btn);
        button_json = (Button) findViewById(R.id.btn_json);
        textView = (TextView) findViewById(R.id.tv_content);
        button_image = (Button) findViewById(R.id.btn_image);
        button_image_load = (Button) findViewById(R.id.btn_image_load);
        button_post = (Button) findViewById(R.id.btn2);
        imageView = (ImageView) findViewById(R.id.iv_image);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNetContent();
            }
        });
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNetContentByPost();
            }
        });
        button_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJson();
            }
        });
        button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
        button_image_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageLoader();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
    }


    private void loadNetContent() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET
                , "http://ifeng.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                textView.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";

            // 重写parseNetworkResponse方法改变返回头参数解决乱码问题
            // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String type = response.headers.get("Content-Type");
                    if (type == null) {
                        type = TYPE_UTF8_CHARSET;
                        response.headers.put("Content-Type", type);
                    } else if (!type.contains("UTF-8")) {
                        type += ";" + TYPE_UTF8_CHARSET;
                        response.headers.put("Content-Type", type);
                    }
                } catch (Exception e) {
                }
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setShouldCache(true);//启用缓存
        requestQueue.add(stringRequest);
    }


    private void loadNetContentByPost() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://172.17.53.1:8080/server-test/test", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                textView.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("lname", "admin");
                map.put("pwd", "123456");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadJson() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://www.weather.com.cn/data/cityinfo/101010100.html",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                textView.setText(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadImage() {
        ImageRequest imageRequest = new ImageRequest("http://e.hiphotos.baidu.com/image/pic/item/d1160924ab18972b40187fcbe4cd7b899e510a94.jpg", new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }

    private void loadImageLoader() {
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.drawable.no_hot_food, R.drawable.ic_share_close);
        imageLoader.get("http://b.hiphotos.baidu.com/image/pic/item/7e3e6709c93d70cf5a0cc307fadcd100bba12bc1.jpg", imageListener);
    }
}
