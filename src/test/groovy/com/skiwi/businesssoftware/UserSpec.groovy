package com.skiwi.businesssoftware

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
@TestMixin(DomainClassUnitTestMixin)
class UserSpec extends Specification {
    def "save valid user"() {
        when: "user is valid"
        def user = new User(email: "test@test.com", password: "test")

        then: "user should be saved"
        user.save()
    }

    def "email should be valid"() {
        when: "email invalid"
        def user = new User(email: "invalid", password: "test")

        then: "saving should fail"
        !user.save(failOnError: false)
    }

    def "email should be unique"() {
        when: "twice same email"
        def user = new User(email: "test@test.com", password: "test")
        def user2 = new User(email: "test@test.com", password: "test")

        then: "saving should fail"
        user.save(flush: true)
        !user2.save(failOnError: false)
    }

    def "person may be attached"() {
        def person = new Person(firstName: "John", lastName: "Doe")

        when: "person not attached"
        def user = new User(email: "john@doe.com", password: "test")

        then: "user should be saved"
        user.save()

        when: "person attached"
        user.person = person

        then: "user should still be saved"
        user.save()
    }
}
