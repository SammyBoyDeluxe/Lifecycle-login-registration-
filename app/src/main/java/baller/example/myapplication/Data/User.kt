package baller.example.myapplication.Data

import BulletTimeEnum

data class User internal constructor(  val username : String,
                                       val password : String,
                                       val kgsOfCocaineTrafficed : Double,
                                       val footNinjasSuplexCount : Int,
                                       val selfDescribedBaller : Boolean,
                                       val timeIntervalSinceLastBulletTime : BulletTimeEnum ) {

        companion object{


        }

}