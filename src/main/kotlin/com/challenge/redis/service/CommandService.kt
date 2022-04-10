package com.challenge.redis.service

import com.challenge.redis.core.ProcessCommand
import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URLDecoder

@Service
class CommandService {

    @Autowired
    private lateinit var commandProcessor: ProcessCommand


    fun processCommandREST(cmd: String): ResultResponse{
        val rawCommand = URLDecoder.decode(cmd, "UTF-8")
        val listCommands = rawCommand.split(" ")
        val paramRequest = ParamsRequest(listCommands.subList(1,listCommands.size))
        return commandProcessor.processCommand(listCommands[0], paramRequest)
    }

    fun processCommandAPI(cmd: String, value: String): ResultResponse{
        return commandProcessor.processCommand(cmd, ParamsRequest(listOf(value)))
    }

}