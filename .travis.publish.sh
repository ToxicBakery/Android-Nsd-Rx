set -e

openssl aes-256-cbc -K $encrypted_3c29eb4f88a8_key -iv $encrypted_3c29eb4f88a8_iv -in maven.keystore.gpg.enc -out maven.keystore.gpg -d
docker run -it -u `stat -c "%u:%g" .` -v ${TRAVIS_BUILD_DIR}:/workspace -w /workspace toxicbakery/alpine-glibc-android /bin/sh -c \
    "./gradlew build bintrayUpload -Pprofile=sources,javadoc --stacktrace --continue --console=plain"