package com.challenge.redis.interfaces

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse

interface Command {
    fun execute(request: ParamsRequest): ResultResponse
    fun getName(): String
}