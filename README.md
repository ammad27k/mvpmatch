# mvpmatch

The project consist of keycloak for openid and oauth2

Checkout branch

go to **cd /dev**
execute -> **docker-compose up**

once the key cloak is up then do following

go to -> **http:localhost:9000**

username -> **admin**, password -> **admin**

go to import from side left bar and impor the **realm-export.json**

Once its done

go to -> **cd /vendingmachine**

run -> **mvn spring-boot:run**

Now the app is up

There is postman collection are also commited, download and sigup and login with user

**There are few thing are left, like some unit test and integration test cases are left and need some testing**
