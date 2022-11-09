package com.kok1337.feature_database_preparation.domain.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.kok1337.feature_database_preparation.di.qualifier.*
import net.lingala.zip4j.ZipFile
import java.io.File
import javax.inject.Inject

class InstallTermuxUseCase @Inject constructor(
    @DirectoryToCopy private val directory: File,
    @InstallerArchive private val installerArchive: File,
    @ProviderPath private val providerPath: String,
    private val context: Context,
    @ApplicationId private val applicationId: String,
) {
    fun invoke() {
        val termuxApkName =
            ZipFile(installerArchive).fileHeaders.map { it.fileName }.first { it.contains(".apk") }
        val termuxApkFile = File(directory, termuxApkName)
        val authority = applicationId + providerPath
        val contentUri = FileProvider.getUriForFile(context, authority, termuxApkFile)
        val install = Intent(Intent.ACTION_VIEW)
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        install.data = contentUri
        startActivity(context, install, null)
    }
}