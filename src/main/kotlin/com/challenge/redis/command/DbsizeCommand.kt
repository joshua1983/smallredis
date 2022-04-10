package com.challenge.redis.command

import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.entity.ResultResponse
import com.challenge.redis.interfaces.Command
import com.challenge.redis.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DbsizeCommand : Command {

    @Autowired
    private lateinit var dataService: DataService

    override fun execute(request: ParamsRequest): ResultResponse {
        var count = dataService.countAllData()
        return ResultResponse(
            response = count.toString(),
            error = false
        )
    }

    override fun getName(): String {
        return "DBSIZE"
    }
}