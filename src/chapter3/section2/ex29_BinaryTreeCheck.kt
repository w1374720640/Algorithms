package chapter3.section2

/**
 * 二叉树检查
 * 编写一个递归的方法isBinaryTree()，接受一个结点Node为参数
 * 如果以该结点为根的子树中的结点总数和计数器的值N相符则返回true，否则返回false
 * 注意：这项检查也能保证数据结构中不存在环，因此这的确是一颗二叉树
 */
fun <K : Comparable<K>> isBinaryTree(node: BinarySearchTree.Node<K, *>): Boolean {
    val leftIsTree = when {
        node.left == null -> true
        //防止子树中有环
        node.count <= node.left!!.count -> false
        else -> isBinaryTree(node.left!!)
    }
    if (!leftIsTree) return false
    val rightIsTree = when {
        node.right == null -> true
        node.count <= node.right!!.count -> false
        else -> isBinaryTree(node.right!!)
    }
    if (!rightIsTree) return false
    val leftCount = node.left?.count ?: 0
    val rightCount = node.right?.count ?: 0
    return node.count == leftCount + rightCount + 1
}

fun main() {
    val charArray = "EASYQUESTION".toCharArray()
    val bst = BinarySearchTree<Char, Int>()
    for (i in charArray.indices) {
        bst.put(charArray[i], i)
    }
    println(isBinaryTree(bst.root!!))

    val rightCount = bst.root?.right?.count!!
    //计数器值错误
    bst.root?.right?.count = 2
    println(isBinaryTree(bst.root!!))

    bst.root?.right?.count = rightCount
    //有环
    bst.root?.right?.right = bst.root?.right
    println(isBinaryTree(bst.root!!))
}