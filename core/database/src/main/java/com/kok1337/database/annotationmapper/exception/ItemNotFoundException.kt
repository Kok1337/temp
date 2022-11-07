package com.kok1337.database.annotationmapper.exception

class ItemNotFoundException(clazz: Class<*>) :
    Exception("Item ${clazz.name} not found.")