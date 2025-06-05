package com.example.chillrelaxing.Timer;

public interface TimerCallBack {
    void onTimerSet(long durationMillis);
    void onTimerCancelled();
    default void onTimerFinished() {}

}
