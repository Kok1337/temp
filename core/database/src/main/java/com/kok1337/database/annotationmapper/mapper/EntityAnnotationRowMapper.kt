package com.kok1337.database.annotationmapper.mapper

import com.kok1337.database.annotationmapper.annotation.Column
import com.kok1337.database.annotationmapper.annotation.Entity
import com.kok1337.database.annotationmapper.exception.ModelWithoutAnnotationError
import com.kok1337.database.annotationmapper.exception.NoEmptyConstructorError
import com.kok1337.database.annotationmapper.exception.NullColumnError
import java.lang.reflect.Field
import java.sql.ResultSet

internal class EntityAnnotationRowMapper<T>(private val clazz: Class<T>) {
    private val allFieldWithColumnAnnotation: Set<Field>
        get() {
            val fieldSet = mutableSetOf<Field>()
            var current: Class<*> = clazz
            while (current.superclass != null) {
                val fields = current.declaredFields
                fieldSet.addAll(fields.filter { it.isAnnotationPresent(Column::class.java) })
                current = current.superclass
            }
            return fieldSet
        }

    // Если имя в аннотации совпадает с названием колонки или
    // Если не указали имя в аннотиации - проверяем на совпадение с именем переменной
    private fun canSetValue(column: Column, mdColumnName: String, field: Field): Boolean {
        return column.value.equals(mdColumnName, ignoreCase = true)
                || column.value == "" && field.name.equals(mdColumnName, ignoreCase = true)
    }

    @Throws(IllegalAccessException::class)
    private fun setValueToField(item: T, field: Field, value: Any?) {
        field.isAccessible = true
        field[item] = value
    }

    fun map(resultSet: ResultSet): T? {
        if (!clazz.isAnnotationPresent(Entity::class.java))
            throw ModelWithoutAnnotationError(clazz)
        val metaData = resultSet.metaData
        val annotatedFieldSet = allFieldWithColumnAnnotation
        return try {
            val item = clazz.getDeclaredConstructor().newInstance() as T
            for (i in 1..metaData.columnCount) {
                val mdColumnName = metaData.getColumnName(i)
                val columnValue = resultSet.getObject(i)
                annotatedFieldSet.forEach { field ->
                    val column =
                        field.getAnnotation(Column::class.java) ?: throw NullColumnError(field)
                    if (canSetValue(column, mdColumnName, field)) {
                        setValueToField(item, field, columnValue)
                    }
                }
            }
            item
        } catch (e: IllegalAccessException) {
            throw NoEmptyConstructorError(clazz)
        } catch (e: InstantiationException) {
            throw NoEmptyConstructorError(clazz)
        }
    }
}