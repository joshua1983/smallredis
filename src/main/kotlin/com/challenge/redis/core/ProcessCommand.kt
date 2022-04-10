package com.challenge.redis.core

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.factory.CommandFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProcessCommand {

    @Autowired
    private lateinit var commandFactory: CommandFactory

    fun processCommand(command: String, params: ParamsRequest): ResultResponse {
        return commandFactory.getCommandByName(command)?.execute(params) ?: ResultResponse(
            response = "Comando no válido.",
            error = true
        )
    }
}