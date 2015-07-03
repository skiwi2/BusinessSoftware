package com.skiwi.businesssoftware

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User, Person])
@TestMixin(DomainClassUnitTestMixin)
class UserServiceSpec extends Specification {
    static nowInstant = Instant.now()

    static doWithSpring = {
        clock(Clock) { bean ->
            bean.factoryMethod = "fixed"
            bean.constructorArgs = [nowInstant, ZoneId.systemDefault()]
        }
    }

    def "test create user"() {
        when: "create user"
        def user = service.createUser("John", null, "Doe", "john@doe.com", "johndoe")

        then: "user is saved and properties are correct"
        user
        def userDb = User.findByEmail("john@doe.com")
        userDb.email == "john@doe.com"
        userDb.password != "johndoe"
        userDb.registrationDate == nowInstant
        userDb.person.firstName == "John"
        userDb.person.middleName == null
        userDb.person.lastName == "Doe"
    }

    def "test create two users with same email"() {
        when: "create two users with same email"
        service.createUser("John", null, "Doe", "info@doe.com", "johndoe")
        service.createUser("Jane", null, "Doe", "info@doe.com", "janedoe")

        then: "exception is thrown"
        thrown(CreateUserException)
    }

    def "test login user"() {
        service.createUser("John", null, "Doe", "john@doe.com", "johndoe")

        when: "login with non-existing email"
        service.loginUser("info@doe.com", "johndoe")

        then: "email is not found"
        def ex = thrown(LoginUserException)
        ex.message == "EMAIL_NOT_FOUND"

        when: "login with incorrect password"
        service.loginUser("john@doe.com", "infodoe")

        then: "password is incorrect"
        def ex2 = thrown(LoginUserException)
        ex2.message == "PASSWORD_INCORRECT"

        when: "login with a valid user"
        def user = service.loginUser("john@doe.com", "johndoe")

        then: "last login time is set"
        user.loginDate == nowInstant
    }
}
