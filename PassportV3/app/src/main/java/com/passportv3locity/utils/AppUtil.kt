package com.passportv3locity.utils

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bumptech.glide.Glide
import com.passportv3locity.R
import com.passportv3locity.common.Constants
import okhttp3.*
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt


object AppUtil {
    private var httpClient: OkHttpClient? = null
    private const val SECOND_MILLIS: Long = 1000
    private const val MINUTE_MILLIS: Long = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS: Long = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS: Long = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS: Long = 7 * DAY_MILLIS
    private const val MONTH_MILLIS: Long = 4 * WEEK_MILLIS
    var cal: Calendar = Calendar.getInstance()
    var dialog: Dialog? = null

    fun hideSoftKeyboard(context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            val view = (context as Activity).currentFocus
            if (view != null) {
                inputManager.hideSoftInputFromWindow(
                    view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    fun changeDateToYear(date: String?, format: String): String? { // 2021-12-15T04:57:57.296Z
        var year: String? = null
        try {
            //current date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            dateFormat.setTimeZone(TimeZone.getTimeZone("ITC"));
            val objDate = dateFormat.parse(date)
            //Expected date format
            val dateFormat2 = SimpleDateFormat(format)
            val convertDate = dateFormat2.format(objDate)
            year = convertDate

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return year
    }


    fun changeDateFormatInDash(date: String?): String? { // 2021-12-15T04:57:57.296Z
        var finalDate: String? = null
        try {
            //current date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val objDate = dateFormat.parse(date)

            //Expected date format
            val dateFormat2 = SimpleDateFormat("dd-MM-yyyy")
            val convertDate = dateFormat2.format(objDate)
            finalDate = convertDate

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return finalDate
    }

    // extension function to get bitmap from assets
    fun Context.assetsToBitmap(fileName: String): Bitmap? {
        return try {
            val stream = assets.open(fileName)
            BitmapFactory.decodeStream(stream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // extension function to convert bitmap to base64 string
    fun Bitmap.toBase64String(): String {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 10, this)
            return android.util.Base64.encodeToString(toByteArray(), android.util.Base64.DEFAULT)
        }
    }

    fun isNullOrBlank(string: String?): Boolean {
        return (string == null || string.equals("", ignoreCase = true) || string.equals(
            "null",
            ignoreCase = true
        ))
    }

    fun showLoader(context: Context, imageViewLoader: ImageView) {
        Glide.with(context).load(R.drawable.loader).into(imageViewLoader)
        imageViewLoader.visibility = View.VISIBLE
    }

    fun hideLoader(imageViewLoader: ImageView) {
        imageViewLoader.visibility = View.GONE
    }

    fun hideLoader(window: Window, colorRes: Color, context: Context) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor(colorRes.toString())
            //  window.statusBarColor = ContextCompat.getColor(context,colorRes)
        }
    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    // use this to change the link color
                    textPaint.color = textPaint.linkColor
                    // toggle below value to enable/disable
                    // the underline shown below the clickable text
                    //   textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
            if (startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
            spannableString.setSpan(
                clickableSpan,
                startIndexOfLink,
                startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }


    fun showShortMsg(context: Context, message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        ).show()
    }

    fun noInternetMessage(context: Context) {
        showShortMsg(context, context.getString(R.string.no_internet))
    }

    fun getErrorMessage(value: String): String {
        //val mainObject = JSONObject(value)
        return try {
            val mainObject = JSONObject(value)
            mainObject.getString("message")
        } catch (e: Exception) {
            println(e)
            "Internal server error"
        }
        //val msg = mainObject.getString("message")
        //return msg
    }

    fun convertDateForServerTwo(date: String, dateFormat: String): String {
        var spf = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
        val newDate: Date = spf.parse(date)
        spf = SimpleDateFormat(dateFormat)
        var updateDate = spf.format(newDate);
        return updateDate
    }

    fun changeDate(date: String, dateFormat: String): String {
        var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())
        val newDate: Date = spf.parse(date)
        spf = SimpleDateFormat(dateFormat)
        var updateDate = spf.format(newDate);
        return updateDate
    }


    fun timeFormat(date: String): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormatter.setTimeZone(TimeZone.getTimeZone("ITC"));
        val date: Date = dateFormatter.parse(date)
        val timeFormatter = SimpleDateFormat("h:mm a")
        val displayValue: String = timeFormatter.format(date)
        return displayValue
    }

    fun getNextDate(date: String, toNextDate: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())
        val myDate: Date = dateFormat.parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = myDate
        calendar.add(Calendar.DAY_OF_YEAR, toNextDate)
        val newDate = calendar.time
        val newDateTwo = dateFormat.format(newDate)
        return changeDate(newDateTwo.toString(), "dd-MM-yyyy")
    }

    fun isNullOrBlank(obj: Any?): Boolean {
        return if (obj is String || obj is CharSequence) {
            if (obj is String) {
                val string = obj
                return string.equals(
                    Constants.BLANK_STRING, ignoreCase = true
                ) || string.equals("null", ignoreCase = true)
            }
            val charSequence = obj as CharSequence
            charSequence.toString()
                .equals(Constants.BLANK_STRING, ignoreCase = true) || charSequence.toString()
                .equals("null", ignoreCase = true)
        } else {
            obj == null
        }
    }


    fun saveMediaToStorage(bitmap: Bitmap, context: Context) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(context, "QR has downloaded", Toast.LENGTH_SHORT).show()
        }
    }

    fun uriToBitmap(selectedFileUri: Uri, context: Context): Bitmap? {
        try {
            val parcelFileDescriptor =
                context.contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun layoutStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            activity.window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val decor: View = activity.window.getDecorView()
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            @Suppress("DEPRECATION") activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun isTimeAutomatic(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(activity.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
        } else {
            Settings.System.getInt(activity.contentResolver, Settings.System.AUTO_TIME, 0) == 1
        }
    }

    fun roundToIntValue(value: Double): Int {
        val x = value
        val y: Int = x.roundToInt()
        return y
    }

    private fun checkDateRange(currentDate:String,notificationDate:String,duration:String) :Boolean
    {
        var difference = ""
        if (duration.equals("Weekly"))
        {
            //difference = getNextDateTwo(notificationDate,7)
            if (difference >= notificationDate && difference <= currentDate)
            {
                Log.d("TAG", "checkDateRange: not-valid")
                return false
            }
        }

        return true
    }
}