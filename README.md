Project to test keycloak integration with spring-boot

Login url:
http://localhost:8484/auth/realms/my_realm/protocol/openid-connect/auth?response_type=code&client_id=my_realm&redirect_uri=http://localhost:8080/not/important/now&code_challenge=Y4fhKdeI8y4rCBTJlfER7bUDrDAYx5LDJRMzVSeDibk&code_challenge_method=S256

Token url:
http://localhost:8484/auth/realms/my_realm/protocol/openid-connect/token

Keycloak is configured by [keycloak-config-cli](https://github.com/adorsys/keycloak-config-cli)

Set variables GOOGLE_CLIENT_SECRET and GOOGLE_CLIENT_ID in keycloak_spring/.env
