package qureno.core

interface Action

object NoAction : Action {
    override fun equals(other: Any?): Boolean = this === other || javaClass == other?.javaClass
    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "NoAction()"
}
