#!/usr/bin/env python
import json
import sys
import threading, logging, time
import multiprocessing

from kafka import KafkaConsumer, KafkaProducer

class Consumer(multiprocessing.Process):
    def __init__(self):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='kafka:9092',group_id='bank',auto_offset_reset='latest',value_deserializer=lambda m: json.loads(m.decode('utf-8')))
        consumer.subscribe(['topic'])

        while not self.stop_event.is_set():
            for message in consumer:
                if message.value['type'] == "PROCESS_PAYMENT":
                    print("Processing Payment for account number "+str(message.value['account'])+" of amount: "+str(message.value['amount'])+"\n",file=sys.stderr)
                if self.stop_event.is_set():
                    break

        consumer.close()


def main():
    tasks = [
        Consumer()
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
