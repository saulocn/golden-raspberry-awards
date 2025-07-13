test:
	./mvnw test

clean:
	./mvnw clean

build: clean
	./mvnw clean package -Dquarkus.package.jar.type=uber-jar

run:
	./mvnw quarkus:dev

build-docker: build
	docker build --no-cache --progress=plain -t saulocn/golden-raspberry-awards .

run-docker: build-docker
	docker run -i --rm -p 8080:8080 saulocn/golden-raspberry-awards

logs:
	docker logs -f saulocn/golden-raspberry-awards