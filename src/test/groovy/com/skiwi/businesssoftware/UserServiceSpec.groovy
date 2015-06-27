package com.skiwi.businesssoftware

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User, Person])
@TestMixin(DomainClassUnitTestMixin)
class UserServiceSpec extends Specification {
    def "test create user"() {
        when: "create user"
        def user = service.createUser("John", null, "Doe", "john@doe.com", "johndoe")

        then: "user is saved and properties are correct"
        user
        def userDb = User.findByEmail("john@doe.com")
        userDb.email == "john@doe.com"
        userDb.password != "johndoe"
        userDb.person.firstName == "John"
        userDb.person.middleName == null
        userDb.person.lastName == "Doe"
    }
}
