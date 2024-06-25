package com.example.contactapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ContactDetails extends AppCompatActivity {

    private static final int EDIT_CONTACT_REQUEST_CODE = 100;

    // View
    private TextView nameTv, phoneTv, emailTv, noteTv, phone, note, mail;
    private ImageView profileIv;

    private String id, Name, Image, Phone, Email, Note;

    private ImageButton back;
    private Button edit, deletebutton, call, message;
    private static final int REQUEST_CALL_PHONE = 1;

    // Database helper
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Khởi tạo đối tượng dbHelper
        dbHelper = new DbHelper(this);

        phone = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        note = findViewById(R.id.note);
        phone.setText("Phone");
        mail.setText("Mail");
        note.setText("Notes");

        back = findViewById(R.id.back);
        edit = findViewById(R.id.Edit);
        deletebutton = findViewById(R.id.deletebutton);

        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        noteTv = findViewById(R.id.noteTv);
        profileIv = findViewById(R.id.profileIv);

        call = findViewById(R.id.call);
        message = findViewById(R.id.message);

        loadDataById();

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + Phone));
                startActivity(intent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra quyền CALL_PHONE
                if (ContextCompat.checkSelfPermission(Activity_ContactDetails.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Nếu đã cấp quyền, thực hiện cuộc gọi
                    makePhoneCall();
                } else {
                    // Nếu chưa cấp quyền, yêu cầu quyền từ người dùng
                    ActivityCompat.requestPermissions(Activity_ContactDetails.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteContact(id);
                onBackPressed();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ContactDetails.this, Activity_AddEditContact.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", Name);
                intent.putExtra("PHONE", Phone);
                intent.putExtra("EMAIL", Email);
                intent.putExtra("NOTE", Note);
                intent.putExtra("IMAGE", Image);
                intent.putExtra("isEditMode", true);
                startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE);
            }
        });

        Button addFavButton = findViewById(R.id.addfav);
        addFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm liên lạc vào danh sách yêu thích
                long result = dbHelper.addToFavorites(id);

                if (result != -1) {
                    Toast.makeText(Activity_ContactDetails.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý nếu có lỗi khi thêm vào danh sách yêu thích
                }
            }
        });
    }

    private void makePhoneCall() {
        // Tạo một Intent để thực hiện cuộc gọi
        String phoneNumber = "tel:" + Phone;
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));

        // Kiểm tra xem ứng dụng có thể xử lý Intent này hay không
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            // Không có ứng dụng nào có thể xử lý cuộc gọi điện
            Toast.makeText(Activity_ContactDetails.this, "Không có ứng dụng nào có thể xử lý cuộc gọi điện.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDataById() {
        // Lấy dữ liệu từ cơ sở dữ liệu
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + id + "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu
                Name = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                Image = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                Phone = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE));
                Email = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                Note = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE));

                // Đặt dữ liệu
                nameTv.setText(Name);
                phoneTv.setText(Phone);
                emailTv.setText(Email);
                noteTv.setText(Note);

                if (Image.equals("null")) {
                    profileIv.setImageResource(R.drawable.baseline_person_24);
                } else {
                    profileIv.setImageURI(Uri.parse(Image));
                }

            } while (cursor.moveToNext());
        }
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_CONTACT_REQUEST_CODE) {
                // Cập nhật màn hình ContactDetails với dữ liệu mới
                loadDataById();
            }
        }
    }
}
