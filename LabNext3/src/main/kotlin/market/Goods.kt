package market

import org.bson.Document

class Goods(val name: String, val cost: Double) {
    constructor(doc: Document) : this(
        doc.getString("name"),
        doc.getDouble("cost")
    )

    fun toDoc(): Document {
        return Document()
            .append("name", name)
            .append("cost", cost)
    }
}