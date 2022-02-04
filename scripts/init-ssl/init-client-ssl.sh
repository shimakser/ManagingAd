#!/usr/bin/env bash

CA_PK='configs/CA-private-key.key'
CA_C='configs/CA-self-signed-certificate.pem'

CLIENT_PK='configs/Client-pk.key'
CLIENT_CSR='configs/Client-csr.csr'
CLIENT_C='configs/Client-certificate.pem'

PASSWORD='password'


openssl genrsa -aes256 -passout pass:$PASSWORD -out $CLIENT_PK 4096
openssl req -new -key $CLIENT_PK -passin pass:$PASSWORD -subj $SUBJECT -out $CLIENT_CSR
openssl x509 -req -in $CLIENT_CSR -CA $CA_C -CAkey $CA_PK -passin pass:$PASSWORD -CAcreateserial -days 365 -out $CLIENT_C
