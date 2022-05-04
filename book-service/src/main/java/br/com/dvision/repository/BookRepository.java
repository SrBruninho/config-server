package br.com.dvision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dvision.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
