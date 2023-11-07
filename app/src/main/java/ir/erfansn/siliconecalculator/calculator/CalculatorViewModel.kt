
package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.EXPRESSION_ARG
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.RESULT_ARG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val historyRepository: HistoryRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _calculation = MutableStateFlow(Calculation())
    private val currentCalculation get() = _calculation.value

    private var previousExpression = currentCalculation.expression

    val uiState = _calculation
        .map(::CalculatorUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CalculatorUiState()
        )

    init {
        updateCalculatorDisplay(
            expression = savedStateHandle[EXPRESSION_ARG],
            result = savedStateHandle[RESULT_ARG]
        )
    }

    private fun updateCalculatorDisplay(expression: String?, result: String?) {
        if (expression == null || result == null) return

        _calculation.update {
            it.copy(expression = expression, result = result)
        }

        previousExpression = expression
    }

    fun performCalculatorButton(calculatorButton: CalculatorButton) {
        if (currentCalculation.resultIsInvalid && calculatorButton != AllClear) return

        viewModelScope.launch(defaultDispatcher) {
            _calculation.update {
                with(calculatorButton) {
                    it.perform().also {
                        if (calculatorButton == Equals) saveCalculationInHistory(it)
                    }
                }
            }
        }
    }

    private suspend fun saveCalculationInHistory(calculation: Calculation) {
        if (calculation.isNotEvaluated || calculation.resultIsInvalid) return

        historyRepository.saveCalculation(calculation)

        previousExpression = calculation.expression
    }

    private val Calculation.isNotEvaluated
        get() = expression.endsWith(lastOperator) || expression.isEmpty() || expression == previousExpression
}
