package com.example.room.mvvm.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.room.mvvm.MyApplication
import com.example.room.mvvm.R
import com.example.room.mvvm.model.ApiResponseClass
import com.example.room.mvvm.repository.Webservice
import com.example.room.mvvm.utils.getIMEI
import com.example.room.mvvm.utils.getIMSI
import com.example.room.mvvm.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: Int = 101
    lateinit var loginViewModel: LoginViewModel

    lateinit var context: Context

    var retrofit: Retrofit? = null
        @Inject set


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (getApplication() as MyApplication).getComponent()?.inject(this)

        context = this@MainActivity

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

//        loginAPI()

        btnAddLogin.setOnClickListener {

            val strUsername = txtUsername.text.toString().trim()
            val strPassword = txtPassword.text.toString().trim()

            if (strUsername.isEmpty()) {
                txtUsername.error = "Please enter the username"
            } else if (strPassword.isEmpty()) {
                txtPassword.error = "Please enter the password"
            } else {

                loadIMEI()

            }
        }

        btnReadLogin.setOnClickListener {

            val strUsername = txtRoomUsername.text.toString().trim()

            loginViewModel.getLoginDetails(context, strUsername)!!.observe(this, Observer {

                if (it == null) {
                    lblReadResponse.text = "Data Not Found"
                    lblUseraname.text = "- - -"
                } else {
                    lblUseraname.text = it.Username

                    AlertDialog.Builder(this)
                        .setTitle(it.Username)
                        .setMessage(it.XAcc)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok,
                            DialogInterface.OnClickListener { dialog, which -> //re-request
                            })
                        .show()

                    lblReadResponse.text = "Data Found Successfully"
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun loadIMEI() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission()
        } else {
            // READ_PHONE_STATE permission is already been granted.
            loginAPI()
        }
    }

    private fun requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_PHONE_STATE
            )
        ) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            AlertDialog.Builder(this)
                .setTitle("Permission Request")
                .setMessage("Please allow permission to login")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, which -> //re-request
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_PHONE_STATE),
                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                        )
                    })
                .show()
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_PHONE_STATE),
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                loginAPI()
            } else {
                alertAlert("Please allow permission to login");
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun loginAPI() {

        progress.visibility = View.VISIBLE

        val apiService: Webservice = retrofit!!.create(Webservice::class.java)

        val call: Call<ApiResponseClass> = apiService.login(this.getIMSI(), this.getIMEI(), "", "")

        call.enqueue(object : Callback<ApiResponseClass> {
            override fun onResponse(
                call: Call<ApiResponseClass>,
                response: Response<ApiResponseClass>
            ) {
                progress.visibility = View.GONE
                if (response.isSuccessful) {

                    val res = response.body()

                    if (res?.errorCode?.equals("00") == true) {
                        loginViewModel.insertData(
                            context, res?.user?.userName ?: "", response.headers()
                                .get("X-Acc")
                                ?: ""
                        )
                        lblInsertResponse.text = "Inserted Successfully"
                        Toast.makeText(this@MainActivity, res.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@MainActivity, res?.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponseClass>, t: Throwable) {
                progress.visibility = View.GONE
                t.printStackTrace()
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun alertAlert(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Request")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                loadIMEI()
            }
            .show()
    }
}