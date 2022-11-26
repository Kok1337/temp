package com.kok1337.feature_database_synchronization.domain.usecase

import android.content.Context
import android.content.Intent
import com.kok1337.feature_database_synchronization.di.qualifier.BackupFolder
import com.kok1337.feature_database_synchronization.di.qualifier.BackupName
import com.kok1337.feature_database_synchronization.di.qualifier.RestoreBackupScript
import java.io.File
import javax.inject.Inject

class RestoreBackupUseCase @Inject constructor(
    @RestoreBackupScript private val restoreBackupScript: String,
    private val convertPathToTermuxPathUseCase: ConvertPathToTermuxPathUseCase,
    @BackupFolder private val backupFolder: File,
    @BackupName private val backupName: String,
    private val context: Context,
) {
    companion object {
        private const val TERMUX_PACKAGE_NAME = "com.termux"
        private const val TERMUX_SERVICE_CLASS_NAME = "com.termux.app.RunCommandService"
        private const val RUN_COMMAND = "com.termux.RUN_COMMAND"
        private const val RUN_COMMAND_PATH = "com.termux.RUN_COMMAND_PATH"
        private const val RUN_COMMAND_ARGUMENTS = "com.termux.RUN_COMMAND_ARGUMENTS"
        private const val RUN_COMMAND_BACKGROUND = "com.termux.RUN_COMMAND_BACKGROUND"
    }

    fun invoke() {
        val intent = Intent()
        intent.setClassName(TERMUX_PACKAGE_NAME, TERMUX_SERVICE_CLASS_NAME)
        intent.action = RUN_COMMAND
        intent.putExtra(RUN_COMMAND_PATH, restoreBackupScript)
        val folderPath = convertPathToTermuxPathUseCase.invoke(backupFolder.path)
        val commandArguments = arrayOf(folderPath, backupName)
        intent.putExtra(RUN_COMMAND_ARGUMENTS, commandArguments)
        intent.putExtra(RUN_COMMAND_BACKGROUND, false)
        context.startService(intent)
    }
}