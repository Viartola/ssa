package es.gorka.edu.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gorka.edu.assembler.Assembler;
import es.gorka.edu.connection.AbstractConnectionManager;
import es.gorka.edu.dto.AuthorDTO;
import es.gorka.edu.model.Author;

@Service
public class AuthorRepository {
	private static final Logger logger = LogManager.getLogger(AuthorRepository.class.getName());
	
	@Autowired
	private AbstractConnectionManager conManager;
	
	@Autowired
	private Assembler<AuthorDTO, Author> asesembler;
	
	public void insertNewAuthor(AuthorDTO authorDto) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Author author = new Author();
		asesembler.toEntity(authorDto, author);
		try {
			connection = conManager.open();
			preparedStatement = connection.prepareStatement("INSERT INTO AUTHOR(nameAuthor, dateOfBirth) " + "VALUES (?, ?)");
			preparedStatement.setString(1, author.getNameAuthor());
			preparedStatement.setDate(2, author.getDateOfBirth());
			preparedStatement.executeUpdate();
			conManager.close(preparedStatement);

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
}