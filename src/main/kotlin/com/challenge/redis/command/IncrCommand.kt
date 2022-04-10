package com.challenge.redis.command

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.NumberFormatException
import kotlin.ClassCastException

@Component
class IncrCommand : Command {

    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        if (request.options.isNotEmpty()){
            var key = request.options[0]
            if (dataService.existData(key)){
                var content = dataService.getData(key)
                var increment = 0
                try {
                    increment = content.value.toInt() + 1
                } catch (e: Exception){
                    when(e){
                        is NumberFormatException -> increment = 0
                        is ClassCastException -> increment = 0
                    }
                }
                content.value = if (increment == 0) 0.toString() else increment.toString()
                dataService.insertData(key, content.value)
                return ResultResponse(
                    response = content.value,
                    error = false
                )
            } else {
                dataService.insertData(key, "0")
                return ResultResponse(
                    response = "0",
                    error = false
                )
            }
        } else {
            return ResultResponse(
                response = "Opcion no válida",
                error = true
            )
        }
    }

    override fun getName(): String {
        return "INCR"
    }
}