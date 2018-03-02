# Akka HTTP microservice example

[![Join the chat at https://gitter.im/theiterators/akka-http-microservice](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/theiterators/akka-http-microservice?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This project demonstrates the [Akka HTTP](http://doc.akka.io/docs/akka-stream-and-http-experimental/current/scala.html) library and Scala to write a simple REST (micro)service. The project shows the following tasks that are typical for most Akka HTTP-based projects:

* starting standalone HTTP server,
* handling file-based configuration,
* logging,
* routing,
* deconstructing requests,
* unmarshalling JSON entities to Scala's case classes,
* marshaling Scala's case classes to JSON responses,
* error handling,
* issuing requests to external services,
* testing with mocking of external services.

The service in the template provides two REST endpoints - one which gives GeoIP info for given IP and another for calculating geographical distance between given pair of IPs. The project uses the service [ip-api](http://ip-api.com/) which offers JSON IP and GeoIP REST API for free for non-commercial use.

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Usage

Start services with sbt:

```
$ sbt
> ~re-start
```

### Testing

Execute tests using `test` command:

```
$ sbt
> test
```
