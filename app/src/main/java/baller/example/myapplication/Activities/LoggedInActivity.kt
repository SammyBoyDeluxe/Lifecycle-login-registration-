package baller.example.myapplication.Activities


import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import baller.example.myapplication.R


class LoggedInActivity : AppCompatActivity() {

    val TAG: String= "Samuel"
    override fun onStart() {
        super.onStart()
        Log.d("Samuel", "onStart: ")

    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return super.onCreateView(parent, name, context, attrs)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Samuel", "onCreate: ")

        setContentView(baller.example.myapplication.R.layout.loggedinactivity)

        val navHostFragment = supportFragmentManager.findFragmentById(baller.example.myapplication.R.id.nav_host_fragment_loggedInActivity) as NavHostFragment?

        Log.d(TAG, navHostFragment.toString())

//        val navController : NavController = navHostFragment?.navController!!
        Log.d("Samuel", "onCreate: 2")

    }

    override fun onStop() {

        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Samuel", "destroy: ")

    }

    override fun onRestart() {

        super.onRestart()
    }




}