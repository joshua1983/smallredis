package com.challenge.redis.command

import com.challenge.redis.entity.Content
import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetCommand : Command {
    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        if (request.options.isNotEmpty()){
            var key = request.options[0]
            if (dataService.existData(key)){
                var content = dataService.getData(key)
                if (contentExpire(content) || content.expire < 0){
                    return ResultResponse(
                        response = content.value,
                        error = false
                    )
                } else {
                    dataService.removeData(key)
                    return ResultResponse(
                        response = "El dato ya expiró",
                        error = false
                    )
                }
            } else{
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

    fun contentExpire(content: Content): Boolean{
        var currentMiliseconds = System.currentTimeMillis()
        return (currentMiliseconds - content.timestamp) < (content.expire * 1000)
    }

    override fun getName(): String {
        return "GET"
    }
}