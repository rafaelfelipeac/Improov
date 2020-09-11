package com.rafaelfelipeac.improov.features.backup.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.dialog.DialogOneButton
import kotlinx.android.synthetic.main.fragment_backup.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader

const val REQUEST_EXPORT = 1
const val REQUEST_IMPORT = 2

const val REQUEST_FILE = 1

@Suppress("TooManyFunctions")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FILE && resultCode == RESULT_OK) {
            viewModel.importDatabase(getDatabase(data?.data!!))
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
        }
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
                openFile()
            } else {
                requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_IMPORT)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.export.observe(this) {
            Log.d("CORINTHIANS", "JSON = $it")

            val file = saveFile(it)

            showSnackBarWithAction(
                requireView(),
                getString(R.string.backup_exported),
                getString(R.string.backup_snackbar_action_share),
                file, ::shareFile
            )
            // update screen with file path
        }

        viewModel.import.observe(this) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.backup_imported),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.backup_imported_error),
                    Toast.LENGTH_SHORT
                ).show()
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
        intentShareFile.putExtra(Intent.EXTRA_STREAM, path)
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.backup_sharing_file_title))
        intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(R.string.backup_sharing_file_description))
        intentShareFile.type = getString(R.string.backup_file_type)

        startActivity(intentShareFile)
    }

    private fun getDatabase(data: Uri?): String {
        val path: Uri = data!!
        val text = StringBuilder()

        try {
            val bufferedReader = BufferedReader(
                InputStreamReader(
                    activity?.contentResolver?.openInputStream(path)!!
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
                Environment.getExternalStorageDirectory()?.path!! + getString(R.string.backup_file_path),
                getString(R.string.backup_file_name))
        file.parentFile?.mkdirs()

        file.writeText(jsonDatabase)

        return file
    }

    private fun openFile() {
        var intentChooseFile = Intent(Intent.ACTION_GET_CONTENT)
        intentChooseFile.type = getString(R.string.backup_file_type)
        intentChooseFile = Intent.createChooser(intentChooseFile, getString(R.string.backup_open_file_title))

        startActivityForResult(intentChooseFile, REQUEST_FILE)
    }
}
