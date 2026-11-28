package com.example.bookmanageapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookmanageapp.CategoryDetailActivity;
import com.example.bookmanageapp.R;
import com.example.bookmanageapp.bean.BooksBean;
import com.example.bookmanageapp.bean.CateBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateBookAdapter extends RecyclerView.Adapter<CateBookAdapter.ViewHolder>{
    Context context;
    List<CateBean.DataDTO> cateList;
    Map<String, Integer> countMap = new HashMap<>();
    public CateBookAdapter(Context context, List<CateBean.DataDTO> cateList) {
        this.context = context;
        this.cateList = cateList != null ? cateList : new ArrayList<>();
    }
    public void setBooksList(List<BooksBean> booksList) {
        countMap.clear();
        if (booksList != null) {
            for (BooksBean book : booksList) {
                String catId = book.getCategoryId();
                if (catId != null) {
                    Integer count = countMap.get(catId);
                    if (count == null) {
                        countMap.put(catId, 1);
                    } else {
                        countMap.put(catId, count + 1);
                    }
                }
            }
        }
        for (CateBean.DataDTO category : cateList) {
            Integer count = countMap.get(category.getId());
            category.setCount(count != null ? count : 0);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CateBean.DataDTO currentData=cateList.get(position);
        holder.tv_name.setText(currentData.getName());
        Integer count = countMap.get(currentData.getId());
        holder.tv_count.setText("共 " + (count != null ? count : 0) + " 本");
        String imageUrl=currentData.getCover();
        if (imageUrl != null && !imageUrl.isEmpty()){
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.iv_icon);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CategoryDetailActivity.class);
                intent.putExtra("category",currentData);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return cateList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_name,tv_count;
        View item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.iv_category_icon);
            tv_name=itemView.findViewById(R.id.tv_category_name);
            tv_count=itemView.findViewById(R.id.tv_category_count);
            item=itemView;
        }
    }
}