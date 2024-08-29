package com.example.art_gallery_studio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.art_gallery_studio.ui.theme.ArtGalleryStudioTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import androidx.compose.ui.text.font.FontStyle

class MainActivity : ComponentActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db = Firebase.firestore

        val imageData = hashMapOf(
            "imageName" to "image1",
            "imageUrl" to "https://your-firebase-storage-url.com/image1.jpg",
            "description" to "Art Image 1"
        )

        db.collection("images")
            .add(imageData)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }

        setContent {
            ArtGalleryStudioTheme {

                ArtGalleryScreen(db)
            }
        }
    }
}

@Composable
fun ArtGalleryScreen(db: FirebaseFirestore) {
    var currentImageIndex by remember { mutableStateOf(0) }
    val images = listOf(
        R.drawable.pic1,
        R.drawable.pic2,
        R.drawable.pic3,
        R.drawable.pic4,
        R.drawable.pic7
    )
    val imageTitles = listOf(
        "New York Downtown Metropolitan",
        "Cherry Walk, Roosevelt Island, New York",
        "Meadow Landscape Grassland Mountain",
        "Cat Sitting on a Rock at Sunset",
        "Bonfire on the Beach"
    )

    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = images[currentImageIndex]),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = imageTitles[currentImageIndex])
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            currentImageIndex = if (currentImageIndex - 1 < 0) images.size - 1 else currentImageIndex - 1
                        }) {
                            Text(text = stringResource(R.string.previous))
                        }
                        Button(onClick = {
                            currentImageIndex = (currentImageIndex + 1) % images.size
                        }) {
                            Text(text = stringResource(R.string.next))
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ArtGalleryScreenPreview() {
    ArtGalleryStudioTheme {
        // Use a mock Firestore instance or pass null for the preview
        ArtGalleryScreen(Firebase.firestore)
    }
}
