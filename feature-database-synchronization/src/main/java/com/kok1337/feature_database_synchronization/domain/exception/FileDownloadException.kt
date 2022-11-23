package com.kok1337.feature_database_synchronization.domain.exception

class FileDownloadException(
    fileName: String,
) : Exception("Ошибка при скачивании файла $fileName")