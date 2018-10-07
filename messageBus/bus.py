import _thread
import json
import uuid
import requests
from flask import Flask, jsonify, request, abort
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
def hello_world():
    return jsonify(messages)


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
    return jsonify(), 200


def send_all_messages(subscribers_routes_data, messages_data):
    with app.app_context():
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        for subscriber in subscribers_routes_data:
            for message in messages_data:
                print("Sending post to "+subscriber+" "+jsonify(message))
                r = requests.post(subscriber, data=jsonify(message), headers=headers)
    return


if __name__ == '__main__':
    app.run(host='0.0.0.0')
