package parameterless

fun main() { // no
}

@JvmName("main")
fun notMain(args: Array<String>) { // yes
}
