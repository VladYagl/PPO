package market

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import rx.Observable

class DB {
    private val database = createMongoClient().getDatabase("silk_road")

    private fun createMongoClient(): MongoClient {
        return MongoClients.create("mongodb://localhost:27017")
    }

    fun insertUser(user: User): Observable<Success> {
        return database.getCollection("user").insertOne(user.toDoc())
    }

    fun findUser(login: String): Observable<User> {
        return database.getCollection("user").find(
            Filters.eq(
                "login",
                login
            )
        ).first().map { user ->
            User(user)
        }
    }

    fun insertGoods(goods: Goods): Observable<Success> {
        return database.getCollection("goods").insertOne(goods.toDoc())
    }

    fun getGoods(): Observable<Goods> {
        return database.getCollection("goods").find().toObservable().map { goods -> Goods(goods) }
    }
}