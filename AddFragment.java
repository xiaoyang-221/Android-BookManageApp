package com.example.bookmanageapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookmanageapp.EditBookActivity;
import com.example.bookmanageapp.MainActivity;
import com.example.bookmanageapp.R;
import com.example.bookmanageapp.bean.CateBean;
import com.example.bookmanageapp.util.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddFragment extends Fragment {
    private ImageView iv_cover;
    private EditText et_title,et_author,et_time,et_intro;
    private Button  btn_cover,btn_cancel,btn_confirm;
    private Spinner sp_type;
    private String selectedCoverUrl;
    private String selectCategoryId = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_two, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_cover= view.findViewById(R.id.iv_add_cover);
        sp_type=view.findViewById(R.id.sp_add_cate);
        et_title=view.findViewById(R.id.et_add_title);
        et_author=view.findViewById(R.id.et_add_author);
        et_time=view.findViewById(R.id.et_add_publish_time);
        et_intro=view.findViewById(R.id.et_add_intro);
        btn_cover=view.findViewById(R.id.btn_select_cover);
        btn_cancel=view.findViewById(R.id.btn_add_cancel);
        btn_confirm=view.findViewById(R.id.btn_add_confirm);
        initSpinner();
        btnCover();
        btnCancel();
        btnConfirm();
    }

    private void initSpinner() {
        List<CateBean.DataDTO> categoryList = new ArrayList<>();
        categoryList.add(new CateBean.DataDTO("cat_01","艺术类"));
        categoryList.add(new CateBean.DataDTO("cat_02","文学类"));
        categoryList.add(new CateBean.DataDTO("cat_03","科技类"));
        categoryList.add(new CateBean.DataDTO("cat_04","历史类"));
        categoryList.add(new CateBean.DataDTO("cat_05","教育类"));
        categoryList.add(new CateBean.DataDTO("cat_06","其他类"));
        ArrayAdapter<CateBean.DataDTO> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categoryList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CateBean.DataDTO selected = (CateBean.DataDTO) parent.getItemAtPosition(position);
                selectCategoryId = selected.getId();
                Log.d("AddFragment", "选中分类: " + selected.getName() + ", ID=" + selectCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectCategoryId = "1";
            }
        });
    }

    private void btnConfirm() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String author=et_author.getText().toString().trim();
                String date=et_time.getText().toString().trim();
                String intro=et_intro.getText().toString().trim();
                String coverUrl=selectedCoverUrl != null ? selectedCoverUrl : "";
                if (title.isEmpty() || author.isEmpty() || date.isEmpty()) {
                    Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                String categoryId = selectCategoryId;
                String json = String.format(
                        "{\"title\":\"%s\",\"author\":\"%s\",\"date\":\"%s\",\"intro\":\"%s\",\"categoryId\":\"%s\",\"cover\":\"%s\"}",
                        title, author, date, intro, categoryId, coverUrl
                );
                String address = "http://10.0.2.2:8080/api/books/add";
                OkHttpUtils.getInstance().doPostJson(address, json, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
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
                et_title.setText("");
                et_author.setText("");
                et_time.setText("");
                et_intro.setText("");
                iv_cover.setImageResource(android.R.drawable.picture_frame);
                selectedCoverUrl = null;
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
                new AlertDialog.Builder(requireContext())
                        .setTitle("选择封面图片")
                        .setItems(displayNames, (dialog, which) -> {
                            selectedCoverUrl = imageNames[which];
                            Glide.with(requireContext())
                                    .load(selectedCoverUrl)
                                    .into(iv_cover);
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }
}