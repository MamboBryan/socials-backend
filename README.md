# Socials Backend :fire:

This is a brief demonstration of a kotlin backend server.

Currently, this project uses the following libraries.

- #### postgres
  - ##### exposed | hikari
- #### gson
- #### jwt tokens

## Tables
- #### users
  - user_id | user_name | user_email | hash (Password hash)
- #### posts
  - post_id | post_content | user_id (Foreign Key from users)
- #### post_likes
  - like_id | post_id (Foreign Key from posts) | user_id (Foreign Key from users)

  
## Endpoints
- ### auth
  - #### signin
  ```
  Method : POST
  Endpoint : url/auth/signin
  Payload : JSON
  Field : {
   email : String
   password : String
  }
  ```
  - #### signup
  ```
  Method : POST
  Endpoint : url/auth/signup
  Payload : JSON
  Field : {
  username : String
  email : String
  password : String
  }
  ```
- ### users
  - #### create
  - #### update
  - #### delete
  - #### user
    - ##### details
    - ##### posts
- ### posts
  - #### create
  - #### update
  - #### delete
  - #### list
  - #### post
    - ##### like
    - ##### unlike
    - ##### likes