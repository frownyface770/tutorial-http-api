package com.example.models

import kotlinx.serialization.Serializable

@Serializable  //Generates JSON representation
// needed for the api responses automatically
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

val customerStorage = mutableListOf<Customer>()
