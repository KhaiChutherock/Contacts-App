package com.example.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng trong cơ sở dữ liệu
        db.execSQL(Constants.CREATE_TABLE);
        db.execSQL(Constants.CREATE_TABLE_CALL_RECENT);
        db.execSQL(Constants.CREATE_TABLE_FAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Thêm dữ liệu vào db
    public long insertContact(String image, String name, String phone, String email, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.C_IMAGE, image);
        contentValues.put(Constants.C_NAME, name);
        contentValues.put(Constants.C_PHONE, phone);
        contentValues.put(Constants.C_EMAIL, email);
        contentValues.put(Constants.C_NOTE, note);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    // Hàm cập nhật để cập nhật dữ liệu trong cơ sở dữ liệu
    public void updateContact(String id, String image, String name, String phone, String email, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.C_IMAGE, image);
        contentValues.put(Constants.C_NAME, name);
        contentValues.put(Constants.C_PHONE, phone);
        contentValues.put(Constants.C_EMAIL, email);
        contentValues.put(Constants.C_NOTE, note);
        db.update(Constants.TABLE_NAME, contentValues, Constants.C_ID + " =? ", new String[]{id});
        db.close();
    }

    public ArrayList<ModelContact> getAllContacts() {
        ArrayList<ModelContact> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelContact modelContact = new ModelContact(
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE))
                );
                arrayList.add(modelContact);
            } while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }

    public ArrayList<ModelContact> getSearchContact(String query) {
        ArrayList<ModelContact> contactList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String queryToSearch = "SELECT * FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.C_NAME + " LIKE '%" + query + "%'";
        Cursor cursor = db.rawQuery(queryToSearch, null);

        if (cursor.moveToFirst()) {
            do {
                ModelContact modelContact = new ModelContact(
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE))
                );
                contactList.add(modelContact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    public void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public long addToFavorites(String contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String checkQuery = "SELECT * FROM " + Constants.TABLE_NAME_FAVORITES +
                " WHERE " + Constants.C_CONTACT_ID_FAVORITES + " = ?";
        Cursor cursor = db.rawQuery(checkQuery, new String[]{contactId});

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1; // Trả về -1 để báo lỗi
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.C_CONTACT_ID_FAVORITES, contactId);
        long id = db.insert(Constants.TABLE_NAME_FAVORITES, null, contentValues);

        cursor.close();
        db.close();
        return id;
    }

    public ArrayList<ModelContact> getAllFavoriteContacts() {
        ArrayList<ModelContact> favoritesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME +
                " INNER JOIN " + Constants.TABLE_NAME_FAVORITES +
                " ON " + Constants.TABLE_NAME + "." + Constants.C_ID +
                " = " + Constants.TABLE_NAME_FAVORITES + "." + Constants.C_CONTACT_ID_FAVORITES;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelContact modelContact = new ModelContact(
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE))
                );
                favoritesList.add(modelContact);
            } while (cursor.moveToNext());
        }
        db.close();
        return favoritesList;
    }

    public void deleteFavoriteContact(String contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME_FAVORITES, Constants.C_CONTACT_ID_FAVORITES + "=?", new String[]{String.valueOf(contactId)});
        db.close();
    }


    public ArrayList<ModelCallRecent> getAllCallRecents() {
        ArrayList<ModelCallRecent> callRecentList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME_CALL_RECENT, null);

        if (cursor.moveToFirst()) {
            do {
                ModelCallRecent modelCallRecent = new ModelCallRecent(
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_CALL_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_CALLER_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_RECEIVER_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_CALL_START_TIME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_CALL_END_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_DURATION_SECONDS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_CALL_TYPE))
                );
                callRecentList.add(modelCallRecent);
            } while (cursor.moveToNext());
        }
        cursor.close(); // Đóng con trỏ sau khi sử dụng
        db.close();
        return callRecentList;
    }

}
