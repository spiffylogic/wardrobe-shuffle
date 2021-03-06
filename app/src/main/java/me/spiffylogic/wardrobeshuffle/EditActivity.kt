package me.spiffylogic.wardrobeshuffle

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.os.Build
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_edit.*
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper

// Reference: https://developer.android.com/training/camera/photobasics.html

// This activity should be used for
// 1) creating a new item
// 2) editing existing one (e.g. retake photo)
// 3) deleting an item

// TODO: don't do SQL on main thread, use Loaders.
// See https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832

class EditActivity : AppCompatActivity() {
    companion object {
        val ITEM_KEY = "ITEM_KEY"
    }

    // TODO: eliminate the redudancy with how we store these pieces of info
    private var itemId = -1
    private val dbHelper: WardrobeDbHelper by lazy { WardrobeDbHelper(this) }
    private var photoFile: File? = null
    private var requestNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        itemId = intent.getIntExtra(ITEM_KEY, -1)

        val item = dbHelper.getItem(itemId)
        if (item != null) {
            if (item.imagePath != "") {
                photoFile = File(item.imagePath)
                photoFile?.let { Util.setImageFromFile(it, photo_view) }
            }
            desc_text.setText(item.description)

            // https://stackoverflow.com/a/5270292/432311
            val date = dbHelper.getLastWornDate(itemId)
            date?.let { last_worn_date.text = SimpleDateFormat("EEEE, MMM d").format(date) }
            // This may be useful later:
            //val c = Calendar.getInstance(); c.time = date
            //val dayOfWeek = c.get(Calendar.DAY_OF_WEEK) // day of week as int (Sunday = 1, Monday = 2, etc.)
        }
        if (last_worn_date.text.isBlank()) {
            last_worn_label.visibility = View.GONE
            last_worn_date.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    fun cameraButtonTapped(v: View) {
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

    fun saveButtonTapped(v: View) {
        val path = photoFile?.absolutePath ?: ""
        val desc = desc_text.text.toString()
        if (itemId < 0) dbHelper.insertItem(path, desc)
        else dbHelper.updateItem(itemId, path, desc)
        finish()
    }

    fun deleteButtonTapped(v: View) {
        if (itemId >= 0) dbHelper.deleteItem(itemId) // TODO: prompt "are you sure?"
        finish()
    }

    // Note: by specifying EXTRA_OUTPUT we told it where to put the data, so data will be null here, hence the ?
    // TODO: Since it seems we need to work with the bitmap directly anyway, consider skipping the fileprovider stuff
    // and just write the file after we've done everything with the bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestNum) {
            photoFile?.let {
                if (resultCode == Activity.RESULT_OK) {
                    Util.setImageFromFile(it, photo_view)
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // clean up that file we created
                    it.delete()
                    photoFile = null
                }
            }
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
