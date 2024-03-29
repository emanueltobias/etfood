package com.emanueltobias.etfood;

import java.math.BigDecimal;

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
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.CozinhaRepository;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;
import com.emanueltobias.etfood.util.DatabaseCleaner;
import com.emanueltobias.etfood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource({"/application-test.properties"})
class RestauranteIT {
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";

	private static final int RESTAURANTE_ID_INEXISTENTE = 100;

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	private String jsonRestauranteCorreto;
    private String jsonRestauranteSemFrete;
    private String jsonRestauranteSemCozinha;
    private String jsonRestauranteComCozinhaInexistente;
    
    private Restaurante burgerTopRestaurante;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		
		jsonRestauranteCorreto = ResourceUtils.getContentFromResource(
                "/json/correto/restaurante-new-york-barbecue.json");
        
        jsonRestauranteSemFrete = ResourceUtils.getContentFromResource(
                "/json/incorreto/restaurante-new-york-barbecue-sem-frete.json");
        
        jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource(
                "/json/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");
        
        jsonRestauranteComCozinhaInexistente = ResourceUtils.getContentFromResource(
                "/json/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");
		
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
    public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
    	RestAssured.given()
            .body(jsonRestauranteCorreto)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value());
    }
	
	@Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
    	RestAssured.given()
            .body(jsonRestauranteSemFrete)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", Matchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }
	
	@Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
    	RestAssured.given()
            .body(jsonRestauranteSemCozinha)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", Matchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }
	
	@Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
    	RestAssured.given()
            .body(jsonRestauranteComCozinhaInexistente)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", Matchers.equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
    }
	
	@Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
    	RestAssured.given()
            .pathParam("restauranteId", burgerTopRestaurante.getId())
            .accept(ContentType.JSON)
        .when()
            .get("/{restauranteId}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("nome", Matchers.equalTo(burgerTopRestaurante.getNome()));
    }
	
	@Test
    public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
    	RestAssured.given()
            .pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
            .accept(ContentType.JSON)
        .when()
            .get("/{restauranteId}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
	
	private void prepararDados() {
		Cozinha cozinhaBrasileira = new Cozinha();
        cozinhaBrasileira.setNome("Brasileira");
        cozinhaRepository.save(cozinhaBrasileira);

        Cozinha cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);
        
        burgerTopRestaurante = new Restaurante();
        burgerTopRestaurante.setNome("Burger Top");
        burgerTopRestaurante.setTaxaFrete(new BigDecimal(5));
        burgerTopRestaurante.setCozinha(cozinhaAmericana);
        restauranteRepository.save(burgerTopRestaurante);
        
        Restaurante comidaMineiraRestaurante = new Restaurante();
        comidaMineiraRestaurante.setNome("Comida Mineira");
        comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(5));
        comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
        restauranteRepository.save(comidaMineiraRestaurante);
	}

}