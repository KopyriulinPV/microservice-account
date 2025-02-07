package com.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroserviceAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAccountApplication.class, args);
	}

}

/*

mvn clean package -DskipTests
docker build -t microservice_account .
docker save -o microservice_account.tar microservice_account

sudo find / -name "microservice_account.tar"
sudo docker load < /home/admin/microservice_account.tar
sudo docker images
sudo docker run --network microservice-network microservice_account
sudo docker run --network microservice-network -e SPRING_DATASOURCE_URL=jdbc:postgresql://d46015a41e7e:5432/postgresdb microservice_account

ip: 89.111.155.206
login: admin
pass: ZfsVks6

http://89.111.155.206
*/
