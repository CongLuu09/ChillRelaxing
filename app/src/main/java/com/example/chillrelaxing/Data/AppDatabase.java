package com.example.chillrelaxing.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;

import com.example.chillrelaxing.Models.LayerSound;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sounds.db";
    private static final int DATABASE_VERSION = 3;

    // Bảng sounds
    public static final String TABLE_SOUNDS = "sounds";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ICON = "iconResId";
    public static final String COLUMN_SOUND = "soundResId";
    public static final String COLUMN_SCENE = "sceneName";

    // Bảng mix_sounds
    public static final String TABLE_MIXS = "mix_sounds";
    public static final String COLUMN_MIX_ID = "_id";
    public static final String COLUMN_MIX_NAME = "name";
    public static final String COLUMN_DEVICE_ID = "deviceId";
    public static final String COLUMN_SOUND_IDS = "soundIds";
    public static final String COLUMN_CREATED_AT = "createdAt";

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SOUNDS_TABLE = "CREATE TABLE " + TABLE_SOUNDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ICON + " INTEGER, " +
                COLUMN_SOUND + " INTEGER, " +
                COLUMN_SCENE + " TEXT)";
        db.execSQL(CREATE_SOUNDS_TABLE);

        String CREATE_MIXS_TABLE = "CREATE TABLE " + TABLE_MIXS + " (" +
                COLUMN_MIX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MIX_NAME + " TEXT, " +
                COLUMN_DEVICE_ID + " TEXT, " +
                COLUMN_SOUND_IDS + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT)";
        db.execSQL(CREATE_MIXS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            String CREATE_MIXS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MIXS + " (" +
                    COLUMN_MIX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MIX_NAME + " TEXT, " +
                    COLUMN_DEVICE_ID + " TEXT, " +
                    COLUMN_SOUND_IDS + " TEXT, " +
                    COLUMN_CREATED_AT + " TEXT)";
            db.execSQL(CREATE_MIXS_TABLE);
        }
    }

    // --- Bảng sounds ---
    public void insertSound(String name, int iconResId, int soundResId, String sceneName) {
        if (isSoundExists(name, sceneName)) return;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_ICON, iconResId);
        values.put(COLUMN_SOUND, soundResId);
        values.put(COLUMN_SCENE, sceneName);
        db.insert(TABLE_SOUNDS, null, values);
        db.close();
    }

    public boolean isSoundExists(String name, String sceneName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SOUNDS, null,
                COLUMN_NAME + "=? AND " + COLUMN_SCENE + "=?",
                new String[]{name, sceneName}, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public List<LayerSound> getAllSavedSounds(String sceneName) {
        List<LayerSound> sounds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SOUNDS, null,
                COLUMN_SCENE + "=?", new String[]{sceneName}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int iconResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON));
                int soundResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SOUND));

                LayerSound layer = new LayerSound(iconResId, name, soundResId, (MediaPlayer) null, 0.1f);
                sounds.add(layer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sounds;
    }

    public void deleteSoundByName(String name, String sceneName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SOUNDS, COLUMN_NAME + "=? AND " + COLUMN_SCENE + "=?",
                new String[]{name, sceneName});
        db.close();
    }

    public void updateSoundName(String oldName, String newName, String sceneName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        db.update(TABLE_SOUNDS, values,
                COLUMN_NAME + "=? AND " + COLUMN_SCENE + "=?",
                new String[]{oldName, sceneName});
        db.close();
    }
    public void deleteSoundsByScene(String sceneName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SOUNDS, COLUMN_SCENE + "=?", new String[]{sceneName});
        db.close();
    }



    // --- Tiện ích xoá toàn bộ dữ liệu ---
    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SOUNDS);
        db.execSQL("DELETE FROM " + TABLE_MIXS);
        db.execSQL("DELETE FROM sqlite_sequence");
        db.close();
    }

    public void clearSounds() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SOUNDS);
        db.close();
    }

    public void clearMixes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MIXS);
        db.close();
    }
}
