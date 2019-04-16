package com.amishgarg.wartube.livedata;

import android.util.Log;
import android.widget.LinearLayout;

import com.amishgarg.wartube.Model.Comment;
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

public class FirebaseCommentLiveData extends LiveData<List<Comment>> {

    private static final String LOG_TAG = "FirebaseCommentLiveData";

    private final Query query;
    private final FirebaseCommentLiveData.MyValueEventListener listener = new FirebaseCommentLiveData.MyValueEventListener();

    public FirebaseCommentLiveData(Query query) {
        this.query = query;
    }

    public FirebaseCommentLiveData(DatabaseReference ref) {
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
            List<Comment> commentList = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                Log.d(LOG_TAG, "Inside on data changed");
                if (postSnapshot != null) {
                    commentList.add(postSnapshot.getValue(Comment.class));

                } else {
                    Log.e(LOG_TAG, "NULL POSTSNAP");
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
