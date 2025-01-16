## How-to

To start everything you can use [docker-compose.yml](docker-compose.yml) which contains

- Postgres with initialized DB
- Flyway which migrates DB
- Minio with a bucket
- The service itself (the latest image will be downloaded from this repo)

## Possible next steps

- One of the most obvious next steps is tracing and metrics (OpenTelemetry, Prometheus). Also, logging. Custom request
  logging, files' metadata (when converting failed) etc.
- More tests
- For API documentation springdoc can be used. It becomes kind of messy in controllers but much easier to maintain an
  up-to-date API spec. Also, this way it's pretty easy to host this spec using something like gitlab-ci etc.
- Although there is a class ResponseEntityExceptionHandler that we can extend, usually we don't want the behaviour that
  handleException method has to offer and want to map all those exceptions to our own error type
- The biggest bottleneck is of course converting. But assuming that audio files are small we probably can disregard
  that. Though, there are still 3 places where something can go wrong (local storage, remote storage and converter). So
  if I were to do something about it, for GET we can do something along the lines "save file to local storage -> return
  200 -> convert asynchronously -> send push with a download link". POST is a bit trickier:
    1. Save file to local storage and update status in DB
    2. Return 200
    3. Convert file and save to local storage (client can already get the file), update status and path in DB (async
       job)
    4. Save file to remote storage, update status and path in DB (async job). In that way we can be sure that file will
       be processed (or at least see a lot of errors but not blocking or breaking anything)
- ws.schild.jave.Encoder.encode can use EncoderProgressListener so we probably can implement websocket to update user on
  the progress. Not sure though just a wild thought
- It's probably better to move scheduling
  of [ClearTempFiles.java](service/src/main/java/coding/challenge/core/task/ClearTempFiles.java) to some configuration
  file where the job's cron and age filter can be fine-tuned using properties
- I need to get more knowledge in codecs, encodings etc. Because it is a rabbit hole for me at this point and
  for [AudioFileFormat.java](service/src/main/java/coding/challenge/core/model/AudioFileFormat.java) I just chose
  attributes and formats that work. Also, uploading, then downloading, then trying to upload downloaded file gives error
  ¯\\_(ツ)_/¯

## Notes and thoughts

- I tried to translate the [service](service) module to Kotlin ([service-kt](service-kt)), not sure how it came out
  though. And probably should have spent this time on tests
- About the library for conversion. I chose [JAVE2](https://github.com/a-schild/jave2) as it was the first library that
  I saw, was a good fit for my needs and I'm somewhat familiar with ffmpeg already
- For a more concise approach ids are just "long" and filenames are pretty simple
- Also, just a (pretty obvious) thought about userId. In a more real environment we'd probably have some kind of user
  auth service and would be getting userId from there instead of expecting it from the client, which will also validate
  that userId exists
- About integration testing. I have experience writing itests with cucumber (without spring integration though), but I
  don't think it is worth the time for this project
- About package [coding.challenge.core.service](service/src/main/java/coding/challenge/core/service): I think that it is
  usually possible to make services without interfaces as they are rarely needed, but still prefer using this more "by
  the book" approach as it makes it possible to do only the skeleton of the project in the beginning, makes project
  layers more defined, may help when someone else is working on the same feature and I believe (not sure though) that
  TDD approach also prefers interfaces
- Still not sure if it was worth using data JPA instead of NamedJdbcTemplate with plain old SQL because it gave me a bit
  of a headache with the persistence API as the last time I was using it was 3 years ago. And its compatibility with
  lombok is quite questionable... Though I do think that the easier DB schema the faster you can create repositories
  with those repo interfaces and it's pretty convenient
- About error handling. I think that in a case where API is served for our own clients (not for 3rd party apps) error
  handling is heavily dependent on how client interprets errors. So in this case I just went with a pretty simple error
  handling. Though there are different approaches: we can omit the message, create lots of error codes for everything
  and let client decide on what to show the user or write descriptive messages so they can be shown to user as is, etc.
- [Messages.java](service/src/main/java/coding/challenge/core/i18n/Messages.java?plain=1#L37) Not the best approach for
  defining supported locales. Instead, we can either parse resource bundle or set it in properties in some way