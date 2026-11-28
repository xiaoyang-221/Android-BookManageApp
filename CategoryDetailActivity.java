package com.example.bookmanageapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bookmanageapp.adapter.BookAdapter;
import com.example.bookmanageapp.bean.BooksBean;
import com.example.bookmanageapp.bean.CateBean;
import com.example.bookmanageapp.util.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CategoryDetailActivity extends AppCompatActivity {
    private TextView tv_name,tv_count;
    private RecyclerView ViewBooks;
    private CateBean.DataDTO categoryData;
    private List<BooksBean> bookList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
    }
    private void initData() {
        categoryData = (CateBean.DataDTO) getIntent().getSerializableExtra("category");
        tv_name.setText(categoryData.getName());
        tv_count.setText(categoryData.getName() + "共有" + categoryData.getCount() + "本书");
        BooksByCategory(categoryData.getId());
    }
    private void BooksByCategory(String id) {
        String address = "http://10.0.2.2:8080/api/books/category/" + id;
        OkHttpUtils.getInstance().daGet(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("CategoryDetail", "书籍接口出错: " + e.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(CategoryDetailActivity.this, "网络访问出错", Toast.LENGTH_SHORT).show()
                );
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String rspData = response.body().string();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(rspData, JsonObject.class);
                if (jsonObject != null && jsonObject.has("data")) {
                    JsonArray dataArray = jsonObject.getAsJsonArray("data");
                    bookList.clear();
                    for (int i = 0; i < dataArray.size(); i++) {
                        JsonElement element = dataArray.get(i);
                        BooksBean book = gson.fromJson(element, BooksBean.class);
                        if (book.getCategoryId() != null && book.getCategoryId().equals(id)) {
                            bookList.add(book);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BookAdapter adapter= new BookAdapter(CategoryDetailActivity.this,bookList);
                            ViewBooks.setAdapter(adapter);
                            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            ViewBooks.setLayoutManager(manager);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
    private void initView() {
        tv_name = findViewById(R.id.tv_category_name);
        tv_count = findViewById(R.id.tv_category_count);
        ViewBooks = findViewById(R.id.rv_category_books);
    }
}