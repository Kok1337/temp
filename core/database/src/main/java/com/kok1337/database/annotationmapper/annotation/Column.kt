package com.kok1337.database.annotationmapper.annotation

import java.lang.annotation.Inherited

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class Column(val value: String = "")