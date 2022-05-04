package br.com.dvision.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dvision.model.Cambio;
import br.com.dvision.repository.CambioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cambio endpoint")
@RestController
@RequestMapping("cambio-service")
public class CambioController {
	
	private Logger logger = LoggerFactory.getLogger(CambioController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private CambioRepository cambioRepository;
	
	@Operation(summary = "Convert value to a specific currency")
	@GetMapping(value = "/{amount}/{from}/{to}")
	public Cambio getCambio(
			@PathVariable("amount") BigDecimal amount,
			@PathVariable("from") String from,
			@PathVariable("to") String to
			) {
		logger.info("getCambio is called with -> {}, {} and {}",amount,from,to);
		
		var cambio = cambioRepository.findByFromAndTo(from, to);
		
		if(cambio == null) throw new RuntimeException("Currency Unsuported");
		
		var port = env.getProperty("local.server.port");
		
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedDecimal = conversionFactor.multiply(amount);
		
		cambio.setConvertedValue(convertedDecimal.setScale(2, RoundingMode.CEILING));
		cambio.setEnvironment(port);
		
		return cambio;
		
	}

}
