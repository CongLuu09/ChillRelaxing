package com.example.chillrelaxing.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillrelaxing.Models.CustomSoundGroup;
import com.example.chillrelaxing.Models.SoundItem;
import com.example.chillrelaxing.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CustomSoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_GROUP = 0;
    private static final int TYPE_SOUND = 1;

    public interface OnSoundClickListener {
        void onSoundClick(SoundItem item);
    }

    private final List<Object> itemList;
    private final OnSoundClickListener listener;
    private final Context context;

    public CustomSoundAdapter(Context context, List<Object> itemList, OnSoundClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return (itemList.get(position) instanceof CustomSoundGroup) ? TYPE_GROUP : TYPE_SOUND;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_custom_group, parent, false);
            return new GroupViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sound_custom, parent, false);
            return new SoundViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itemList.get(position);

        if (holder instanceof GroupViewHolder) {
            CustomSoundGroup group = (CustomSoundGroup) item;
            ((GroupViewHolder) holder).tvGroupTitle.setText(group.getGroupName());

            Log.d("AdapterBind", "🟩 Group: " + group.getGroupName());

        } else if (holder instanceof SoundViewHolder) {
            SoundItem sound = (SoundItem) item;
            SoundViewHolder soundHolder = (SoundViewHolder) holder;

            soundHolder.tvLabel.setText(sound.getName());

            // ✅ In log đầy đủ thông tin
            Log.d("AdapterBind", "🎵 Binding sound: " + sound.getName() +
                    ", iconResId = " + sound.getIconResId() +
                    ", locked = " + sound.isLocked() +
                    ", sourceType = " + sound.getSourceType());

            // Nếu iconResId <= 0 thì dùng icon mặc định để tránh ảnh trắng
            if (sound.getIconResId() > 0) {
                soundHolder.imgIcon.setImageResource(sound.getIconResId());
            } else {
                soundHolder.imgIcon.setImageResource(R.drawable.bird_chirping); // bạn thay icon mặc định nếu có
            }

            soundHolder.imgLock.setVisibility(sound.isLocked() ? View.VISIBLE : View.GONE);

            soundHolder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onSoundClick(sound);
            });
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder for group title
    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupTitle;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupTitle = itemView.findViewById(R.id.tv_group_title);
        }
    }

    // ViewHolder for sound item
    public static class SoundViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgIcon;
        TextView tvLabel;
        ImageView imgLock;

        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            imgLock = itemView.findViewById(R.id.imgLock);
        }
    }
}