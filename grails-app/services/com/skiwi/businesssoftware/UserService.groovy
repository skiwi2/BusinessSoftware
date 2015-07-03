package com.skiwi.businesssoftware

import grails.transaction.Transactional
import groovy.transform.InheritConstructors
import org.mindrot.jbcrypt.BCrypt

import java.time.Clock
import java.time.Instant

@Transactional
class UserService {
    Clock clock

    User createUser(String firstName, String middleName, String lastName, String email, String password) {
        if (User.countByEmail(email)) {
            throw new CreateUserException("EMAIL_IN_USE")
        }

        def salt = BCrypt.gensalt(12)
        def encodedPassword = BCrypt.hashpw(password, salt)
        def user = new User(email: email, password: encodedPassword, registrationDate: Instant.now(clock))
        def person = new Person(firstName: firstName, middleName: middleName, lastName: lastName)
        person.save()
        user.person = person
        user.save()
    }

    User loginUser(String email, String password) {
        def user = User.findByEmail(email)
        if (!user) {
            throw new LoginUserException("EMAIL_NOT_FOUND")
        }
        if (!BCrypt.checkpw(password, user.password)) {
            throw new LoginUserException("PASSWORD_INCORRECT")
        }

        user.loginDate = Instant.now(clock)
        user.save()
    }
}

@InheritConstructors
class CreateUserException extends RuntimeException { }

@InheritConstructors
class LoginUserException extends RuntimeException { }