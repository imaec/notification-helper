package com.imaec.notificationhelper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
                // Log.d("app :::: ", "${app.activityInfo.loadLabel(pkgMgr)} / ${app.activityInfo.packageName}")
                if (app.activityInfo.packageName == packageName) {
                    return app.activityInfo.loadLabel(pkgMgr).toString()
                }
            }

            return ""
        }

        fun getAppIcon(context: Context, packageName: String): Drawable? {
            return try {
                context.packageManager.getApplicationIcon(packageName)
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
    }
}