package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//Create a function to group everything that falls under the "/customer"
//endpoint. Then create a block for each HTTP method. This is one of the way we can do it.

//Note to self:
//All of this is very rudimentary and will fail in production

fun Route.customerRouting() {
    route("/customer") {
        //Will use this for every customer
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        //Targets specific customer
        get("{id?}") {
            //if the "call.parameters["id"] is not null, it returns it
            //if that is null, it will go ahead with the right hand side of the elvis operator
            //and return a 400 badrequest code
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                customerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            //Crazy statement. This will iterate over the customerStorage list and remove the id
            //if it exists in the list. This will return true if it does remove something.
            // And respond accordingly
            if (customerStorage.removeIf {it.id == id}) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

    }
}










