package com.emanueltobias.etfood;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.repository.CozinhaRepository;
import com.emanueltobias.etfood.util.DatabaseCleaner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource({"/application-test.properties"})
class CadastroCozinhaIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		databaseCleaner.clearTables();
		prepararDados();
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConter3Cozinhas_QuandoConsultarCozinhas() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(3))
			.body("nome", Matchers.hasItems("Tailandesa", "Indiana"));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadaastrarCozinha() {
		RestAssured.given()
			.body("{\"nome\": \"Chinesa\" }")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorreto_QuandoConsultarCozinhaExistente() {
		RestAssured.given()
			.pathParam("idCozinha", 2)
			.accept(ContentType.JSON)
		.when()
			.get("/{idCozinha}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", Matchers.equalTo("Americana"));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaExistente() {
		RestAssured.given()
			.pathParam("idCozinha", 100)
			.accept(ContentType.JSON)
		.when()
			.get("/{idCozinha}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Americana");
		cozinhaRepository.save(cozinha2);
		
		Cozinha cozinha3 = new Cozinha();
		cozinha3.setNome("Indiana");
		cozinhaRepository.save(cozinha3);
	}

}