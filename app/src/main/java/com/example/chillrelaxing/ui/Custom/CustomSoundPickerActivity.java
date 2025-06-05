package com.example.chillrelaxing.ui.Custom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillrelaxing.Adapter.CustomSoundAdapter;
import com.example.chillrelaxing.Models.CustomSound;
import com.example.chillrelaxing.Models.CustomSoundGroup;
import com.example.chillrelaxing.Models.SoundItem;
import com.example.chillrelaxing.R;
import com.example.chillrelaxing.utils.AdManager;
import com.example.chillrelaxing.utils.SoundUnlockManager;

import java.util.ArrayList;
import java.util.List;

public class CustomSoundPickerActivity extends AppCompatActivity implements CustomSoundAdapter.OnSoundClickListener {

    private RecyclerView recyclerViewCustomSounds;
    private CustomSoundAdapter adapter;
    private ImageView btnClose;
    private TextView tvTitle;
    private AdManager adManager;
    private SoundUnlockManager unlockManager;
    private List<Object> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sound_picker);

        recyclerViewCustomSounds = findViewById(R.id.recyclerViewCustomSounds);
        btnClose = findViewById(R.id.btnClose);
        tvTitle = findViewById(R.id.tvTitle);

        adManager = new AdManager();
        adManager.init(this);
        unlockManager = new SoundUnlockManager(this);

        setupRecyclerView();
        setupListeners();
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        displayList = getAllCustomItems();
        adapter = new CustomSoundAdapter(this, displayList, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = displayList.get(position);
                return (item instanceof CustomSoundGroup) ? 3 : 1;
            }
        });

        recyclerViewCustomSounds.setLayoutManager(layoutManager);
        recyclerViewCustomSounds.setAdapter(adapter);
    }

    private List<Object> getAllCustomItems() {
        List<Object> allItems = new ArrayList<>();

        for (CustomSoundGroup group : getCustomSoundList()) {
            allItems.add(group);

            for (CustomSound sound : group.getSounds()) {
                SoundItem item = SoundItem.fromLocalRes(
                        sound.getTitle(),
                        validateIconResId(sound.getImageResId()),
                        sound.getSoundResId()
                );
                item.setLocked(!unlockManager.isUnlocked(item.getUnlockKey()));
                allItems.add(item);
            }
        }

        return allItems;
    }

    private int validateIconResId(int iconResId) {
        return (iconResId <= 0) ? R.drawable.bird_chirping : iconResId;
    }

    @Override
    public void onSoundClick(SoundItem item) {
        if (!item.isLocked()) {
            returnResult(item);
            return;
        }

        adManager.showAd(this, () -> {
            unlockManager.unlockSound(item.getUnlockKey());
            item.setLocked(false);
            adapter.notifyDataSetChanged();
            returnResult(item);
        });
    }

    private void returnResult(SoundItem item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", item.getName());
        resultIntent.putExtra("iconResId", item.getIconResId());
        resultIntent.putExtra("soundResId", item.getSoundResId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private List<CustomSoundGroup> getCustomSoundList() {
        List<CustomSoundGroup> groups = new ArrayList<>();

        List<CustomSound> rainSounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("Rain Sounds", rainSounds));
        rainSounds.add(new CustomSound(R.drawable.heavy_rain, R.raw.heavy_rain, "Heavy Rain"));
        rainSounds.add(new CustomSound(R.drawable.light_rain, R.raw.light_rain, "Light Rain"));
        rainSounds.add(new CustomSound(R.drawable.storm, R.raw.thunder, "Storm"));
        rainSounds.add(new CustomSound(R.drawable.rain_on_tent, R.raw.rain_tent, "Rain on Tent"));
        rainSounds.add(new CustomSound(R.drawable.rain_on_window, R.raw.rain_window, "Rain on Window"));
        rainSounds.add(new CustomSound(R.drawable.rain_in_forest, R.raw.rain_forest, "Rain in Forest"));

        List<CustomSound> natureSounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("Nature Sounds", natureSounds));
        natureSounds.add(new CustomSound(R.drawable.forest_night, R.raw.forest, "Forest Night"));
        natureSounds.add(new CustomSound(R.drawable.ocean_waves, R.raw.drip, "Ocean Waves"));
        natureSounds.add(new CustomSound(R.drawable.birds_singing, R.raw.bird, "Birds Singing"));
        natureSounds.add(new CustomSound(R.drawable.frog_croaking, R.raw.frog, "Frogs Croaking"));
        natureSounds.add(new CustomSound(R.drawable.crickets_chirping, R.raw.cricket, "Crickets Chirping"));
        natureSounds.add(new CustomSound(R.drawable.wind_blowing, R.raw.wind, "Wind Blowing"));
        natureSounds.add(new CustomSound(R.drawable.snow_falling, R.raw.snow, "Snow Falling"));
        natureSounds.add(new CustomSound(R.drawable.ocean_waves, R.raw.wave, "Ocean Waves"));

        List<CustomSound> fireSounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("Fire Sounds", fireSounds));
        fireSounds.add(new CustomSound(R.drawable.crackling_fire, R.raw.fire, "Crackling Fire"));
        fireSounds.add(new CustomSound(R.drawable.fireplace_ambience, R.raw.fireplace, "Fireplace Ambience"));

        List<CustomSound> citySounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("City Sounds", citySounds));
        citySounds.add(new CustomSound(R.drawable.traffic_noise, R.raw.traffic, "Traffic Noise"));
        citySounds.add(new CustomSound(R.drawable.cafe_background, R.raw.cafe, "Cafe Background"));
        citySounds.add(new CustomSound(R.drawable.train_passing, R.raw.train, "Train Passing"));
        citySounds.add(new CustomSound(R.drawable.airplane_flying, R.raw.airplane, "Airplane Flying"));

        List<CustomSound> Cafeandchill = new ArrayList<>();
        groups.add(new CustomSoundGroup("Cafe & Chill", Cafeandchill));
        Cafeandchill.add(new CustomSound(R.drawable.cafe_music, R.raw.chill, "Cafe Music"));
        Cafeandchill.add(new CustomSound(R.drawable.chill_ambience, R.raw.chill_ambience, "Chill Ambience"));
        Cafeandchill.add(new CustomSound(R.drawable.chillfeel, R.raw.chillsound, "Chill Music"));
        Cafeandchill.add(new CustomSound(R.drawable.cafe_ambience, R.raw.pianochill, "Chill Relax"));
        Cafeandchill.add(new CustomSound(R.drawable.cafe_background, R.raw.gitarchill, "Chill Music"));

        List<CustomSound> homeSounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("Home Sounds", homeSounds));
        homeSounds.add(new CustomSound(R.drawable.fan_blowing, R.raw.fan, "Fan Blowing"));
        homeSounds.add(new CustomSound(R.drawable.washing_machine, R.raw.washing_machine, "Washing Machine"));
        homeSounds.add(new CustomSound(R.drawable.fridge_humming, R.raw.fridge, "Fridge Humming"));
        homeSounds.add(new CustomSound(R.drawable.dishwasher, R.raw.bowl, "Dishwasher"));

        List<CustomSound> instrumentSounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("Instrument Sounds", instrumentSounds));
        instrumentSounds.add(new CustomSound(R.drawable.piano, R.raw.piano, "Piano"));
        instrumentSounds.add(new CustomSound(R.drawable.acoustic_guitar, R.raw.guitar, "Acoustic Guitar"));
        instrumentSounds.add(new CustomSound(R.drawable.violin, R.raw.violin, "Violin"));
        instrumentSounds.add(new CustomSound(R.drawable.flute, R.raw.flute, "Flute"));
        instrumentSounds.add(new CustomSound(R.drawable.drum, R.raw.drums, "Drum"));
        instrumentSounds.add(new CustomSound(R.drawable.saxophone, R.raw.saxophone, "Saxophone"));
        instrumentSounds.add(new CustomSound(R.drawable.harp, R.raw.harp, "Harp"));
        instrumentSounds.add(new CustomSound(R.drawable.clarinet, R.raw.clarinet, "Clarinet"));

        List<CustomSound> animalSounds = new ArrayList<>();
        groups.add(new CustomSoundGroup("Animal Sounds", animalSounds));
        animalSounds.add(new CustomSound(R.drawable.dog_darking, R.raw.dog_bark, "Dog Barking"));
        animalSounds.add(new CustomSound(R.drawable.cat_meowing, R.raw.cat_purring, "Cat Meowing"));
        animalSounds.add(new CustomSound(R.drawable.bird_chirping, R.raw.bird, "Bird Chirping"));
        animalSounds.add(new CustomSound(R.drawable.horse_neigh, R.raw.horse, "Horse Neigh"));
        animalSounds.add(new CustomSound(R.drawable.cow_mooing, R.raw.cow, "Cow Mooing"));
        animalSounds.add(new CustomSound(R.drawable.frog_croaking, R.raw.frog, "Frog Croaking"));
        animalSounds.add(new CustomSound(R.drawable.duck_duack, R.raw.duck, "Duck Quack"));
        animalSounds.add(new CustomSound(R.drawable.crickets_chirping, R.raw.cricket, "Crickets Chirping"));

        return groups;
    }
}