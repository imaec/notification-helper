package com.imaec.notificationhelper.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream


class Utils {

    companion object {
        fun getAppName(context: Context, packageName: String): String {
            val pkgMgr = context.packageManager
            val listApps: List<ResolveInfo>

            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            listApps = pkgMgr.queryIntentActivities(intent, 0)

            for (app in listApps) {
                if (app.activityInfo.packageName == packageName) {
                    return app.activityInfo.loadLabel(pkgMgr).toString()
                }
            }

            return ""
        }

        fun getAppIcon(context: Context, packageName: String?): Drawable? {
            return try {
                context.packageManager.getApplicationIcon(packageName ?: "")
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }

        fun getByteArray(bitmap: Bitmap?): ByteArray? {
            if (bitmap == null) return null

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        fun getBitmap(byteArray: ByteArray?): Bitmap? {
            if (byteArray == null) return null

            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun getBitmap(context: Context, icon: Icon?): Bitmap? {
            icon?.let {
                return getBitmap(it.loadDrawable(context))
            } ?: return null
        }

        fun getBitmap(drawable: Drawable): Bitmap? {
            if (drawable is BitmapDrawable) {
                drawable.bitmap?.let {
                    return it
                }
            }

            val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                Bitmap.createBitmap(
                    1,
                    1,
                    Bitmap.Config.ARGB_8888
                ) // Single color bitmap will be created of 1x1 pixel
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
}