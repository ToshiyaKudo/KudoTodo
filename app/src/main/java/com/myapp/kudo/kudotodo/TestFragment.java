package com.myapp.kudo.kudotodo;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment implements
        View.OnClickListener,android.widget.CompoundButton.OnCheckedChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RequestQueue mQueue;

    Button bt1;//登録用ボタン
    RadioGroup rg;
    RadioButton r1,r2;
    Spinner sp1;//既存の項目表示用
    LinearLayout l1,l2;//radioボタンの背景変化用
    int Rid=R.id.radio0;//ラジオボタン判断用
    EditText titleEt,newEt,idEt,passEt,memoEt,urlEt;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registration, container, false);

        titleEt=(EditText)view.findViewById(R.id.save_et1);
        newEt=(EditText)view.findViewById(R.id.save_et2);
        idEt=(EditText)view.findViewById(R.id.save_et3);
        passEt=(EditText)view.findViewById(R.id.save_et4);
        memoEt=(EditText)view.findViewById(R.id.save_et5);
        urlEt=(EditText)view.findViewById(R.id.save_et6);
        bt1=(Button)view.findViewById(R.id.save_bt1);
        bt1.setOnClickListener(this);
        l1=(LinearLayout)view.findViewById(R.id.save_lin1);
        l2=(LinearLayout)view.findViewById(R.id.save_lin2);
        rg=(RadioGroup)view.findViewById(R.id.save_radioGroup1);
        r1=(RadioButton)view.findViewById(R.id.radio0);
        r2=(RadioButton)view.findViewById(R.id.radio1);
        r1.setOnCheckedChangeListener(this);
        r2.setOnCheckedChangeListener(this);
        sp1=(Spinner)view.findViewById(R.id.save_sp1);
        l1.setBackgroundColor(Color.rgb(215, 204, 200));

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.radio0:
                if(isChecked){
                    Rid=R.id.radio0;
                    sp1.setEnabled(true);
                    r2.setChecked(false);
                    newEt.setEnabled(false);
                    l1.setBackgroundColor(Color.rgb(215, 204, 200));
                    l2.setBackgroundColor(Color.rgb(247, 243, 239));
                }else{
                    r2.setChecked(true);
                }

                break;
            case R.id.radio1:
                if(isChecked){
                    Rid=R.id.radio1;
                    r1.setChecked(false);
                    sp1.setEnabled(false);
                    l1.setBackgroundColor(Color.rgb(247, 243, 239));
                    l2.setBackgroundColor(Color.rgb(215, 204, 200));
                    newEt.setEnabled(true);
                    newEt.requestFocus();
                }else{
                    r1.setChecked(true);
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {

        //新規登録用ダイアログ
        class SaveDialog extends DialogFragment{
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder d3=new AlertDialog.Builder(getActivity());
                d3.setTitle(titleEt.getText().toString());
                d3.setMessage(titleEt.getText().toString()+"を登録しました");
                d3.setPositiveButton("続けて入力", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        titleEt.getText().clear();
                        newEt.getText().clear();
                        idEt.getText().clear();
                        passEt.getText().clear();
                        sp1.setSelection(0);
                        r1.setChecked(true);
                    }
                });

                return d3.create();
            }
        }

        if(TextUtils.isEmpty(titleEt.getText().toString()) || titleEt.getText().toString().trim().trim().equals("") ||
        TextUtils.isEmpty(idEt.getText().toString()) || idEt.getText().toString().trim().trim().equals("")){

            ErrorDialog.newInstance().show(getFragmentManager(), null);

        }else {

            String url =
                    "http://203.189.96.52/todo/post";

            String category = null;

            if (Rid == R.id.radio0) {
                category = (String) sp1.getSelectedItem();
            }//カテゴリーを新規作成
            if (Rid == R.id.radio1) {
                //新規カテゴリー名が未記入のときはカテゴリーを未登録にする
                if (TextUtils.isEmpty(newEt.getText().toString()) || newEt.getText().toString().trim().trim().equals("")) {
                    category = "未登録";
                } else {
                    category = newEt.getText().toString();
                }
            }
            mQueue = Volley.newRequestQueue(getActivity());
            try {
                // サーバへ送信するデータ
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("category", category);
                jsonRequest.put("title", titleEt.getText().toString());
                jsonRequest.put("subtitle", idEt.getText().toString());

                mQueue.add(new JsonObjectRequest(url, jsonRequest,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // JSONObjectのパース、List、
                                HashSet<String> hashSet = new HashSet<String>();
                                ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
                                FragmentPagerAdapter pAdapter = (FragmentPagerAdapter) pager.getAdapter();
                                TestFragment1 testFrag =(TestFragment1) pAdapter.instantiateItem(pager, 0);
                                testFrag.getTodoData();

                                new SaveDialog().show(getFragmentManager(),null);
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // エラー処理 error.networkResponseで確認
                                // エラー表示など
                                Log.e("ERROR", "onResponse error=" + error);
                            }
                        }));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSpinner(HashSet hash){

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapter.addAll(hash);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);
    }

    public static class ErrorDialog extends DialogFragment {

        public static DialogFragment newInstance() {
            return new ErrorDialog();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder d=new AlertDialog.Builder(getActivity());
            d.setMessage("未入力の項目があります");
            d.setPositiveButton("了解", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

            return d.create();
        }
    }
}


