package com.example.chillrelaxing.Data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.chillrelaxing.Data.Entity.SoundEntity;

import java.util.List;

@Dao
public interface SoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSound(SoundEntity sound);

    @Query("SELECT * FROM sounds")
    List<SoundEntity> getAllSounds();
}