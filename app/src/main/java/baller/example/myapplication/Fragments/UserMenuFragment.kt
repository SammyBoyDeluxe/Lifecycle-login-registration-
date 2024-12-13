package baller.example.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import baller.example.myapplication.Activities.MainActivity
import baller.example.myapplication.R
import baller.example.myapplication.utils.SharedPreferencesDAO
import kotlinx.coroutines.launch

class UserMenuFragment : Fragment() {

    val intentActionNameMainActivity : String = "android.intent.action.MAIN"

    lateinit var mainActivityIntent : Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.logged_in_activity_user_menu, container, false)



        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*Set onClickfunctions for the different buttons*/
        val navController = findNavController()
        /*Initiates the intent on required activity*/
        mainActivityIntent = Intent(intentActionNameMainActivity)
        view.findViewById<Button>(R.id.updateUserInfoMenuButton).setOnClickListener {
            navController.navigate(R.id.action_userMenuFragment_to_signUpFragment2)

        }

        view.findViewById<Button>(R.id.remove_account_button).setOnClickListener {
            /*Indicates that sign out/deletion process has started*/
            showProgressBar(50)
            lifecycleScope.launch {
                /*Removes the user and then logs them out*/
                SharedPreferencesDAO.deleteAccount()
                SharedPreferencesDAO.logOutUser()
            }.invokeOnCompletion {
                showProgressBar(100)
                if (it == null) {
                    hideProgressBar()
                    startActivity(mainActivityIntent)
                    /*Navigates to main menu and pops the entire backstack including the usermenu-fragment entry (popupto + popupinlusive)*/
                } else {
                    hideProgressBar()
                    notifyOfInternalFailure(it)
                }
            }

//            val menuToMainActivityIntent : Intent = Intent(activity,MainActivity::class.java)
//            startActivity(menuToMainActivityIntent)


        }

        view.findViewById<Button>(R.id.sign_out_button).setOnClickListener {
        /*Removes the logged_in_user entry from the SharedPreferences*/
            showProgressBar(50)
            lifecycleScope.launch {

                SharedPreferencesDAO.logOutUser()

            }.invokeOnCompletion {  /*PopsuptoInclusive the current fragment*/
                startActivity(mainActivityIntent)
                showProgressBar(100)
                hideProgressBar()
 }



        }
    }

    /** Notifies user of internal failure in invokeOnCompletion after suspend-functions
     *
     */
    private fun notifyOfInternalFailure(it: Throwable) {
        showToast("Internal Failure : " + it.message.toString(), true)
    }

    /**Shows a toast in the current context with the designated String and using a
     * long toast-message-time if isLongTimed == true, short otherwise
     */
    private fun showToast(toastString : String, isLongTimed : Boolean) {
        if(isLongTimed)Toast.makeText(context,toastString,Toast.LENGTH_LONG).show()
            else Toast.makeText(context,toastString,Toast.LENGTH_SHORT).show()
    }

    /**Shows the progressbar with its associated box (ConstraintLayout) and TextView
     * (if not already showing) and updates the progress to the given progress (out of a hundred) given as a parameter
     *   @param progressOutOf100
     */
    private fun showProgressBar(progressOutOf100 : Int) : Unit{
        val progressBar : ProgressBar? = view?.findViewById(R.id.progressBarUserMenuFragment)
        /*The progressBarBox is the box that holds  the loading-text text view as well as the progress bar and provides a background*/
        val progressBarBox : ConstraintLayout? =
            view?.findViewById(R.id.progressBarBoxUserMenuFragment)
        /*Progress to default : (x/100)*/
        /*Makes sure that we don´t update isGone too many times*/
        if(progressBarBox?.isGone == true) progressBarBox.isGone = false
       // progressBar?.setProgress(progressOutOf100,true)


    }

    /**Hides the progressbar with its associated box (ConstraintLayout) and TextView
     *  and sets the progress to zero for the next usage
     *
     */
    private fun hideProgressBar():Unit{
        val progressBar : ProgressBar? = view?.findViewById(R.id.progressBarUserMenuFragment)
        /*The progressBarBox is the box that holds  the loading-text text view as well as the progress bar and provides a background*/
        val progressBarBox : ConstraintLayout? = view?.findViewById(R.id.progressBarBoxUserMenuFragment)

        progressBar?.setProgress(0,true)
        /*Progress to default : (x/100)*/
        if(progressBarBox?.isGone == false) progressBarBox.isGone = true


    }


}