package com.rafaelfelipeac.improov.features.backup.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.formatToDate
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentBackupBinding
import com.rafaelfelipeac.improov.features.dialog.DialogOneButton
import com.rafaelfelipeac.improov.features.dialog.DialogTwoButtons
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Date

const val REQUEST_EXPORT = 1
const val REQUEST_IMPORT = 2

const val REQUEST_FILE = 1

@Suppress("TooManyFunctions")
class BackupFragment : BaseFragment() {

    private val viewModel by lazy { viewModelProvider.backupViewModel() }

    private var binding by viewBinding<FragmentBackupBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return FragmentBackupBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    private fun setScreen() {
        setTitle(getString(R.string.backup_title))
        showBackArrow()
        hideNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        setBehaviours()
        observeViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FILE && resultCode == RESULT_OK) {
            viewModel.importDatabase(getDatabase(data?.data))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_EXPORT -> {
                    viewModel.exportDatabase()
                }
                REQUEST_IMPORT -> {
                    openFile()
                }
            }
        } else {
            val dialog = DialogTwoButtons(
                getString(R.string.backup_permission_storage_settings_message),
                getString(R.string.backup_permission_storage_settings_negative),
                getString(R.string.backup_permission_storage_settings_positive)
            )

            dialog.setOnClickListener(object : DialogTwoButtons.OnClickListener {
                override fun onNegative() {
                    dialog.dismiss()
                }

                override fun onPositive() {
                    val intentDetails = Intent()
                    val uri = Uri.fromParts("package", activity?.packageName, null)
                    intentDetails.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intentDetails.data = uri
                    startActivity(intentDetails)

                    dialog.dismiss()
                }
            })

            dialog.show(parentFragmentManager, "")
        }
    }

    private fun setBehaviours() {
        binding.backupHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsBackup()
            setupBottomSheetTip()
            showBottomSheetTips()
        }

        binding.backupExportButton.setOnClickListener {
            if (checkPermissions()) {
                viewModel.exportDatabase()
            } else {
                requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_EXPORT)
            }
        }

        binding.backupImportButton.setOnClickListener {
            if (checkPermissions()) {
                openFile()
            } else {
                requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_IMPORT)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.exportDate.collect {
                if (it > 0) {
                    binding.backupExportDate.text = String.format(
                        getString(R.string.backup_date_export), Date(it).formatToDate(requireContext())
                    )
                    binding.backupExportDate.visible()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.importDate.collect {
                if (it > 0) {
                    binding.backupImportDate.text = String.format(
                        getString(R.string.backup_date_import), Date(it).formatToDate(requireContext())
                    )
                    binding.backupImportDate.visible()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.export.collect {
                if (it.isNotEmpty()) {
                    viewModel.getExportDate()

                    val file = saveFile(it)

                    showSnackBarWithAction(
                        requireView(),
                        getString(R.string.backup_export_success),
                        getString(R.string.backup_snackbar_action_save),
                        file,
                        ::shareFile
                    )
                } else {
                    showSnackBar(getString(R.string.backup_export_error))
                }
            }
        }

        lifecycleScope.launch {
            viewModel.import.collect {
                if (it) {
                    viewModel.getImportDate()

                    showSnackBar(getString(R.string.backup_import_success))
                } else {
                    showSnackBar(getString(R.string.backup_import_error))
                }
            }
        }
    }

    private fun shareFile(file: Any) {
        val path = FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.file_provider_path),
            (file as File)
        )

        val intentShareFile = Intent(Intent.ACTION_SEND)
        intentShareFile.type = getString(R.string.backup_file_type)
        intentShareFile.putExtra(Intent.EXTRA_STREAM, path)
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intentShareFile.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.backup_sharing_file_title)
        )
        intentShareFile.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.backup_sharing_file_description)
        )

        startActivity(Intent.createChooser(intentShareFile, getString(R.string.backup_sharing_file_choose)))
    }

    private fun getDatabase(data: Uri?): String {
        val path: Uri? = data
        val text = StringBuilder()

        try {
            val bufferedReader = BufferedReader(
                InputStreamReader(
                    path?.let { activity?.contentResolver?.openInputStream(it) }
                )
            )

            var line: String?

            while (bufferedReader.readLine().also { line = it } != null) {
                text.append(line)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return text.toString()
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
            val dialog = DialogOneButton(getString(R.string.backup_permission_storage_message))

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

    @Suppress("DEPRECATION")
    private fun saveFile(jsonDatabase: String): File {
        val file = File(
            Environment.getExternalStorageDirectory()?.path + getString(R.string.backup_file_path),
            getString(R.string.backup_file_name)
        )
        file.parentFile?.mkdirs()

        file.writeText(jsonDatabase)

        return file
    }

    private fun openFile() {
        var intentChooseFile = Intent(Intent.ACTION_GET_CONTENT)
        intentChooseFile.type = getString(R.string.backup_file_type)
        intentChooseFile = Intent.createChooser(
            intentChooseFile,
            getString(R.string.backup_open_file_title)
        )

        startActivityForResult(intentChooseFile, REQUEST_FILE)
    }
}
