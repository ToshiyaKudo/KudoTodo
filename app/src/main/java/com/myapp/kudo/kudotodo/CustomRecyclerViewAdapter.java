package com.myapp.kudo.kudotodo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ross on 2015/04/13.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = -1;
    private static final int ITEM = 1;

    protected Context context;
    protected LayoutInflater inflater;
    private RequestQueue mQueue;

    protected List<CategoryListData> categoryList;
    protected List<List<RowListData>> rowList;
    private List<SummaryPath> summaryPathList;

    public CustomRecyclerViewAdapter(Context context, List<CategoryListData> categoryList, List<List<RowListData>> rowList){
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.categoryList = categoryList;
        this.rowList = rowList;
        this.summaryPathList = getSummaryPathList(categoryList, rowList);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(i == HEADER){
            view = inflater.inflate(R.layout.category_list, null);
            return new VHHeader(view);
        }else {
            view = inflater.inflate(R.layout.item_list, null);

            LinearLayout.LayoutParams param =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
            view.setLayoutParams(param);
            return new VHItem(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final SummaryPath summaryPath = summaryPathList.get(i);

        if(viewHolder instanceof VHHeader){
            VHHeader vhHeader = (VHHeader)viewHolder;
            CategoryListData categoryListData = categoryList.get(summaryPath.section);
            vhHeader.categoryText.setText(categoryListData.getCategory());


        }else if(viewHolder instanceof VHItem){
            VHItem vhItem = (VHItem)viewHolder;
            final RowListData rowListData = rowList.get(summaryPath.section).get(summaryPath.row);
            vhItem.title.setText(rowListData.getTitle());
            vhItem.subTitle.setText(rowListData.getSubTitle());
            vhItem.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = summaryPathList.indexOf(summaryPath);
                    summaryPathList.remove(index);
                    notifyItemRemoved(index);

                    String url =
                            "http://203.189.96.52/todo/delete";

                    mQueue = Volley.newRequestQueue(context);
                    try {
                        // サーバへ送信するデータ
                        JSONObject jsonRequest = new JSONObject();
                        jsonRequest.put("id", rowListData.getId());

                        mQueue.add(new JsonObjectRequest(url, jsonRequest,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // JSONObjectのパース、List、
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
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(isHeader(position)){
            return HEADER;
        }else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return summaryPathList.size();
    }

    private List<SummaryPath> getSummaryPathList(List<CategoryListData> categoryList, List<List<RowListData>> rowList){
        List<SummaryPath> summaryPathList = new ArrayList<SummaryPath>();
        for (int i = 0; i < categoryList.size(); i++) {
            SummaryPath sectionIndexPath = new SummaryPath();
            sectionIndexPath.section = i;
            sectionIndexPath.row = HEADER;
            summaryPathList.add(sectionIndexPath);

            List<RowListData> rowListDataSection = rowList.get(i);
            for (int j = 0; j < rowListDataSection.size(); j++) {
                SummaryPath rowIndexPath = new SummaryPath();
                rowIndexPath.section = i;
                rowIndexPath.row = j;
                summaryPathList.add(rowIndexPath);
                
            }

        }

        return summaryPathList;
    }

    public boolean isHeader(int position){
        SummaryPath summaryPath = summaryPathList.get(position);

        return summaryPath.row == HEADER;
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView title;
        TextView subTitle;
        Button button;

        public VHItem(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.textTitle);
            subTitle = (TextView)itemView.findViewById(R.id.subText);
            button = (Button)itemView.findViewById(R.id.delete_bt1);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView categoryText;

        public VHHeader(View itemView) {
            super(itemView);
            categoryText = (TextView)itemView.findViewById(R.id.categoryText);

        }
    }
}
