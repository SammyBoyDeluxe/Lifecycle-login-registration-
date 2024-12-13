enum class BulletTimeEnum(var interval : Int) {
    ONE_MONTH_AGO(1),
    THREE_MONTHS_AGO(3),
    HUNDRED_MONTHS_AGO(100);

    // You can also define functions or override methods here if needed
    fun describe(): String {
        return " $interval months ago"
    }
    companion object {
        /** Used to match descriptions of BulletTimeEnums
         *  ->In:Chosen spinner option as String  Returns : BulletTimeEnum
         */
        fun unDescribe(spinnerSelectedItemAsString: String): BulletTimeEnum? {
            if (spinnerSelectedItemAsString == " 1 months ago") return ONE_MONTH_AGO
            else if (spinnerSelectedItemAsString == " 3 months ago") return THREE_MONTHS_AGO
            else if (spinnerSelectedItemAsString == " 100 months ago") return HUNDRED_MONTHS_AGO

            return null
        }

    }
}