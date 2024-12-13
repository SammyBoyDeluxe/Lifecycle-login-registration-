package baller.example.myapplication.Fragments



// LoginFragment.kt
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import baller.example.myapplication.Activities.LoggedInActivity
import baller.example.myapplication.Data.FragmentDataSet
import baller.example.myapplication.R
import baller.example.myapplication.utils.SharedPreferencesDAO
import kotlinx.coroutines.launch

class LoginInputFragment : Fragment() {
    /*To start the next activity*/
    val actionNameLoggedInActivity : String = "action.APP_ACTION_TO_LOGGED_IN_ACTIVITY"
  lateinit var loggedInActivityIntent : Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_input_fragment, container, false)

        Log.d("Samuel", "onCreate: login")


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loggedInActivityIntent = Intent(requireContext(),LoggedInActivity::class.java)
        /*On entry to the screen we retrieve any data that might have been entered before and set the
        * textfields to that dataset */
        var usernameInput: EditText = view.findViewById<EditText>(R.id.username_input)
        var passwordInput: EditText = view.findViewById<EditText>(R.id.password_input)
        showProgressBar(0)
        var fragmentDataSet : FragmentDataSet? = null
        lifecycleScope.launch{
             fragmentDataSet   = SharedPreferencesDAO.retrieveDataSet(true)
            showProgressBar(50)
        }.invokeOnCompletion {
            /*The throwable will be null on completion, an exception upon cancelation*/
            if(it == null){
                showProgressBar(100)

                /*We only sey  data from the fragmentDataSet if it exists*/
                    if (fragmentDataSet != null) {
                        setLoginFragmentDataSet(usernameInput, fragmentDataSet, passwordInput)
                        hideProgressBar()
                    }
                else{
                    /*Upon running but not finding anything we hide the progressbar */
                    showProgressBar(100)
                        hideProgressBar()

                    }



        }

        }
        view.findViewById<Button>(R.id.toggleButtonLogin).setOnClickListener({
            showProgressBar(0)
            /*Before exiting the fragment we save the data*/
            lifecycleScope.launch() {
                showProgressBar(50)
                SharedPreferencesDAO.saveDataSet(
                    true,
                    usernameInput.text.toString(),
                    passwordInput.text.toString(),
                    null,
                    null,
                    null,
                    null
                )
                    showProgressBar(100)
                    showToast("The input data was successfully saved to the FragmentDataSet",false)
                    findNavController().navigate(R.id.action_loginInputFragment_to_signupfragment)
                    hideProgressBar()
                }


            })

        Log.d("Samuel", "button: ")
        view.findViewById<Button>(R.id.login_button).setOnClickListener({
            if(validateLoginAttributes(usernameInput, passwordInput)) {
                Log.d("Samuel", "validerad cred: ")

                lifecycleScope.launch {
                    try {
                        val user = SharedPreferencesDAO.loginUser(usernameInput.text.toString(), passwordInput.text.toString())
                        if (user != null){
                            Log.d("Samuel", "try login: ")

                            startActivity(loggedInActivityIntent)
                            Log.d("Samuel", "logged in via activity: ")

                        }
                    } catch (e: Exception) {
                        showToast(e.message.toString(),true)
                    }
                }


                /*if login is successful we should change to the LoggedInActivity*/
                //findNavController().navigate(R.id.action_signUpFragment_to_logged_in_activity)

            }
            /*Notifies user of wrongful input*/
            else notifyOfInvalidInput()

        })






    }


    /**Projects a text that prompts user to redo the login-input
     *
     */
    private fun notifyOfInvalidInput() {
        Toast.makeText(
            context, "Please enter valid login-information, try again",
            Toast.LENGTH_LONG
        ).show()
    }

    /** Sets the input to the last input of the LoginFragment - Saves on navigating to the other
     * fragment, clears on login
     *
     */
    private fun setLoginFragmentDataSet(
        usernameInput: EditText,
        fragmentDataSet: FragmentDataSet?,
        passwordInput: EditText
    ) {
        usernameInput.setText(fragmentDataSet?.username!!.toString())
        passwordInput.setText(fragmentDataSet?.password!!.toString())
    }

    /**
     * Validates the sign-up attributes to ensure all required fields are filled.
     * If any input is blank, the function will return false, indicating invalid input.
     *
     * @param usernameInput The EditText for the username input.
     * @param passwordInput The EditText for the password input.
     *
     * @return Boolean indicating whether all attributes are valid.
     */
    private  fun validateLoginAttributes(
        usernameInput: EditText,
        passwordInput: EditText,
    ): Boolean {
        // Check if any of the fields are blank or empty
        if (usernameInput.text.trim().isEmpty() ||
            passwordInput.text.trim().isEmpty()
        ) {
            /* Return false if any field is incomplete*/
            return false
        } else {
            return true
        }
    }

    /**Shows a toast with the specified string in the fragment
     * @param showString : String to be shown in Toast
     * @param isLong : true if length should be set to Toast.LENGTH_LONG, false if short
     */
    private fun showToast(showString : String, isLong : Boolean){
        if(isLong)
        Toast.makeText(context,showString, Toast.LENGTH_LONG).show()
        else Toast.makeText(context,showString,Toast.LENGTH_SHORT).show()
    }

    /**Shows the progressbar with its associated box (ConstraintLayout) and TextView
     * (if not already showing) and updates the progress to the given progress (out of a hundred) given as a parameter
     *   @param progressOutOf100
     */
    private fun showProgressBar(progressOutOf100 : Int) : Unit{
        val progressBar : ProgressBar? = view?.findViewById(R.id.progressBarLoginFragment)
        /*The progressBarBox is the box that holds  the loading-text text view as well as the progress bar and provides a background*/
        val progressBarBox : ConstraintLayout? =
            view?.findViewById(R.id.progressBarBoxLoginInputFragment)
        /*Progress to default : (x/100)*/
        /*Makes sure that we don´t update isGone too many times*/
        if(progressBarBox?.isGone == true) progressBarBox.isGone = false
        progressBar?.setProgress(progressOutOf100,true)


    }

    /**Hides the progressbar with its associated box (ConstraintLayout) and TextView
     *  and sets the progress to zero for the next usage
     *
     */
    private fun hideProgressBar():Unit{
        val progressBar : ProgressBar? = view?.findViewById(R.id.progressBarLoginFragment)
        /*The progressBarBox is the box that holds  the loading-text text view as well as the progress bar and provides a background*/
        val progressBarBox : ConstraintLayout? = view?.findViewById(R.id.progressBarBoxLoginInputFragment)
        progressBar?.setProgress(0,true)
        /*Progress to default : (x/100)*/
        if(progressBarBox?.isGone == false) progressBarBox.isGone = true


    }





}






