set -e

# Prepare gradle.properties
echo -e "" >> "gradle.properties"
echo -e "NEXUS_USERNAME=${SONATYPE_USERNAME}" >> "gradle.properties"
echo -e "NEXUS_PASSWORD=${SONATYPE_PASSWORD}" >> "gradle.properties"
echo -e "signing.keyId=${SIGNING_KEY}" >> "gradle.properties"
echo -e "signing.password=${SIGNING_KEY_PASSWORD}" >> "gradle.properties"
echo -e "signing.secretKeyRingFile=../keystore_secret.gpg" >> "gradle.properties"