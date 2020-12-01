# SOMAPASS
![logo](https://github.com/kairanskrr/1d-test/blob/main/somapasslogo.png?raw=true)
## API Documentation

## Domain: https://somapass.xyz


## List of routes
 (can try in your own browser)

### /login
Req: `https://somapass.xyz/login/:userid/:password`

Example: `https://somapass.xyz/login/1111111/password`

### /checkin

Req: `https://somapass.xyz/checkin/:userid/:password/:locationid/:locationname`

Example: `https://somapass.xyz/checkin/1111111/password/1/DSL`

### /checkout

Req: `https://somapass.xyz/checkout/:userid/:password/:uuid/`

Example: `https://somapass.xyz/checkout/1111111/password/7ea859a3-e79e-49d6-a88b-f028030d7caa`

### /currentcheckins

Req: `https://somapass.xyz/currentcheckins/:userid/:password/`

Example: `https://somapass.xyz/currentcheckins/1111111/password`

### /latestcheckin

Will return JSON object of latest check in. If not checked in anywhere, will return 404.

Req: `https://somapass.xyz/latestcheckin/:userid/:password/`

Example: `https://somapass.xyz/latestcheckin/1111111/password`

