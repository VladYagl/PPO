package market

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoClient
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.QueryStringDecoder
import io.reactivex.netty.channel.ContentSource
import io.reactivex.netty.protocol.http.server.HttpServer
import org.bson.Document
import rx.Observable
import java.io.File
import java.nio.charset.Charset

private val index = File("res/template.html").readText()

private val database = createMongoClient().getDatabase("silk_road")

private fun createMongoClient(): MongoClient {
    return MongoClients.create("mongodb://localhost:27017")
}

private val currency = mapOf(
    "usd" to 1.0,
    "eur" to 1.13,
    "rub" to 0.015
)

fun ContentSource<ByteBuf>.mapParams(): Observable<Map<String, String>> {
    return this.map {
        val query = it.toString(Charset.defaultCharset())
        val decoder = QueryStringDecoder("?$query")
        decoder.parameters().mapValues { (_, value) -> value.first() }
    }
}

fun main(args: Array<String>) {
    HttpServer
        .newServer(8080)
        .start { req, resp ->
            println()
            println(req)
            val response = when (req.decodedPath) {
                "/" -> Observable.just(index)
                "/reg" -> {
                    req.content.mapParams().switchMap { params ->
                        database.getCollection("user").insertOne(
                            Document()
                                .append("name", params["name"])
                                .append("login", params["login"])
                                .append("currency", params["currency"])
                        )
                    }.map {
                        index
                    }.defaultIfEmpty("Oops! can't register (same login)")
                }
                "/add" -> {
                    req.content.mapParams().switchMap { params ->
                        database.getCollection("user").find(eq("login", params["add_login"]))
                            .first().map { user ->
                                Pair(params["cost"]!!.toDouble() * currency[user["currency"]]!!, params["goods"])
                            }
                    }.switchMap { goods ->
                        database.getCollection("goods").insertOne(
                            Document()
                                .append("name", goods.second)
                                .append("cost", goods.first)
                        )
                    }.map {
                        index
                    }.defaultIfEmpty("Oops! can't add goods")
                }
                "/list" -> {
                    req.content.mapParams().switchMap { params ->
                        database.getCollection("user").find(eq("login", params["list_login"]))
                            .first().map { user ->
                                currency[user["currency"]]!!
                            }
                    }.switchMap { currency ->
                        database.getCollection("goods").find().toObservable().map {
                            Pair(it.getString("name"), it.getDouble("cost") / currency)
                        }
                    }.map {
                        it.first + " -- " + it.second.toString() + "\n"
                    }.defaultIfEmpty("Oops! can't list goods (no such user)")
                }
                else -> Observable.just("Oops! No such page ")
            }
            resp.writeString(response)
        }
        .awaitShutdown()
}

