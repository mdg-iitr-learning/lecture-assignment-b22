package com.amishgarg.wartube.Activity


import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

import com.amishgarg.wartube.Adapter.PostsRecyclerAdapter
import com.amishgarg.wartube.FirebaseUtil
import com.amishgarg.wartube.Model.Author
import com.amishgarg.wartube.Model.Post
import com.amishgarg.wartube.ViewModels.PostsViewModel
import com.amishgarg.wartube.R
import com.amishgarg.wartube.ViewModels.PostDetailViewModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.collect.Lists
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_posts.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PostsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PostsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PostsFragment : Fragment() {


    val TAG = "PostsFragmentDebug"

    lateinit var progressBar: ProgressBar
    var user: FirebaseUser? = null
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var postsViewModel : PostsViewModel
    private lateinit var postsDetailViewModel: PostDetailViewModel
    private lateinit var mAdapter : PostsRecyclerAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view : View = inflater.inflate(R.layout.fragment_posts, container, false)

        val myTopPostsQuery = FirebaseUtil.getBaseRef().child("posts")
                .orderByChild("timestamp")


        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        if(user == null)
        {
            findNavController().navigate(R.id.welcome_dest)
        }
        else
        {
            if(!user!!.isEmailVerified){
                findNavController().navigate(R.id.welcome_dest)
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference()

        progressBar = view.findViewById(R.id.progressBar_cyclic)
        val fab : FloatingActionButton = view.findViewById(R.id.fab)

        recyclerView = view.findViewById<RecyclerView>(R.id.posts_recycler)

        viewManager = LinearLayoutManager(context).apply {
            this.reverseLayout = true
            this.stackFromEnd = true
        }


        fab.setOnClickListener {
            findNavController().navigate(R.id.new_post_dest)
        }

        return view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val myTopPostsQuery = FirebaseUtil.getBaseRef().child("posts")
                .orderByChild("timestamp")


        val options : FirebaseRecyclerOptions<Post> = FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(myTopPostsQuery, Post::class.java)
                .build()


        progressBar_cyclic.visibility = View.VISIBLE
        mAdapter = PostsRecyclerAdapter(options, progressBar)

        recyclerView.layoutManager = viewManager
        recyclerView.adapter = mAdapter


    }


    override fun onStart() {
        super.onStart()
        mAdapter.startListening();
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }
}





















/*
        val myTopPostsQuery = databaseReference.child("posts")
                .orderByChild("timestamp")

        myTopPostsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    Log.d("PostsLoading", post?.author?.display_name)
                    if (post != null) {
                        posts.add(post)
                        viewAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("PostsLoading", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
*/


