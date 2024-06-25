package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment_Home extends Fragment {

    private ImageButton fab;
    private RecyclerView contactRv;
    private DbHelper dbHelper;
    private SearchView searchbar;
    private Adapter_Contact adapterContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new DbHelper(requireContext());
        fab = view.findViewById(R.id.fab);

        contactRv = view.findViewById(R.id.contactRv);
        searchbar = view.findViewById(R.id.searchbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), Activity_AddEditContact.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });

        searchbar.clearFocus();
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContact(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return true;
            }
        });
        loadData();
        return view;
    }

    private void loadData() {

        // Lấy danh sách tất cả các liên hệ từ cơ sở dữ liệu
        ArrayList<ModelContact> contactList = dbHelper.getAllContacts();

        // Sắp xếp danh sách theo tên
        Collections.sort(contactList, new Comparator<ModelContact>() {
            @Override
            public int compare(ModelContact contact1, ModelContact contact2) {
                return contact1.getName().compareToIgnoreCase(contact2.getName());
            }
        });

        // Kiểm tra xem Adapter đã được khởi tạo chưa
        if (adapterContact == null) {
            // Nếu chưa, khởi tạo Adapter mới và đặt nó cho RecyclerView
            adapterContact = new Adapter_Contact(requireActivity(), contactList);
            contactRv.setAdapter(adapterContact);
        } else {
            // Nếu đã khởi tạo, cập nhật dữ liệu trong Adapter và thông báo thay đổi
            adapterContact.updateData(contactList);
        }
    }

    // Tìm kiếm liên hệ dựa trên từ khóa
    private void searchContact(String query) {
        adapterContact = new Adapter_Contact(requireActivity(), dbHelper.getSearchContact(query));
        contactRv.setAdapter(adapterContact);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Gọi hàm loadData khi Fragment được resume để cập nhật dữ liệu
        loadData();
    }
}
