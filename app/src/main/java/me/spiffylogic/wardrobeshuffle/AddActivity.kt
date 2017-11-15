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
import android.support.media.ExifInterface
import android.os.Build
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.BitmapFactory

// Reference: https://developer.android.com/training/camera/photobasics.html

class AddActivity : AppCompatActivity() {
    var photoView: ImageView? = null
    var photoFile: File? = null
    var requestNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        photoView = findViewById(R.id.photo_view)
    }

    fun dispatchTakePictureIntent(v: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            try {
                photoFile = createImageFile();
            } catch (ex: IOException) {
                Log.e("Markus", "Something went wrong when creating image file")
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this,
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
    // TODO: Since it seems we need to work with the bitmap directly anyway, consider skipping the fileprovider stuff
    // and just write the file after we've done everything with the bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestNum && resultCode == Activity.RESULT_OK && photoFile != null) {
            // Note: be sure to use the support version (android.support.media.ExifInterface)
            val ei = ExifInterface(photoFile!!.path)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            Log.d("Markus", String.format("orientation: %s", orientation))

            // See answers to https://stackoverflow.com/q/14066038/432311
            val bitmap = BitmapFactory.decodeFile(photoFile!!.path)
            val rotateAngle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
            if (rotateAngle != 0f) {
                val newBitmap = rotateImage(bitmap, rotateAngle)
                photoView?.setImageBitmap(newBitmap)
            } else {
                photoView?.setImageURI(FileProvider.getUriForFile(this,
                        "me.spiffylogic.wardrobeshuffle.fileprovider", photoFile))
            }

        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        Log.d("Markus", String.format("Rotating %f degrees", angle))
        val matrix = Matrix()
        matrix.postRotate(angle)
        val rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
        source.recycle()
        return rotatedBitmap
    }

    // TODO: consider incorporating this, see https://developer.android.com/training/camera/photobasics.html#TaskScalePhoto
    private fun setPic() {
        val mImageView = photoView
        val mCurrentPhotoPath = photoFile?.path
        if (mImageView is ImageView && mCurrentPhotoPath is String) {
            // Get the dimensions of the View
            val targetW = mImageView.getWidth()
            val targetH = mImageView.getHeight()

            // Get the dimensions of the bitmap
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor
            bmOptions.inPurgeable = true

            val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
            mImageView.setImageBitmap(bitmap)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create a unique image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + ".jpg"
        val photosDir = File(filesDir, "photos/")
        val imageFile = File(photosDir, imageFileName)

        // Make sure the containing directory exists!
        // https://stackoverflow.com/a/26386970/432311
        if (!imageFile.parentFile.exists()) {
            imageFile.parentFile.mkdirs()
        }

        Log.d("Markus", "Image file created: " + imageFile.getAbsolutePath())
        return imageFile
    }
}
