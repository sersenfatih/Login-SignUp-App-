package com.sersenfatih.login_signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.password

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance()
        val kullanıcı =auth.currentUser
        val verification =auth.currentUser?.isEmailVerified
        if (verification==true){
            if (kullanıcı!=null){
                val intent=Intent(this,HomeScreen::class.java)
                startActivity(intent)
                finish()
            }
        }



    }
    fun LoginC(view:View){

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this,"E-mail or password can not be empty.", Toast.LENGTH_SHORT).show()

        }
        else{auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                val verification =auth.currentUser?.isEmailVerified
                if (verification==true){
                        Toast.makeText(this,"Login Successful", Toast.LENGTH_LONG).show()
                    val intent=Intent(this,HomeScreen::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this,"Please verify your e-mail", Toast.LENGTH_LONG).show()
                }

            }
        }.addOnFailureListener{
            Toast.makeText(this,"E-mail or password is incorrect", Toast.LENGTH_LONG).show()
        }}
    }
    fun SignUpC(view: View){
        val mail = email.text.toString()
        val password = password.text.toString()
        if (password == null || mail==null ) {
            Toast.makeText(this,"E-mail or password can not be empty",Toast.LENGTH_SHORT).show()

        } else {

            auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (it.isSuccessful) Toast.makeText(
                            this,
                            "Thanks for signing up.Please verify your e-mail",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                val verification =auth.currentUser?.isEmailVerified
                if(password.length<6) {Toast.makeText(applicationContext, "Your password must be at least 6 characters long.", Toast.LENGTH_LONG).show()}
                else if(verification==false) {Toast.makeText(applicationContext, "Your e-mail is already registered.", Toast.LENGTH_LONG).show()}
                else {Toast.makeText(applicationContext, "Your e-mail is already registered.", Toast.LENGTH_LONG).show()}

            }
        }
    }
    fun ForgetPass(view: View){
        password.isVisible=false
        loginBTN.text="Send Reset Link"
        ForgotPassword.isVisible=false
        SignUpBTN.isVisible=false
        loginBTN.setOnClickListener {
            auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
                Toast.makeText(this, "We've sent you an email. Just follow the instructions to reset your password.", Toast.LENGTH_SHORT).show()
                val intent =Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

