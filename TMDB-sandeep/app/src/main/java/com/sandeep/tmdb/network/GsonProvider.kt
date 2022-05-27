package com.sandeep.tmdb.network

import com.sandeep.tmdb.network.serializer.LocalDateSerializerDeserializer
import com.sandeep.tmdb.network.serializer.LocalTimeSerializer
import com.sandeep.tmdb.network.serializer.ZonedDateTimeDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZonedDateTime


object GsonProvider {

    fun get(): Gson = this.gson

    private val gson by lazy {
        GsonBuilder()
                .registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
                .registerTypeAdapter(LocalDate::class.java, LocalDateSerializerDeserializer())
                .create()
    }
}