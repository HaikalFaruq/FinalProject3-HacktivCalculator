
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.register

fun <T> NamedDomainObjectContainer<T>.benchmark(configurationAction: Action<in T>) {
    register("benchmark", configurationAction)
}
