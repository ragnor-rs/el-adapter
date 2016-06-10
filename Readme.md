ElAdapter
=========
[![](https://img.shields.io/badge/JitPack-v0.1-blue.svg?style=flat)](https://jitpack.io/#m039/el-adapter) [![Build Status](https://travis-ci.org/m039/el-adapter.svg?branch=develop)](https://travis-ci.org/m039/el-adapter)

ElAdapter library allows you to decouple RecylerView's adapters code in more handy, simple and beatiful way.

If you have a lot of adapters in your project you should definitely give it a try!

Features
--------

pros:
* modular code
* flyent interface for creating and binding views
* decoupling all inner logic of RecylerView's adapter to interfaces
* very easy to use
* generics

cons:
* early stage of development (not all functions are implemented)

Installation
------------

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add the dependency:

```gradle
dependencies {
    compile 'com.github.m039:el-adapter:v0.1'
}
```

Example
-------

I bet you won't write any shorter:

```java
ListItemAdapter listAdapter = new ListItemAdapter();

listAdapter
  .addViewCreator(String.class, parent -> new TextView(getActivity()))
  .addViewBinder((view, item) -> view.setText(item));

listAdapter
  .addItem("Hello");
  
recyclerView.setAdapter(listAdapter);
```

ElAdapter is intended to use with java 8 (or retrolambda)

License
-------

Copyright (c) 2016 Dmitry Mozgin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
