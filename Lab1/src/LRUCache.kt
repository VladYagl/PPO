class LRUCache<T>(val capacity: Int) {
    private val hashMap: HashMap<Int, Node> = HashMap()
    private var first: Node? = null
    private var last: Node? = null

    //Do we really need those?
    private var timer: Int = 0

    init {
        if (capacity <= 1) {
            throw Exception("Capacity must be positive")
        }
    }

    fun printPrivateInfo() {
        println("first = (${first?.key}, ${first?.value}) ---- last = (${last?.key}, ${last?.value})")
    }

    private inner class Node(val key: Int, var value: T) {
        var next: Node? = null
        var prev: Node? = null
    }

    private fun addNode(key: Int, value: T): Node {
        val node = Node(key, value)
        node.next = first

        if (last == null) {
            last = node
        }
        first?.prev = node
        first = node

        return node
    }

    private fun insertNode(node: Node) {
        node.next = first
        node.prev = null
        first?.prev = node
        first = node
        if (last == null) {
            last = node
        }
    }

    private fun removeNode(node: Node) {
        if (node == last) {
            last = node.prev
        }
        if (node == first) {
            first = node.next
        }
        node.prev?.next = node.next
        node.next?.prev = node.prev
    }

    fun put(key: Int, value: T) {
        assert(hashMap.size <= capacity)
        val assertSize = hashMap.size

        if (!hashMap.containsKey(key)) {
            val node = addNode(key, value)

            if (hashMap.size == capacity) {
                hashMap.remove(last!!.key)
                removeNode(last!!)
            }
            hashMap[key] = node
        } else {
            hashMap[key]!!.value = value
        }


        assert(first != null)
        assert(last != null)
        assert(hashMap.size in 1..capacity)
        assert(hashMap.size >= assertSize)
        assert(first!!.key == key)
    }

    fun get(key: Int): T {
        assert(hashMap.size <= capacity)

        val node = hashMap[key] ?: throw NoSuchElementException()
        removeNode(node)
        insertNode(node)

        assert(first != null)
        assert(last != null)
        assert(hashMap.size in 1..capacity)
        assert(first!!.key == key)
        return node.value
    }
}