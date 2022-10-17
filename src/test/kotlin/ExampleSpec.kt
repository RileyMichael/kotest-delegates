import io.kotest.core.names.TestName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

class ExampleSpec : FreeSpec({
  "a example test" - {
    `some assertion`
  }
})

val Assertions.`some assertion` by reusableTest {
  1 + 1 shouldBe 2
}

typealias Assertions = FreeSpecContainerScope

fun reusableTest(block: suspend () -> Unit) = ReusableTestDelegate(block)

class ReusableTestDelegate(private val block: suspend () -> Unit) {
  operator fun getValue(thisRef: FreeSpecContainerScope?, property: KProperty<*>) {
    val functionName = getFunctionNameForTest()
    if (thisRef != null) runBlocking {
      // this blocks
      thisRef.registerTest(functionName, false, null) { block() }
    }
  }
}

// some logic to determine test name from stacktrace
fun getFunctionNameForTest() = TestName("a test")