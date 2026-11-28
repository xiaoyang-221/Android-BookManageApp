package com.example.bookmanageapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bookmanageapp.adapter.MyFragmentAdapter;
import com.example.bookmanageapp.fragment.AddFragment;
import com.example.bookmanageapp.fragment.BooksFragment;
import com.example.bookmanageapp.fragment.CategoryFragment;
import com.example.bookmanageapp.util.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton rb1,rb2,rb3;
    private Fragment booksfragment,addfragment,catefragment;
    private List<Fragment> fragmentList;
    private int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
        initMove();
        initOnClick();
    }

    private void initOnClick() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                if(checkedId==R.id.home){
                    position=0;
                    viewPager.setCurrentItem(position);
                }else if(checkedId==R.id.add){
                    position=1;
                    viewPager.setCurrentItem(position);
                } else if (checkedId==R.id.category) {
                    position=2;
                    viewPager.setCurrentItem(position);
                }else {
                    position=0;
                    viewPager.setCurrentItem(position);
                }
            }
        });
    }

    private void initMove() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton)(radioGroup.getChildAt(position))).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        booksfragment=new BooksFragment();
        addfragment=new AddFragment();
        catefragment=new CategoryFragment();
        fragmentList.add(booksfragment);
        fragmentList.add(addfragment);
        fragmentList.add(catefragment);
        MyFragmentAdapter adapter=new MyFragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        RadioButton rb=(RadioButton) (radioGroup.getChildAt(position));
        rb.setChecked(true);
    }

    private void initView() {
        viewPager=findViewById(R.id.view_pager);
        radioGroup=findViewById(R.id.radio_group);
        rb1=findViewById(R.id.home);
        rb2=findViewById(R.id.add);
        rb3=findViewById(R.id.category);
        fragmentList=new ArrayList<>();
    }
}