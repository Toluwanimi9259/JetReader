package com.techafresh.jetreader.util


class Wrapper<T, Boolean, E : Exception>(
    var data : T? = null,
    var loading : Boolean? = null,
    var e: E? = null
) {

}