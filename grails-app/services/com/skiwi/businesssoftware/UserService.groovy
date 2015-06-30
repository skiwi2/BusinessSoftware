package com.skiwi.businesssoftware

import grails.transaction.Transactional
import groovy.transform.InheritConstructors
import org.mindrot.jbcrypt.BCrypt

@Transactional
class UserService {
    User createUser(String firstName, String middleName, String lastName, String email, String password) {
        if (User.countByEmail(email)) {
            throw new CreateUserException("EMAIL_IN_USE")
        }

        def salt = BCrypt.gensalt(12)
        def encodedPassword = BCrypt.hashpw(password, salt)
        def user = new User(email: email, password: encodedPassword)
        def person = new Person(firstName: firstName, middleName: middleName, lastName: lastName)
        person.save()
        user.person = person
        user.save()
    }
}

@InheritConstructors
class CreateUserException extends RuntimeException { }