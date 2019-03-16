package market

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.QueryStringDecoder
import io.reactivex.netty.channel.ContentSource
import io.reactivex.netty.protocol.http.server.HttpServer
import rx.Observable
import java.io.File
import java.nio.charset.Charset

private val index = File("res/template.html").readText()

fun ContentSource<ByteBuf>.mapParams(): Observable<Map<String, String>> {
    return this.map {
        val query = it.toString(Charset.defaultCharset())
        val decoder = QueryStringDecoder("?$query")
        decoder.parameters().mapValues { (_, value) -> value.first() }
    }
}

fun main(args: Array<String>) {
    val db = DB()
    HttpServer
        .newServer(8080)
        .start { req, resp ->
            println()
            println(req)
            val response = when (req.decodedPath) {
                "/" -> Observable.just(index)
                "/reg" -> {
                    req.content.mapParams().switchMap { params ->
                        db.insertUser(
                            User(
                                params["name"]!!,
                                params["login"]!!,
                                Currency.fromString(params["currency"]!!)
                            )
                        )
                    }.map {
                        index
                    }.defaultIfEmpty("Oops! can't register (same login)")
                }
                "/add" -> {
                    req.content.mapParams().switchMap { params ->
                        db.findUser(params["add_login"]!!).map { user ->
                            Goods(params["goods"]!!, params["cost"]!!.toDouble() * user.currency.value)
                        }
                    }.switchMap { goods ->
                        db.insertGoods(goods)
                    }.map {
                        index
                    }.defaultIfEmpty("Oops! can't add goods")
                }
                "/list" -> {
                    req.content.mapParams().switchMap { params ->
                        db.findUser(params["list_login"]!!).map { user -> user.currency }
                    }.switchMap { currency ->
                        db.getGoods().map {
                            Pair(it.name, it.cost / currency.value)
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

