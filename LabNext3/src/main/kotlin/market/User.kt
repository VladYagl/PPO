package market

import org.bson.Document

class User(val name: String, val login: String, val currency: Currency) {
    constructor(doc: Document) : this(
        doc.getString("name"),
        doc.getString("login"),
        Currency.fromString(doc.getString("currency"))
    )

    fun toDoc(): Document {
        return Document()
            .append("name", name)
            .append("login", login)
            .append("currency", currency.abr)
    }
}