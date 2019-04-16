package com.amishgarg.wartube.livedata;

import android.util.Log;

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

public class PostDetailLiveData extends LiveData<Post> {

    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;
    private final PostDetailLiveData.MyValueEventListener listener = new PostDetailLiveData.MyValueEventListener();

    public PostDetailLiveData(Query query) {
        this.query = query;
    }

    public PostDetailLiveData(DatabaseReference ref) {
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
            setValue(dataSnapshot.getValue(Post.class));
            // posts.setValue(postList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }

}