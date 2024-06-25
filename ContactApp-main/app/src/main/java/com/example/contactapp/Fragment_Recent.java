package com.example.contactapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class Fragment_Recent extends Fragment {

    private RecyclerView recentCallsRv;
    private Adapter_Recent_Calls recentCallsAdapter;
    private ArrayList<ModelCallRecent> recentCallsList;

    public Fragment_Recent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the list of recent calls from the database
        recentCallsList = new DbHelper(getContext()).getAllCallRecents();
        // Initialize the adapter and set the list of recent calls
        recentCallsAdapter = new Adapter_Recent_Calls(getContext(), recentCallsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        // Find RecyclerView from the layout fragment_recent.xml
        recentCallsRv = view.findViewById(R.id.recyclerViewRecentCalls);

        // Set LayoutManager and Adapter for RecyclerView
        recentCallsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recentCallsRv.setAdapter(recentCallsAdapter);

        return view;
    }

    // This method can be called when you want to update the recent calls list
    public void updateRecentCallsList() {
        // Update the list of recent calls from the database
        recentCallsList = new DbHelper(getContext()).getAllCallRecents();
        // Notify the adapter about the data change
        recentCallsAdapter.notifyDataSetChanged();
    }
}
