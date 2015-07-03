package com.skiwi.businesssoftware

import java.time.Instant

class User {
    static constraints = {
        email email: true, unique: true
        token nullable: true
        person nullable: true
        registrationDate nullable: true
        loginDate nullable: true
    }

    String email
    String password
    String token
    Person person
    Instant registrationDate
    Instant loginDate
}
