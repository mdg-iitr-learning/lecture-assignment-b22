package com.amishgarg.wartube.Activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amishgarg.wartube.Adapter.PostsRecyclerAdapter
import com.amishgarg.wartube.FirebaseUtil
import com.amishgarg.wartube.PicassoUtil
import com.amishgarg.wartube.Model.Post
import com.amishgarg.wartube.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ProfileFragment : Fragment() {


    var user: FirebaseUser? = null
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var mAdapter : PostsRecyclerAdapter
    private lateinit var userName : TextView
    private lateinit var userImageView : ImageView
    private lateinit var userId : String
    lateinit var progress2Bar: ProgressBar
    lateinit var myTopPostsQuery : Query
    var postsToShow : Boolean = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        if(user == null)
        {
            findNavController().navigate(R.id.welcome_dest)
        }
        else if (!user!!.isEmailVerified)
        {
                findNavController().navigate(R.id.welcome_dest)

        }
        databaseReference = FirebaseDatabase.getInstance().getReference()

         userName= view.findViewById<TextView>(R.id.user_name)
         userImageView = view.findViewById<ImageView>(R.id.user_img)
         userId = user?.uid.toString()
        userName.setText(user?.displayName)
        PicassoUtil.loadImagePicasso(user?.photoUrl.toString(), userImageView)
         myTopPostsQuery = FirebaseUtil.getBaseRef().child("people").
                child(userId).child("posts").orderByChild("timestamp")

        val options : FirebaseRecyclerOptions<Post> = FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(myTopPostsQuery, Post::class.java).build()




        progress2Bar = view.findViewById(R.id.progressBar_profile)
        mAdapter = PostsRecyclerAdapter(options, progress2Bar)

        recyclerView = view.findViewById<RecyclerView>(R.id.user_postsrecycler)

        viewManager = LinearLayoutManager(context).apply {
            this.reverseLayout = true
            this.stackFromEnd = true
        }
        return view
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         if(postsToShow) {
             progress2Bar.visibility = View.VISIBLE
         }else
         {
             Toast.makeText(context, "No posts to show!!",
                     Toast.LENGTH_LONG).show()
         }

         recyclerView.layoutManager = viewManager
         recyclerView.adapter = mAdapter

     }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening();
        myTopPostsQuery.addValueEventListener(listener())

    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
        myTopPostsQuery.removeEventListener(listener())
    }

    fun listener() : ValueEventListener = object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {

            postsToShow = p0.hasChildren()

            if(postsToShow) {
                progress2Bar.visibility = View.VISIBLE
            }else
            {
                progress2Bar.visibility = View.GONE
            }

        }

    }

}
