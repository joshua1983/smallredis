package com.challenge.redis.service

import com.challenge.redis.entity.Content
import java.util.concurrent.ConcurrentHashMap

class DataSource private constructor() : ConcurrentHashMap<String, Content>() {
    companion object{
        val instance: ConcurrentHashMap<String, Content> by lazy { ConcurrentHashMap<String, Content>() }
    }
}