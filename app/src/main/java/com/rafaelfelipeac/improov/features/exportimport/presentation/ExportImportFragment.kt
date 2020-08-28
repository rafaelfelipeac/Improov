package com.rafaelfelipeac.improov.features.exportimport.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_export_import.*

class ExportImportFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setScreen()

        return inflater.inflate(R.layout.fragment_export_import, container, false)
    }

    private fun setScreen() {
        setTitle(getString(R.string.export_import_title))
        showBackArrow()
        hideNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBehaviours()
    }

    private fun setBehaviours() {
        exportImportHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsDivideAndConquer()
            setupBottomSheetTip()
            showBottomSheetTips()
        }
    }
}
