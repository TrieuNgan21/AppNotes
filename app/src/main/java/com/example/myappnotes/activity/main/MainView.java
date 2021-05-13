package com.example.myappnotes.activity.main;

import java.util.List;


public interface MainView {
    void showloading();
    void hideloading();
   // void onGetResult(List<Note> notes);
    void onErrorloading(String message);
}
