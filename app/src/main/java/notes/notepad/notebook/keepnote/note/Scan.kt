package com.arnd.airdoc

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import notes.notepad.notebook.keepnote.note.Add_New_Document
import notes.notepad.notebook.keepnote.note.home_screen


class Scan : AppCompatActivity() {


    lateinit var imageView: ImageView

    lateinit var editText: EditText

    lateinit var bttn: Button
    lateinit var slt: Button
    lateinit var dtc: Button
    lateinit var scrlv: ScrollView
    lateinit var svbtn: Button
    lateinit var back: ImageButton

    private lateinit var txtv: TextView
    lateinit var txtr: TextView
    private val RECORD_REQUEST_CODE = 101
    private val TAG = "PermissionDemo"
    lateinit var drwimg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)



        imageView = findViewById(R.id.imageView)
        drwimg = findViewById(R.id.scan1_animation)

        editText = findViewById(R.id.editText)
        scrlv = findViewById(R.id.scview)


        bttn = findViewById(R.id.bttn)
        svbtn = findViewById(R.id.svbtn)
        back = findViewById(R.id.top_txt)

        txtv = findViewById(R.id.txtv)
        txtr = findViewById(R.id.txtr)


        slt = findViewById(R.id.slt)
        dtc = findViewById(R.id.dtc)

        svbtn.setOnClickListener() {
            val intent = Intent(this, Add_New_Document::class.java)
            intent.putExtra("TextBox", editText.text.toString())
            startActivity(intent)

        }

        setupPermissions()


    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val a = Intent(Intent.ACTION_MAIN)
                    a.addCategory(Intent.CATEGORY_HOME)
                    a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(a)

                    Toast.makeText(this, "Give Storage Permission", Toast.LENGTH_LONG).show()
                    slt.visibility = View.GONE


                } else {
                    slt.visibility = View.VISIBLE

                }
            }
        }
    }


    fun selectImage(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(data!!.data)
            drwimg.visibility = View.GONE
        }
    }

    fun startRecognizing(v: View) {
        if (imageView.drawable != null) {
            editText.setText("")
            v.isEnabled = false
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(image)
                    .addOnSuccessListener { firebaseVisionText ->
                        v.isEnabled = true
                        processResultText(firebaseVisionText)
                        //Toast.makeText(this, "Detected", Toast.LENGTH_LONG).show()
                        editText.visibility = View.VISIBLE
                        bttn.visibility = View.VISIBLE
                        txtv.visibility = View.GONE
                        txtr.visibility = View.VISIBLE
                        svbtn.visibility = View.VISIBLE
                        scrlv.visibility = View.VISIBLE
                        back.visibility = View.GONE
                        slt.visibility = View.GONE
                        dtc.visibility = View.GONE
                        imageView.visibility = View.GONE


                    }
                    .addOnFailureListener {
                        v.isEnabled = true
                        editText.setText("Failed")
                    }
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG).show()
        }

    }


    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            editText.setText("No Text Found")
            return
        }
        for (block in resultText.textBlocks) {
            val blockText = block.text
            editText.append(blockText + "\n")
        }
    }

    fun again(view: View) {

        val intent = Intent(this, Scan::class.java)
        startActivity(intent)

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        val darkflag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (darkflag == Configuration.UI_MODE_NIGHT_YES) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    fun scn_back(view: View) {

        val intent = Intent(this, home_screen::class.java)
        startActivity(intent)

    }
    fun scn_back2(view: View) {

        val intent = Intent(this, home_screen::class.java)
        startActivity(intent)
    }


}
