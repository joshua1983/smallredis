package com.challenge.redis.service

import com.challenge.redis.entity.Content
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class DataService {

    fun insertData(key: String, value: String){
        val data = this.getAllData()
        synchronized(this){
            data.put(key, Content(
                value = value,
                timestamp = System.currentTimeMillis(),
                expire = -1
            ))
        }
    }

    fun insertData(key: String, value: String, expire: Int){
        val data = this.getAllData()
        synchronized(this){
            data.put(key, Content(
                value = value,
                timestamp = System.currentTimeMillis(),
                expire = expire
            ))
        }
    }

    fun removeData(key: String){
        val data = this.getAllData()
        data.remove(key)
    }

    fun existData(key: String): Boolean{
        return this.getAllData().containsKey(key)
    }

    fun getData(key: String): Content{
        val data = this.getAllData()
        return data.get(key) as Content
    }

    fun getAllData(): ConcurrentHashMap<String, Content> {
        return DataSource.instance
    }

    fun countAllData(): Int{
        return this.getAllData().size
    }
}