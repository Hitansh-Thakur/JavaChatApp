## Lessons Learned

1. **Object Serialization**: Understand how to serialize and deserialize objects in Java using `ObjectOutputStream` and `ObjectInputStream`.

2. **Stream Order**: Remember the order of stream creation: 
> "Always create ObjectOutputStream before ObjectInputStream".
 
 This is crucial to avoid `EOFException` when reading from the input stream.

3. **Avoid mixing ObjectOutputStream and DataOutputStream.**:use one or the other.

4. **Different Types of Streams**: Familiarize  with different types of streams: `FileInputStream`, `FileOutputStream`, `ObjectInputStream`, and `ObjectOutputStream`.

4. **Only create single ObjectOutput and ObjectInput stream per Socket**: results in `StreamCorruptedException`.
4. **Closing Streams closes Sockets**
5. **Sync between Client and Server**: Understand the importance of synchronization between client and server when sending and receiving objects. The server should be ready to receive an object before the client sends it.

## TODOs:

- [x] Smooth msg exchange between client and server.
- [x] Multiple clients support.
    - [x] add a new thread for each client.
    - [x] store username and out streams in a map.

- [ ] support to send files over the network.
    - [ ] add a new command to send files.
    - [ ] add a new command to receive files.

- [ ] add a database to store user credentials and messages.
- [ ] add a GUI to the client.