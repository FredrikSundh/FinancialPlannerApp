package com.example.personalfinancetool.ui.Budgeting

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.personalfinancetool.adapter.budgetViewAdapter
import com.example.personalfinancetool.databinding.FragmentBudgetingBinding
import com.example.personalfinancetool.model.IncomeExpenseItem

// NOTE:
//Add new item as well as the finItems LiveData Likely Belongs in a viewModel, will change later
// I also need to add a database which will correspond to finItems
// Make it so the list sorts entries by size when added so that salary will always be on top
// Add remove button to each list entry item - This might mean i have to add clicklistener to
//The adapter class
// Rename description to tag and set max char count of 20 or something
// MÅSTE lägga till ViewModel med korrekt funktionalitet för det börjar bli ohanterbart
// Viewmodeln måste skaps och där måste det finnas Onclick funktioner
// Så att jag kan sätta upp LIVEDATA i viewmodeln som jag kan ändra i viewmodeln när man klickar
// Eventuellt har adaptern tillgång till viewmodeln men lär inte behövas
class BudgetingFragment : Fragment() {
    val finItemList = mutableListOf<IncomeExpenseItem>()
    val finItems = MutableLiveData<List<IncomeExpenseItem>?>()

    private var _binding: FragmentBudgetingBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(BudgetingViewModel::class.java)

        _binding = FragmentBudgetingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /**
        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        **/
        /**
        finItemList.add(IncomeExpenseItem(100,"lön"))
        finItemList.add(IncomeExpenseItem(-50,"chips"))
        finItemList.add(IncomeExpenseItem(-50, "chips"))
        finItemList.add(IncomeExpenseItem(-50, "chips"))
        finItemList.add(IncomeExpenseItem(-50, "chips"))
        finItemList.add(IncomeExpenseItem(-50, "chips"))
        finItemList.add(IncomeExpenseItem(-50, "chips"))
        **/
        finItems.value = finItemList


        val myAdapter = budgetViewAdapter(finItemList)
        binding.budgetListRv.adapter = myAdapter

        binding.addItemButton.setOnClickListener {
            showAddItemDialog()
        }



        finItems.observe(viewLifecycleOwner) { // If the Livedata Changes, update the adapter
            val newAdapter = budgetViewAdapter(finItems.value as MutableList<IncomeExpenseItem>)
            binding.budgetListRv.adapter = newAdapter
            updateDisposableInc()
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // adds all incomes and expenses together and displays disposable income
    private fun updateDisposableInc() {
        val currentList = finItems.value
        var accumulate : Int = 0
        if (currentList != null) {
            for (expense in currentList ) {
                accumulate += expense.krValue
            }
        }
        binding.DisposableTextView.text = "Your monthly disposable income is : $accumulate KR"
    }


    private fun AddNewItem(amount : Int, description : String) { // Takes data from dialog and updates livedata
        val currentList = finItems.value?.toMutableList()
        val newItem = IncomeExpenseItem(amount,description)
        currentList?.add(newItem)
        finItems.value = currentList
    }

    private fun showAddItemDialog() {

        val alert = AlertDialog.Builder(this.context)
        alert.setTitle("New Income/Expense Item")

        val layout = LinearLayout(this.context)
        layout.orientation = LinearLayout.VERTICAL

        val description = EditText(this.context)
        description.hint = "Add Income/Expense Description"
        layout.addView(description)

        val krValue = EditText(this.context)
        krValue.hint = "Enter Income/Expense Amount"

        layout.addView(krValue)


        alert.setView(layout)

        alert.setPositiveButton(
            "Add Item"
        ) { dialog, whichButton ->
            val amount = krValue.text.toString().toInt()
            val text = description.text.toString()
            AddNewItem(amount,text)
        }


        alert.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton ->
            dialog.dismiss()
            // what ever you want to do with No option.
        }

        alert.show()
    }



}
