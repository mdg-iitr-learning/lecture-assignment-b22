package com.amishgarg.wartube.Activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amishgarg.wartube.Adapter.CommentListAdapter
import com.amishgarg.wartube.FirebaseUtil
import com.amishgarg.wartube.PicassoUtil
import com.amishgarg.wartube.Model.Author
import com.amishgarg.wartube.Model.Comment
import com.amishgarg.wartube.R
import com.amishgarg.wartube.TimeDisplay
import com.amishgarg.wartube.ViewModels.PostDetailViewModel
import com.amishgarg.wartube.ViewModels.PostDetailViewModelFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Lists
import com.amishgarg.wartube.Model.Post
import com.google.firebase.database.*


class PostDetailFragment : Fragment() {

    lateinit var postAuthorIcon : ImageView
    lateinit var postAuthorName : TextView
    lateinit var posttimestamp: TextView
    lateinit var postText : TextView
    lateinit var postPhoto: ImageView
    lateinit var postKey : String
    lateinit var likesTextView: TextView
    lateinit var comTextView: TextView
    lateinit var newCommentText : EditText
    lateinit var postCommentButton : ImageButton
    lateinit var postLikeCheckBox: CheckBox
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val mAdapter = CommentListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)
        val args : Bundle? = arguments
        postKey = args?.getString("postKey")!!
        Log.d("PostKey", postKey)
        postAuthorIcon = view.findViewById(R.id.post_author_icond)
        postAuthorName = view.findViewById(R.id.post_author_named)
        posttimestamp = view.findViewById(R.id.post_timestampd)
        postText = view.findViewById(R.id.post_textd)
        postPhoto =view.findViewById(R.id.post_photod)
        likesTextView = view.findViewById(R.id.post_num_likesd)
        newCommentText = view.findViewById(R.id.new_post_comment)
        postCommentButton = view.findViewById(R.id.post_comment_button)
        comTextView = view.findViewById(R.id.post_num_comd)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_comments)
        viewManager = LinearLayoutManager(context)
        postLikeCheckBox = view.findViewById(R.id.like_buttond)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_comments).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = mAdapter


        }

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent()
        intent.putExtra("postKey", postKey)

        val postDetailViewModel : PostDetailViewModel by lazy { ViewModelProviders.of(this, PostDetailViewModelFactory(intent.getStringExtra("postKey"))).get(PostDetailViewModel::class.java)
        }


        postDetailViewModel.getPost().observe(this, Observer {
            postAuthorName.text = it.author.display_name
            PicassoUtil.loadImagePicasso(it.author.profile_pic, postAuthorIcon)
            postText.text = it.text
            posttimestamp.text = TimeDisplay(it.timestamp as Long).getTimeDisplay()
            if(it.type == 1) {
                PicassoUtil.loadImagePicasso(it.full__url, postPhoto)
            }
            else{
                postPhoto.layoutParams.height = 0
            }
                likesTextView.text = it.likes.toString()
            comTextView.text = it.comments.toString()

        })

        postDetailViewModel.getCommentsList().observe(this, Observer {
            mAdapter.submitList(Lists.reverse(it))
            mAdapter.notifyDataSetChanged()

        })

        FirebaseUtil.getBaseRef().child("likes").child(postKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("PostDetailFragment", p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                    if(p0.hasChild(FirebaseUtil.getCurrentUserId())) {
                        postLikeCheckBox.isChecked = true
                    }
            }
        })

        postLikeCheckBox.setOnClickListener {
            val isChecked : Boolean = (it as CheckBox).isChecked

            val likesRef = FirebaseUtil.getLikesRef().child(postKey)
            val addLike = HashMap<String, Boolean?>()
            addLike.put(FirebaseUtil.getCurrentUserId(), true)

            val remLike = HashMap<String, Boolean?>()
            remLike.put(FirebaseUtil.getCurrentUserId(), null)



            if (isChecked) {
                likesRef.updateChildren(addLike as Map<String, Any>) { databaseError, databaseReference ->
                    val postlikesRef = FirebaseUtil.getPostsRef().child(postKey)
                    postlikesRef.runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                            val p = mutableData.getValue(Post::class.java)
                            p!!.likes = p.likes + 1
                            mutableData.value = p
                            return Transaction.success(mutableData)
                        }

                        override fun onComplete(databaseError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                            if (databaseError != null) {
                                Log.d("Likes increment", "postTransaction:onComplete:$databaseError")
                            }
                        }
                    })
                }
            } else if (!isChecked) {

                likesRef.updateChildren(remLike as Map<String, Any>) { databaseError, databaseReference ->
                    val postlikesRef = FirebaseUtil.getPostsRef().child(postKey)
                    postlikesRef.runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                            val p = mutableData.getValue(Post::class.java)
                            p!!.likes = p.likes - 1
                            mutableData.value = p
                            return Transaction.success(mutableData)
                        }

                        override fun onComplete(databaseError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                            if (databaseError != null) {
                                Log.d("Likes increment", "postTransaction:onComplete:$databaseError")
                            }
                        }
                    })
                }
            }


        }

        postCommentButton.setOnClickListener {
            if(newCommentText.text.toString().length > 3) {
                uploadComment(newCommentText.text.toString())
            }
            else
            {
                Toast.makeText(context, "At least 3 characters required",
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadComment(text: String){
        val uploadComment = HashMap<String, Any>()
        val commentTimestamp  = System.currentTimeMillis()
        val author : Author = FirebaseUtil.getAuthor()
        val commentKey = author.uid+commentTimestamp
        val newComment = Comment(author, text, commentTimestamp.toString() )
        uploadComment[FirebaseUtil.getCommentsPath() + postKey + "/" + commentKey] = ObjectMapper().convertValue(newComment, Map::class.java)

        val comReference = FirebaseUtil.getPostsRef().child(postKey)

        comReference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val p = mutableData.getValue(Post::class.java)
                        ?: return Transaction.success(mutableData)

                p.comments = p.comments+1
                // Set value and report transaction success
                mutableData.value = p
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                    databaseError: DatabaseError?,
                    b: Boolean,
                    dataSnapshot: DataSnapshot?
            ) {
                // Transaction completed
                if(databaseError!=null) {
                    Log.d("Comment increment", "postTransaction:onComplete:" + databaseError!!)
                }
            }
        })

        FirebaseUtil.getBaseRef().updateChildren(uploadComment) { databaseError, databaseReference ->
            if (databaseError == null)
            {
                Toast.makeText(context, "Comment Uploaded",
                        Toast.LENGTH_SHORT).show()
                newCommentText.text.clear()
            }
            else
            {
                Toast.makeText(context, "Error uploading comment",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

}
