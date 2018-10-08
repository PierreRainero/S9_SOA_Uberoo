import _thread
import sys
import uuid
from random import randint

import requests
import time
from flask import Flask, jsonify, request, abort, json
from flask.json import JSONEncoder


class MyJSONEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, CommandMessage):
            return {
                'type': obj.type,
                'address': obj.address,
                'food': obj.food
            }
        return super(MyJSONEncoder, self).default(obj)


app = Flask(__name__)
app.json_encoder = MyJSONEncoder

subscribers_routes = []
subscribers_ids = []
messages = []


class CommandMessage(object):
    type = ""
    address = ""
    food = []


@app.route('/messages', methods=["GET"])
def get_messages():
    return jsonify(messages)


@app.route('/subscribers', methods=["GET"])
def get_subscribers():
    return jsonify(subscribers_routes)


@app.route('/subscribe', methods=["POST"])
def subscribe():
    if not request.json:
        abort(400)
    subscribers_routes.append(request.json["route"])
    user_id = uuid.uuid4()
    subscribers_ids.append(str(user_id))
    return jsonify({'subscriber': user_id, 'route': request.json["route"]})


@app.route('/unsubscribe', methods=["POST"])
def unsubscribe():
    if not request.json:
        abort(400)
    index = subscribers_ids.index(request.json["uuid"])
    subscribers_ids.pop(index)
    subscribers_routes.pop(index)
    return jsonify({'status': "ok"}), 200


@app.route('/message', methods=["POST"])
def post_message():
    if not request.json:
        abort(400)
    command_message = CommandMessage()
    command_message.address = request.json["address"]
    command_message.type = request.json["type"]
    command_message.food = request.json["food"]
    messages.append(command_message)
    _thread.start_new_thread(send_all_messages, (subscribers_routes, messages,))
    # for subscriber in subscribers_routes:
    #     for message in messages:
    #         execute_post(subscriber, message)
    return jsonify({"status": "ok"}), 200


def send_all_messages(subscribers_routes_data, messages_data):
    with app.app_context():
        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
        for subscriber in subscribers_routes_data:
            for message in messages_data:
                print("Sending post to " + str(subscriber), file=sys.stderr)
                requests.post(str(subscriber), data=json.dumps(message), headers=headers)
                print("POST sent",file=sys.stderr)


# def execute_post(subscriber, message):
#     headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
#     print("Sending post to " + str(subscriber), file=sys.stderr)
#     requests.post(str(subscriber), data=json.dumps(message), headers=headers)


if __name__ == '__main__':
    app.run(host='0.0.0.0')
