package lab.pguma.asset.tdd.fizzbuzz

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("FizzBuzz数列と変換規則を扱うFizzBuzzクラス")
class FizzBuzzTest {

    val fizzbuzz = FizzBuzz()

    @Nested
    inner class convertメソッドが数を文字列に変換する() {

        @Nested
        inner class _3の倍数を渡すとFizzに変換する() {
            @Test
            fun _3を渡すとFizzに変換する() {
                // 検証
                assertEquals("Fizz", fizzbuzz.convert(3))
            }
        }

        @Nested
        inner class _5の倍数を渡すとBuzzに変換する() {
            @Test
            fun _5を渡すとBuzzに変換する() {
                // 検証
                assertEquals("Buzz", fizzbuzz.convert(5))
            }
        }

        @Nested
        inner class _3と5の両方の倍数を渡すとFizzBuzzに変換する() {
            @Test
            fun _15を渡すとFizzBuzzに変換する() {
                // 検証
                assertEquals("FizzBuzz", fizzbuzz.convert(15))
            }
        }

        @Nested
        inner class その他の数を渡すと数値文字列に変換する {
            @Test
            fun _1を渡すと文字列1に変換する() {
                // 検証
                assertEquals("1", fizzbuzz.convert(1))
            }
        }
    }
}