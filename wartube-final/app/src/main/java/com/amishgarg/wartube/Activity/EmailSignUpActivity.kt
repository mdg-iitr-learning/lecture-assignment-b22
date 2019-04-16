package com.amishgarg.wartube.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.amishgarg.wartube.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_email_sign_up.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class EmailSignUpActivity : AppCompatActivity() {



    private var mAuth: FirebaseAuth? = null
    val TAG ="Signup Activity"
    lateinit var databaseReference: DatabaseReference
    val GALLERY = 1
    val CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_up)


        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val signupbutton : Button = findViewById(R.id.signup_button)
        signupbutton.setOnClickListener {
            createAccount(email_up.text.toString(), pass_up.text.toString())
        }
        val imageView = findViewById<ImageView>(R.id.user_pic)

   /*     val bitmap = imageView.drawable as Bitmap
        imageView.setImageBitmap(bitmap)*/
        imageView.setOnClickListener {
            showDialog()
        }
        val verifyButton = findViewById<Button>(R.id.verify_button)
        verifyButton.setOnClickListener {
            sendEmailVerification()
        }

    }


    private fun showDialog()
    {
        val pictureDialog = AlertDialog.Builder(this@EmailSignUpActivity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }


    private fun choosePhotoFromGallary()
    {
        val galleryIntent: Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera()
    {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
                    //val path = saveImage(bitmap)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    user_pic.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            user_pic!!.setImageBitmap(thumbnail)
            //saveImage(thumbnail)

        }


    }



    private fun validateForm(): Boolean {
        var valid = true

        val email = email_up.text.toString()
        if (TextUtils.isEmpty(email)) {
            email_up.error = "Required."
            valid = false
        } else {
            email_up.error = null
        }

        val password = pass_up.text.toString()
        if (TextUtils.isEmpty(password)) {
            pass_up.error = "Required."
            valid = false
        } else {
            pass_up.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        //progressDialog.hide();

        if (user != null) {


            val  bitmap : Bitmap = (user_pic.drawable as BitmapDrawable).bitmap

            val storageReference : StorageReference = FirebaseStorage.getInstance().reference.child("users").child(user.uid)

            var baos : ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byedata = baos.toByteArray()
            val uploadTask = storageReference.putBytes(byedata)
            var downloadurl : String? = null

            uploadTask.addOnSuccessListener { taskSnapshot ->
                Log.d("Upload", "success")
                storageReference.downloadUrl.addOnSuccessListener{
                    downloadurl = it.toString()
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setDisplayName("Jane Q. User")
//                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                            .build();
//
                    val profileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(name_up.text.toString()).setPhotoUri(it).build()
                    user.updateProfile(profileChangeRequest)
                    Log.d("Upload", downloadurl)

                    Log.w(TAG, user.getEmail())
                    val s: CharSequence = user.email.toString()
                    Snackbar.make(this.findViewById(R.id.welcome_layout), s , Snackbar.LENGTH_SHORT).show()
                    val uMap = HashMap<String, Any>()
                    uMap["display_name"] = user.displayName.toString()
                    uMap["profile_pic"] = user.photoUrl.toString()
                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                        override fun onComplete(p0: Task<InstanceIdResult>) {
                            if(p0.isSuccessful)
                            {
                                Log.d("FirebaseNotif", p0.result?.token)
                                uMap["token"] = p0.result?.token.toString()
                                databaseReference.child("people").child(user!!.uid).updateChildren(uMap, object: DatabaseReference.CompletionListener
                                {
                                    override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                                        if (p0 != null) {
                                            Toast.makeText(this@EmailSignUpActivity,
                                                    "Couldn't save user data: " + p0.message,
                                                    Toast.LENGTH_LONG).show()
                                        }}
                                }
                                )
                            }
                            else
                            {
                                Log.d("FirebaseNotif", p0.exception?.message)
                            }
                        }
                    })

                }


            }.addOnFailureListener {
                Log.d("Upload", "Failed")
            }




            Log.d("WelcomeActivity", "COde is here 2")
            //  setContentView(R.layout.)
        } else {
            Toast.makeText(this,"Signed Out", Toast.LENGTH_LONG)
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        // showProgressDialog()

        // [START create_user_with_email]
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                signup_button.isEnabled = false
                signup_button.visibility  = View.GONE
                verify_button.visibility = View.VISIBLE
                verify_button.isEnabled = true
                Toast.makeText(baseContext,
                        "Verify your account",
                        Toast.LENGTH_LONG).show()
                val user = mAuth?.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT).show()
                updateUI(null)
            }

            // [START_EXCLUDE]
            //  hideProgressDialog()
            // [END_EXCLUDE]
        }
        // [END create_user_with_email]
    }

    private fun sendEmailVerification() {
        // Disable button
        verify_button.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = mAuth?.currentUser
        user?.sendEmailVerification()
                ?.addOnCompleteListener(this) { task ->
                    // [START_EXCLUDE]
                    // Re-enable button

                    verify_button.isEnabled = true

                    if (task.isSuccessful) {
                        Toast.makeText(baseContext,
                                "Verification email sent to ${user.email} ",
                                Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(baseContext,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                    // [END_EXCLUDE]
                }
        // [END send_email_verification]
    }
}
