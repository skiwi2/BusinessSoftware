package com.skiwi.businesssoftware

import grails.transaction.Transactional
import org.mindrot.jbcrypt.BCrypt

@Transactional
class UserService {
    User createUser(String firstName, String middleName, String lastName, String email, String password) {
        def salt = BCrypt.gensalt(12)
        def encodedPassword = BCrypt.hashpw(password, salt)
        def user = new User(email: email, password: encodedPassword)
        def person = new Person(firstName: firstName, middleName: middleName, lastName: lastName)
        person.save()
        user.person = person
        user.save()
    }
}
