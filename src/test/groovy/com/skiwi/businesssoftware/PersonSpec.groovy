package com.skiwi.businesssoftware

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Person)
@TestMixin(DomainClassUnitTestMixin)
class PersonSpec extends Specification {
    def "save minimal person"() {
        when: "minimal person is valid"
        def person = new Person(firstName: "John", lastName: "Doe")

        then: "person should be saved"
        person.save()
    }

    def "email should be valid"() {
        when: "email valid"
        def person = new Person(firstName: "John", lastName: "Doe", email: "john@doe.com")

        then: "person should be saved"
        person.save()

        when: "email invalid"
        def person2 = new Person(firstName: "Jane", lastName: "Doe", email: "invalid")

        then: "saving should fail"
        !person2.save(failOnError: false)
    }
}
