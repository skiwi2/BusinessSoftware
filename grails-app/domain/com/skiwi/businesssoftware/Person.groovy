package com.skiwi.businesssoftware

class Person {
    static constraints = {
        middleName nullable: true
        city nullable: true
        street nullable: true
        houseNumber nullable: true
        zipCode nullable: true
        email email: true, nullable: true
        bankAccount nullable: true
    }

    static hasMany = [
        telephoneNumbers: String
    ]

    String firstName
    String middleName
    String lastName
    String city
    String street
    String houseNumber
    String zipCode
    String email
    List<String> telephoneNumbers
    String bankAccount
}
