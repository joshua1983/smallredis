package com.challenge.redis.command

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SetCommand : Command {

    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        if (request.options.size == 2) {
            return setValuesAndExecute(request.options)
        } else if (request.options.size > 2 && request.options[2].equals("EX")) {
            return verifyTimeAndSetValuesAndExecute(request.options)
        }
        return ResultResponse(
            response = "Opcion no valida para el comando SET",
            error = true
        )
    }

    override fun getName(): String {
        return "SET"
    }

    fun setValuesAndExecute(keyValues: List<String>): ResultResponse {
        var key = keyValues[0]
        var value = keyValues[1]
        try {
            dataService.insertData(key, value)
        } catch (e: Exception) {
            return ResultResponse(
                response = "Error de ejecucion: " + e.message,
                error = true
            )
        }
        return ResultResponse(
            response = "Dato registrado",
            error = false
        )
    }

    fun verifyTimeAndSetValuesAndExecute(keyValues: List<String>): ResultResponse {
        var key = keyValues[0]
        var value = keyValues[1]
        if (keyValues.size == 4) {
            try {
                var seconds = keyValues[3].toInt()
                dataService.insertData(key, value, seconds)
            } catch (e: Exception) {
                return ResultResponse(
                    response = "Error de ejecucion: " + e.message,
                    error = true
                )
            }
            return ResultResponse(
                response = "Dato registrado",
                error = false
            )
        } else {
            return ResultResponse(
                response = "Opcion no válida",
                error = true
            )
        }

    }

}