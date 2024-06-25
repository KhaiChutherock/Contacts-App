package com.example.contactapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Activity_AddEditContact extends AppCompatActivity {

    private ImageView profileIv;
    private EditText nameEt, phoneEt, emailEt, noteEt;
    private ImageButton back;
    private Button done;

    private String id, image, name, phone, email, note;
    private boolean isEditMode;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri imageUri;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);
        // Ẩn thanh action bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        dbHelper = new DbHelper(this);

        // Khai báo quyền hạn
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Ánh xạ các thành phần giao diện
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.name);
        phoneEt = findViewById(R.id.phone);
        emailEt = findViewById(R.id.email);
        noteEt = findViewById(R.id.note);
        done = findViewById(R.id.done);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        String phoneNumber = intent.getStringExtra("PHONE");
        phoneEt.setText(phoneNumber);

        if (isEditMode) {
            // Nếu ở chế độ chỉnh sửa, lấy dữ liệu từ Intent và hiển thị lên giao diện
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            phone = intent.getStringExtra("PHONE");
            email = intent.getStringExtra("EMAIL");
            note = intent.getStringExtra("NOTE");
            image = intent.getStringExtra("IMAGE");

            nameEt.setText(name);
            phoneEt.setText(phone);
            emailEt.setText(email);
            noteEt.setText(note);

            imageUri = Uri.parse(image);

            if (image.equals("null")) {
                profileIv.setImageResource(R.drawable.baseline_person_24);
            } else {
                profileIv.setImageURI(imageUri);
            }
        }

        // Xử lý sự kiện khi nhấn nút "Done"
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                onBackPressed();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Back"
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Xử lý sự kiện khi nhấn vào hình ảnh để chọn ảnh đại diện
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
    }

    // Phương thức hiển thị hộp thoại chọn ảnh từ Camera hoặc Gallery
    private void showImagePickerDialog() {
        String options[] = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn một tùy chọn");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        }).create().show();
    }

    // Phương thức chọn ảnh từ Gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_FROM_GALLERY_CODE);
    }

    // Phương thức chụp ảnh từ Camera
    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE);
    }

    // Phương thức lưu dữ liệu vào cơ sở dữ liệu
    private void saveData() {
        name = nameEt.getText().toString();
        phone = phoneEt.getText().toString();
        email = emailEt.getText().toString();
        note = noteEt.getText().toString();

        if (!name.isEmpty() || !phone.isEmpty() || !email.isEmpty() || !note.isEmpty()) {
            if (isEditMode) {
                // Nếu đang ở chế độ chỉnh sửa, cập nhật dữ liệu
                dbHelper.updateContact(
                        "" + id,
                        "" + imageUri,
                        "" + name,
                        "" + phone,
                        "" + email,
                        "" + note
                );

                Toast.makeText(getApplicationContext(), "Đã cập nhật thành công....", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu là chế độ thêm mới, chèn dữ liệu mới vào cơ sở dữ liệu
                long id = dbHelper.insertContact(
                        "" + imageUri,
                        "" + name,
                        "" + phone,
                        "" + email,
                        "" + note
                );
                // Kiểm tra việc chèn dữ liệu thành công, hiển thị thông báo toast
            }
        } else {
            Toast.makeText(getApplicationContext(), "Không có gì để lưu....", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý sự kiện khi nhấn nút "Back" trên thanh action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // Phương thức kiểm tra quyền hạn Camera
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Phương thức yêu cầu quyền hạn Camera
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_CODE);
    }

    // Phương thức kiểm tra quyền hạn Lưu trữ
    private boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    // Phương thức yêu cầu quyền hạn Lưu trữ
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    // Phương thức xử lý kết quả khi nhận được phản hồi từ Camera hoặc Gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cần cấp quyền Camera và Lưu trữ..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cần cấp quyền Lưu trữ..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    // Phương thức xử lý kết quả sau khi chọn ảnh từ Camera hoặc Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                // Nếu chọn ảnh từ Gallery, mở thư viện CropImage để cắt ảnh
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(Activity_AddEditContact.this);
            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                // Nếu chụp ảnh từ Camera, mở thư viện CropImage để cắt ảnh
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(Activity_AddEditContact.this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                // Nhận kết quả sau khi cắt ảnh và hiển thị lên ImageView
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                profileIv.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // Hiển thị thông báo nếu có lỗi xảy ra trong quá trình cắt ảnh
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
