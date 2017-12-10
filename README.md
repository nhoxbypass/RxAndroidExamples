# RxAndroidEx

[![Build Status](https://travis-ci.org/nhoxbypass/RxAndroidEx.svg?branch=master)](https://travis-ci.org/nhoxbypass/RxAndroidEx) 

Series of example designed to convery a basic understanding of Reactive Programming using [rxJava](https://github.com/ReactiveX/RxJava) & [rxAndroid](https://github.com/ReactiveX/RxAndroid)

What is Reactive Programming?
-------

 - Reactive programming is an extension of the Observer software design pattern, where an object has a list of Observers that are dependent on it, and these Observers are notified by the object whenever it’s state changes.

 - In computing, reactive programming is an asynchronous programming paradigm concerned with data streams and the propagation of change. 

   So why do we need Asynchronous work? The simple answer is we want to improve the user experience. We want to make our application more responsive. We want to deliver a smooth user experience to our users without freezing the main thread, slowing them down and we don’t want to provide the jenky performance to our users.

**Reactive code can simplify the process of writing async code.**

It is hard to explain reactive programming in a nutshell. Well, at least I couldn’t find any. You can say that reactive programming is an Observer template on steroids. Or rather, reactive programming is programming aimed at flows. The main idea is in presenting events and data as flows that can be unified, filtered, transformed, and separated. Sounds quite vague but I hope some of the examples below will help make sense of it.

What is Rx?
-------

Despite being a fairly new paradigm, reactive programming is receiving quite a major distribution. Libraries allowing to write reactive code have been implemented for multiple languages. Some of the most popular among such libraries are RxJava, RxKotlin, RxSwift, RxJS, etc.

In simple words, In Rx programming data flows emitted by one component and the underlying structure provided by the Rx libraries will propagate those changes to another component those are registered to receive those data changes. Long story short: Rx is made up of three key points.

**RX = OBSERVABLE + OBSERVER + SCHEDULERS**

So in this repository, we are going to discuss these points in detail one by one.


## Table of Contents

- [Example 1: First step in to rxJava and rxAndroid using Observable.just()](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example1Activity.java)
- [Example 2: Async network call using Observable.callable()](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example2Activity.java)
- [Example 3: Async network call using Single.callable()](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example3Activity.java)
- [Example 4: Click counter using Subject](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example4Activity.java)
- [Example 5: Convert number value stream to String value stream using map() function](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example5Activity.java)
- [Summary 1: Cities name search box](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Summary1Activity.java)
  * [debounce() operator](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Summary1Activity.java#L73)
  * [map() function](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Summary1Activity.java#L75)
   * [Schedulers](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Summary1Activity.java#L88)
  * [Publish subject](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Summary1Activity.java#L98)
  * [Observable flow](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Summary1Activity.java#L125)
- [Example 6: Rx Schedulers](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example6Activity.java)
- [Example 7: Hot/cold observable](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example7Activity.java)
- [Example 8: Async network call using Retrofit combined with Observable](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example8Activity.java)
- [Example 9: Avoid memory leaking using RxLifeCycle](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example9Activity.java)
- [Example 10: Make Android's UI more reactive using RxBinding](https://github.com/nhoxbypass/RxAndroidEx/blob/master/app/src/main/java/iceteaviet/com/rxandroidex/Example10Activity.java)
- [About](#about)

About
-------
This RxAndroidExample repository is created and maintained by [nhoxbypass](https://www.facebook.com/hieutam22) - an junior mobile engineer - owner of [iceteaviet](iceteaviet.com).


License
-------

Copyright 2017 iceteaviet

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
