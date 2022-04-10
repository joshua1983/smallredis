package com.challenge.redis.command

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.NumberFormatException

@Component
class ZaddCommand : Command {
    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        if (request.options.isNotEmpty()){
            var key = request.options[0]
            var dataMap: HashMap<Int, String> =  HashMap()
            var scoreMembers = request.options.subList(1,request.options.size)
            scoreMembers.forEachIndexed{ index, element ->
                if (isNumber(element)){
                    dataMap.put(element.toInt(), scoreMembers[index+1])
                }
            }
            dataService.insertData(key,Gson().toJson(dataMap))
            return ResultResponse(
                response = scoreMembers.joinToString(" "),
                error = false
            )
        } else {
            return ResultResponse(
                response = "Opcion no válida",
                error = true
            )
        }
    }

    override fun getName(): String {
        return "ZADD"
    }

    fun isNumber(value: String): Boolean{
        return try{
            value.toInt()
            true
        } catch(ex: NumberFormatException){
            false
        }
    }
}