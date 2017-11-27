package me.spiffylogic.wardrobeshuffle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.support.media.ExifInterface
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.ImageView
import java.io.File
import java.io.FileNotFoundException

class Util {
    companion object {
        fun setImageFromFile(imageFile: File, imageView: ImageView) {
            // Note: be sure to use the support version (android.support.media.ExifInterface)
            val ei: ExifInterface
            try {
                ei = ExifInterface(imageFile.path)
            } catch (ex: FileNotFoundException) {
                ex.printStackTrace()
                return
            }
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            Log.d("Markus", String.format("orientation: %s", orientation))

            // See answers to https://stackoverflow.com/q/14066038/432311
            val bitmap = BitmapFactory.decodeFile(imageFile.path)
            val rotateAngle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
            if (rotateAngle != 0f) {
                val newBitmap = rotateImage(bitmap, rotateAngle)
                imageView.setImageBitmap(newBitmap)
            } else {
                imageView.setImageBitmap(bitmap)
//                imageView.setImageURI(FileProvider.getUriForFile(context,
//                        "me.spiffylogic.wardrobeshuffle.fileprovider", imageFile))
            }
        }

        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            Log.d("Markus", String.format("Rotating %f degrees", angle))
            val matrix = Matrix()
            matrix.postRotate(angle)
            val rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                    matrix, true)
            source.recycle()
            return rotatedBitmap
        }

        // TODO: consider incorporating this
        // See https://developer.android.com/training/camera/photobasics.html#TaskScalePhoto
        private fun setPic(photoPath: String, imageView: ImageView) {
            // Get the dimensions of the View
            val targetW = imageView.width
            val targetH = imageView.height

            // Get the dimensions of the bitmap
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(photoPath, bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor
            bmOptions.inPurgeable = true

            val bitmap = BitmapFactory.decodeFile(photoPath, bmOptions)
            imageView.setImageBitmap(bitmap)
        }
    }

}