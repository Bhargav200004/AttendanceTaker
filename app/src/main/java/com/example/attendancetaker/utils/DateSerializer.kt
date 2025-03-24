package com.example.attendancetaker.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateSerializer : KSerializer<Date>{

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale.getDefault() )

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date" , PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return formatter.parse(decoder.decodeString()) ?: throw IllegalArgumentException("Invalid Date format")
    }



}