package com.challenge.redis

import com.challenge.redis.core.ProcessCommand
import com.challenge.redis.entity.ParamsRequest
import com.challenge.redis.service.DataService
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [RedisChallengeApplication::class])
class RedisChallengeApplicationTests {

    @Autowired
    private lateinit var dataService: DataService

    @Autowired
    private lateinit var processCommand: ProcessCommand

    @Test
    fun testSet() {
        var valueToSet = "data"
        var keyToSet = "key"
        var listParams = listOf(keyToSet, valueToSet)
        var paramsRequest = ParamsRequest(listParams)
        processCommand.processCommand("SET", paramsRequest)
        var datos = dataService.getAllData()
        Assert.assertNotNull(datos.get(keyToSet))
        Assert.assertEquals(datos.get(keyToSet)?.value, valueToSet)
    }

    @Test
    fun testSetdWithEXpireTime() {
        var keyToSet = "key"
        var valueToSet = "data"
        var expireSeconds = "2"
        var listParams = listOf(keyToSet, valueToSet, "EX", expireSeconds)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        Thread.sleep(2001)
        var datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals("El dato ya expiró"))
    }

    @Test
    fun testSetdWithEXpireTimeLessLimit() {
        var keyToSet = "key"
        var valueToSet = "data"
        var expireTime = "20"
        var listParams = listOf(keyToSet, valueToSet, "EX", expireTime)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        var datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals(valueToSet))
    }

    @Test
    fun testGet() {
        var keyToSet = "key"
        var valueToSet = "data"
        var listParams = listOf(keyToSet, valueToSet)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        var datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals(valueToSet))
    }

    @Test
    fun testDel() {
        var keyToSet = "key"
        var valueToSet = "data"
        var listParams = listOf(keyToSet, valueToSet)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestDel = ParamsRequest(listOf(keyToSet))
        var paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        processCommand.processCommand("DEL", paramsRequestDel)
        var datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals("No existe un item con esa llave."))
    }

    @Test
    fun testDbSize() {
        var keyToSet = "key"
        var valueToSet = "data"
        var listParams = listOf(keyToSet, valueToSet)
        var paramsRequestSet = ParamsRequest(listParams)
        processCommand.processCommand("SET", paramsRequestSet)
        var datos = processCommand.processCommand("DBSIZE", ParamsRequest(listOf()))
        Assert.assertTrue(datos.response.equals("1"))
    }

    @Test
    fun testIncrementValueINCR() {
        var keyToSet = "key"
        var valueToSet = "0"
        var listParams = listOf(keyToSet, valueToSet)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestIncr = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        var datos = processCommand.processCommand("INCR", paramsRequestIncr)
        Assert.assertTrue(datos.response.equals("1"))
    }

    @Test
    fun testIncrementValueINCRnonIntegerValue() {
        var keyToSet = "key"
        var valueToSet = "data"
        var listParams = listOf(keyToSet, valueToSet)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestIncr = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        var datos = processCommand.processCommand("INCR", paramsRequestIncr)
        Assert.assertTrue(datos.response.equals("0"))
    }

    @Test
    fun testAddMultipleScoresZADD() {
        var keyToSet = "key"
        var valueToSetIndex1 = "1"
        var valueToSetValue1 = "data"
        var valueToSetIndex2 = "2"
        var valueToSetValue2 = "data"
        var listParams = listOf(keyToSet, valueToSetIndex1, valueToSetValue1, valueToSetIndex2, valueToSetValue2)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestIncr = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("ZADD", paramsRequestSet)
        var datos = processCommand.processCommand("GET", paramsRequestIncr)
        Assert.assertTrue(datos.response.equals("{\"1\":\"data\",\"2\":\"data\"}"))
    }

    @Test
    fun testNumberElementsZCARD() {
        var keyToSet = "key"
        var valueToSetIndex1 = "1"
        var valueToSetValue1 = "data"
        var valueToSetIndex2 = "2"
        var valueToSetValue2 = "data"
        var listParams = listOf(keyToSet, valueToSetIndex1, valueToSetValue1, valueToSetIndex2, valueToSetValue2)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestZcard = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("ZADD", paramsRequestSet)
        var datos = processCommand.processCommand("ZCARD", paramsRequestZcard)
        Assert.assertTrue(datos.response.equals("2"))
    }

    @Test
    fun testRankElementZRANK() {
        var keyToSet = "key"
        var valueToSetIndex1 = "2"
        var valueToSetValue1 = "data uno"
        var valueToSetIndex2 = "1"
        var valueToSetValue2 = "data dos"
        var listParams = listOf(keyToSet, valueToSetIndex1, valueToSetValue1, valueToSetIndex2, valueToSetValue2)
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestZrank = ParamsRequest(listOf(keyToSet, "data uno"))
        processCommand.processCommand("ZADD", paramsRequestSet)
        var datos = processCommand.processCommand("ZRANK", paramsRequestZrank)
        Assert.assertTrue(datos.response.equals("1"))
    }

    @Test
    fun testGetElementsByRangeZRange() {
        var keyToSet = "key"
        var valueToSetIndex1 = "2"
        var valueToSetValue1 = "data dos"
        var valueToSetIndex2 = "1"
        var valueToSetValue2 = "data uno"
        var valueToSetIndex3 = "3"
        var valueToSetValue3 = "data tres"
        var listParams = listOf(
            keyToSet,
            valueToSetIndex1,
            valueToSetValue1,
            valueToSetIndex2,
            valueToSetValue2,
            valueToSetIndex3,
            valueToSetValue3
        )
        var paramsRequestSet = ParamsRequest(listParams)
        var paramsRequestZrange = ParamsRequest(listOf(keyToSet, "0","1"))
        processCommand.processCommand("ZADD", paramsRequestSet)
        var datos = processCommand.processCommand("ZRANGE", paramsRequestZrange)
        Assert.assertTrue(datos.response.equals("data uno"))
    }

}
