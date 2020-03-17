package com.rafaelfelipeac.improov.future.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.features.main.MainActivity
import kotlinx.android.synthetic.main.bottom_sheet_add.*

class AddFragment : BottomSheetDialogFragment() {

    val navController get() = (activity as MainActivity).navController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_button_goal.setOnClickListener {
            navController.navigate(AddFragmentDirections.actionNavigationAddToNavigationGoalForm())
        }

        add_button_habit.setOnClickListener {
            navController.navigate(AddFragmentDirections.actionNavigationAddToNavigationHabitForm())
        }
    }
}