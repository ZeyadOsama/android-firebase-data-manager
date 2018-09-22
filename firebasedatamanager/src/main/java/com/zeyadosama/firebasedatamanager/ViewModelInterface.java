package com.zeyadosama.firebasedatamanager;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface ViewModelInterface<T extends DatabaseAccessibleObject> {

    void writeToDatabase(T object);

    void writeToDatabase(T object, String databasePath);

    void editInDatabase(T object, String databasePath);

    void removeFromDatabase(String databasePath);

    LiveData<List<T>> getObjectsList();
}
