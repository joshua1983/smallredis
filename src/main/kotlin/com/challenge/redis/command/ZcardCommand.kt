package com.challenge.redis.command

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ZcardCommand : Command {
    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        if (request.options.isNotEmpty()) {
            var key = request.options[0]
            if (dataService.existData(key)){
                var data = dataService.getData(key)
                var dataFromJson = Gson().fromJson(data.value,HashMap<Int,String>()::class.java)

                return ResultResponse(
                    response = dataFromJson.size.toString(),
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
        return "ZCARD"
    }
}