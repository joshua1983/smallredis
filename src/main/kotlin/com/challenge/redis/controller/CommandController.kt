package com.challenge.redis.controller


import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.service.CommandService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class CommandController {
    private val logger = LoggerFactory.getLogger(CommandController::class.java)

    @Autowired
    private lateinit var commandService: CommandService

    @GetMapping
    fun processCommand(@RequestParam cmd: String):ResultResponse{
        logger.info("Procensando comando [{}]", cmd)
        return commandService.processCommandREST(cmd)
    }

    @PutMapping("/{key}")
    @PostMapping("/{key}")
    fun postCommand(@PathVariable key: String, @RequestBody value: String): ResultResponse{
        logger.info("Procesando PUT [{}] ", key)
        return commandService.processCommandAPI("SET", value)
    }

    @DeleteMapping("/{key}")
    fun delCommand(@PathVariable key: String): ResultResponse{
        logger.info("Procesando DEL [{}] ", key)
        return commandService.processCommandREST("DEL " + key)
    }
}


