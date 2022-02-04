#!/usr/bin/env bash

# .key - private key.
# .csr - certificate signing request.
# .pem - certificate.
# .p12 - keystore with certificates (keystore) and a store of trusted certificates (truststore).

CA_PK='configs/CA-private-key.key'
CA_SUBJECT='/CN=Certificate authority/'
CA_CSR='configs/CA-certificate-signing-request.csr'
CA_C='configs/CA-self-signed-certificate.pem'

SERVER_PK='configs/Server-pk.key'
SERVER_SUBJECT='/CN=localhost/'
SERVER_CSR='configs/Server-certificate-signing-request.csr'
SERVER_C='configs/Server-certificate.pem'

SERVER_KEYSTORE='Server-keystore.p12'
SERVER_TRUSTSTORE='Server-truststore.p12'

PASSWORD='password'


private_key() {
        openssl genrsa -aes256 -passout pass:password -out $1 4096
}

certificate_signing_request() {
        openssl req -new -key $1 -passin pass:password -subj "$2" -out $3
}

sign_request() {
        openssl x509 -req -in $1 -CA $2 -CAkey $3 -passin pass:password -CAcreateserial -days 365 -out $4
}


rm -f *.key *.pem *.csr *.p12

# CA
private_key $CA_PK
certificate_signing_request $CA_PK "$CA_SUBJECT" $CA_CSR
openssl x509 -req -in $CA_CSR -signkey $CA_PK -passin pass:$PASSWORD -days 365 -out $CA_C

# SERVER
private_key $SERVER_PK
certificate_signing_request $SERVER_PK "$SERVER_SUBJECT" $SERVER_CSR
sign_request $SERVER_CSR $CA_C $CA_PK $SERVER_C

# KEYSTORE
openssl pkcs12 -export -in $SERVER_C -inkey $SERVER_PK -passin pass:$PASSWORD -passout pass:$PASSWORD -out $SERVER_KEYSTORE
keytool -import -file $CA_C -keystore $SERVER_TRUSTSTORE -storetype PKCS12 -storepass $PASSWORD -noprompt
