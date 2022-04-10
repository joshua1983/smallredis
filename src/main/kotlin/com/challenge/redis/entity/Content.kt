package com.challenge.redis.entity

data class Content(var value: String, var timestamp: Long, var expire: Int) {
}