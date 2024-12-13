package baller.example.myapplication.utils

import BulletTimeEnum
import android.content.SharedPreferences
import baller.example.myapplication.Data.FragmentDataSet
import baller.example.myapplication.Data.User
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**A class with a companionobject to handle specific actions used for sharedpreferences:
 * Such as, inscribing a User-object Json-string with their username as key(sign up),
 * checking whether input data matches entries in SharedPreferences (login) or updating
 * the JSON-string under a specifc username-key (user-info update)
 */
class SharedPreferencesDAO(val sharedPreferences: SharedPreferences) {

    companion object {
        /**These constants are used to access the different keys under which we persist different
         * data from the application
         *
         */
        const val LOGGED_IN_USER_SHAREDPREF_KEY = "logged_in_user"
        const val CURRENT_SIGN_UP_FRAGMENT_SHAREDPREFERENCES_KEY = "currentSignUpFragmentReferences"
        const val CURRENT_LOGIN_FRAGMENT_SHAREDPREFERENCES_KEY = "currentLoginFragmentReferences"


        /**This variable is used for the sharedPreferences for the application
         *
         */
        var sharedPreferences: SharedPreferences? = null
        private val gsonObject: Gson = Gson()

        /**Takes input from the sign-up fragment and converts it to a user-object
         *  - Will be used for comparisons, entry of a user into the SharedPreferences
         *  and for updating information
         */
        private fun createUserInstanceFromInput(
            username: String,
            password: String,
            kgsOfCokeTrafficked: Double,
            footNinjaSuplexCount: Int,
            selfDescribedBaller: Boolean,
            timeSinceLastBulletTime: BulletTimeEnum
        ): User {

            /*Validation of entered information is done in the associated fragment*/
            return User(
                username,
                password,
                kgsOfCokeTrafficked,
                footNinjaSuplexCount,
                selfDescribedBaller,
                timeSinceLastBulletTime
            )
        }

        /**Registers user in the sharedpreferences, returns null upon failed registration (username taken)
         *
         */
        suspend fun signUpUser(
            username: String,
            password: String,
            kgsOfCokeTrafficked: Double,
            footNinjaSuplexCount: Int,
            selfDescribedBaller: Boolean,
            timeSinceLastBulletTime: BulletTimeEnum
        ): User? {
            /*Creates User object from input*/
            val userToSignUp: User = createUserInstanceFromInput(
                username,
                password,
                kgsOfCokeTrafficked,
                footNinjaSuplexCount,
                selfDescribedBaller,
                timeSinceLastBulletTime
            )
            return withContext(Dispatchers.IO) {
                /*Generates a JSON-string containing the user information*/
                val signUpUserJsonString: String = gsonObject.toJson(userToSignUp)
                val userJsonString: String? = sharedPreferences!!.getString(username, null)
                /*Creates a UserObject from the string if it exists*/
                if (userJsonString == null) {
                    /*Writes the JSON-String under in the sharedpreferences, using the username as a key*/

                    sharedPreferences!!.edit().putString(username, signUpUserJsonString).apply()
                    /*To pass the loggedInUser to the next activity we input the user under loggedInuser in sharedpreferences*/
                    sharedPreferences!!.edit().putString("logged_in_user", signUpUserJsonString)
                        .apply()

                    return@withContext userToSignUp
                } else {
                    return@withContext null
                }

            }
        }

        /**Checks if user exists, returns the User-object associated with the User on credentials-match,
         * returns null otherwise.
         * Will only perform on instantiated sharedPreferences-variable
         */
      suspend fun loginUser(
            username: String,
            password: String,
        ): User? {
            return withContext(Dispatchers.IO,  {
                val userJsonString: String? = sharedPreferences?.getString(username, null)
                /*Creates a UserObject from the string if it exists*/
                if (userJsonString != null) {
                    /*If a user is found under that key we instantiate the object and validate the password
                      if the password matches we return the User object for future data-access of saved user data*/
                    val usernameMatch: User = gsonObject.fromJson(userJsonString, User::class.java)
                    if (usernameMatch.password == password) {
                        sharedPreferences!!.edit()
                            .putString(LOGGED_IN_USER_SHAREDPREF_KEY, userJsonString).apply()

                        return@withContext usernameMatch
                    } else return@withContext null
                }
                /*If no username-match was found, the JsonString is null and we return null to signify failed login*/
                else return@withContext null

            })
        }








        /**Inserts user-information in a already defined username-key - Username is not permitted to be  changed for this reason
         *
         */
        suspend fun updateUser(
            userToUpdate: User,
            password: String,
            selfDescribedBaller: Boolean,
            kgsOfCokeTrafficked: Double,
            footNinjaSuplexCount: Int,
            timeSinceLastBulletTime: BulletTimeEnum
        ) {
            /*We take the whole signupfragment as data and as such none will be null*/

            /*We create a user-object and generate a JSON-string with the updated data*/

            withContext(Dispatchers.IO) {
                val updatedUser: User = createUserInstanceFromInput(
                    userToUpdate.username,
                    password,
                    kgsOfCokeTrafficked,
                    footNinjaSuplexCount,
                    selfDescribedBaller,
                    timeSinceLastBulletTime
                )
                val updatedUserJsonString: String = gsonObject.toJson(updatedUser, User::class.java)


                sharedPreferences!!.edit().putString(userToUpdate.username, updatedUserJsonString)
                    .commit()


            }
        }
        /**Saves all available input data to be retrieved on configChange and onStart
         *  -> This is called onStop.
         *  The dataset is saved as a JSON-String of the UserDataSet-object under "currentFragmentReferences"
         * It will either be saved under currentLoginFragmentReferences, if isLoginFragment == true
         * or under currentSignUpFragmentReferences if not
         */
        suspend fun saveDataSet(                              isLoginFragment : Boolean,
                                                      username:String?,
                                                       password: String?,
                                                       selfDescribedBaller: Boolean?,
                                                       kgsOfCokeTrafficked: Double?,
                                                       footNinjaSuplexCount: Int?,
                                                       timeSinceLastBulletTime: BulletTimeEnum?
        ){
            withContext(Dispatchers.IO) {
                val fragmentDataSet: FragmentDataSet = FragmentDataSet(
                    username,
                    password,
                    selfDescribedBaller,
                    kgsOfCokeTrafficked,
                    footNinjaSuplexCount,
                    timeSinceLastBulletTime
                )
                withContext(Dispatchers.IO) {
                    val fragmentDataSetJsonString: String = gsonObject.toJson(fragmentDataSet)
                    if (isLoginFragment) {
                        sharedPreferences!!.edit()
                            .putString("currentLoginFragmentReferences", fragmentDataSetJsonString)
                            .commit()
                    } else {
                        sharedPreferences!!.edit()
                            .putString("currentSignUpFragmentReferences", fragmentDataSetJsonString)
                            .commit()


                    }
                }
            }
            }


        /**Gets the last input by the user on the current page
         *
         */
        suspend fun retrieveDataSet(isLoginFragment : Boolean ) : FragmentDataSet? {
            return withContext(Dispatchers.IO){
                if (isLoginFragment) {
                    val currentDataSet: FragmentDataSet? = gsonObject.fromJson(
                        sharedPreferences!!.getString(
                            "currentLoginFragmentReferences",
                            null
                        ), FragmentDataSet::class.java
                    )
                    return@withContext currentDataSet
                } else {
                    val currentDataSet: FragmentDataSet? = gsonObject.fromJson(
                        sharedPreferences!!.getString(
                            "currentSignUpFragmentReferences",
                            null
                        ), FragmentDataSet::class.java
                    )
                    return@withContext currentDataSet

                }
            }
        }



        /**Upon changing fragments and screens we clear the data set, that is we set the
         *  currentfragmentReferences-String to null
         */
        suspend fun clearDataSet(isLoginFragment: Boolean){
            withContext(Dispatchers.IO){
                if(isLoginFragment) {
                sharedPreferences!!.edit()
                    .putString(CURRENT_LOGIN_FRAGMENT_SHAREDPREFERENCES_KEY, null).commit()
            }
            else
            {       sharedPreferences!!.edit()
                .putString(CURRENT_SIGN_UP_FRAGMENT_SHAREDPREFERENCES_KEY, null).commit()


            }}


        }

        /**If there is a logged in user we remove that entry from our
         * SharedPreferences-file - Can only be accessed by logged in user (naturally)
         */
       suspend fun deleteAccount(){
            var loggedInUser : User? = getLoggedInUser()
            withContext(Dispatchers.IO) {
                sharedPreferences!!.edit().remove(loggedInUser?.username)
                    .commit()
            }


        }

        /**Gets logged in user if one is logged in, otherwise returns null
         *
         */
        suspend fun getLoggedInUser() : User? {
                return withContext(Dispatchers.IO,{
                 return@withContext gsonObject.fromJson(
                     sharedPreferences?.getString(LOGGED_IN_USER_SHAREDPREF_KEY, null),
                     User::class.java
                 )
             })


        }

        /**Removes the entry under the LOGGED_IN_USER_SHAREDPREF_KEY to signify that no user is logged in
         *
         */
        suspend fun logOutUser(){
            withContext(Dispatchers.IO,{
                sharedPreferences!!.edit()?.remove(LOGGED_IN_USER_SHAREDPREF_KEY)?.commit()
            })

        }
    }






}

