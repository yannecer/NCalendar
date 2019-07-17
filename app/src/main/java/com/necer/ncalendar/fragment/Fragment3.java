package com.necer.ncalendar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.RecyclerViewAdapter;

public class Fragment3 extends Fragment {

    private RecyclerView recyclerView;
    private TextView tv_no_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment3, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setVisibility(View.GONE);
        tv_no_data.setVisibility(View.VISIBLE);
    }
}
