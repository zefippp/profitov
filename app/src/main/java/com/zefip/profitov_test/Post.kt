package com.zefip.demo7877231

class Post(
    var type: String,
    var payload: Payload
)

class Payload(var text: String, var url: String)