package com.example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.runtime.Micronaut.build
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.example")
		.start()
}

@Controller
class Controller {

	@Get("/serial", produces = [MediaType.APPLICATION_JSON])
	suspend fun serial() =
		(0..5).map {
			println("Processing (serial): $it ...")
			process(it)
		}

	@Get("/parallel", produces = [MediaType.APPLICATION_JSON])
	suspend fun parallel() = coroutineScope {
		(0..5).map {
			async {
				println("Processing (parallel): $it ...")
				process(it)
			}
		}.awaitAll()
	}

	private suspend fun process(i: Int): Int {
		delay(100)
		return i
	}

}