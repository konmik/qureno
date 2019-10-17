# Qureno - Queue-Reduce-Notify architecture for Android

Qureno is a simplified version of Redux.

## About

This architecture was in use for quite some time at our company and it shown itself as being reliable
and it requires lesser amount of code comparing to other Redux implementations.
This project contains my reimplementation of the architecture we were using, with all
of the core rewritten from scratch and most features simplified.

The main difference from Redux is that instead of middleware (wrapping functions) we're
using queue of actions PLUS "effect" functions that are used for I/O.

This approach trades flexibility for simplicity.
We want simpler code - it is easier to write, easier to read, modify or delete.
The goal is to have the minimum possible code maintenance cost.

Another strong point of Qureno is it's Component system out of the box.
We can define "update view" and "reduce" functions and get a Component - something
like a custom view from old days, but without inheritance and other horrors.
After we create a component we can use it as a brick to build more complex views.  

Qureno is proven to be enough for building Android applications of big complexity
(it's flexibility is even enough to run a stack of screens inside of a view).

Yes, testability is as good as it is in Redux - the best we can dream about.
Put data in, compare the output using simple Java comparison, done.
No need to create dependency spaghetti to test a function, ever.

Dependency injection frameworks, thanks to functional nature, are not needed.

Complex multithreading solutions, such as RxJava are also not needed - the logic is completely disconnected
from lifecycles so we do not need to keep subscriptions (do not need to unsubscribe) and
we do not need to consider view appearance at random moments. All networking code is a simple function
call without even callbacks.

Here is some typical code:

```
fun requestData(dispatch: Dispatch) {
    ioToUi({ request(arguments) }) { result ->
        result.onSuccess { dispatch(DataLoaded(it)) }
        result.onFailure { dispatch(DataLoadingFailure()) }
    }
}
```

Simple code like this is enough to handle multithreading without infamous Android memory leaks. 
There is just ONE subscription for the entire Qureno screen that will just be un/resubscribed
automatically and views will be updated with new data. 

# How it works

Every time an external event happens (a button gets pressed, backend returns a response, etc),
an `Action` object gets created and dispatched, like this:

```Kotlin
dispatch(OnSubmitClick(text = textView.text))
```

If an action gets dispatched while processing another action, it gets put into a queue.

The first thing after dispatching an action is *reduction*.
Reduction is a term from functional programming, it means we're processing a list and get a single value at the end.
In Redux we're processing a sequence of actions and the result if the actions is an object that we call *State*.
Inside State we put all the variables we normally keep inside of custom views, fragments and presenters.

Thanks to functional approach we can keep state as an immutable object,
this approach allows us to be safe when dealing with multithreading
and code becomes simpler overall. Here is an example of State and Reduce function:

```
data class State(val status: String)

data class OnDataLoaded(val items: List<String>) : Action

private fun State.reduce(action: Action): State =
    when (action) {
        OnDataLoaded -> copy(status = "Loaded: " + action.items.size)
        else -> this
    }
```

After updating state it comes the Notify phase - we're sending (new state + action) pair
to update views, start I/O operations and to subscribe to external event sources.

There are many notification handling functions that can be created here.

Here are examples:

```
private fun View.update(state: State, dispatch: Dispatch) {
    findViewById<TextView>(R.id.status).text = state.status
    findViewById<View>(R.id.reload_button).setOnClickListener {
        dispatch(OnReloadClick())
    }
}
```

```
private fun Node.effect(state: State, action: Action) {
    Toast.makeText(app, "Effect", Toast.LENGTH_SHORT).show()
}
```

There can be written a big amount of different Notify functions of different types, depending on use case.
After writing Notify functions for some screen area they can be packed and
combined together with Reduce function to form a `Component`.

Qureno `Component` system allows to write functions separately to update views and to do I/O operations.
This separation to View and I/O is caused by the temporary nature of Android views.
When View disappears I/O functions can continue working without taking lifecycles into consideration.
When View appears it will be recreated automatically by calling `View.crete` and `View.update` functions.

## Order-independence

It is a good practice to write Notify functions in order-independent way.

For example, a good-behaving `View.update` function must rebind *all* fields,
so the order in which it gets called becomes insignificant.
A properly created `View.update` function
will just bind state to view, no if/else attached - it will survive if actions were
called in different order, it will survive lifecycles and atomic war.

Order-independence is the main quality why functional programming is
considered to much more reliable than programs written in other styles.
By leveraging Redux architecture we're getting the a convenient tool
which allows writing order-independent code easy.

Reduce functions are order-independent in most cases.

## Dispatch instead of calling methods

Dispatching guarantees that there are no cases when two actions get handled simultaneously.

For example, it is pretty common to subscribe, change some variables, save data and redraw
something on the screen in response to user actions in usual Android applications.

This approach of doing many things at once leads of having code that is hard to follow
without debugger and a couple of glasses.

Qureno dispatcher makes sure that all actions gets processed sequentially, in strict order.
Component system encourages developers to also separate
functions that are doing I/O, view updates and state changes so they are not calling each other. 

The downside of dispatching instead of calling functions is that it relies on `when` condition.
It means that an action can be dispatched and there is no guarantee on compiler level that it will be
handled properly. Sat, if we dispatch `OnReloadClick` action and there is no
`if (action is OnReloadClick) blah()` code exists, the action will not lead to any changes.
In other architectures it is a given that when we're calling a function it actually gets executed.
There are probably ways to alleviate this issue, but they were not investigated yet. 
