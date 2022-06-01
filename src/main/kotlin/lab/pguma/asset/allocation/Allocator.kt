package lab.pguma.asset.allocation

class Allocator<T: ClassInterval>(
    private val classIntervals: ClassIntervals<T>
) {
    fun allocate(total: Int): Map<T, Int> {
        require(total > 0) { "arg must not be less than 1" }

        val initialContainer = classIntervals.initialContainer()
        allocate(classIntervals.startClass(), total, initialContainer)
        return initialContainer.toMap()
    }

    private fun allocate(
        current: T,
        rest: Int,
        container: MutableMap<T, Int>
    ) {
        val classRange = current.range()
        val extract = (classRange - kotlin.math.abs((rest - classRange).coerceAtMost(0))).coerceAtLeast(0)
        container[current] = extract

        if(rest - extract == 0) {
            return
        }

        allocate(current.next() as T, rest - extract, container)
    }
}

interface ClassInterval {
    fun pre(): ClassInterval
    fun next(): ClassInterval
    val border: Int
    fun range() = border - pre().border
}

object ZeroClassInterval : ClassInterval {
    override val border = 0
    override fun pre(): Nothing = throw UnsupportedOperationException()
    override fun next(): Nothing = throw UnsupportedOperationException()
}

class MaxClassInterval(private val preClassInterval :ClassInterval) : ClassInterval {
    override val border = Int.MAX_VALUE
    override fun pre() = preClassInterval
    override fun next(): Nothing = throw UnsupportedOperationException()
}

interface ClassIntervals<T: ClassInterval> {
    fun startClass(): T
    fun initialContainer(): MutableMap<T, Int>
}