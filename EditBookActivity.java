package com.example.bookmanageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class EditBookActivity extends AppCompatActivity {
    private ImageView iv_cover;
    private EditText et_title,et_author,et_time,et_intro;
    private Button btn_cover,btn_cancel,btn_confirm;
    private String selectedCoverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        loadData();
        btnCover();
        btnCancel();
        btnConfirm();
    }

    private void btnConfirm() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String author = et_author.getText().toString();
                String date = et_time.getText().toString();
                String intro = et_intro.getText().toString();
                String coverUrl = selectedCoverUrl != null ? selectedCoverUrl : "";
                BooksBean originalBook = (BooksBean) getIntent().getSerializableExtra("books");
                String bookId = originalBook.getBookId();
                String json = String.format(
                        "{\"bookId\":\"%s\",\"title\":\"%s\",\"author\":\"%s\",\"date\":\"%s\",\"intro\":\"%s\",\"categoryId\":\"%s\",\"cover\":\"%s\"}",
                        bookId, title, author, date, intro, originalBook.getCategoryId(), coverUrl
                );
                String address = "http://10.0.2.2:8080/api/books/update";
                OkHttpUtils.getInstance().doPostJson(address, json, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(EditBookActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(() -> {
                            Toast.makeText(EditBookActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditBookActivity.this,MainActivity.class));
                            finish();
                        });
                    }
                });
            }
        });
    }

    private void btnCancel() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditBookActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void btnCover() {
        btn_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] imageNames = {
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/00.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/01.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/02.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/03.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/04.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/05.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/06.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/07.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/08.png",
                        "https://yang-ai-java.oss-cn-beijing.aliyuncs.com/09.png"
                };

                String[] displayNames = {
                        "封面 00", "封面 01", "封面 02", "封面 03", "封面 04",
                        "封面 05", "封面 06", "封面 07", "封面 08", "封面 09"
                };
                AlertDialog show = new AlertDialog.Builder(EditBookActivity.this)
                        .setTitle("选择封面图片")
                        .setItems(displayNames, (dialog, which) -> {
                            String selectedImage = imageNames[which];
                            Glide.with(EditBookActivity.this)
                                    .load(selectedImage)
                                    .into(iv_cover);
                            selectedCoverUrl = selectedImage;
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void loadData() {
        BooksBean books=(BooksBean) getIntent().getSerializableExtra("books");
        et_title.setText(books.getTitle());
        et_author.setText(books.getAuthor());
        et_time.setText(books.getDate());
        et_intro.setText(books.getIntro());
        selectedCoverUrl = books.getCover();
        String cover=books.getCover();
        if(cover !=null && !cover.isEmpty()){
            Glide.with(this)
                    .load(cover)
                    .into(iv_cover);
        }
    }

    private void initView() {
        iv_cover=findViewById(R.id.iv_edit_cover);
        et_title=findViewById(R.id.et_edit_title);
        et_author=findViewById(R.id.et_edit_author);
        et_time=findViewById(R.id.et_edit_publish_time);
        et_intro=findViewById(R.id.et_edit_intro);
        btn_cover=findViewById(R.id.btn_edit_select_cover);
        btn_cancel=findViewById(R.id.btn_edit_cancel);
        btn_confirm=findViewById(R.id.btn_edit_confirm);
    }
}