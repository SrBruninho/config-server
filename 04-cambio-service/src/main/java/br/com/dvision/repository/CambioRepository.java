package br.com.dvision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dvision.model.Cambio;

public interface CambioRepository extends JpaRepository<Cambio, Long>{
	
	Cambio findByFromAndTo(String from, String to);

}
