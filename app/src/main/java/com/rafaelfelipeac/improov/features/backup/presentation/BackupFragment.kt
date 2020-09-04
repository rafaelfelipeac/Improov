package com.rafaelfelipeac.improov.features.backup.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.DialogOneButton
import kotlinx.android.synthetic.main.fragment_backup.*
import java.io.File

const val REQUEST_EXPORT = 1
const val REQUEST_IMPORT = 2

class BackupFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<BackupViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_backup, container, false)
    }

    private fun setScreen() {
        setTitle(getString(R.string.backup_title))
        showBackArrow()
        hideNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBehaviours()
        observeViewModel()
    }

    private fun setBehaviours() {
        backupHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsBackup()
            setupBottomSheetTip()
            showBottomSheetTips()
        }

        backupButtonExport.setOnClickListener {
            if (checkPermissions()) {
                viewModel.exportDatabase()
            } else {
                requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_EXPORT)
            }
        }

        backupButtonImport.setOnClickListener {
            if (checkPermissions()) {
                viewModel.importDatabase(openFile())
            } else {
                requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_IMPORT)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.export.observe(this) {
            Log.d("CORINTHIANS", "JSON = $it")

            createFile(it)

            Toast.makeText(requireContext(), "Backup exported.", Toast.LENGTH_SHORT).show()

            // snackbar with button to open path?
        }

        viewModel.import.observe(this) {
            if (it) {
                Toast.makeText(requireContext(), "Backup imported with success.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error in import.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_EXPORT -> {
                    viewModel.exportDatabase()
                }
                REQUEST_IMPORT -> {
                    viewModel.importDatabase(openFile())
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun checkPermission(permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions(permission: String, requestCode: Int) {
        if (shouldShowRequestPermissionRationale(permission)) {
            val dialog = DialogOneButton("Storage permission")

            dialog.setOnClickListener(object : DialogOneButton.OnClickListener {
                override fun onOK() {
                    requestPermissions(
                        arrayOf(permission),
                        requestCode
                    )

                    dialog.dismiss()
                }
            })

            dialog.show(parentFragmentManager, "")
        } else {
            requestPermissions(
                arrayOf(permission),
                requestCode
            )
        }
    }

    private fun createFile(jsonDatabase: String) {
        val file = File(Environment.getExternalStorageDirectory().path + "/Improov/Backup/backup.txt")
        file.parentFile?.mkdirs()

        file.writeText(jsonDatabase)
    }

    private fun openFile() : String {
        return ""
    }
}
