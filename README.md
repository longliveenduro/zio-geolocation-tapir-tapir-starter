# Disclaimer
Created to reproduce https://github.com/zio/zio/issues/7631.

Used JDK: 17.0.5

# Usage
```sbt test```

reveals:

```
[error] -- Error: /home/chris/prj/toil/zio-geolocation-tapir-tapir-starter/src/test/scala/com/tsystems/toil/BooleanSpec.scala:9:8 
[error]  9 |        assertTrue(BooleanClass.aBoolean == false)
[error]    |        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
[error]    |Exception occurred while executing macro expansion.
[error]    |java.lang.Exception: Expr cast exception: false
[error]    |of type: false
[error]    |did not conform to type: java.lang.Boolean
[error]    |
[error]    |	at scala.quoted.runtime.impl.QuotesImpl.asExprOf(QuotesImpl.scala:73)

```
