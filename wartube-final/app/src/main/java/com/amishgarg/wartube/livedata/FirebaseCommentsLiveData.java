package com.amishgarg.wartube.livedata;

import android.util.Log;

import com.amishgarg.wartube.Model.Comment;
import com.amishgarg.wartube.Model.Comments;
import com.amishgarg.wartube.Model.Likes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class FirebaseCommentsLiveData extends LiveData<List<Comments>> {
    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;
    private final FirebaseCommentsLiveData.MyValueEventListener listener = new FirebaseCommentsLiveData.MyValueEventListener();



    public FirebaseCommentsLiveData(Query query) {
        this.query = query;
    }

    public FirebaseCommentsLiveData(DatabaseReference ref) {
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

            List<Comments> commentList = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                Log.d(LOG_TAG, "Inside on data changed comments");
                if (postSnapshot != null) {
                    commentList.add(postSnapshot.getValue(Comments.class));

                } else {
                    Log.e(LOG_TAG, "NULL POSTSNAP comments");
                }
            }

            setValue(commentList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }
}
