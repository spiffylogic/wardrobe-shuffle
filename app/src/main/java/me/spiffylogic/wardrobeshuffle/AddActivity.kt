package me.spiffylogic.wardrobeshuffle

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.net.Uri
import android.support.v4.content.FileProvider
import android.widget.ImageView
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.os.Build

// Reference: https://developer.android.com/training/camera/photobasics.html

class AddActivity : AppCompatActivity() {
    var photoView: ImageView? = null
    var photoURI: Uri? = null
    var requestNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        photoView = findViewById(R.id.photo_view)
    }

    fun dispatchTakePictureIntent(v: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile();
            } catch (ex: IOException) {
                Log.e("Markus", "Something went wrong when creating image file")
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                    "me.spiffylogic.wardrobeshuffle.fileprovider", photoFile)

                // This hack seems to be needed on KitKat (API 19)
                // https://stackoverflow.com/a/18332000/432311
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    val resInfoList = packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        grantUriPermission(resolveInfo.activityInfo.packageName, photoURI,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                requestNum = Random().nextInt(1000) // let's be sure we always match up the request and result correctly
                startActivityForResult(takePictureIntent, requestNum);
            }
        }
    }

    // Note: by specifying EXTRA_OUTPUT we told it where to put the data, so data will be null here, hence the ?
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestNum && resultCode == Activity.RESULT_OK) {
            photoView?.setImageURI(photoURI)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create a unique image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + ".jpg"
        val photosDir = File(filesDir, "photos/")
        val image = File(photosDir, imageFileName)

        // Make sure the containing directory exists!
        // https://stackoverflow.com/a/26386970/432311
        if (!image.parentFile.exists()) {
            image.parentFile.mkdirs()
        }

        Log.d("Markus", "Image file created: " + image.getAbsolutePath())
        return image
    }
}
