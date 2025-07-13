test:
	./mvnw test

clean:
	./mvnw clean

build: clean
	./mvnw compile

run:
	./mvnw quarkus:dev

build-docker: clean
	./mvnw package -Dquarkus.container-image.build=true

run-docker: build-docker
	docker run -i --rm -p 8080:8080 saulocn/golden-raspberry-awards

build-native-docker: clean
	./mvnw package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true

run-native-docker: build-native-docker
	docker run -i --rm -p 8080:8080 saulocn/golden-raspberry-awards

logs:
	docker logs -f saulocn/golden-raspberry-awards