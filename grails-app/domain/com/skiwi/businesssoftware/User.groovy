package com.skiwi.businesssoftware

class User {
    static constraints = {
        email email: true, unique: true
        token nullable: true
        person nullable: true
    }

    String email
    String password
    String token
    Person person
}
