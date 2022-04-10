# Redis Challenge

### Documentación de referencia

La aplicación está diseñada para responder a los siguientes comandos y emular su comportamiento
en un mapa de datos en memoria. No utiliza conexion a base de datos ni bases de datos en 
memoria


1. ​SET​ ​key value
2. ​SET​ ​key value EX seconds ​(need not implement other SET options) 3. ​GET​ ​key
3. ​DEL​ ​key
4. ​DBSIZE
5. ​INCR​ ​key
6. ​ZADD​ ​key score member
7. ​ZCARD​ ​key
8. ​ZRANK​ ​key member
9. ​ZRANGE​ ​key start stop

Las pruebas unitarias estan en RedisChallengeApplicationTest y cubren los casos
para los comandos mencionados.

Se utilizó un patron de diseño Factory para optimizar la creacion de los comandos en conjunto con el patrón Facade para ocultar la
complejidad de los comandos.

Josue Barrios Rodriguez
