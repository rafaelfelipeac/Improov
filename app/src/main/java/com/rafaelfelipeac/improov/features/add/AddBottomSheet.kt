package com.rafaelfelipeac.improov.features.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.databinding.BottomSheetAddBinding
import com.rafaelfelipeac.improov.features.main.MainActivity

class AddBottomSheet : BottomSheetDialogFragment() {

    private var binding by viewBinding<BottomSheetAddBinding>()

    val navController get() = (activity as MainActivity).navController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return BottomSheetAddBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBehaviours()
    }

    private fun setupBehaviours() {
        binding.addButtonGoal.setOnClickListener {
            navController.navigate(AddBottomSheetDirections.addToGoalForm())
        }

        binding.addButtonHabit.setOnClickListener {
            navController.navigate(AddBottomSheetDirections.addToHabitForm())
        }
    }
}
