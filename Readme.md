# Introduction
`kvstore` is a simple, extensible, multithreaded, persistent key-value store written in Java. This project is primarily geared towards learning but if you're feeling adventurous, feel free to use it in production :full_moon_with_face:.

# Usage
1. Clone/download the project to your machine.
2. Build the application `jar` by running `mvn clean package` from the project's root. The built `jar` is placed in the `target` directory by default.
3. Modify the configuration file (described below) to your needs.
4. Run the application `jar` using `java -jar {path-to-jar-file} {path-to-config-file}`. For example, you can run the following command from the project's root using the default config file: `java -jar target/kvstore-1.0-SNAPSHOT.jar config`.
5. Use the HTTP API (described below) to interact with the store.

## Config
The application expects a path to a configuration file when it starts up. This file can be used to set things like the key-value store implementation, persistence settings, server port, etc.

The config file should contain property configurations in the format `property: value`. You can look at the default config file in the project for reference.

Below are the properties you can use to configure the store:

- `kvstore.implementation`
    
    Possible values: `hashmap` | `concurrent_hashmap`

    Sets the underlying implementation of the key-value store. Different implementations provide different benefits in terms of performance, memory usage and querying speed. 
    
    The `hashmap` implementation uses a _synchronised_ `java.util.HashMap` which allows only one thread to write to it at one time. This can make operations on the store slow but makes it easy to reason about.
    
    The `concurrent_hashmap` implementation uses a `java.util.ConcurrentHashMap` which can handle concurrent writes and hence offers better performance.

    You can easily add your own implementations by extending the abstract `KVStore` class and providing your own implementations for the required operations. For example, a BTree implementation (WIP) would offer better performance on range queries.

- `persist.implementation`
    
    Possible values: `wal`

    Sets the strategy to be used for persisting the store to disk. Different strategies provide different benefits in terms of durability guarantees, performance, and disk-usage.

    The _write-ahead logging_ (`wal`) strategy records each write operation on the store before it is performed. These records are persisted to disk and can be used to recover the store in the event of a failure.
    This is done by reapplying all the recorded operations to a new store, in chronological order. This approach increases the average write-operation time but offers greater durability.
    To improve this performance, disk writes can be buffered so that every operation doesn't need to write to disk. However, this introduces a window between the last flush and a failure during which data could be lost.

    Another strategy is snapshotting. This strategy takes a snapshot of the store following a set schedule and saves this snapshot to disk. This can be performed concurrently and can potentially offer better performance depending on the store size and implementation.
    Recovery can be performed by simply reading the snapshot and reconstructing the store. The downside to this approach is the potential for data loss. Snapshotting operations can be expensive and hence it might not be optimal to perform them too frequently.
    Hence, if a failure occurs after a snapshot operation and before the next snapshot event, the changes to the store in between are lost.

- `persist.data_directory`

    A full path to where you want the store to persist its state. The directory is created if it doesn't exist.
    Each persister implementation would ideally create a subdirectory inside this directory to keep its data. This avoids different strategies from conflicting with each other. It's up to the implementation to maintain whatever data it needs to perform persistence and recovery.

    For example, the `wal` persister creates a `wal` subdirectory in the data directory where it stores its log files.

- `persist.recover_state`

    Possible values: `true` | `false`

    If `true` the application performs a recovery before starting the main server. The configured persister will look for saved data in the `data_directory` configured.
    
    For example, the `wal` persister looks for log files inside the `data_directory/wal` directory and replays them in chronological order.

    If `false`, no recovery is attempted and the server starts normally.

- `server.port`

    A port number for the server to use. The port must not be in use by another application as no attempt is made to find another free port.

## HTTP API

The HTTP API is the primary (and only) way of interacting with the store. All endpoints return the status code `200` for a successful operation, so you should rely on this to determine success/failure.

- **Add a key to the store**

    Method: `POST`
    
    Endpoint: `/keys/{key}`

    Request body: String representations of Integer, Double, Boolean, and String values.

    Examples:
    
    `curl -X POST http://localhost:8000/keys/hello -d "world"`

    This request will associate the key `hello` with the string value `world`.

    `curl -X POST http://localhost:8000/keys/is-this-a-good-keystore -d "true"`

    Associates the key `is-this-a-good-keystore` with the boolean value `true`. The value provided value is converted to a primitive data type if possible. This can make querying operations like sorting, max, min, etc. easier to perform.

- **Get a key from the store**

    Method: `GET`

    Endpoint: `/keys/{key}`

    Returns: String representation of the value associated with key. `null` if no such key exists.

    Example:

    `curl -X GET http://localhost:8000/keys/hello`

    Following from the previous example, this would return the string `world` as the response body.

- **Delete a key from the store**

    Method: `DELETE`

    Endpoint: `/keys/{key}`

    Example:

    `curl -X DELETE http://localhost:8000/keys/hello`

    Following from the previous example, this would delete the key `hello` from the store.

# Contributing

This is nowhere near a perfect implementation and I would greatly appreciate your help in making this a good learning resource for everyone interested. You can email me at `krishnakeshan18@gmail.com` for errors, suggestions, questions or anything else. If you would like to make a code change to the project please feel free to create a PR and I'll take a look.

Before you get started though, please sign this [CLA](https://www.youtube.com/watch?v=dQw4w9WgXcQ).
 