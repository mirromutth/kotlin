// IGNORE_BACKEND: JS_IR
// TODO: muted automatically, investigate should it be ran for JS or not
// IGNORE_BACKEND: JS, NATIVE

// WITH_REFLECT

package test

class G<T>(val s: T) {

}

public interface ErrorsJvmTrait {
    companion object {
        public val param : G<String> = G("STRING")
    }
}

public class ErrorsJvmClass {
    companion object {
        @JvmField public val param : G<String> = G("STRING")
    }
}

fun box(): String {
    val genericTypeInClassObject = ErrorsJvmTrait.javaClass.getDeclaredField("param").getGenericType()
    if (genericTypeInClassObject.toString() != "test.G<java.lang.String>") return "fail1: $genericTypeInClassObject"

    val genericTypeInClass = ErrorsJvmClass::class.java.getField("param").getGenericType()
    if (genericTypeInClass.toString() != "test.G<java.lang.String>") return "fail1: genericTypeInClass"
    return "OK"
}
