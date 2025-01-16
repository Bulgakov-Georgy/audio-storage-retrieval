FROM bellsoft/liberica-openjdk-debian:17
RUN groupadd audio-storage-retrieval && useradd -s /bin/bash -u 1000 -g audio-storage-retrieval audio-storage-retrieval
COPY --chown=audio-storage-retrieval build/libs/service-latest.jar /usr/local/audio-storage-retrieval/service.jar
WORKDIR /usr/local/audio-storage-retrieval
USER 1000
ENTRYPOINT java -jar service.jar