package com.skiwi.businesssoftware.api.user

import com.skiwi.businesssoftware.Person
import com.skiwi.businesssoftware.User
import com.skiwi.businesssoftware.UserService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

import java.time.Clock
import java.time.Instant

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([UserService, User, Person])
@TestMixin(DomainClassUnitTestMixin)
class UserControllerSpec extends Specification {
    static doWithSpring = {
        clock(Clock) { bean ->
            bean.factoryMethod = "systemDefaultZone"
        }
    }

    def "create user"() {
        when: "first user created"
        def result = controller.createUser(new CreateUserMessage("John", null, "Doe", "info@doe.com", "johndoe"))

        then: "success and user is created"
        result.success
        User.countByEmail("info@doe.com") == 1

        when: "second user created with same email"
        def result2 = controller.createUser(new CreateUserMessage("Jane", null, "Doe", "info@doe.com", "johndoe"))

        then: "no success and user is not created"
        !result2.success
        result2.message == "EMAIL_IN_USE"
        User.countByEmail("info@doe.com") == 1
    }

    def "login user"() {
        controller.createUser(new CreateUserMessage("John", null, "Doe", "john@doe.com", "johndoe"))

        when: "invalid user data"
        def result = controller.loginUser(new LoginUserMessage("info@doe.com", "johndoe"))

        then: "no success"
        !result.success
        result.message == "EMAIL_NOT_FOUND"
        session.user == null
        session.token == null

        when: "correct login data"
        def expectedUser = User.findByEmail("john@doe.com")
        def result2 = controller.loginUser(new LoginUserMessage("john@doe.com", "johndoe"))

        then: "success and user is saved into session"
        result2.success
        session.user == expectedUser
        session.token != null

        when: "already logged in"
        def result3 = controller.loginUser(new LoginUserMessage("john@doe.com", "johndoe"))

        then: "no success"
        !result3.success
        result3.message == "ALREADY_LOGGED_IN"
    }

    def "logout user"() {
        controller.createUser(new CreateUserMessage("John", null, "Doe", "john@doe.com", "johndoe"))

        when: "user is not logged in"
        def result = controller.logoutUser(new Object())

        then: "no success"
        !result.success
        result.message == "NOT_LOGGED_IN"

        when: "user is logged in"
        controller.loginUser(new LoginUserMessage("john@doe.com", "johndoe"))

        then: "success"
        def result2 = controller.logoutUser(new Object())
        result2.success
        session.user == null
        session.token == null
    }
}
