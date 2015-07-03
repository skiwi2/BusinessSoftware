package com.skiwi.businesssoftware

import java.time.Instant

class User {
    static constraints = {
        email email: true, unique: true
        person nullable: true
        registrationDate nullable: true
        loginDate nullable: true
    }

    String email
    String password
    Person person
    Instant registrationDate
    Instant loginDate
}
