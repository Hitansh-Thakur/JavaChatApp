## Lessons Learned

1. **Object Serialization**: Understand how to serialize and deserialize objects in Java using `ObjectOutputStream` and `ObjectInputStream`.

2. **Stream Order**: Remember the order of stream creation: 
> "Always create ObjectOutputStream before ObjectInputStream".
 
 This is crucial to avoid `EOFException` when reading from the input stream.

3. **Avoid mixing ObjectOutputStream and DataOutputStream.**

4. **Different Types of Streams**: Familiarize  with different types of streams: `FileInputStream`, `FileOutputStream`, `ObjectInputStream`, and `ObjectOutputStream`.

5. **Sync between Client and Server**: Understand the importance of synchronization between client and server when sending and receiving objects. The server should be ready to receive an object before the client sends it.

## TODOs:

- [x] Smooth msg exchange between client and server.
- [ ] Multiple clients support.
    - [ ] add a new thread for each client.
    - [ ] store username and in and out streams in a map.