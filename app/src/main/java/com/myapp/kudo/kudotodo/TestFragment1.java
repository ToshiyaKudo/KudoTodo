package com.myapp.kudo.kudotodo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request.Method;
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
import java.util.Objects;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.myapp.kudo.kudotodo.TestFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue mQueue;
    CustomRecyclerViewAdapter customAdapter;
    View view;



    public static TestFragment1 newInstance(String param1, String param2) {
        TestFragment1 fragment = new TestFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TestFragment1() {
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
        view = inflater.inflate(R.layout.fragment_test_fragment3, container, false);

        getTodoData();

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    public void getTodoData(){

        String url =
                "http://203.189.96.52/todo";

        mQueue = Volley.newRequestQueue(getActivity());
        mQueue.add(new JsonObjectRequest(Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // JSONObjectのパース、List、
                        HashSet<String> hashSet = new HashSet<String>();

                        try {
                            JSONArray jarray = response.getJSONArray("members");

                            List<CategoryListData> categoryList = new ArrayList<CategoryListData>();
                            List<List<RowListData>> rowList = new ArrayList<List<RowListData>>();

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject member = jarray.getJSONObject(i);
                                String category = member.getString("category");
                                hashSet.add(category);
                            }

                            ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
                            FragmentPagerAdapter pAdapter = (FragmentPagerAdapter) pager.getAdapter();
                            TestFragment testFrag =(TestFragment) pAdapter.instantiateItem(pager, 1);
                            testFrag.setSpinner(hashSet);

                            for(Object obj :hashSet){
                                categoryList.add(new CategoryListData((String)obj));
                                List<RowListData> rowListDatas = new ArrayList<RowListData>();

                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject member = jarray.getJSONObject(i);
                                    String category = member.getString("category");

                                    if(category.equals((String)obj)){
                                        String title = member.getString("title");
                                        String subtitle = member.getString("subtitle");
                                        String id = member.getString("id");
                                        rowListDatas.add(new RowListData(title,subtitle,id));
                                    }
                                }
                                rowList.add(rowListDatas);
                            }

                            customAdapter = new CustomRecyclerViewAdapter(getActivity(),categoryList,rowList);

                            recyclerView.setAdapter(customAdapter);

                        } catch (JSONException e) {
                            // TODO 自動生成された catch ブロック
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override public void onErrorResponse(VolleyError error) {
                        // エラー処理 error.networkResponseで確認
                        // エラー表示など
                        Log.e("ERROR", "onResponse error=" + error);
                    }
                }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
