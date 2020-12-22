# change to ssl directory
cd ssl

#build new keyfile, make sure to verify the password and name when copying this script.
openssl pkcs12 -export -out tomcat.p12 -in /opt/psa/var/modules/letsencrypt/etc/live/voortgang.educom.nu/cert.pem -inkey /opt/psa/var/modules/letsencrypt/etc/live/voortgang.educom.nu/privkey.pem -name tomcat -passin pass:?120qhZl -passout pass:7.UHgk3+

#remove old certificate from the keystore
keytool -delete -alias tomcat -deststorepass 7.UHgk3+ -keystore voortgang.keystore

#convert to keystore
keytool -importkeystore \
        -deststorepass 7.UHgk3+ -destkeypass 7.UHgk3+ -destkeystore voortgang.keystore \
        -srckeystore tomcat.p12 -srcstoretype PKCS12 -srcstorepass 7.UHgk3+ \
        -alias tomcat

# remove old key file
rm tomcat.p12 

#go to the main dir again
cd ..