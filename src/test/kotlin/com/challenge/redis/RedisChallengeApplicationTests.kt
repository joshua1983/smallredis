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
        val valueToSet = "data"
        val keyToSet = "key"
        val listParams = listOf(keyToSet, valueToSet)
        val paramsRequest = ParamsRequest(listParams)
        processCommand.processCommand("SET", paramsRequest)
        val datos = dataService.getAllData()
        Assert.assertNotNull(datos.get(keyToSet))
        Assert.assertEquals(datos.get(keyToSet)?.value, valueToSet)
    }

    @Test
    fun testSetdWithEXpireTime() {
        val keyToSet = "key"
        val valueToSet = "data"
        val expireSeconds = "2"
        val listParams = listOf(keyToSet, valueToSet, "EX", expireSeconds)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        Thread.sleep(2001)
        val datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals("El dato ya expiró"))
    }

    @Test
    fun testSetdWithEXpireTimeLessLimit() {
        val keyToSet = "key"
        val valueToSet = "data"
        val expireTime = "20"
        val listParams = listOf(keyToSet, valueToSet, "EX", expireTime)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        val datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals(valueToSet))
    }

    @Test
    fun testGet() {
        val keyToSet = "key"
        val valueToSet = "data"
        val listParams = listOf(keyToSet, valueToSet)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        val datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals(valueToSet))
    }

    @Test
    fun testDel() {
        val keyToSet = "key"
        val valueToSet = "data"
        val listParams = listOf(keyToSet, valueToSet)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestDel = ParamsRequest(listOf(keyToSet))
        val paramsRequestGet = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        processCommand.processCommand("DEL", paramsRequestDel)
        val datos = processCommand.processCommand("GET", paramsRequestGet)
        Assert.assertTrue(datos.response.equals("No existe un item con esa llave."))
    }

    @Test
    fun testDbSize() {
        val keyToSet = "key"
        val valueToSet = "data"
        val listParams = listOf(keyToSet, valueToSet)
        val paramsRequestSet = ParamsRequest(listParams)
        processCommand.processCommand("SET", paramsRequestSet)
        val datos = processCommand.processCommand("DBSIZE", ParamsRequest(listOf()))
        Assert.assertTrue(datos.response.equals("1"))
    }

    @Test
    fun testIncrementValueINCR() {
        val keyToSet = "key"
        val valueToSet = "0"
        val listParams = listOf(keyToSet, valueToSet)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestIncr = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        val datos = processCommand.processCommand("INCR", paramsRequestIncr)
        Assert.assertTrue(datos.response.equals("1"))
    }

    @Test
    fun testIncrementValueINCRnonIntegerValue() {
        val keyToSet = "key"
        val valueToSet = "data"
        val listParams = listOf(keyToSet, valueToSet)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestIncr = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("SET", paramsRequestSet)
        val datos = processCommand.processCommand("INCR", paramsRequestIncr)
        Assert.assertTrue(datos.response.equals("0"))
    }

    @Test
    fun testAddMultipleScoresZADD() {
        val keyToSet = "key"
        val valueToSetIndex1 = "1"
        val valueToSetValue1 = "data"
        val valueToSetIndex2 = "2"
        val valueToSetValue2 = "data"
        val listParams = listOf(keyToSet, valueToSetIndex1, valueToSetValue1, valueToSetIndex2, valueToSetValue2)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestIncr = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("ZADD", paramsRequestSet)
        val datos = processCommand.processCommand("GET", paramsRequestIncr)
        Assert.assertTrue(datos.response.equals("{\"1\":\"data\",\"2\":\"data\"}"))
    }

    @Test
    fun testNumberElementsZCARD() {
        val keyToSet = "key"
        val valueToSetIndex1 = "1"
        val valueToSetValue1 = "data"
        val valueToSetIndex2 = "2"
        val valueToSetValue2 = "data"
        val listParams = listOf(keyToSet, valueToSetIndex1, valueToSetValue1, valueToSetIndex2, valueToSetValue2)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestZcard = ParamsRequest(listOf(keyToSet))
        processCommand.processCommand("ZADD", paramsRequestSet)
        val datos = processCommand.processCommand("ZCARD", paramsRequestZcard)
        Assert.assertTrue(datos.response.equals("2"))
    }

    @Test
    fun testRankElementZRANK() {
        val keyToSet = "key"
        val valueToSetIndex1 = "2"
        val valueToSetValue1 = "data uno"
        val valueToSetIndex2 = "1"
        val valueToSetValue2 = "data dos"
        val listParams = listOf(keyToSet, valueToSetIndex1, valueToSetValue1, valueToSetIndex2, valueToSetValue2)
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestZrank = ParamsRequest(listOf(keyToSet, "data uno"))
        processCommand.processCommand("ZADD", paramsRequestSet)
        val datos = processCommand.processCommand("ZRANK", paramsRequestZrank)
        Assert.assertTrue(datos.response.equals("1"))
    }

    @Test
    fun testGetElementsByRangeZRange() {
        val keyToSet = "key"
        val valueToSetIndex1 = "2"
        val valueToSetValue1 = "data dos"
        val valueToSetIndex2 = "1"
        val valueToSetValue2 = "data uno"
        val valueToSetIndex3 = "3"
        val valueToSetValue3 = "data tres"
        val listParams = listOf(
            keyToSet,
            valueToSetIndex1,
            valueToSetValue1,
            valueToSetIndex2,
            valueToSetValue2,
            valueToSetIndex3,
            valueToSetValue3
        )
        val paramsRequestSet = ParamsRequest(listParams)
        val paramsRequestZrange = ParamsRequest(listOf(keyToSet, "0","1"))
        processCommand.processCommand("ZADD", paramsRequestSet)
        val datos = processCommand.processCommand("ZRANGE", paramsRequestZrange)
        Assert.assertTrue(datos.response.equals("data uno"))
    }

}
