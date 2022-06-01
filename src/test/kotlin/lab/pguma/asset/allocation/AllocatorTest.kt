package lab.pguma.asset.allocation

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsMapContaining
import org.junit.jupiter.api.Test

enum class OneHundredFrequencyClassInterval(
    override val border: Int
):ClassInterval {
    ONE_HUNDRED(100) {
        override fun pre() = ZeroClassInterval
        override fun next() = TWO_HUNDRED
    },
    TWO_HUNDRED(200) {
        override fun pre() = ONE_HUNDRED
        override fun next() = THREE_HUNDRED
    },
    THREE_HUNDRED(300) {
        override fun pre() = TWO_HUNDRED
        override fun next() = FOUR_HUNDRED
    },
    FOUR_HUNDRED(400) {
        override fun pre() = THREE_HUNDRED
        override fun next() = FIVE_HUNDRED_OVER
    },
    FIVE_HUNDRED_OVER(500) {
        override fun pre() = FOUR_HUNDRED
        override fun next() = MaxClassInterval(this)

        override fun range(): Int {
            return next().border - pre().border
        }
    }
}

object OneHundredFrequencyClassIntervals:ClassIntervals<OneHundredFrequencyClassInterval> {
    override fun startClass() = OneHundredFrequencyClassInterval.ONE_HUNDRED

    override fun initialContainer(): MutableMap<OneHundredFrequencyClassInterval, Int> {
        return OneHundredFrequencyClassInterval.values()
            .associateWith { 0 }
            .toMutableMap()
    }
}

internal class AllocatorTest {

    private val allocator = Allocator(OneHundredFrequencyClassIntervals)

    @Test
    fun 複数階級にまたがる数値の配分処理にて正しい配分結果が得られること() {
        val result = allocator.allocate(333)
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.ONE_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.TWO_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.THREE_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.FOUR_HUNDRED, 33))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.FIVE_HUNDRED_OVER, 0))
    }

    @Test
    fun 初期階級以下の数値の配分処理にて正しい配分結果が得られること() {
        val result = allocator.allocate(59)
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.ONE_HUNDRED, 59))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.TWO_HUNDRED, 0))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.THREE_HUNDRED, 0))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.FOUR_HUNDRED, 0))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.FIVE_HUNDRED_OVER, 0))
    }

    @Test
    fun 最終階級に及ぶ数値の配分処理にて正しい配分結果が得られること() {
        val result = allocator.allocate(666)
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.ONE_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.TWO_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.THREE_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.FOUR_HUNDRED, 100))
        assertThat(result, IsMapContaining.hasEntry(OneHundredFrequencyClassInterval.FIVE_HUNDRED_OVER, 266))
    }
}