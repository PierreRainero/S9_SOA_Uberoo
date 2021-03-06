#!/usr/bin/env python
import json
import sys
import threading, logging, time
import multiprocessing

from kafka import KafkaConsumer, KafkaProducer

class Topic(multiprocessing.Process):
    def __init__(self):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='kafka:9092',group_id='bank',auto_offset_reset='latest',value_deserializer=lambda m: json.loads(m.decode('utf-8')))
        consumer.subscribe(['topic'])
        producer = KafkaProducer(bootstrap_servers='kafka:9092')

        while not self.stop_event.is_set():
            for message in consumer:
                if message.value['type'] == "PROCESS_PAYMENT":
                    print("<topic> Processing Payment for account number "+str(message.value['account'])+" of amount: "+str(message.value['amount'])+"\n",file=sys.stderr)
                    producer.send('topic', str.encode("{\"type\":\"PAYMENT_CONFIRMATION\",\"id\":"+str(message.value['id'])+",\"status\":true}"))
                if self.stop_event.is_set():
                    break

        consumer.close()
        producer.close()

class Bank(multiprocessing.Process):
    def __init__(self):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='kafka:9092',group_id='bank',auto_offset_reset='latest',value_deserializer=lambda m: json.loads(m.decode('utf-8')))
        consumer.subscribe(['bank'])
        producer = KafkaProducer(bootstrap_servers='kafka:9092')

        while not self.stop_event.is_set():
            for message in consumer:
                if message.value['type'] == "PROCESS_PAYMENT":
                    print("<bank> Processing Payment for account number "+str(message.value['account'])+" of amount: "+str(message.value['amount'])+"\n",file=sys.stderr)
                    producer.send('bank', str.encode("{\"type\":\"PAYMENT_CONFIRMATION\",\"id\":"+str(message.value['id'])+",\"status\":true}"))
                if self.stop_event.is_set():
                    break

        consumer.close()
        producer.close()

class Coursier(multiprocessing.Process):
    def __init__(self):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='kafka:9092',group_id='bank',auto_offset_reset='latest',value_deserializer=lambda m: json.loads(m.decode('utf-8')))
        consumer.subscribe(['coursier'])
        producer = KafkaProducer(bootstrap_servers='kafka:9092')

        while not self.stop_event.is_set():
            for message in consumer:
                if message.value['type'] == "PROCESS_PAYMENT":
                    print("<coursier> Processing Payment for account number "+str(message.value['account'])+" of amount: "+str(message.value['amount'])+"\n",file=sys.stderr)
                    producer.send('coursier', str.encode("{\"type\":\"PAYMENT_CONFIRMATION\",\"id\":"+str(message.value['id'])+",\"status\":true}"))
                if self.stop_event.is_set():
                    break

        consumer.close()
        producer.close()

def main():
    tasks = [
        Topic(),
        Bank(),
        Coursier()
    ]

    for t in tasks:
        t.start()

    while True:
        time.sleep(3)

    for task in tasks:
        task.stop()

    for task in tasks:
        task.join()


if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO)
    main()
