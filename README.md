# SafeChat

An open sourced REST API for a secure messaging app.

This API allows users to create temporary chat rooms that are easy to set up for them to securely send messages. All messages are encrypted using standard encryption algorithm
AES/CBC/PKCS5Padding along with PBKDF2 with SHA256 to derive the secret key for message encryption.

Chat room setup does not require user authentication and can handle multiple participants.

## Table of Contents

- [Getting Started](#getting-started)
- [Using the API](#using-the-api)
    - [Creating a Room](#creating-a-room)
    - [Joining a Room](#joining-a-room)
    - [Sending a Message](#sending-a-message)
    - [Fetching Messages](#fetching-messages)

## Getting Started

In order to get the app working, you'll need to make sure you have Docker installed

If you are running on a LINUX based machine, run this command \
Do NOT run this command if you are on WINDOWS or MAC

```shell
export DOCKER_GATEWAY_HOST=172.17.0.1
```

Run the following command at the root of the project to start the App (make sure that Docker is running in your machine).
```
docker-compose up -d
```

To test that the services are working, go to http://localhost:8080/, you should see a welcome message.

Now that the services are up and running, you can get started. \

If you want to stop the App you can do one the following:
```shell
docker-compose down               // stop the app but keep the images stored on your system

docker-compose down --rmi all     // stop the app remove the images from your system
```

## Using the API

### Creating a Room

```
POST /room
```

To create a room, a user must enter a nickname for themselves. Each room will have a 20 character alphanumeric code associated with it. 

**Example:**
```
Request URL: 
POST localhost:8080/room

Request Body:
{
    "user" : "John Doe"
}

cURL Request:
curl --location --request POST 'localhost:8080/room' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user" : "John Doe"
}'

Response:
{
    "timestamp": "2022-05-16T21:09:39",
    "status": "SUCCESS",
    "code": 200,
    "payload": {
        "roomCode": "uChRsWnw9h8nQINgtTFo"
    }
}
```

### Joining a Room

```
POST /room/:roomCode
```

When a room is created, we can have more users join the room. Once again these new users will need to provide a username.
Two users with the same name cannot be in the same room.

**Example:**
```
Request URL: 
POST localhost:8080/room/uChRsWnw9h8nQINgtTFo

Request Body:
{
    "user" : "Jane Smith"
}

cURL Request:
curl --location --request POST 'localhost:8080/room/uChRsWnw9h8nQINgtTFo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user" : "Jane Smith"
}'

Response:
{
    "timestamp": "2022-05-16T21:09:39",
    "status": "SUCCESS",
    "code": 200,
    "payload": {
        "roomCode": "uChRsWnw9h8nQINgtTFo"
    }
}
```

### Sending a Message

```
POST /message
```

A user can send a message to the room where it can be read by all participants of the room. All messages are encrypted by using the room code as a password before being stored in the database. 

**Example:**
```
Request URL: 
POST localhost:8080/message

Request Body:
{
    "sender" : "John Doe",
    "roomCode": "uChRsWnw9h8nQINgtTFo",
    "message" : "Hey There"
}

cURL Request:
curl --location --request POST 'localhost:8080/message' \
--header 'Content-Type: application/json' \
--data-raw '{
    "sender" : "John Doe",
    "roomCode": "uChRsWnw9h8nQINgtTFo",
    "message" : "Hey There"
}'

Response:
{
    "timestamp": "2022-05-16T21:19:29",
    "status": "SUCCESS",
    "code": 200,
    "payload": {}
}
```

### Fetching Messages

```
GET /room/:roomCode/message?lastMessageDate=<lastMessageDate>
```
- `lastMessageDate` the datetime of the last message that was fetched, must be in following format: `yyyy-MM-dd'T'HH:mm:ss`

This will fetch messages from the room. If the `lastMessageDate` query parameter is not provided then all messages since room creation will be fetched.
If it provided, then only messages that were sent after the provided datetime will be fetched.

**Example: Fetching All Messages**
```
Request URL: 
GET localhost:8080/room/uChRsWnw9h8nQINgtTFo/message

cURL Request:
curl --location --request GET 'localhost:8080/room/uChRsWnw9h8nQINgtTFo/message'

Response:
{
    "timestamp": "2022-05-16T21:26:54",
    "status": "SUCCESS",
    "code": 200,
    "payload": [
        {
            "messageId": "937fe87c-805d-470d-83cb-9b0343bb1be2",
            "message": "Hey There",
            "user": "John Doe",
            "date": "2022-05-16T21:19:29"
        },
        {
            "messageId": "daea1899-c946-43c7-9d6c-3f424e32c75b",
            "message": "Hi",
            "user": "Jane Smith",
            "date": "2022-05-16T21:25:47"
        },
        {
            "messageId": "aacefaec-f044-40ca-b662-bdf439b6ff57",
            "message": "How's it going?",
            "user": "Jane Smith",
            "date": "2022-05-16T21:26:08"
        },
        {
            "messageId": "d9a93beb-5cd4-4061-b906-e395082a2548",
            "message": "Good, how about you?",
            "user": "John Doe",
            "date": "2022-05-16T21:26:30"
        },
        {
            "messageId": "4d8510eb-619d-4789-9772-0b016aa6cdb9",
            "message": "Great!",
            "user": "Jane Smith",
            "date": "2022-05-16T21:26:43"
        }
    ]
}
```

**Example: Fetching New Messages**
```
Request URL: 
GET localhost:8080/room/uChRsWnw9h8nQINgtTFo/message?lastMessageDateTime=2022-05-16T21:26:30

cURL Request:
curl --location --request GET 'localhost:8080/room/uChRsWnw9h8nQINgtTFo/message?lastMessageDateTime=2022-05-16T21:26:30'

Response:
{
    "timestamp": "2022-05-16T21:28:03",
    "status": "SUCCESS",
    "code": 200,
    "payload": [
        {
            "messageId": "4d8510eb-619d-4789-9772-0b016aa6cdb9",
            "message": "Great!",
            "user": "Jane Smith",
            "date": "2022-05-16T21:26:43"
        }
    ]
}
```
