package com.example.routes

import com.example.models.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.RoundingMode

//Things to be aware of:
//Predicates. Used in filtering functions such as find, filter etc
//In general it is a statement about something that is either true or false.
//In programming, predicates represent single argument functions that return a boolean value.
//We can see the use of this in the routes functions, where we try to find the id
//using a lambda function (anonymous function).
//fun Route.listOrdersRoute() {
//    get("/order") {
//        if (orderStorage.isNotEmpty()) {
//            call.respond(orderStorage)
//        }
//    }
//}
//fun Route.getOrderRoute() {
//    get("/order/{id?}") {
//        val id = call.parameters["id"] ?: return@get call.respondText(
//            "Bad request",
//            status = HttpStatusCode.BadRequest)
//        //orderStorage.find returns the element itself if it does find it or null if it doesn't
//        //Instead of returning a boolean. So if its null it will return not found.
//        val order = orderStorage.find {it.number == id} ?: return@get call.respondText(
//            "Not Found",
//            status = HttpStatusCode.NotFound
//        )
//        call.respond(order)
//    }
//}
//fun Route.totalizeOrderRoute() {
//    get("/order/{id?}/total") {
//        val id = call.parameters["id"] ?: return@get call.respondText(
//            "Bad request",
//            status = HttpStatusCode.BadRequest
//        )
//        val order = orderStorage.find { it.number == id} ?: return@get call.respondText(
//            "Not Found",
//            status = HttpStatusCode.NotFound
//        )
//        val total = order.contents.sumOf {item -> item.price * item.amount}
//        call.respond(total)
//
//    }
//}
class OrderRoutes {
    fun setupOrderRoutes(route: Route) {
        route.route("/order") {
            listOrdersRoute()
            getOrderRoute()
            totalizeOrderRoute()
            createOrder()
        }
    }
    private fun Route.listOrdersRoute() {
        get {
            if (orderStorage.isNotEmpty()) {
                call.respond(orderStorage)
            }else {
                call.respondText("No orders found", status = HttpStatusCode.OK)
            }
        }
    }
    private fun Route.getOrderRoute() {
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Bad request",
                status = HttpStatusCode.BadRequest)
            //orderStorage.find returns the element itself if it does find it or null if it doesn't
            //Instead of returning a boolean. So if its null it will return not found.
            val order = orderStorage.find {it.number == id} ?: return@get call.respondText(
                "Not Found",
                status = HttpStatusCode.NotFound
            )
            call.respond(order)
        }
    }
    private fun Route.createOrder() {
        post {
            val order = call.receive<Order>()
            orderStorage.add(order)
            call.respondText("Order stored correctly", status = HttpStatusCode.Created)
        }
    }


    private fun Route.totalizeOrderRoute() {
        get("{id?}/total") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Bad request",
                status = HttpStatusCode.BadRequest
            )
            val order = orderStorage.find { it.number == id} ?: return@get call.respondText(
                "Not Found",
                status = HttpStatusCode.NotFound
            )
            val total = order.contents.sumOf {item -> item.price * item.amount}
            val roundedTotal = total.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            call.respond(roundedTotal)

        }
    }


}