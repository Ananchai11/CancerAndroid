package com.rmutto.cancerandroid

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.os.StrictMode
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class Cancer1Activity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")

    lateinit var editTextAge: EditText
    lateinit var SpinGender: Spinner
    lateinit var editTextBMI: EditText
    lateinit var SpinSmoke: Spinner
    lateinit var SpinHis: Spinner
    lateinit var editTextPhy: EditText
    lateinit var editTextAlco: EditText
    lateinit var SpinGene: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cancer1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        editTextAge = findViewById(R.id.editTextAge)
        SpinGender = findViewById(R.id.spinGender)
        editTextBMI = findViewById(R.id.editTextBMI)
        SpinSmoke = findViewById(R.id.spinSmoke)
        SpinHis = findViewById(R.id.spinHis)
        editTextPhy = findViewById(R.id.editTextPhy)
        editTextAlco = findViewById(R.id.editTextAlco)
        SpinGene = findViewById(R.id.spinGene)

        val adaptergender = ArrayAdapter.createFromResource(
            this,
            R.array.gender,  // ใส่ชื่อของ array ที่ต้องการใช้
            android.R.layout.simple_spinner_item
        )
        adaptergender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinGender.setAdapter(adaptergender);

        val adaptersmoke = ArrayAdapter.createFromResource(
            this,
            R.array.smoke,  // ใส่ชื่อของ array ที่ต้องการใช้
            android.R.layout.simple_spinner_item
        )
        adaptersmoke.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinSmoke.setAdapter(adaptersmoke);

        val adapterhis = ArrayAdapter.createFromResource(
            this,
            R.array.his,  // ใส่ชื่อของ array ที่ต้องการใช้
            android.R.layout.simple_spinner_item
        )
        adapterhis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinHis.setAdapter(adapterhis);

        val adaptergene = ArrayAdapter.createFromResource(
            this,
            R.array.gene,  // ใส่ชื่อของ array ที่ต้องการใช้
            android.R.layout.simple_spinner_item
        )
        adaptergene.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinGene.setAdapter(adaptergene);


        val ButtonGo = findViewById<Button>(R.id.buttonGo)

        ButtonGo.setOnClickListener {
            val url: String = getString(R.string.root_url)

            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()
                .add("age", editTextAge.text.toString())
                .add("gender", SpinGender.selectedItemId.toString())
                .add("bmi", editTextBMI.text.toString())
                .add("smoking", SpinSmoke.selectedItemId.toString())
                .add("geneticrisk", SpinGene.selectedItemId.toString())
                .add("physicalactivity", editTextPhy.text.toString())
                .add("alcoholIntake", editTextAlco.text.toString())
                .add("cancerhistory", SpinHis.selectedItemId.toString())
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val data = JSONObject(response.body!!.string())
                if (data.length() > 0) {
                    val prediction = data.getString("prediction")
                    val message = "ผลการวิเคราะห์ คือ $prediction "
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("การตรวจโรคมะเร็ง")
                    builder.setMessage(message)
                    builder.setNeutralButton("OK", clearText())
                    val alert = builder.create()
                    alert.show()

                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "ไม่สามารถเชื่อต่อกับเซิร์ฟเวอร์ได้",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun clearText(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialog, which ->
            editTextAge.text.clear()
            SpinGender.setSelection(0)
            editTextBMI.text.clear()
            SpinSmoke.setSelection(0)
            SpinHis.setSelection(0)
            editTextPhy.text.clear()
            editTextAlco.text.clear()
            SpinGene.setSelection(0)
        }

    }
}