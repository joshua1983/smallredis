package com.challenge.redis.command

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DelCommand :Command {
    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        if (request.options.isNotEmpty()){
            var key = request.options[0]
            if (dataService.existData(key)){
                dataService.removeData(key)
                return ResultResponse(
                    response = "Contenido eliminado.",
                    error = false
                )
            } else {
                return ResultResponse(
                    response = "No existe un item con esa llave.",
                    error = true
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
        return "DEL"
    }
}