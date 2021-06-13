package com.example.permission

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


private const val LOCATION_CODE = 102
private const val IMAGE_CODE = 103

class MainActivity : AppCompatActivity() {

    private lateinit var camera: Button
    private lateinit var location: Button
//    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera = findViewById(R.id.openCamera)
        location = findViewById(R.id.locationAccess)

        setListener()


    }

    private fun setListener() {
        camera.setOnClickListener {
            //request camera permission
            requestPermission(IMAGE_CODE, Manifest.permission.CAMERA, "camera")
        }

        location.setOnClickListener {
            //request location permission
            requestPermission(LOCATION_CODE, Manifest.permission.ACCESS_FINE_LOCATION, "location")
        }

    }

    private fun requestPermission(requestCode: Int, permission: String, permission_name:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {                     // Check OS Version
            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED ->
                    Log.e("Permissions", "requestPermission: permission granted" )             // if (checkSelfPermission(Manifest.permission.CAMERA/LOCATION) == PackageManager.PERMISSION_GRANTED)

                shouldShowRequestPermissionRationale(permission) -> showDialog(permission_name, permission, requestCode)                                        // to explain why they need a permission, if permission denied
                else -> {
                    val getPermission = arrayOf(permission)
                    ActivityCompat.requestPermissions(this, getPermission, requestCode)
                    Log.e("Permissions", "requestPermission: permission denied" )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun check() {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission refused!", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "permission granted!", Toast.LENGTH_SHORT).show()
        }

        when(requestCode){
            LOCATION_CODE -> check()
            IMAGE_CODE -> check()
        }

    }

    fun showDialog(permission_name:String, permission: String, requestCode: Int){
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setMessage("Permission to access your $permission_name is required to this app")
            setPositiveButton("OK"){_,_ ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }.create().show()
    }

}

/*
val resultLauncher = registerForActivityResult(                     // startActivityForResult
ActivityResultContracts.StartActivityForResult()){              // {} -> listener
result ->
if (result.resultCode == Activity.RESULT_OK){
val data = result.data
Log.e("TAG", "resultLauncher:")
}
}
*/

