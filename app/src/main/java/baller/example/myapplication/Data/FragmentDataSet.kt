package baller.example.myapplication.Data

import BulletTimeEnum

/**This class presents a nullable collection of the dataset required to instantiate a User.
 * This is used to hold current state of any UI (Since at most what you can do is register all of the
 * variables) and is saved as a JSON-object, having the same formalism as our User-class has for actually
 * validated User-attributes ()
 */
data class FragmentDataSet(var username : String?,
                       var password: String?,
                       var selfDescribedBaller: Boolean?,
                       var kgsOfCokeTrafficked: Double?,
                       var footNinjaSuplexCount: Int?,
                       var timeSinceLastBulletTime: BulletTimeEnum?
) {


}