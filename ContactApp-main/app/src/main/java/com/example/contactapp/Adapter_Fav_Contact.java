package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Fav_Contact extends RecyclerView.Adapter<Adapter_Fav_Contact.FavContactViewHolder> {

    private Context context;
    private ArrayList<ModelContact> favContactList;
    private DbHelper dbHelper;

    static final int REQUEST_CALL_PHONE = 1;

    public Adapter_Fav_Contact(Context context, ArrayList<ModelContact> favContactList) {
        this.context = context;
        this.favContactList = favContactList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public FavContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_favcontact_item, parent, false);
        FavContactViewHolder vh = new FavContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavContactViewHolder holder, int position) {
        ModelContact modelContact = favContactList.get(position);

        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();
        String phone = modelContact.getPhone();
        String email = modelContact.getEmail();
        String note = modelContact.getNote();

        // Hiển thị tên liên lạc
        holder.favContactName.setText(name);

        // Hiển thị hình ảnh liên lạc hoặc mặc định nếu không có hình
        if (image.equals("null")) {
            holder.favContactImage.setImageResource(R.drawable.baseline_person_24);
        } else {
            holder.favContactImage.setImageURI(Uri.parse(image));
        }

        // Xử lý sự kiện khi nhấn vào nút gọi
        holder.favContactDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra quyền CALL_PHONE
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Nếu đã cấp quyền, thực hiện cuộc gọi
                    makePhoneCall(phone);
                } else {
                    // Nếu chưa cấp quyền, yêu cầu quyền từ người dùng
                    ActivityCompat.requestPermissions((Activity_Main) context, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                }
            }
        });

        // Xử lý sự kiện khi nhấn vào item để xem chi tiết liên lạc
        holder.favContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_ContactDetails.class);
                intent.putExtra("contactId", id);
                context.startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn vào nút xóa liên lạc khỏi danh sách yêu thích
        holder.favContactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteFavoriteContact(id);
                notifyDataSetChanged();
                ArrayList<ModelContact> contactList = dbHelper.getAllFavoriteContacts();
                updatefavData(contactList);
            }
        });
    }

    // Phương thức thực hiện cuộc gọi điện thoại
    private void makePhoneCall(String phoneNumber) {
        String dialNumber = "tel:" + phoneNumber;
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(dialNumber));

        if (dialIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(dialIntent);
        } else {
            Toast.makeText(context, "Không có ứng dụng nào có thể xử lý cuộc gọi điện.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return favContactList.size();
    }

    // Lớp ViewHolder cho item trong RecyclerView
    public static class FavContactViewHolder extends RecyclerView.ViewHolder {

        ImageView favContactImage, favContactDial;
        TextView favContactName, favContactEdit, favContactDelete;
        View favContactLayout;

        // Constructor
        public FavContactViewHolder(@NonNull View itemView) {
            super(itemView);

            // Khởi tạo các view
            favContactImage = itemView.findViewById(R.id.contact_image);
            favContactDial = itemView.findViewById(R.id.contact_number_dial);
            favContactName = itemView.findViewById(R.id.contact_name);
            favContactDelete = itemView.findViewById(R.id.contact_delete);
            favContactEdit = itemView.findViewById(R.id.contact_edit);
            favContactLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    // Phương thức cập nhật dữ liệu cho danh sách liên lạc yêu thích
    public void updatefavData(ArrayList<ModelContact> newFavContactList) {
        this.favContactList = newFavContactList;
        notifyDataSetChanged();
    }
}
