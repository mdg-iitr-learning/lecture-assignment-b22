package com.amishgarg.wartube.livedata;

import android.util.Log;

import com.amishgarg.wartube.Model.Likes;
import com.amishgarg.wartube.Model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirebaseLikesLiveData extends LiveData<List<Likes>> {

    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;
    private final FirebaseLikesLiveData.MyValueEventListener listener = new FirebaseLikesLiveData.MyValueEventListener();



    public FirebaseLikesLiveData(Query query) {
        this.query = query;
    }

    public FirebaseLikesLiveData(DatabaseReference ref) {
        this.query = ref;
    }



    @Override
    protected void onActive() {
        Log.d(LOG_TAG, "onActive");
        query.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(LOG_TAG, "onInactive");
        query.removeEventListener(listener);
    }



    private class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            List<Likes> likesList = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                Log.d(LOG_TAG, "Inside on data changed likees");
                if (postSnapshot != null) {
                    likesList.add(postSnapshot.getValue(Likes.class));

                } else {
                    Log.e(LOG_TAG, "NULL POSTSNAP likes");
                }
            }

            setValue(likesList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }

}
