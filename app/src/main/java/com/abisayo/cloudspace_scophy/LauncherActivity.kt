package com.abisayo.cloudspace_scophy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abisayo.cloudspace_scophy.databinding.ActivityLauncherBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.util.concurrent.Executor

class LauncherActivity : AppCompatActivity() {
    lateinit var executor:Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this,executor,
            object : BiometricPrompt.AuthenticationCallback(){

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,"Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,"Authentication succeeded", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, AdmissionStatusActivity::class.java)
                    startActivity(intent)

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext,"Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("")
            .setNegativeButtonText("use Fingerprint to Login")
            .setConfirmationRequired(false)
            .build()

        binding.processAdmin.setOnClickListener {
            val intent = Intent(this, ProcessAdmissionActivity::class.java)
            startActivity(intent)
        }




    }

    fun biometricAuth(view: View) {

        biometricPrompt.authenticate(promptInfo)

    }
}