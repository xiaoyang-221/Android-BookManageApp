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
import com.example.bookmanageapp.DetailBookActivity;
import com.example.bookmanageapp.R;
import com.example.bookmanageapp.bean.BooksBean;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    Context context;
    List<BooksBean> bookList;
    public BookAdapter(Context context, List<BooksBean> bookList) {
        this.context = context;
        this.bookList = bookList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_book,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BooksBean currentData=bookList.get(position);
        holder.tv_isbn.setText(currentData.getBookId());
        holder.tv_title.setText(currentData.getTitle());
        holder.tv_author.setText(currentData.getAuthor());
        holder.tv_publish_time.setText(currentData.getDate());
        String imageUrl = currentData.getCover();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.iv_cover);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailBookActivity.class);
                intent.putExtra("books",currentData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cover;
        TextView tv_isbn,tv_title,tv_author,tv_publish_time;
        View item;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            iv_cover=itemView.findViewById(R.id.iv_cover);
            tv_isbn=itemView.findViewById(R.id.tv_isbn);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_author=itemView.findViewById(R.id.tv_author);
            tv_publish_time=itemView.findViewById(R.id.tv_publish_time);
            item=itemView;
        }
    }
}
