package com.example.bookmanageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.bumptech.glide.Glide;
import com.example.bookmanageapp.bean.BooksBean;
import com.example.bookmanageapp.util.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailBookActivity extends AppCompatActivity {
    private TextView tv_title,tv_author,tv_time,tv_intro;
    private ImageView iv_cover;
    private Button btn_edit,btn_detele;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        //将数据动态注入
        loadData();
        //修改
        btnEdit();
        //删除
        btnDetele();
    }

    private void btnDetele() {
        btn_detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BooksBean book = (BooksBean) getIntent().getSerializableExtra("books");
                if (book == null) return;
                String bookId = book.getBookId();
                String address = "http://10.0.2.2:8080/api/books/delete/" + bookId;
                OkHttpUtils.getInstance().daGet(address, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(DetailBookActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String rspData = response.body().string();
                        runOnUiThread(() -> {
                            Toast.makeText(DetailBookActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                });
            }
        });
    }

    private void btnEdit() {
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BooksBean book = (BooksBean) getIntent().getSerializableExtra("books");
                if (book == null) return;
                Intent intent = new Intent(DetailBookActivity.this, EditBookActivity.class);
                intent.putExtra("books", book);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        // 获取传递过来的图书对象
        BooksBean book = (BooksBean) getIntent().getSerializableExtra("books");
        // 显示数据
        tv_title.setText(book.getTitle());
        tv_author.setText(book.getAuthor());
        tv_time.setText(book.getDate());
        tv_intro.setText(book.getIntro());
        // 加载封面图片
        String coverUrl = book.getCover();
        if (coverUrl != null && !coverUrl.isEmpty()) {
            Glide.with(this)
                    .load(coverUrl)
                    .into(iv_cover);
        }
    }

    private void initView() {
        tv_title=findViewById(R.id.tv_detail_title);
        tv_author=findViewById(R.id.tv_detail_author);
        tv_time=findViewById(R.id.tv_detail_time);
        tv_intro=findViewById(R.id.tv_detail_intro);
        iv_cover=findViewById(R.id.iv_detail_cover);
        btn_edit=findViewById(R.id.btn_detail_edit);
        btn_detele=findViewById(R.id.btn_detail_delete);
    }
}