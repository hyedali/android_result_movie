package com.example.samplerequest_movie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    RecyclerView recyclerView;
    MovieAdapter adapter;

    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void makeRequest(){
        String url = editText.getText().toString();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> " + response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청보냄");
    }

    public void println(String data){
        //textView.append(data + "\n");
    }

    public void processResponse(String response){
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);
        println("영화 정보의 수 : " + movieList.boxOfficeResult.dailyBoxOfficeList.size());

        for(int i=0; i<movieList.boxOfficeResult.dailyBoxOfficeList.size(); i++){
            Movie movie = movieList.boxOfficeResult.dailyBoxOfficeList.get(i);

            adapter.addItem(movie);
        }

        adapter.notifyDataSetChanged();
    }
}