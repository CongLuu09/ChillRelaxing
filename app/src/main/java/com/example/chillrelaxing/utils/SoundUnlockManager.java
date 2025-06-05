package com.example.chillrelaxing.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SoundUnlockManager {
    private static final String PREF_NAME = "unlocked_sounds";
    private static final long UNLOCK_DURATION = 7L * 24 * 60 * 60 * 1000; // 7 ngày

    private final SharedPreferences prefs;

    public SoundUnlockManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Kiểm tra âm thanh đã được mở khóa chưa
    public boolean isUnlocked(String unlockKey) {
        long unlockUntil = prefs.getLong(unlockKey, 0);
        return System.currentTimeMillis() < unlockUntil;
    }

    // Mở khóa âm thanh trong 7 ngày
    public void unlockSound(String unlockKey) {
        long validUntil = System.currentTimeMillis() + UNLOCK_DURATION;
        prefs.edit().putLong(unlockKey, validUntil).apply();
    }

    // (Tuỳ chọn) Xoá trạng thái mở khóa
    public void resetUnlock(String unlockKey) {
        prefs.edit().remove(unlockKey).apply();
    }

    // (Tuỳ chọn) Xoá toàn bộ mở khóa
    public void resetAll() {
        prefs.edit().clear().apply();
    }
}
