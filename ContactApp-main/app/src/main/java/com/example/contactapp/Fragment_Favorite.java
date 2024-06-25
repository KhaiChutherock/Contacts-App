package com.example.contactapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class Fragment_Favorite extends Fragment {

    private RecyclerView favContactRv;
    private Adapter_Fav_Contact favContactAdapter;
    private ArrayList<ModelContact> favContactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        favContactRv = view.findViewById(R.id.favcontactRv);
        favContactList = new ArrayList<>();
        favContactAdapter = new Adapter_Fav_Contact(getActivity(), favContactList);

        favContactRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        favContactRv.setAdapter(favContactAdapter);

        // Load danh sách yêu thích từ cơ sở dữ liệu
        loadFavoriteContacts();

        return view;
    }

    private void loadFavoriteContacts() {
        // Sử dụng DbHelper để lấy danh sách yêu thích
        DbHelper dbHelper = new DbHelper(getActivity());
        favContactList.clear();
        favContactList.addAll(dbHelper.getAllFavoriteContacts());

        // Tạo một Adapter mới với danh sách mới
        Adapter_Fav_Contact newAdapter = new Adapter_Fav_Contact(getActivity(), favContactList);

        // Gán Adapter mới cho RecyclerView
        favContactRv.setAdapter(newAdapter);
    }
}
