import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerContext
import io.kotest.core.test.TestType
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

class ExampleSpec: FreeSpec({
  "a example test" - {
    `some assertion`
  }
})

val Assertions.`some assertion` by reusableTest {
  1 + 1 shouldBe 2
}

typealias Assertions = FreeSpecContainerContext
fun reusableTest(block: suspend () -> Unit) = ReusableTestDelegate(block)

class ReusableTestDelegate(private val block: suspend () -> Unit) {
  operator fun getValue(thisRef: FreeSpecContainerContext?, property: KProperty<*>) {
    val functionName = getFunctionNameForTest()
    if (thisRef != null) runBlocking { thisRef.addTest(functionName, TestType.Test) { block() } }
  }
}

// some logic to determine test name from stacktrace
fun getFunctionNameForTest() = "a test"