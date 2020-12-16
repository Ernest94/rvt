# change to ssl directory
cd ssl
#remove old key store
rm educom_voortgang.keystore
#build new keyfile, make sure to verify the password and name when copying this script.
openssl pkcs12 -export -out keystore.p12 -in /opt/psa/var/modules/letsencrypt/etc/live/voortgang.educom.nu/cert.pem -inkey /opt/psa/var/modules/letsencrypt/etc/live/voortgang.educom.nu/privkey.pem -name tomcat -passin pass:?120qhZl -passout pass:?120qhZl
#convert to keystore
keytool -importkeystore \
        -deststorepass ?120qhZl -destkeypass ?120qhZl -destkeystore educom_voortgang.keystore \
        -srckeystore keystore.p12 -srcstoretype PKCS12 -srcstorepass ?120qhZl \
        -alias tomcat
# remove old key file
rm keystore.p12
#go to the main dir again
cd ..