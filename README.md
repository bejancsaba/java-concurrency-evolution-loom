# java-concurrency-evolution-loom
At the creation of this repository project loom is not release yet. It is available as Early Access as part of JDK 16 binary.

Unfortunately neither IntelliJ nor Gradle is supporting JDK 16 right now so this repositary is created to decouple and 
extend the code examples available in java-concurrency-evolution repository.

To build and execute the code example
```
mvn clean package
java -cp target/loom-1.0-SNAPSHOT.jar com.concurrency.Main
```