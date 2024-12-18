package com.example.adminpuriesfooddelivery

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.google.firebase.firestore.FirebaseFirestore
import com.example.adminpuriesfooddelivery.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addItemButton.setOnClickListener {
            val itemName = binding.enterFoodName.text.toString()
            val itemDescription = binding.description.text.toString()
            val itemPriceString = binding.enterFoodPrice.text.toString()
            val ingredients = binding.ingredients.text.toString()

            // Check if price is a valid number
            val itemPrice = try {
                itemPriceString.toDouble()
            } catch (e: NumberFormatException) {
                0.0 // Set default value or show error
            }

            if (itemName.isNotEmpty() && itemDescription.isNotEmpty() && itemPrice > 0 && ingredients.isNotEmpty()) {
                val itemData = hashMapOf(
                    "name" to itemName,
                    "description" to itemDescription,
                    "price" to itemPrice,
                    "ingredients" to ingredients, // Ingredients added here
                    "imagePath" to "internal_storage_path_to_image.jpg" // Store the internal file path here
                )

                // Add the item data to Firestore
                db.collection("items")
                    .add(itemData)
                    .addOnSuccessListener {
                        // Handle success (e.g., show a success message)
                        println("Item added successfully")
                    }
                    .addOnFailureListener { e ->
                        // Handle failure (e.g., show an error message)
                        println("Error adding item: ${e.message}")
                    }
            } else {
                // Handle missing fields (e.g., show error messages)
                println("Please fill all fields correctly")
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            // Display the image in the ImageView
            binding.selectedImage.setImageURI(uri)

            // Save the image to internal storage
            try {
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                val savedFile = saveImageToInternalStorage(bitmap)
                // Optionally, store the saved file path for use later
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): File {
        // Create a file in the app's internal storage to save the image
        val directory = filesDir
        val imageFile = File(directory, "item_image_${System.currentTimeMillis()}.jpg")

        // Save the bitmap as a JPEG file
        val fileOutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        return imageFile
    }
}
