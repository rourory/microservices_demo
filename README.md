# What is it

**JUST FOR DEMO PURPOSES**

System that:
1. Receives public posts from Mastodon social network;
2. Put into Elasticsearch through Kafka broker;
3. Allows you to search what people talk among saved posts.

# Setting up

### Setting up the Keycloak Auth Server

1. Restore keycloak.keycloak.nb3 file with Navicat Premium 16 in keycloak DB, keycloak scheme.
2. After that you'll be able to run keycloak-auth-server.yaml in Docker with predefined settings.
3. Don't change the keycloak image version!

### Running the cluster

```bash
cd ./docker-compose
docker-compose up
```

### Getting access key from Auth Server

Do **POST** request by Postman:
http://localhost:8086/realms/microservices_realm/protocol/openid-connect/token

**Body** (x-www-form-urlencoded):

- grant_type: password
- username: app_superuser
- password: password
- client_id: elastic-query-web-client
- client_secret: B8p4wfP31riOnzvIzmkD21RWFYZSfR1b


### Do request to API
___
Do **GET** by Postman with your access key (from previous step) to receive all the posts: 
http://localhost:8183/documents

**Headers**:
- Accept: application/vnd.api.v1+json

___

Do **GET** by Postman with your access key (from previous step) to receive post by its ID: 
http://localhost:8183/documents/1

**Headers**:
- Accept: application/vnd.api.v1+json

___

Do **POST** by Postman with your access key (from previous step) to receive posts by including text:
http://localhost:8183/documents/search-by-text

**Body** (raw JSON):
{
"text": "Trump"
}

**Headers**:
- Accept: application/vnd.api.v1+json
___