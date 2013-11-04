package cz.robotron.examples.xtend

import java.util.List

class TestData {
	def static List<Person> getPersonData() {

		var list = newArrayList()

		list.add(
			new Person => [
				firstName = 'Homer'
				lastName = 'Simpson'
				address = new Address => [
					street = '742 Evergreen Terrace'
					city = 'SpringField'
				]
			])

		list.add(
			new Person => [
				firstName = 'Enrico'
				lastName = 'Bernardetti'
				address = new Address => [
					street = '12 Asmolovova'
					city = 'Litomerice'
				]
			])

		list.add(
			new Person => [
				firstName = 'Steffen'
				lastName = 'Effinge'
				address = new Address => [
					street = '78 Heidelbergerstraße'
					city = 'Dresden'
				]
			])

		list.add(
			new Person => [
				firstName = 'Karl'
				lastName = 'Valenstein'
				address = new Address => [
					street = '17 Schloß'
					city = 'Frydlant'
				]
			])

		return list
	}
}
