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
import android.widget.Toast;

import com.example.bookmanageapp.R;
import com.example.bookmanageapp.adapter.CateBookAdapter;
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
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CategoryFragment extends Fragment {
    private RecyclerView viewCate;
    private CateBookAdapter adapter;
    private List<CateBean.DataDTO> cateList = new ArrayList<>();
    private List<BooksBean> booksList = new CopyOnWriteArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_three,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        viewCate=view.findViewById(R.id.rv_category);
        adapter = new CateBookAdapter(getActivity(), cateList);
        viewCate.setAdapter(adapter);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        viewCate.setLayoutManager(manager);
        initCategoryData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initCategoryData();
    }

    private void initCategoryData() {
        String address = "http://10.0.2.2:8080/api/categories";
        OkHttpUtils.getInstance().daGet(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("CategoryFragment","网络访问出错");
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
                CateBean cateBean = gson.fromJson(rspData, CateBean.class);
                if (cateBean != null && cateBean.getData() != null) {
                    cateList.clear();
                    cateList.addAll(cateBean.getData());
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            initBooksData();
                        }
                    });
                }
            }
        });
    }

    private void initBooksData() {
        String address = "http://10.0.2.2:8080/api/books";
        OkHttpUtils.getInstance().daGet(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("CategoryFragment", "书籍接口出错: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String rspData = response.body().string();
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(rspData, JsonObject.class);
                    if (jsonObject != null && jsonObject.has("data")) {
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");
                        booksList.clear();
                        for (int i = 0; i < dataArray.size(); i++) {
                            JsonElement element = dataArray.get(i);
                            BooksBean book = gson.fromJson(element, BooksBean.class);
                            booksList.add(book);
                        }
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setBooksList(booksList);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}