package com.example.contactapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

public class Fragment_Keypad extends Fragment {
    private StringBuilder enteredNumbers = new StringBuilder();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pad, container, false);
        TextView addNumberTextView = view.findViewById(R.id.textViewAddNumber);
        EditText editTextPhone = view.findViewById(R.id.editTextPhone);

        // Khai báo và xử lý sự kiện cho các nút số
        MaterialButton[] numberButtons = new MaterialButton[]{
                view.findViewById(R.id.materialButton1),
                view.findViewById(R.id.materialButton2),
                view.findViewById(R.id.materialButton3),
                view.findViewById(R.id.materialButton4),
                view.findViewById(R.id.materialButton5),
                view.findViewById(R.id.materialButton6),
                view.findViewById(R.id.materialButton7),
                view.findViewById(R.id.materialButton8),
                view.findViewById(R.id.materialButton9),
                view.findViewById(R.id.materialButton0),
                view.findViewById(R.id.materialButtonsao),
                view.findViewById(R.id.materialButtonthang),
        };

        for (MaterialButton button : numberButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khi một nút số được bấm, thêm số vào StringBuilder và hiển thị trong EditText
                    enteredNumbers.append(button.getText());
                    editTextPhone.setText(enteredNumbers.toString());
                }
            });
        }

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if there is any input in the EditText
                if (charSequence.length() > 0) {
                    // If there is input, show "Add Number" TextView
                    addNumberTextView.setVisibility(View.VISIBLE);
                } else {
                    // If there is no input, hide "Add Number" TextView
                    addNumberTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });

        // Xử lý sự kiện cho nút "Add Number" TextView
        addNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy số điện thoại từ các số đã nhập
                String phoneNumber = editTextPhone.getText().toString();

                // Kiểm tra xem số điện thoại đã nhập có khác rỗng hay không
                if (!phoneNumber.isEmpty()) {
                    // Tạo intent để chuyển đến hoạt động AddEditContact để thêm dữ liệu mới
                    Intent intent = new Intent(getContext(), Activity_AddEditContact.class);

                    // Truyền số điện thoại vào hoạt động AddEditContact
                    intent.putExtra("PHONE", phoneNumber);

                    // Truyền một dữ liệu boolean để xác định đó là để thêm dữ liệu mới
                    intent.putExtra("isEditMode", false);

                    // Bắt đầu intent
                    startActivity(intent);
                } else {
                    // Hiển thị thông báo nếu số điện thoại là rỗng
                    Toast.makeText(getContext(), "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện cho nút backspace
        MaterialButton buttonBack = view.findViewById(R.id.materialButtonback);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu có số để xóa
                if (enteredNumbers.length() > 0) {
                    // Xóa ký tự cuối cùng
                    enteredNumbers.deleteCharAt(enteredNumbers.length() - 1);
                    editTextPhone.setText(enteredNumbers.toString());
                }
            }
        });

        // Xử lý sự kiện cho nút call
        MaterialButton buttonCall = view.findViewById(R.id.materialButtoncall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động khi nút call được bấm (ví dụ: gọi điện thoại)
                String phoneNumber = editTextPhone.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    // Gọi điện thoại với số đã nhập
                    makePhoneCall(phoneNumber);
                } else {
                    // Hiển thị thông báo nếu số điện thoại không hợp lệ
                    Toast.makeText(getContext(), "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Hàm mẫu để gọi điện thoại
    private void makePhoneCall(String phoneNumber) {
        if (getContext() != null) {
            // Kiểm tra quyền CALL_PHONE
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // Thực hiện hành động gọi điện thoại
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(dialIntent);
            } else {
                // Yêu cầu quyền CALL_PHONE nếu chưa được cấp
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }
}
