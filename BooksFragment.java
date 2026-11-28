package com.example.bookmanageapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmanageapp.R;
import com.example.bookmanageapp.adapter.BookAdapter;
import com.example.bookmanageapp.bean.BooksBean;
import com.example.bookmanageapp.util.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BooksFragment extends Fragment {
    private RecyclerView ViewBooks;
    private TextView count;
    public interface OnBookCountListener {
        void onBookCount(int count);
    }
    private OnBookCountListener countListener;
    public void setOnBookCountListener(OnBookCountListener listener) {
        this.countListener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_one,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ViewBooks=view.findViewById(R.id.rv_books);
        count=view.findViewById(R.id.tv_book_count);
    }
    @Override
    public void onResume(){
        super.onResume();
        initData();
    }
    private void initData() {
        ViewBooks.clearFocus();
        if (ViewBooks.getAdapter() != null) {
            ViewBooks.getAdapter().notifyDataSetChanged();
        }
        String address=String.format("http://10.0.2.2:8080/api/books");
        OkHttpUtils.getInstance().daGet(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("BooksFragment","网络访问出错");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"网络访问出错",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String rspData=response.body().string();
                Gson gson=new Gson();
                JsonObject jsonObject = gson.fromJson(rspData, JsonObject.class);
                if (!jsonObject.has("data")) {
                    Log.e("BooksFragment", "没有 data 字段，返回: " + rspData);
                    return;
                }
                JsonArray dataArray = jsonObject.getAsJsonArray("data");
                Type listType = new TypeToken<List<BooksBean>>() {}.getType();
                List<BooksBean> bookList = gson.fromJson(dataArray, listType);
                final int bookCount = bookList != null ? bookList.size() : 0;
                if (countListener != null) {
                    countListener.onBookCount(bookCount);
                }
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count != null) {
                            count.setText("共有书籍：" + bookCount + " 本");
                        }
                        BookAdapter adapter=new BookAdapter(getActivity(),bookList);
                        ViewBooks.setAdapter(adapter);
                        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                        ViewBooks.setLayoutManager(manager);
                    }
                });
            }
        });
    }
}