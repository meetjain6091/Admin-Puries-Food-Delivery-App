package com.example.adminpuriesfooddelivery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminpuriesfooddelivery.databinding.ActivityAdminProfileBinding
import com.example.adminpuriesfooddelivery.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var adminRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminRef = database.reference.child("user")

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveInfoBtn.setOnClickListener{
            updateUserData()
        }
        binding.name.isEnabled=false
        binding.address.isEnabled=false
        binding.email.isEnabled=false
        binding.phone.isEnabled=false
        binding.password.isEnabled=false
        binding.saveInfoBtn.isEnabled = false

        var isEnable=false
        binding.editButton.setOnClickListener {
            isEnable= ! isEnable
            binding.name.isEnabled=isEnable
            binding.address.isEnabled=isEnable
            binding.email.isEnabled=isEnable
            binding.phone.isEnabled=isEnable
            binding.password.isEnabled=isEnable
            if(isEnable)
            {
                binding.name.requestFocus()
            }
            binding.saveInfoBtn.isEnabled = isEnable
        }


        retriveUserData()



    }



    private fun retriveUserData(){
        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null){
            val userRef = adminRef.child(currentUserUid)

            adminRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var ownerName = snapshot.child("name").getValue()
                        var ownerEmail = snapshot.child("email").getValue()
                        var ownerPassword = snapshot.child("password").getValue()
                        var ownerAddress = snapshot.child("address").getValue()
                        var ownerPhone = snapshot.child("phone").getValue()

                        setDataToTextView(ownerName,ownerEmail,ownerPassword,ownerAddress,ownerPhone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminProfileActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }


    private fun setDataToTextView(owner: Any?,email : Any?,password : Any?,address : Any?,phone : Any?){
        binding.name.setText(owner.toString())
        binding.email.setText(email.toString())
        binding.password.setText(password.toString())
        binding.address.setText(address.toString())
        binding.phone.setText(phone.toString())
    }

    private fun updateUserData(){

        var updateName = binding.name.text.toString()
        var updateemail = binding.email.text.toString()
        var updatePassword = binding.password.text.toString()
        var upadateAddress = binding.address.text.toString()
        var updatePhone = binding.phone.text.toString()

        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null){
            val userRef = adminRef.child(currentUserUid)

            userRef.child("name").setValue(updateName)
            userRef.child("email").setValue(updateemail)
            userRef.child("password").setValue(updatePassword)
            userRef.child("phone").setValue(updatePhone)
            userRef.child("address").setValue(upadateAddress)

                Toast.makeText(this,"Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                auth.currentUser?.updateEmail(updateemail)
                auth.currentUser?.updatePassword(updatePassword)
        }else{
            Toast.makeText(this,"Profile Updation Failed", Toast.LENGTH_SHORT).show()
        }
    }
}