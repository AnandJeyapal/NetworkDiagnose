package net.com.diagnose.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.com.diagnose.R;
import net.com.diagnose.activity.LogDetailsActivity;
import net.com.diagnose.bean.Logurl;
import net.com.diagnose.utils.Contans;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewFrgUrl extends BaseFragment {
    @Override
    public boolean onBackPressed() {
        return false;
    }

    //@BindView(R.id.rv_preview)


    RecyclerView recyclerView;




    List<Logurl> harEntryList = new ArrayList<>();


    PreviewAdapter previewAdapter;

    Boolean isHiddenHID = false;

    static PreviewFrgUrl previewFrgUrl;

    public static PreviewFrgUrl getInstance() {
        /*if (previewFragment == null) {
            previewFragment = new PreviewFragment();
        }*/
        previewFrgUrl = new PreviewFrgUrl();
        return previewFrgUrl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        ButterKnife.bind(this, view);
        recyclerView =(RecyclerView)view.findViewById(R.id.rv_preview);
        harEntryList= Contans.getList();
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(previewAdapter = new PreviewAdapter());

        return view;
    }

    private class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyViewHolder>  {

        @Override
        public PreviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PreviewAdapter.MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_preview, parent, false));
        }

        @Override
        public void onBindViewHolder(PreviewAdapter.MyViewHolder holder, int position) {
            Logurl harEntry = harEntryList.get(position);
            holder.rootView.setOnClickListener(new ClickListner(harEntry));
            holder.tv.setText(harEntry.getUrl());

            holder.iconView.setImageDrawable(getResources().getDrawable(R.drawable.ic_description_black_24dp));
           }

        @Override
        public int getItemCount() {
            return harEntryList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            TextView detailTextView;
            View rootView;
            ImageView iconView;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv_url);
                detailTextView = (TextView) view.findViewById(R.id.tv_detail);
                rootView = view;
                iconView = (ImageView) view.findViewById(R.id.iv_icon);
            }
        }

    }
    public class ClickListner implements View.OnClickListener {
        Logurl harEntry;

        public ClickListner(Logurl harEntry){
            this.harEntry = harEntry;
        }

        @Override
        public void onClick(View view) {
           // if(harLog.getEntries().indexOf(harEntry)>=0) {
                isHiddenHID = true;
                Intent intent = new Intent(getContext(), LogDetailsActivity.class);
                intent.putExtra("pos", harEntry);
                getActivity().startActivity(intent);
           // }
        }
    }



    public void filterItem(CharSequence s){
        if(previewAdapter!=null) {
            //previewAdapter.getFilter().filter(s);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // 这里为了解决返回后焦点在搜索栏的bug
        if(recyclerView!=null) {
            recyclerView.requestFocus();
           /* if(((MainActivity)getActivity()).searchView!=null) {
                filterItem(((MainActivity) getActivity()).searchView.getQuery());
            }*/
        }
    }


}
