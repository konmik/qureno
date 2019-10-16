# Qureno - Queue-Reduce-Notify architecture for Android

Qureno is a simplified version of Redux.

It was in use for quite some time at our company and it shown itself as being reliable
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
