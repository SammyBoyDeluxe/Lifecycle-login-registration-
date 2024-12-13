package baller.example.myapplication.Fragments

import BulletTimeEnum
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import baller.example.myapplication.Activities.LoggedInActivity
import baller.example.myapplication.Activities.MainActivity
import baller.example.myapplication.Data.FragmentDataSet
import baller.example.myapplication.Data.User
import baller.example.myapplication.R
import baller.example.myapplication.utils.SharedPreferencesDAO
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    /*Set to n/a id-value since that should always be set to one of the options from the spinner*/
    lateinit var chosenBulletTimeEnum : BulletTimeEnum
    /*To start the loggedInActivity upon sign up in Main Activity*/
    lateinit var loggedInActivityIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*Will be used to obtain set information in update on loggedinactivity*/
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout first (so spinner can be added)
        val view = inflater.inflate(baller.example.myapplication.R.layout.signup_input_fragment, container, false)

        instantiateSpinner(view)

        return view
    }

    /**Instantiates the spinner with an array adapter and the BulletTimeEnum class
     *
     */
    private fun instantiateSpinner(view: View) {


        // Set up the spinner
        val spinner: Spinner? = view.findViewById(R.id.bullet_time_spinner) // Use inflated view
        val bulletTimeOptions =
            BulletTimeEnum.values().map { it.describe() } // Use values(), not entries
        val adapter: ArrayAdapter<String>? = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, bulletTimeOptions)
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            /*Asserting our values get updated upon being clicked*/
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

               parent?.setSelection(0)
            }
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpButton = view.findViewById<Button>(R.id.signup_button)
        val navButtonSignUpFragment = view.findViewById<Button>(R.id.navButtonSignUpFragment)
        signUpButton.setText(R.string.sign_up_string_for_signupbutton_mainactivity)
        navButtonSignUpFragment.setText(R.string.loginString)
        if (requireActivity() is MainActivity) {
            loggedInActivityIntent =  Intent(requireActivity(),LoggedInActivity::class.java)
            val signUpFragment: View = view.findViewById<ConstraintLayout>(R.id.signup_form_box)
            /*If there is retrievable data we get this from the retrieve data-function*/
            var signUpFragmentDataSet: FragmentDataSet? = null
            lifecycleScope.launch {

              signUpFragmentDataSet = SharedPreferencesDAO.retrieveDataSet(false)

            }
            var usernameInput: EditText = signUpFragment.findViewById<EditText>(R.id.username_input)
            var passwordInput: EditText = signUpFragment.findViewById<EditText>(R.id.password_input)
            var cokeInput: EditText =
                signUpFragment.findViewById<EditText>(R.id.kgs_of_cocaine_input)
            var footNinjaInput: EditText =
                signUpFragment.findViewById<EditText>(R.id.foot_ninjas_suplex_input)
            var selfDescribedBallerInput: Switch =
                signUpFragment.findViewById<Switch>(R.id.self_described_baller_switch)
            var timeSinceLastBulletTimeInput: Spinner =
                signUpFragment.findViewById<Spinner>(R.id.bullet_time_spinner)
            if(signUpFragmentDataSet != null) {
            setSignUpFragmentDataSet(
                signUpFragmentDataSet,
                usernameInput,
                passwordInput,
                cokeInput,
                footNinjaInput,
                selfDescribedBallerInput,
                timeSinceLastBulletTimeInput
            )}


            if(activity is MainActivity) {
                navButtonSignUpFragment.setOnClickListener {
                    /*Sets the text to login if it has been changed to back in the LoggedInActivity*/
                    this.getText(R.string.loginString)
                    /*Prevents null-exception on .toDouble()*/
//                    var cokeInputForDataSetMethodAsString : String = cokeInput.text.toString()
//                    var cokeInputForDataSetMethodAsDouble : Double? = null
                    /*The EditText-Element will return empty strings upon having no input - We need to check so that it is not empty to start*/
//                    if(!cokeInputForDataSetMethodAsString?.isEmpty()!!){
//                         cokeInputForDataSetMethodAsDouble = cokeInputForDataSetMethodAsString.toDouble()
//
//                    }
                    lifecycleScope.launch {
                        SharedPreferencesDAO.saveDataSet(
                        false, usernameInput.text.toString(),
                        passwordInput.text.toString(),
                        selfDescribedBallerInput.isActivated,
                        cokeInput.text.toString().toDoubleOrNull(),
                        footNinjaInput.text.toString().toIntOrNull(),
                        BulletTimeEnum.unDescribe(timeSinceLastBulletTimeInput.selectedItem.toString())

                    )

                    }.invokeOnCompletion {
                        if(it == null){
                        this.findNavController()
                            .navigate(R.id.action_signUpFragment_to_LoginInputFragment)}
                        else notifyOfInternalFailure(it)
                    }


                }
                signUpButton.setOnClickListener {
                    this.getText(R.string.sign_up_string_for_signupbutton_mainactivity)
                    val signUpFragment = view.findViewById<ConstraintLayout>(R.id.signup_form_box)
                    val usernameInput: EditText =
                        signUpFragment.findViewById<EditText>(R.id.username_input)
                    val passwordInput: EditText =
                        signUpFragment.findViewById<EditText>(R.id.password_input)
                    val cokeInput: EditText =
                        signUpFragment.findViewById<EditText>(R.id.kgs_of_cocaine_input)
                    val footNinjaInput: EditText =
                        signUpFragment.findViewById<EditText>(R.id.foot_ninjas_suplex_input)
                    val selfDescribedBallerInput: Switch =
                        signUpFragment.findViewById<Switch>(R.id.self_described_baller_switch)
                    val timeSinceLastBulletTimeInputSpinner: Spinner =
                         signUpFragment.findViewById<Spinner>(R.id.bullet_time_spinner)
                    val selectedValue = timeSinceLastBulletTimeInputSpinner.selectedItem.toString()
                    /*Upon pushing the sign-up we should validate the different inputs and if they are sufficient we
            * should sign the user up*/
                    if (validateSignUpAttributes(
                            usernameInput,
                            passwordInput,
                            cokeInput,
                            footNinjaInput,
                            timeSinceLastBulletTimeInputSpinner,
                            selfDescribedBallerInput
                        )
                    ) {


                        var signUpUser : User? = null
                        lifecycleScope.launch {
                             signUpUser  = SharedPreferencesDAO.signUpUser(
                            usernameInput.text.toString(),
                            passwordInput.text.toString(),
                            cokeInput.text.toString().toDouble(),
                            footNinjaInput.text.toString().toInt(),
                            selfDescribedBallerInput.isActivated,
                            BulletTimeEnum.unDescribe(timeSinceLastBulletTimeInputSpinner.selectedItem.toString())!!)
                        }.invokeOnCompletion {

                            if(it == null ){
                                if(signUpUser!= null){
                                /*On failure free writing we launch a dispatchers.io-call to complete this job before changing activity*/
                                lifecycleScope.launch(Dispatchers.IO,CoroutineStart.DEFAULT) {
                                    SharedPreferencesDAO.clearDataSet(
                                        false
                                    )

                                }
                                    showToast(" Sign up : Successful! ", false)
                                    startActivity(Intent(activity, LoggedInActivity::class.java))


                                } else showToast("Sorry, the user-name already exist, please choose another",false)

                            }
                            else{
                                notifyOfInternalFailure(it)


                            }

                        }

                        /*We clear the previously saved sign-up-FragmentDataSet*/

                    }
                }


            }
        } else if (requireActivity() is LoggedInActivity) {
            /*If in logged in activity we want the buttons to reflect that we´re updating the logged in user´s
            * account*/
            val signUpFragment = view.findViewById<ConstraintLayout>(R.id.signup_form_box)
            navButtonSignUpFragment.setText(R.string.navbutton_back_string)
            signUpButton.setText(R.string.update_string_for_signup_button_loggedinactivity)
            val usernameInput: EditText =
                signUpFragment.findViewById<EditText>(R.id.username_input)
            val passwordInput: EditText =
                signUpFragment.findViewById<EditText>(R.id.password_input)
            val cokeInput: EditText =
                signUpFragment.findViewById<EditText>(R.id.kgs_of_cocaine_input)
            val footNinjaInput: EditText =
                signUpFragment.findViewById<EditText>(R.id.foot_ninjas_suplex_input)
            val selfDescribedBallerInput: Switch =
                signUpFragment.findViewById<Switch>(R.id.self_described_baller_switch)
            val timeSinceLastBulletTimeInputSpinner: Spinner =
                signUpFragment.findViewById<Spinner>(R.id.bullet_time_spinner)
            val selectedValue = timeSinceLastBulletTimeInputSpinner.selectedItem.toString()


            setLoggedInUserDataToSignUpFragment(
                usernameInput,
                passwordInput,
                cokeInput,
                footNinjaInput,
                selfDescribedBallerInput,
                timeSinceLastBulletTimeInputSpinner
            )
            instantiateSpinner(view)

            navButtonSignUpFragment.setOnClickListener {
                /*Upon c*/


                this.findNavController().navigate(R.id.action_signUpFragment2_to_userMenuFragment)
            }
            signUpButton.setOnClickListener {
                this.getText(R.string.update_string_for_signup_button_loggedinactivity)
                val signUpFragment = view.findViewById<ConstraintLayout>(R.id.signup_form_box)

                val usernameInput: EditText =
                    signUpFragment.findViewById<EditText>(R.id.username_input)
                val passwordInput: EditText =
                    signUpFragment.findViewById<EditText>(R.id.password_input)
                val cokeInput: EditText =
                    signUpFragment.findViewById<EditText>(R.id.kgs_of_cocaine_input)
                val footNinjaInput: EditText =
                    signUpFragment.findViewById<EditText>(R.id.foot_ninjas_suplex_input)
                val selfDescribedBallerInput: Switch =
                    signUpFragment.findViewById<Switch>(R.id.self_described_baller_switch)
                val timeSinceLastBulletTimeInputSpinner: Spinner  =
                    signUpFragment.findViewById<Spinner>(R.id.bullet_time_spinner)
                /*Upon pushing the sign-up we should validate the different inputs and if they are sufficient we
            * should sign the user up*/
                if (validateSignUpAttributes(
                        usernameInput,
                        passwordInput,
                        cokeInput,
                        footNinjaInput,
                        timeSinceLastBulletTimeInputSpinner,
                        selfDescribedBallerInput
                    )
                ) {

                     chosenBulletTimeEnum  = BulletTimeEnum.valueOf(
                        timeSinceLastBulletTimeInputSpinner.selectedItem.toString()
                    )
                    lifecycleScope.launch {
                        val loggedInUser : User? = SharedPreferencesDAO.getLoggedInUser()
                        SharedPreferencesDAO.updateUser(
                        loggedInUser!!,
                        passwordInput.text.toString(),
                        selfDescribedBallerInput.isActivated,
                        cokeInput.text.toString().toDouble(),
                        footNinjaInput.text.toString().toInt(),
                        BulletTimeEnum.valueOf(
                            timeSinceLastBulletTimeInputSpinner.selectedItem.toString()
                        )
                    )
                        /*Upon completion we present a toast and let the user decide when to go back to the previous user-menu*/
                        Toast.makeText(context, "Baller info successfully updated!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }


            }
        }
    }

    private fun notifyOfInternalFailure(it: Throwable) {
        showToast("Internal failure: " + it.message.toString(), true)
    }

    private fun setLoggedInUserDataToSignUpFragment(
        usernameInput: EditText,
        passwordInput: EditText,
        cokeInput: EditText,
        footNinjaInput: EditText,
        selfDescribedBallerInput: Switch,
        timeSinceLastBulletTimeInput: Spinner
    ) {  var username : String = ""
         var password : String = ""
         var kgsOfCocaineTrafficked : String = ""
         var footNinjaSuplexCount : String = ""
         var selfDescribedBaller : Boolean = false
         var timeIntervalSinceLastBulletTime : BulletTimeEnum = BulletTimeEnum.ONE_MONTH_AGO


        lifecycleScope.launch {
             username   = SharedPreferencesDAO.getLoggedInUser()?.username.toString()
             password   = SharedPreferencesDAO.getLoggedInUser()?.password.toString()
             kgsOfCocaineTrafficked  = SharedPreferencesDAO.getLoggedInUser()?.kgsOfCocaineTrafficed.toString()
             footNinjaSuplexCount  = SharedPreferencesDAO.getLoggedInUser()?.footNinjasSuplexCount.toString()
             selfDescribedBaller   = SharedPreferencesDAO.getLoggedInUser()!!.selfDescribedBaller
             timeIntervalSinceLastBulletTime  = SharedPreferencesDAO.getLoggedInUser()?.timeIntervalSinceLastBulletTime!!
        }.invokeOnCompletion {
            if(it == null){
                usernameInput.setText(username)
                passwordInput.setText(password)
                cokeInput.setText(kgsOfCocaineTrafficked)
                footNinjaInput.setText(footNinjaSuplexCount)
                selfDescribedBallerInput.isActivated = selfDescribedBaller

                timeSinceLastBulletTimeInput.setSelection(
                        getBulletTimeEnumPosition(timeSinceLastBulletTimeInput.adapter,timeIntervalSinceLastBulletTime)
                    )}
                else{
                    notifyOfInternalFailure(it)
                }



        }










    }

    /**
     * Validates the sign-up attributes to ensure all required fields are filled.
     * If any input is blank, the function will return false, indicating invalid input.
     *
     * @param usernameInput The EditText for the username input.
     * @param passwordInput The EditText for the password input.
     * @param cokeInput The EditText for the coke input (expects a numeric value).
     * @param footNinjaInput The EditText for the foot ninja suplex count input (expects a numeric value).
     * @param timeSinceLastBulletTimeInput The EditText for the BulletTimeEnum input.
     * @param selfDescribedBallerInput The switch for self-described baller status.
     * @return Boolean indicating whether all attributes are valid.
     */
  private  fun validateSignUpAttributes(
        usernameInput: EditText,
        passwordInput: EditText,
        cokeInput: EditText,
        footNinjaInput: EditText,
        timeSinceLastBulletTimeInputSpinner: Spinner,
        selfDescribedBallerInput: Switch
    ): Boolean {
        val selectedValueSpinner = timeSinceLastBulletTimeInputSpinner.selectedItem.toString()
        // Check if any of the fields are blank or empty
        if (usernameInput.text.trim().isEmpty() ||
            passwordInput.text.trim().isEmpty() ||
            cokeInput.text.trim().isEmpty() ||
            footNinjaInput.text.trim().isEmpty() ||
            selectedValueSpinner.trim().isEmpty()
        ) {
            /* Return false if any field is incomplete*/
            return false
        }

        /* Parse cokeInput and footNinjaInput  to ensure they are valid numbers*/
        val cokeInputString = cokeInput.text.toString()
        val footNinjaCountString = footNinjaInput.text.toString()

        return try {
            /* Attempt to parse cokeInput as Double and footNinjaInput as Int*/
            cokeInputString.toDouble()
            footNinjaCountString.toInt()

            /* Attempt to parse BulletTimeEnum from the provided input*/
            BulletTimeEnum.unDescribe(selectedValueSpinner)

            /* If all parsing succeeds and no fields are blank, return true*/
            true
        } catch (e: Exception) {
            /* Return false if any parsing fails (invalid number or enum value)*/
            false
        }
    }

    /**Gets the last FragmentDataSet if such exists and sets the fragment-UI-components to match
     * the data-set.
     *
     *
     */
    private fun setSignUpFragmentDataSet(
        signUpFragmentDataSet: FragmentDataSet?,
        usernameInput: EditText,
        passwordInput: EditText,
        cokeInput: EditText,
        footNinjaInput: EditText,
        selfDescribedBallerInput: Switch,
        timeSinceLastBulletTimeInputSpinner: Spinner
    ) {
        if (signUpFragmentDataSet != null) {
            val savedEnum = BulletTimeEnum.unDescribe(signUpFragmentDataSet.timeSinceLastBulletTime.toString())
            usernameInput.setText(signUpFragmentDataSet.username!!)
            passwordInput.setText(signUpFragmentDataSet.password!!)
            cokeInput.setText(signUpFragmentDataSet.kgsOfCokeTrafficked?.toString().orEmpty())
            footNinjaInput.setText(signUpFragmentDataSet.footNinjaSuplexCount?.toString().orEmpty())
            selfDescribedBallerInput.isActivated = signUpFragmentDataSet.selfDescribedBaller ?: false

            if(signUpFragmentDataSet.timeSinceLastBulletTime != null) {
                timeSinceLastBulletTimeInputSpinner.setSelection(
                    getBulletTimeEnumPosition(
                        timeSinceLastBulletTimeInputSpinner.adapter,
                        signUpFragmentDataSet.timeSinceLastBulletTime!!
                    )
                )
            }

        }


    }
    private fun getBulletTimeEnumPosition(
        adapter: SpinnerAdapter,
        value: BulletTimeEnum
    ): Int {
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).equals(BulletTimeEnum.unDescribe(value.toString()))) {
                return i
            }
        }
        return -1
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
        val progressBar : ProgressBar? = view?.findViewById(R.id.progressBarSignUpFragment)
        /*The progressBarBox is the box that holds  the loading-text text view as well as the progress bar and provides a background*/
        val progressBarBox : ConstraintLayout? =
            view?.findViewById(R.id.progressBarBoxSignUpFragment)
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
        val progressBar : ProgressBar? = view?.findViewById(R.id.progressBarSignUpFragment)
        /*The progressBarBox is the box that holds  the loading-text text view as well as the progress bar and provides a background*/
        val progressBarBox : ConstraintLayout? = view?.findViewById(R.id.progressBarBoxSignUpFragment)

        progressBar?.setProgress(0,true)
        /*Progress to default : (x/100)*/
        if(progressBarBox?.isGone == false) progressBarBox.isGone = true


    }

}