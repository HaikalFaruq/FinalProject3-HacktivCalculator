
package ir.erfansn.siliconecalculator

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.calculator.button.operator.Add
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
class SiliconeCalculatorTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<SiliconeCalculatorActivity>()

    @Test
    fun savedCalculation_whenRetrieveItFromHistory_showItAsCurrentCalculation() {
        with(composeRule) {
            onNodeWithContentDescription(activity.getString(R.string.calculations_history))
                .performClick()
            onNodeWithContentDescription(activity.getString(R.string.clear_history))
                .performClick()
            onNodeWithTag("history:clear")
                .performClick()

            listOf("1", "2", Add.symbol, "3", "4", Equals.symbol).forEach {
                onNodeWithTag("calculator:$it")
                    .performClick()
            }
            onNodeWithTag("calculator:expression")
                .assertTextEquals("12 + 34")
            onNodeWithTag("calculator:result")
                .assertTextEquals("46.0")

            onNodeWithTag("calculator:${AllClear.symbol}")
                .performClick()
            onNodeWithTag("calculator:expression")
                .assertTextEquals("")
            onNodeWithTag("calculator:result")
                .assertTextEquals("0")

            onNodeWithContentDescription(activity.getString(R.string.calculations_history))
                .performClick()
            onNodeWithTag("history:items")
                .onChildren()
                .onFirst()
                .performClick()

            onNodeWithTag("calculator:expression")
                .assertTextEquals("12 + 34")
            onNodeWithTag("calculator:result")
                .assertTextEquals("46.0")
        }
    }
}
