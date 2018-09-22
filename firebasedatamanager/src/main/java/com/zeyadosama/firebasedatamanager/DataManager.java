package com.zeyadosama.firebasedatamanager;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public abstract class DataManager<T extends DatabaseAccessibleObject> {

    private String TAG;

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<T>> liveData;
    private List<T> objectsList;
    private Class dataClass;

    public DataManager(Class c) {
        this.dataClass = c;
        this.TAG = c.getSimpleName();
        this.liveData = new MutableLiveData<>();
        this.objectsList = new ArrayList<>();
    }

    public DataManager(Class c, DatabaseReference databaseReference) {
        this.dataClass = c;
        this.TAG = c.getSimpleName();
        this.databaseReference = databaseReference;
        this.liveData = new MutableLiveData<>();
        this.objectsList = new ArrayList<>();
    }

    public DataManager(Class c, FirebaseUser user, DatabaseReference databaseReference) {
        this.dataClass = c;
        this.TAG = c.getSimpleName();
        this.user = user;
        this.databaseReference = databaseReference;
        this.liveData = new MutableLiveData<>();
        this.objectsList = new ArrayList<>();
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public MutableLiveData<List<T>> getLiveData() {
        return liveData;
    }

    public List<T> getObjectsList() {
        return objectsList;
    }

    /**
     * Writes a generic object to Firebase.
     *
     * @param object The object that can be written to Firebase
     * @see DatabaseAccessibleObject
     */
    public void writeToDatabase(T object) {
        this.databaseReference.push().setValue(object);
        Log.d(TAG, LogMessages.DATA_MANAGER + LogMessages.CHILD_ADDED);
    }

    /**
     * Writes a generic object to Firebase.
     * <p>
     * This method should <b>not</b> take an object that does not implement
     * {@link DatabaseAccessibleObject#getDatabaseReference()}
     * </p>
     *
     * @param object            The object that can be written to Firebase
     * @param databaseReference The reference in database that the object is queried in
     * @see DatabaseAccessibleObject
     */
    public void writeToDatabase(T object, String databaseReference) {
        this.databaseReference.child(databaseReference).setValue(object);
        Log.d(TAG, LogMessages.DATA_MANAGER + LogMessages.CHILD_RE_ADDED);
    }

    /**
     * Edits a generic object in Firebase.
     * <p>
     * This method should <b>not</b> take an object that does not implement
     * {@link DatabaseAccessibleObject#getDatabaseReference()}
     * </p>
     *
     * @param object            The object that can be written to Firebase
     * @param databaseReference The reference in database that the object is queried in
     * @see DatabaseAccessibleObject
     */
    public void editInDatabase(T object, String databaseReference) {
        this.databaseReference.child(databaseReference).setValue(object);
        Log.d(TAG, LogMessages.DATA_MANAGER + LogMessages.CHILD_EDITED);
    }

    /**
     * Removes a generic object from Firebase.
     * <p>
     * This method should <b>not</b> take an object that does not implement
     * {@link DatabaseAccessibleObject#getDatabaseReference()}
     * </p>
     *
     * @param databaseReference The reference in database that the object is queried in
     * @see DatabaseAccessibleObject
     */
    public void removeFromDatabase(String databaseReference) {
        this.databaseReference.child(databaseReference).removeValue();
        Log.d(TAG, LogMessages.DATA_MANAGER + LogMessages.CHILD_REMOVED);
    }

    /**
     * @return MutableLiveData A List having all objects in the node {@link #databaseReference}
     */
    public MutableLiveData<List<T>> getObjectsListFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                objectsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseAccessibleObject object;
                    object = (DatabaseAccessibleObject) snapshot.getValue(dataClass);
                    if (object != null) {
                        Log.d(TAG, LogMessages.DATA_MANAGER
                                + LogMessages.ON_DATA_CHANGE
                                + LogMessages.OBJECT_RETRIEVE_SUCCESS);
                        object.setDatabaseReference(snapshot.getKey());
                        objectsList.add((T) object);
                    } else
                        Log.e(TAG, LogMessages.DATA_MANAGER
                                + LogMessages.ON_DATA_CHANGE
                                + LogMessages.OBJECT_RETRIEVE_FAILED);

                }
                liveData.setValue(objectsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return liveData;
    }

    /**
     * A util class having constant Strings to be used in Log messages
     */
    private final static class LogMessages {

        private final static String DATA_MANAGER = "DataManager: ";
        private final static String ON_DATA_CHANGE = "onDataChange: ";

        private final static String OBJECT_RETRIEVE_SUCCESS = "Object retrieved successfully";
        private final static String OBJECT_RETRIEVE_FAILED = "Object retrieved failed";

        private final static String CHILD_ADDED = "Child added to database";
        private final static String CHILD_RE_ADDED = "Child re-added to database";
        private final static String CHILD_REMOVED = "Child removed from database";
        private final static String CHILD_EDITED = "Child edited in database";
    }
}
