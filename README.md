# [WIP] :construction: Socials Backend :fire:

This is a brief demonstration of a kotlin backend server.

Currently, this project uses the following libraries.

- #### postgres
    - ##### exposed | hikari
- #### gson
- #### jwt tokens

## How To Start

Clone the project and build. Open the Application.kt file located `./src/main/kotlin/com/mambobryan`, on the right side
of `fun main()` you'll see a run button, click and :drum: your server is started. Head over to postman or insomnia to test the [endpoints](#endpoints)

##### REMOTE : `https://mambo-socials.herokuapp.com/`

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
  Field :  {
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
    - #### update
  ```
  Method : PUT
  Endpoint : url/users
  Payload : JSON
  Field : {
  username : String
  email : String
  password : String
  }
  ```
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