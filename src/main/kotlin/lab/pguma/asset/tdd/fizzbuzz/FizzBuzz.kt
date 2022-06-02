package lab.pguma.asset.tdd.fizzbuzz

class FizzBuzz {
    fun convert(number: Int): String {
        return when {
            number % 3 == 0 && number % 5 == 0 -> "FizzBuzz"
            number % 3 == 0 -> "Fizz"
            number %5 == 0 -> "Buzz"
            else -> number.toString()
        }
    }
}