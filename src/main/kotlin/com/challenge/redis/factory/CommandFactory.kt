package com.challenge.redis.factory

import com.challenge.redis.interfaces.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommandFactory @Autowired constructor(commands: Set<Command>) {

    private val mapCommands = hashMapOf<String, Command>()

    init {
        createMapCommands(commands)
    }

    fun createMapCommands(commands: Set<Command>){
        commands.forEach {
            command -> mapCommands[command.getName()] = command
        }
    }

    fun getCommandByName(name: String): Command?{
        return mapCommands[name]
    }
}