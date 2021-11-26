# Spring MVC RxJava handlers

> A Spring Boot starter for RxJava integration

[![Build Status](https://travis-ci.org/jmnarloch/rxjava-spring-boot-starter.svg?branch=master)](https://travis-ci.org/jmnarloch/rxjava-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/jmnarloch/rxjava-spring-boot-starter/badge.svg?branch=master&service=github)](https://coveralls.io/github/jmnarloch/rxjava-spring-boot-starter?branch=master)

## Setup

Add the Spring Boot starter to your project:

```xml
<dependency>
  <groupId>io.jmnarloch</groupId>
  <artifactId>rxjava-spring-boot-starter</artifactId>
  <version>2.0.0</version>
</dependency>
```

Note:
If you need RxJava 1.4.x support use version 1.0.0. For RxJava2 use 2.x.

## Usage

### Basic

Registers Spring's MVC return value handlers for `rx.Observable` and `rx.Single` types. You don't need to any longer use
blocking operations or assign the values to DeferredResult or ListenableFuture instead you can declare that your REST
endpoint returns Observable.

Example:

```
@RestController
public static class InvoiceResource {

    @RequestMapping(method = RequestMethod.GET, value = "/invoices", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Observable<Invoice> getInvoices() {

        return Observable.just(
                new Invoice("Acme", new Date()),
                new Invoice("Oceanic", new Date())
        );
    }
}
```

The `Observable` will wrap any produced results into a list and make it process through Spring's message converters.
In case if you need to return exactly one result you can use `rx.Single` instead. You can think of `rx.Single`
as counterpart of Spring's `DeferredResult` or `ListenableFuture`. Also with `rx.Single`, and unlike with `rx.Observable`
it is possible to return `ResponseEntity` in order to have the control of the HTTP headers or the status code of the
response.

Note: The `HandlerReturnValueHandler` for Observable uses 'toList' operator to aggregate the results, which
is not workable with really long infinitive running Observables, from which is not possible to unsubscribe.

In some scenarios when you want to have more control over the async processing you can use either `ObservableDeferredResult`
or `SingleDeferredResult`, those are the specialized implementation of `DeferredResult` allowing for instance of setting
the processing timeout per response.

### Server side events

Spring 4.2 introduced `ResponseBodyEmitter` for long-lived HTTP connections and streaming the response data. One of
available specialized implementations is `ObservableSseEmitter` that allows to send server side event produced
from `rx.Observable`.

Example:

```
@RestController
public static class Events {

    @RequestMapping(method = RequestMethod.GET, value = "/messages")
    public ObservableSseEmitter<String> messages() {
        return new ObservableSseEmitter<String>(
            Observable.just(
                "message 1", "message 2", "message 3"
            )
        );
    }
}
```

This will output:

```
data: message 1

data: message 2

data: message 3
```

The SSE can be conveniently consumed by a JavaScript client for instance.

## Properties

The only supported property is `rxjava.mvc.enabled` which allows to disable this extension.

```
rxjava.mvc.enabled=true # true by default
```

## License

Apache 2.0
