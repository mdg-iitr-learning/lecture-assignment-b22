package com.amishgarg.wartube.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amishgarg.wartube.FirebaseUtil
import com.amishgarg.wartube.Model.Comment
import com.amishgarg.wartube.Model.Post
import com.amishgarg.wartube.livedata.FirebaseCommentLiveData
import com.amishgarg.wartube.livedata.FirebaseQueryLiveData
import com.amishgarg.wartube.livedata.PostDetailLiveData

class PostDetailViewModel(val postsKey: String) : ViewModel() {

    val myTopPostsQuery = FirebaseUtil.getBaseRef().child("posts")
            .child(postsKey).orderByValue()

    val myTopCommentsQuery = FirebaseUtil.getCommentsRef()
            .child(postsKey).orderByChild("timestamp")

    private val liveData = PostDetailLiveData(myTopPostsQuery)

    private val commentLiveData = FirebaseCommentLiveData(myTopCommentsQuery)

    private val commentSize : MutableLiveData<Int> = MutableLiveData()

    fun getPost() : LiveData<Post>
    {
        Log.d("PostLiveData", "returning post")
        return liveData
    }

    fun getCommentsList() : LiveData<List<Comment>>
    {
        Log.d("PostLiveData", "returning cmt")

        return commentLiveData
    }
    fun getCommentSize(list:List<Comment>):LiveData<Int>{
        commentSize.value = list.size
        Log.d("PostLiveData", list.size.toString())
        return commentSize
    }


    private val liveCount  = Transformations.switchMap(getCommentsList()){input -> getCommentSize(input)}


    fun getLiveCount() : LiveData<Int> {
        return liveCount
    }

}