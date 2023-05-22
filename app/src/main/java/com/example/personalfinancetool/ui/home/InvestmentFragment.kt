package com.example.personalfinancetool.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.personalfinancetool.databinding.FragmentInvestmentBinding
import com.example.personalfinancetool.ui.Budgeting.BudgetingViewModel

class InvestmentFragment : Fragment() {
    val budgetViewModel : BudgetingViewModel by activityViewModels()

    private var _binding: FragmentInvestmentBinding? = null

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

        binding.textHome.text = budgetViewModel.disposableIncome.value.toString()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}