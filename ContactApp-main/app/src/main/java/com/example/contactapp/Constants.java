package com.example.contactapp;

public class Constants {

    // Tên database
    public static final String DATABASE_NAME = "CONTACT_DB";

    // Phiên bản database
    public static final int DATABASE_VERSION = 2;

    // Tên bảng liên lạc
    public static final String TABLE_NAME = "CONTACT_TABLE";

    // Tên cột trong bảng liên lạc
    public static final String C_ID = "ID";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_NAME = "NAME";
    public static final String C_PHONE = "PHONE";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_NOTE = "NOTE";

    // Truy vấn tạo bảng liên lạc
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_NAME + " TEXT, "
            + C_PHONE + " TEXT, "
            + C_EMAIL + " TEXT, "
            + C_NOTE + " TEXT "
            + " );";

    // Tên bảng danh sách yêu thích
    public static final String TABLE_NAME_FAVORITES = "FAVORITE_TABLE";

    // Tên cột trong bảng danh sách yêu thích
    public static final String C_ID_FAVORITES = "FAV_ID";
    public static final String C_CONTACT_ID_FAVORITES = "CONTACT_ID";

    // Truy vấn tạo bảng danh sách yêu thích
    public static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_NAME_FAVORITES + "( "
            + C_ID_FAVORITES + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_CONTACT_ID_FAVORITES + " INTEGER, "
            + "FOREIGN KEY(" + C_CONTACT_ID_FAVORITES + ") REFERENCES " + DATABASE_NAME + "(" + C_ID + ")"
            + " );";

    // Tên bảng cuộc gọi gần đây
    public static final String TABLE_NAME_CALL_RECENT = "CALL_RECENT_TABLE";
    public static final String C_CALL_ID = "CALL_ID";
    public static final String C_CALLER_ID = "CALLER_ID";
    public static final String C_RECEIVER_ID = "RECEIVER_ID";
    public static final String C_CALL_START_TIME = "CALL_START_TIME";
    public static final String C_CALL_END_TIME = "CALL_END_TIME";
    public static final String C_DURATION_SECONDS = "DURATION_SECONDS";
    public static final String C_CALL_TYPE = "CALL_TYPE"; // Thêm khai báo cho cột CALL_TYPE

    // Truy vấn tạo bảng cuộc gọi gần đây
    public static final String CREATE_TABLE_CALL_RECENT = "CREATE TABLE " + TABLE_NAME_CALL_RECENT + "( "
            + C_CALL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_CALLER_ID + " INTEGER, "
            + C_RECEIVER_ID + " INTEGER, "
            + C_CALL_START_TIME + " TEXT, "
            + C_CALL_END_TIME + " TEXT, "
            + C_DURATION_SECONDS + " INTEGER, "
            + C_CALL_TYPE + " INTEGER"
            + " );";

}
