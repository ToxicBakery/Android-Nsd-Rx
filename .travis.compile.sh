set -e

docker run -it -u `stat -c "%u:%g" .` -v ${TRAVIS_BUILD_DIR}:/workspace -w /workspace toxicbakery/alpine-glibc-android /bin/sh -c \
    "./gradlew build test --stacktrace --continue --console=plain"