package br.com.dvision.controller;

import java.util.Date;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import br.com.dvision.model.Book;
import br.com.dvision.proxy.CambioProxy;
import br.com.dvision.repository.BookRepository;
import br.com.dvision.response.Cambio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book endpoint")
@RestController
@RequestMapping("book-service")
public class BookController{

	@Autowired
	private Environment env;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CambioProxy cambioProxy;
	
	@Operation(summary = "Find a specific book by your ID")
	@GetMapping(value = "/{id}/{currency}")
	public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		
		
		var book = bookRepository.getById(id);
		
		if(book == null) throw new RuntimeException("Book not found !");
		
		var cambio = cambioProxy.getCambio(book.getPrice(), "USD", currency );
		var port = env.getProperty("local.server.port");
		
		book.setEnvironment( "Book Port: " + port + " Cambio port: " + cambio.getEnvironment());
		book.setPrice( cambio.getConvertedValue());
		
		return book;
		
	}
	/**@GetMapping(value = "/{id}/{currency}")
	public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		
		
		var book = bookRepository.getById(id);
		
		if(book == null) throw new RuntimeException("Book not found !");
		
		HashMap<String, String> params = new HashMap();
		
		params.put("amount",book.getPrice().toString());
		params.put("from", "USD");
		params.put("to",currency);
		
		var responseCambio = new RestTemplate()
			.getForEntity("http://localhost:3000/cambio-service/{amount}/{from}/{to}", 
					Cambio.class,
					params);
		
		var port = env.getProperty("local.server.port");
		
		var cambio = responseCambio.getBody();
		
		book.setEnvironment( port );
		book.setPrice( cambio.getConvertedValue());
		
		return book;
		
	}*/
}
