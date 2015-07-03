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

        then: "success result and user is created"
        result.success
        User.countByEmail("info@doe.com") == 1

        when: "second user created with same email"
        def result2 = controller.createUser(new CreateUserMessage("Jane", null, "Doe", "info@doe.com", "johndoe"))

        then: "no success result and user is not created"
        !result2.success
        result2.message
        User.countByEmail("info@doe.com") == 1
    }
}
