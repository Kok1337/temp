package com.kok1337.database.annotationmapper.exception

class ItemNotFoundByParamException(clazz: Class<*>, paramName: String, paramValue: Any) :
    Exception("Item ${clazz.name} with $paramName=$paramValue not found.")