# Bank external service in Python (using kafka-python)

This service connects to kafka (ip: kafka:9092), it connects to two topics named : "**topic**" and ""**bank**". Reads messages of type : **PROCESS_PAYMENT** and responds with messages of type **PAYMENT_CONFIRMATION**.

## Messages structures

## Input

* PROCESS_PAYMENT example:

```json
{"type":"PROCESS_PAYMENT","account":"numero de compte","amount":10.0,"id":-1}
```

## Output

* PAYMENT_CONFIRMATION example:

```json
{"type":"PAYMENT_CONFIRMATION","status":true,"id":-1}
```

## Build

```bash
./compile.sh
```

## Test in Docker

```bash
docker-compose up
```