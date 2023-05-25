package com.example.personalfinancetool.ui.home

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.personalfinancetool.R
import com.example.personalfinancetool.databinding.FragmentInvestmentBinding
import com.example.personalfinancetool.model.IndexPrice
import com.example.personalfinancetool.network.AlphaVantageAPI
import com.example.personalfinancetool.network.AlphaVantageApiService
import com.example.personalfinancetool.network.AlphaVantageResponse
import com.example.personalfinancetool.ui.Budgeting.BudgetingViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

class InvestmentFragment : Fragment() {
    val budgetViewModel : BudgetingViewModel by activityViewModels()

    private var _binding: FragmentInvestmentBinding? = null
    var returnOnInvestment : String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(InvestmentViewModel::class.java)

        _binding = FragmentInvestmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set monthly disposable income from shared model
        binding.textHome.text = "Your monthly disposable income : " + budgetViewModel.disposableIncome.value.toString() + " Kr"

        //Tries to display the investment return if numbers are already available
        if(budgetViewModel.calcResult != "") {
            binding.calculatedResult.text =  budgetViewModel.calcResult
        }


        // Shows popup window
        binding.questionImage.setOnClickListener{
            val popupView = inflater.inflate(R.layout.info_popup,null)
            val button = popupView.findViewById<Button>(R.id.popup_button)
            val width = ViewGroup.LayoutParams.WRAP_CONTENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            val window = PopupWindow(popupView,width,height,true)
            val constraints = binding.constraint
            setPopupText(popupView)
            window.showAtLocation(constraints, Gravity.BOTTOM,0,240)

            button.setOnClickListener {
                window.dismiss()
            }

        }

        var setByOtherListener = 0

        // Sets the monthly contribution textfield to change as you fill percentages
        binding.disposablePercent.addTextChangedListener {
             if (it.toString() == "") {
                binding.MonthlySave.text = Editable.Factory.getInstance().newEditable("0")
            } else {
                val percentageAsDecimal =
                    (binding.disposablePercent.text.toString().toDouble()) / 100

                val calcAmount = (budgetViewModel.disposableIncome.value)?.toDouble()
                binding.MonthlySave.text = Editable.Factory.getInstance()
                    .newEditable((((calcAmount!! * percentageAsDecimal).toInt()).toString()))
            }

        }


        // API BUTTON START
        binding.APIButton.setOnClickListener {

            val popupView = inflater.inflate(R.layout.index_list_popup,null)

            val button = popupView.findViewById<Button>(R.id.dismiss_button)

            val sp500 = popupView.findViewById<TextView>(R.id.SP500Item)
            val dowjones = popupView.findViewById<TextView>(R.id.Dow_Jones_Item)
            val nasdaq100 = popupView.findViewById<TextView>(R.id.NASDAQ_100)

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            val window = PopupWindow(popupView,width,height,true)
            val constraints = binding.constraint
            //setPopupText(popupView)

            sp500.setOnClickListener{
                var sp500ReturnString : String = ""
                val job = GlobalScope.launch {
                    val APIresponse: AlphaVantageResponse =
                        AlphaVantageAPI.AlphaVantageRetrofitService.getIndexData(symbol = "SPY")
                    sp500ReturnString = calculateIndexReturns(APIresponse.priceList)
                }
                runBlocking {
                    job.join()
                }
                binding.expectedReturn.text = Editable.Factory.getInstance().newEditable(sp500ReturnString)
                window.dismiss()

            }

            dowjones.setOnClickListener{
                var DJreturnString : String = ""
                val job = GlobalScope.launch {
                    val APIresponse: AlphaVantageResponse =
                        AlphaVantageAPI.AlphaVantageRetrofitService.getIndexData(symbol = "DIA")
                    DJreturnString = calculateIndexReturns(APIresponse.priceList)
                }
                runBlocking {
                    job.join()
                }
                binding.expectedReturn.text = Editable.Factory.getInstance().newEditable(DJreturnString)
                window.dismiss()

            }

            nasdaq100.setOnClickListener{
                var nasdaqReturnString : String = ""
                val job = GlobalScope.launch {
                    val APIresponse: AlphaVantageResponse =
                        AlphaVantageAPI.AlphaVantageRetrofitService.getIndexData(symbol = "QQQ")
                    nasdaqReturnString = calculateIndexReturns(APIresponse.priceList)
                }
                runBlocking {
                    job.join()
                }
                binding.expectedReturn.text = Editable.Factory.getInstance().newEditable(nasdaqReturnString)
                window.dismiss()

            }




            window.showAtLocation(constraints, Gravity.BOTTOM,0,240)

            button.setOnClickListener {
                window.dismiss()
            }
        }
        // API BUTTON END

        binding.calculateButton.setOnClickListener {// Sets the textview to display the calculated returns
            try {
                val result = formatCurrency(calculateReturns()) + " Kr"
                budgetViewModel.calcResult = result
                binding.calculatedResult.text = result// Formats returns according to swedish standard
            } catch (e:Exception) {
                Toast.makeText(context, "Error calculating result, make sure all fields of the calculator have something in them", Toast.LENGTH_SHORT).show()
            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        returnOnInvestment = binding.calculatedResult.text.toString()
        _binding = null
    }

    //This function calculates your returns given that
    // annual returns are uniform during the year
    fun calculateReturns() : Long {
        val monthlyContribution = binding.MonthlySave.text.toString().toInt()
        val nrOfYears = binding.savingTime.text.toString().toInt()
        val returnPercent = binding.expectedReturn.text.toString().toDouble()
        val percentageAsDouble = (returnPercent/100) + 1.0
        val monthlyPercentageAsDouble = ((returnPercent/100)/12) + 1.0
        var accumulator = 0.0
        for(i in 1..nrOfYears) {
           for(i in 1..12) {
               accumulator += monthlyContribution
               accumulator = accumulator * monthlyPercentageAsDouble
           }
        }
        return accumulator.toLong()
    }



    fun formatCurrency(number: Long): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("sv", "SE"))
        return numberFormat.format(number)
    }


    fun setPopupText(view : View) {
        val text = view.findViewById<TextView>(R.id.popup_text)
        val monthlyPayment = binding.MonthlySave.text.toString()
        val investmentTime = binding.savingTime.text.toString()
        val yearlyReturn = binding.expectedReturn.text.toString()
        val expectedCapital = binding.calculatedResult.text
        try {
            val savedCapital = monthlyPayment.toInt()*(investmentTime.toInt())*12
            val savedCapitalText = formatCurrency(savedCapital.toLong())
            val returnOnCapital = formatCurrency(calculateReturns().toLong() - savedCapital.toLong())
            text.text = "If you invested $monthlyPayment Kr a month for $investmentTime" +
                    " years with a return of $yearlyReturn% annually you can expect a return of $expectedCapital of which $savedCapitalText Kr would be money you've saved" +
                    " and $returnOnCapital Kr from market returns. \n" +
                    "\nNote: this assumes" +
                    " that there are no deviations from your expected return and that returns are uniform throughout the year"
        } catch(e:Exception) {
            text.text = "Error loading Information one or more fields of" +
                    " the calculator may be empty"
        }


    }



    fun calculateIndexReturns(priceList: Map<String, IndexPrice>): String {
        var indexPrices : MutableList<IndexPrice> = mutableListOf()
        var counter = 0
        for (value in priceList) { // each entry is one month so 12 entries is 1 year
            indexPrices.add(value.value) // adds all indexprice objects sorted by date to a list
            counter++
        }
        val lastPrice = indexPrices.get(0).price.toDouble()
        val earliestPrice = indexPrices.get(indexPrices.size -1).price.toDouble()
        val percentualIncrease = ((lastPrice.toDouble()/earliestPrice.toDouble()) * 100) -100 // -100 to represent the principal as anything under 1.0 would represent a loss
        val years = counter.toDouble()/12
        val averageReturn = percentualIncrease/years
        return calculateCAGR(lastPrice,earliestPrice,years)
    }

    fun calculateCAGR(lastPrice : Double,earliestPrice:Double, Years : Double) : String {
        //This function calculates the CAGR for the period
        //instead of calculating the decimal value and doing -1
        // I simply do the exponent with the percent increase that has already
        //had 100 subtracted to represent the principal
        val quotient = lastPrice/earliestPrice
        val CAGR = (Math.pow(quotient,(1.0/Years)) -1) * 100
        val roundedValue = String.format("%.2f", CAGR) // formats the CAGR to a 2 decimal point percent string

        return roundedValue
    }
}