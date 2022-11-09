package com.kok1337.feature_database_preparation.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.kok1337.feature_database_preparation.di.qualifier.TermuxPackage
import javax.inject.Inject

class DeleteTermuxUseCase @Inject constructor(
    @TermuxPackage private val termuxPackage: String,
    private val context: Context,
) {
    fun invoke() {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$termuxPackage")
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }
}