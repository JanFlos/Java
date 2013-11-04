package cz.robotron.examples.xtend

import cz.robotron.xtend.annotations.Observable
import java.util.List

@Observable
class Person {
	
	String firstName
	String lastName
	Address address
}

@Observable
class Persons{
	List<Person> childs 
}