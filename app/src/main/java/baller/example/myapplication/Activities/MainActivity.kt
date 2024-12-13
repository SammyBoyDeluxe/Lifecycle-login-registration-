package baller.example.myapplication.Activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import baller.example.myapplication.Data.User
import baller.example.myapplication.R
import baller.example.myapplication.utils.SharedPreferencesDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextKey

class MainActivity : AppCompatActivity() {


    var navHostFragmentLoginRegistration: NavHostFragment? = supportFragmentManager
        .findFragmentById(R.id.authorization_fragment_container) as? NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*In the application Shared Preferences is our usable persistence layer. This
//        * will be coupled with the context based bundle-based onRestore and onSave-overrides to assert
//        * UI-persistence across sessions/configuration changes.
//        * Logged in user data and register user data will both be handled as entries of the user class
//        * input as a JSONstring under the key of the username of the user   */
        val sharedPreferences: SharedPreferences? = applicationContext.getSharedPreferences(
            "baller_example_lifecycle_mobilutveckling_sharedpreferences_KEY",
            MODE_PRIVATE
        )
        /*Sets the sharedPreference-object to the sharedPreference-file of the application*/
        SharedPreferencesDAO.sharedPreferences = sharedPreferences

        setContentView(R.layout.activity_main)


        // Set up NavHostFragments for both header and main navigation





        // Edge-to-Edge Display Compatibility
        enableEdgeToEdge()

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onRestart() {
        this.onRestoreInstanceState(bundleOf())
        super.onRestart()
    }



    override fun onStart() {
        super.onStart()


        // Ensure that navHostFragmentLoginRegistration and NavController are not null
        var navController: NavController? = navHostFragmentLoginRegistration?.navController
        // Access ToggleButton from the main layout instead of from navHostFragmentLoginRegistration.view


//        /*Sets click listener for login button to pass data to logic layer from input*/
//        findViewById<Button>(R.id.login_button).setOnClickListener {
//            /*Reads the login fragment and tests it against the database, null returned upon failure*/
//          var loggedInUser : User? = readLoginFragment()
//            val loggedInActivityIntent : Intent = Intent(this, LoggedInActivity::class.java)
//
//            if(loggedInUser != null){
//                startActivity(loggedInActivityIntent)
//
//            }
//            else{
//
//                Toast.makeText(this,"Sorry, the user-credentials did not exist in our database",
//                    Toast.LENGTH_LONG).show()
//            }
//        }
    }

//    /** Reads, validates and performs sign up if all appropriate values are present
//     *
//     */
//    fun readSignUpFragmentValues(): User? {
//        val signUpFragment = this.findViewById<ConstraintLayout>(R.id.signup_form_box)
//
//        val usernameInput: EditText = signUpFragment.findViewById<EditText>(R.id.username_input)
//        val passwordInput: EditText = signUpFragment.findViewById<EditText>(R.id.password_input)
//        val cokeInput: EditText = signUpFragment.findViewById<EditText>(R.id.kgs_of_cocaine_input)
//        val footNinjaInput: EditText =
//            signUpFragment.findViewById<EditText>(R.id.foot_ninjas_suplex_input)
//        val selfDescribedBallerInput: Switch =
//            signUpFragment.findViewById<Switch>(R.id.self_described_baller_switch)
//        val timeSinceLastBulletTimeInput: EditText =
//            signUpFragment.findViewById<EditText>(R.id.bullet_time_spinner)
//        /*If any input is blank, whether in updateUserData or in signUpUser we should return a false to indicate the values were not complete
//        * and the user should be asked to retry input*/
//        if (usernameInput.text.trim().isEmpty() || passwordInput.text.trim()
//                .isEmpty() || cokeInput.text.trim().isEmpty()
//            || footNinjaInput.text.trim().isEmpty() || timeSinceLastBulletTimeInput.text.trim()
//                .isEmpty()
//        ) {
//
//            return null
//        } else {
//
//
//            var cokeInputString : String = cokeInput.text.toString()
//            var footNinjaCountString : String = footNinjaInput.text.toString()
//            var signedUpUser :  User? = null
//
//            /**Internal callbackfunction to return the signedupUser on completed job
//             *
//             */
//            private fun completeFunction() : User?{
//
//                return signedUpUser
//            }
//            lifecycleScope.launch(){  signedUpUser  =  SharedPreferencesDAO.signUpUser(
//                usernameInput.text.toString(),
//                passwordInput.text.toString(),
//                cokeInputString.toDouble(),
//                footNinjaCountString.toInt(),
//                selfDescribedBallerInput.isActivated,
//                BulletTimeEnum.valueOf(timeSinceLastBulletTimeInput.text.toString())
//            )
//                }.invokeOnCompletion {
//                    if(it == null){
//
//                        completeFunction()
//                    }
//            }
//
//
//
//        }
//
//    }
//
//    /** Reads, validates and performs login if all appropriate values are present
//     *
//     * returns null on fail, the associated user-instance on match with username and password
//     */
//    fun readLoginFragment(): User? {
//        val loginFragment = this.findViewById<ConstraintLayout>(R.id.login_form_box)
//
//        val usernameInput: EditText = loginFragment.findViewById<EditText>(R.id.username_input)
//        val passwordInput: EditText = loginFragment.findViewById<EditText>(R.id.password_input)
//
//        /*If any input is blank, whether in updateUserData or in signUpUser we should return a false to indicate the values were not complete
//        * and the user should be asked to retry input*/
//        if (usernameInput.text.trim().isEmpty() || passwordInput.text.trim()
//                .isEmpty()
//        ) {
//
//            return null
//        } else {
//            val loggedInUser : User? =  SharedPreferencesDAO.loginUser(
//                usernameInput.text.toString(),
//                passwordInput.text.toString())
//            return loggedInUser
//
//
//        }
//
//    }

    override fun onDestroy() {
        super.onDestroy()
    }

}