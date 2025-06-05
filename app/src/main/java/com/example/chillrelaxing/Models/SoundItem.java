package com.example.chillrelaxing.Models;

import com.example.chillrelaxing.R;

public class SoundItem {
    public static final int TYPE_SOUND = 1;

    public enum SourceType {
        LOCAL_RES,      // từ res/raw
        FILE,           // từ tệp trong máy
        URL             // từ internet
    }

    private final int type = TYPE_SOUND;

    private String name;
    private int iconResId;
    private boolean isLocked;
    private String groupName;

    private SourceType sourceType;
    private int soundResId;        // nếu là LOCAL_RES
    private String filePath;       // nếu là FILE
    private String url;            // nếu là URL

    private String unlockKey;      // dùng để lưu trạng thái mở khóa (unique id)

    public SoundItem(String name, int iconResId, boolean isLocked, SourceType sourceType) {
        this.name = name;
        this.iconResId = iconResId;
        this.isLocked = isLocked;
        this.sourceType = sourceType;
    }

    // === Factory methods ===

    public static SoundItem fromLocalRes(String name, int iconResId, int resId) {
        if (iconResId <= 0) {
            iconResId = R.drawable.bird_chirping; // icon mặc định
        }
        SoundItem item = new SoundItem(name, iconResId, true, SourceType.LOCAL_RES);
        item.soundResId = resId;
        item.unlockKey = "res_" + resId;
        return item;
    }


    public static SoundItem fromFile(String name, int iconResId, String path) {
        if (iconResId <= 0) {
            iconResId = R.drawable.bird_chirping;
        }
        SoundItem item = new SoundItem(name, iconResId, true, SourceType.FILE);
        item.filePath = path;
        item.unlockKey = "file_" + path.hashCode();
        return item;
    }


    public static SoundItem fromUrl(String name, int iconResId, String url) {
        if (iconResId <= 0) {
            iconResId = R.drawable.bird_chirping;
        }
        SoundItem item = new SoundItem(name, iconResId, true, SourceType.URL);
        item.url = url;
        item.unlockKey = "url_" + url.hashCode();
        return item;
    }


    // === Getters & Setters ===

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean hasGroupName() {
        return groupName != null && !groupName.isEmpty();
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public int getSoundResId() {
        return soundResId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getUrl() {
        return url;
    }

    public String getUnlockKey() {
        return unlockKey;
    }
}
