package com.amishgarg.wartube.Activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.amishgarg.wartube.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.*
import com.amishgarg.wartube.ViewModels.PostDetailViewModel
import com.amishgarg.wartube.ViewModels.PostDetailViewModelFactory
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.iid.InstanceIdResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amishgarg.wartube.FirebaseUtil
import com.amishgarg.wartube.Model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class MainActivity : androidx.appcompat.app.AppCompatActivity() {

    val CHANNEL_ID = "wartube";
    val CHANNEL_NAME = "Wartube"
    val CHANNEL_DESC = "Posts Notifications"

    //todo: loading dialogs wherever required
    //    //todo: Material UI
    //    //todo: Add Likes section
    //    //todo: Add comments section
    //    //todo: Navigation, back stack, toolbar, animations
    //    //todo: MVVM to NewPostFragment,
    //    //todo: Solve Item Repeating bug
    //    //todo: replace handler, runnable with something, else in YoutubeRepository
    //    //todo: Add room
    //    //todo: Add Notification system for new post
    //    //todo: display full image when clicked on image

    //todo: Most imp todo Welcome Activity backstakc null

    // private val GOOGLE_YOUTUBE_API_KEY = "AIzaSyBV4XQEZ9l1HZeBQFL6ZZvHYfMhtnqUkmw"
    // private val CHANNEL_ID_TS = "UCq-Fj5jknLsUf-MWSy4_brA"
    // private val CHANNEL_ID_PDP = "UC-lHJZR3Gqxm24_Vd_AJ5Yw"
    // var SUBS_TS = 0
    // var SUBS_PDP = 0

    companion object {

        lateinit var host: NavHostFragment
        lateinit var navController: NavController
        lateinit var tokenList : MutableList<String>

    }

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar and host and navController
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        tokenList = ArrayList<String>()

         host = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

         navController = host.navController

         setupBottomNav(navController)

        /*navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.equals())
        })*/
        Log.d("MainActivityStuff", getVisibleFragment().toString() )
//            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        // Get token
      /*  FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("notificationfb", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result!!.token


                    Log.d("notificationfb", token)
                    Toast.makeText(this@MainActivity, token, Toast.LENGTH_SHORT).show()
                })

*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel : NotificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        FirebaseMessaging.getInstance().subscribeToTopic("updates")
//        displayNotification()


        val tokenQuery =  FirebaseUtil.getBaseRef().child("people")
                .orderByChild("token")
        tokenQuery.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("FirebaseNotif", p0.message)

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(peopleSnapshot in p0.children)
                {
                    if(peopleSnapshot.exists()) {
                        if(peopleSnapshot.getValue(User::class.java)?.token != null){
                                tokenList.add(peopleSnapshot.getValue(User::class.java)?.token!!)
                                Log.d("FirebaseNotif", "tokenlist" + tokenList[0])
                            }
                    else
                        {
                            Log.d("FirebaseNotif", "tkoennull")
                        }
                    }
                    else{
                        Log.d("FirebaseNotif", "peoplenull")

                    }
                }
            }
        })
    }


    private fun setupBottomNav(navController: NavController)
    {
        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    fun getVisibleFragment(): Fragment? {
        val fragmentManager = this@MainActivity.supportFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible)
                    return fragment
            }
        }
        return null
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        return when (item?.itemId) {
            R.id.settings_dest -> {
                if(item?.itemId == R.id.settings_dest)
                {
                    if(FirebaseAuth.getInstance().currentUser != null) {
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this, "Successfully Signed out",
                                Toast.LENGTH_SHORT).show()
                    }else
                    {
                        Toast.makeText(this, "User not signed in",
                                Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

 /*
    }*/

}
