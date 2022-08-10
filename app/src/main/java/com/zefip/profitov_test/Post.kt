package com.zefip.profitov_test

class Post(
    var type: String,
    var payload: Payload
)
class Payload(var text: String, var url: String)